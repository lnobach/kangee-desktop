package de.roo.ui.swing.wizardry.setup;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import de.roo.BuildConstants;
import de.roo.engine.setup.ISetupContext;
import de.roo.ui.swing.util.DropBoxLayout;
import de.roo.ui.swing.wizardry.common.IRooWizardStep;
import de.roo.ui.swing.wizardry.common.IValidInputListener;

public class ChoosePort implements IRooWizardStep {

	private StepComponent comp;

	private ISetupContext ctx;
	
	public ChoosePort(ISetupContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public String getCaption() {
		return "Choose Port";
	}

	@Override
	public Component getComponent(IValidInputListener l) {
		comp = new StepComponent(l);
		return comp;
	}

	@Override
	public String getShortDesc() {
		return "Choose a port where " + BuildConstants.PROD_TINY_NAME + " will listen on";
	}

	@Override
	public boolean mayGoToNextStep() {
		return true;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTransitToNextStep() {
		ctx.getConf().setValue("Port", comp.getSelectedPort());
	}

	@Override
	public void onTransitToPreviousStep() {
		// TODO Auto-generated method stub

	}
	
	class StepComponent extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1761625944900774796L;
		private JRadioButton useRecommendedPort;
		private JRadioButton useCustomPort;
		JSpinner portSel;

		public StepComponent(IValidInputListener l) {
			
			DropBoxLayout ly = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
			ly.setForceFillSize(true);
			this.setLayout(ly);

			int port = ctx.getConf().getValueInt("Port", BuildConstants.DEFAULT_PORT);
			
			useRecommendedPort = new JRadioButton("Use recommended port (" + BuildConstants.DEFAULT_PORT + ")", port == BuildConstants.DEFAULT_PORT);
			JLabel useRecommPortDesc = new JLabel("This port is recommended for single computers and home LANs, most client firewalls do not block it.");
			
			useCustomPort = new JRadioButton("Use custom port", port != 8080);
			SpinnerModel model = new SpinnerNumberModel(port == BuildConstants.DEFAULT_PORT ? 10434 : port, 0, 65535, 1);		
			portSel = new JSpinner(model);
			portSel.setEnabled(port != BuildConstants.DEFAULT_PORT);
			
			ActionListener radioChangeListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == useCustomPort) {
						portSel.setEnabled(true);
					} else {
						portSel.setEnabled(false);
					}
				}
			};
			
			useRecommendedPort.addActionListener(radioChangeListener);
			useCustomPort.addActionListener(radioChangeListener);
			
			ButtonGroup g = new ButtonGroup();
			g.add(useRecommendedPort);
			g.add(useCustomPort);
			
			this.add(useRecommendedPort);
			this.add(useRecommPortDesc);
			
			this.add(useCustomPort);
			this.add(portSel);
			
		}
		
		public int getSelectedPort() {
			if (useRecommendedPort.isSelected()) return BuildConstants.DEFAULT_PORT;
			else return (Integer)portSel.getValue();
		}
		
	}

	@Override
	public IRooWizardStep getNextStep() {
		return new AdvancedSetup1(ctx);
	}

	@Override
	public boolean hasNextStep() {
		return true;
	}

	@Override
	public void onAppear() {
		// TODO Auto-generated method stub
		
	}

}














