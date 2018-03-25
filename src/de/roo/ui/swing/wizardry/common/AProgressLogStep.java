package de.roo.ui.swing.wizardry.common;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import de.roo.engine.setup.ISetupContext;
import de.roo.ui.swing.util.LogBox;
import de.roo.util.ILogStatusRunnable;
import de.roo.util.ILogStatusRunnable.ILogStatusHandler;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public abstract class AProgressLogStep implements IRooWizardStep {

	private ISetupContext ctx;
	
	WorkerProc wProc = null;
	
	LogBox logbox;
	JLabel statusTextLabel;
	JProgressBar progBar;
	
	IValidInputListener l = null;
	
	static final int PROGRESS_RESOLUTION = 100;

	public AProgressLogStep(ISetupContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public abstract String getCaption();
	
	public abstract ILogStatusRunnable getProcessToRun();

	@Override
	public Component getComponent(IValidInputListener l) {
		this.l = l;
		
		logbox = new LogBox(ctx.getLog());
		statusTextLabel = new JLabel("initStatus");
		progBar = new JProgressBar();
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.NONE;
		c.ipady = 10;
		c.weightx = 1;
		c.weighty = 0;
		panel.add(statusTextLabel, c);
		
		c.ipady = 30;
		c.gridy = 1;
		panel.add(progBar, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 2;
		c.weighty = 1;
		panel.add(logbox);
		
		return panel;
	}

	@Override
	public abstract IRooWizardStep getNextStep();

	@Override
	public abstract String getShortDesc();

	@Override
	public abstract boolean hasNextStep();

	public abstract boolean mayGoOnOnUnsuccessful();
	
	@Override
	public boolean mayGoToNextStep() {
		return wProc.hasFinished() && (mayGoOnOnUnsuccessful() || wProc.wasSuccessful());
	}

	@Override
	public void onCancel() {
		terminateWProc();
	}

	@Override
	public void onTransitToNextStep() {
		terminateWProc();
	}

	@Override
	public void onTransitToPreviousStep() {
		terminateWProc();
	}
	
	@Override
	public void onAppear() {
		createNewProc();
	}
	
	protected boolean procWasSuccessful() {
		return wProc != null && wProc.wasSuccessful();
	}
	
	protected boolean procHasFinished() {
		return wProc != null && wProc.hasFinished();
	}
	
	void createNewProc() {
		if (wProc != null) {
			wProc.terminate();
		}
		wProc = new WorkerProc();
	}
	
	void terminateWProc() {
		if (wProc == null) {
			wProc.terminate();
			wProc = null;
		}
	}
	
	class WorkerProc implements ILogStatusHandler {
		
		Thread workerThread;
		ILogStatusRunnable runnable = getProcessToRun();
		boolean terminated = false;
		boolean finished = false;
		boolean success = false;

		public WorkerProc() {
			workerThread = new Thread() {
				
				public void run() {
					try {
						success = runnable.run(WorkerProc.this, logbox);
						if (terminated) success = false;
						finished = true;
						setCurrentProgress(1);
						onFinish();
					} catch (InterruptedException ex) {
						ctx.getLog().warn(this, "Interrupted process " + this);
						success = false;
						terminated = true;
						finished = true;
						onFinish();
					} catch (Exception ex) {
						logbox.error(this, "Uncaught exception occurred. ", ex);
						success = false;
						finished = true;
						setCurrentProgress(1);
						onFinish();
					}
				}
				
			};
		}
		
		protected void onFinish() {
			// TODO Auto-generated method stub
			
		}

		public boolean wasSuccessful() {
			return success;
		}

		public void terminate() {
			terminated = true;
			workerThread.interrupt();
		}

		@Override
		public void setCurrentJob(String status) {
			statusTextLabel.setText(status);
		}

		@Override
		public void setCurrentProgress(double progress) {
			setProgress(progress);
		}

		@Override
		public boolean wasTerminated() {
			return terminated;
		}
		
		public boolean hasFinished() {
			return finished;
		}
		
	}
	
	void setProgress(double progress) {
		progBar.setValue((int)(progress*PROGRESS_RESOLUTION));
	}
	
}
