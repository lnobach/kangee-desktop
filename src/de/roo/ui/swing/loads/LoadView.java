package de.roo.ui.swing.loads;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IAbstractLoad.IUploadListener;
import de.roo.model.uiview.IAbstractLoad.LoadState;
import de.roo.model.uiview.IRooResource;
import de.roo.srv.Upload;
import de.roo.ui.swing.common.JPrecisionProgressBar;
import de.roo.ui.swing.exLAF.common.RooLabelUI;
import de.roo.ui.swing.exLAF.loads.RooLoadViewUI;
import de.roo.ui.swing.exLAF.main.RooMainLoadProgressBarUI;
import de.roo.ui.swing.loads.triggers.ShowPrefs;
import de.roo.ui.swing.loads.triggers.StopClose;
import de.roo.ui.swing.menu.TriggerToolbarButton;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.util.NumberFormatToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LoadView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8067436578733521774L;

	JLabel state;
	JLabel sender;
	JLabel progressLabel;

	JButton info;
	JButton closeBtn;

	JPrecisionProgressBar progressBar;
	private IAbstractLoad load;
	private ILog log;

	static final long UPDATE_INTERVAL = 100; // ms

	StopClose stopClose;
	Trigger showPrefs;
	
	public LoadView(IAbstractLoad load, IRooResource parent, ILog log, IWritableConf conf) {

		this.setUI(new RooLoadViewUI());
		
		this.load = load;
		this.log = log;

		load.addListener(this.new LoadListener());
		
		stopClose = new StopClose(parent, load);
		showPrefs = new ShowPrefs(load, log, conf);
		
		progressBar = new JPrecisionProgressBar();
		progressBar.setUI(new RooMainLoadProgressBarUI());
		state = new JLabel("<HTML><b>" + getStateString() + "</b></HTML>");
		state.setUI(new RooLabelUI());
		state.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		sender = new JLabel(load.getHTTPRequest().getRequesterInfo().getRequesterIP().getHostName());
		sender.setUI(new RooLabelUI());
		sender.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		progressLabel = new JLabel(formatProgress());
		progressLabel.setUI(new RooLabelUI());
		progressLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		this.setLayout(new GridBagLayout());

		info = new TriggerToolbarButton(showPrefs, false, true);
		closeBtn = new TriggerToolbarButton(stopClose, false, true);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;

		this.add(state, c);

		c.gridx = 1;
		c.gridheight = 2;
		c.weightx = 0;

		this.add(closeBtn, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridheight = 1;
		c.weighty = 0;

		this.add(progressLabel, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;

		this.add(sender, c);

		c.gridx = 1;
		c.gridheight = 2;
		c.weightx = 0;
		c.weighty = 1;

		this.add(info, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;

		this.add(progressBar, c);
		
		updateElementsStateCh();

	}
	
	public void updateElementsStateCh() {
		updateElements();
		if (load.getState() == LoadState.RUNNING)
			new PeriodicUpdateThread().start();
	}

	public void updateElements() {
		state.setText("<HTML><b>" + getStateString() + "</b></HTML>");
		progressLabel.setText(formatProgress());
		progressBar.setProgress(load.getProgress());
		
		LoadState s = load.getState();
		
		if (s == LoadState.SUCCESS
				|| s == LoadState.FAILED || s == LoadState.CANCELLED) stopClose.setStateStop(false);
	}

	public class LoadListener implements IUploadListener {

		@Override
		public void stateChanged(IAbstractLoad upload) {
			updateElementsStateCh();
		}

	}

	String formatProgress() {
		return NumberFormatToolkit.formatSIPrefix(load.getCurrentUploadAmount(),
				2, true)
				+ "B of "
				+ NumberFormatToolkit.formatSIPrefix(load
						.getRequiredUploadAmount(), 2, true)
				+ "B, "
				+ NumberFormatToolkit.formatPercentage(load.getProgress(), 2);
	}

	public String getStateString() {
		if (load.getState() == LoadState.PREPARING) {
			return "Preparing...";
		} else if (load.getState() == LoadState.RUNNING) {
			return (load instanceof Upload)?"Uploading...":"Downloading...";
		} else if (load.getState() == LoadState.SUCCESS) {
			return "Success.";
		} else if (load.getState() == LoadState.CANCELLED) {
			return "Cancelled.";
		} else return "Failed.";
	}

	public class PeriodicUpdateThread extends Thread {

		public void run() {

			while (load.getState() == LoadState.RUNNING) {
				updateElements();
				try {
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException e) {
					log.error(this, "Thread sleep closed unexpectedly.", e);
				}
			}

		}

	}

	public IAbstractLoad getUpload() {
		return load;
	}

	public JButton getCloseButton() {
		return closeBtn;
	}
	
	public JButton getPrefsButton() {
		return info;
	}

}
