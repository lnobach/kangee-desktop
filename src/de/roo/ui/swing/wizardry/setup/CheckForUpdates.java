package de.roo.ui.swing.wizardry.setup;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.roo.engine.setup.ISetupContext;
import de.roo.ui.swing.util.DropBoxLayout;
import de.roo.ui.swing.wizardry.common.IRooWizardStep;
import de.roo.ui.swing.wizardry.common.IValidInputListener;

public class CheckForUpdates implements IRooWizardStep {

	private StepComponent comp;

	private ISetupContext ctx;
	
	public CheckForUpdates(ISetupContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public String getCaption() {
		return "Update Policy";
	}

	@Override
	public Component getComponent(IValidInputListener l) {
		comp = new StepComponent(l);
		return comp;
	}

	@Override
	public String getShortDesc() {
		return "Choose if Kangee shall automatically check for updates on every startup, if connected to the Internet.";
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
		ctx.getConf().setValue("CheckForUpdates", comp.shallCheckForUpdates());
	}

	@Override
	public void onTransitToPreviousStep() {
		// TODO Auto-generated method stub

	}
	
	class StepComponent extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6936241909055520104L;
		
		JCheckBox shallCheckForUpdatesBox;

		public StepComponent(IValidInputListener l) {
			
			DropBoxLayout ly = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
			ly.setForceFillSize(true);
			this.setLayout(ly);

			boolean shallCheckForUpdates = ctx.getConf().getValueBoolean("CheckForUpdates", true);
			
			shallCheckForUpdatesBox = new JCheckBox("Check for updates on every startup (recommended).", shallCheckForUpdates);
			
			this.add(shallCheckForUpdatesBox);
			
		}
		
		public boolean shallCheckForUpdates() {
			return shallCheckForUpdatesBox.isSelected();
		}
		
	}

	@Override
	public IRooWizardStep getNextStep() {
		return null;
	}

	@Override
	public boolean hasNextStep() {
		return false;
	}

	@Override
	public void onAppear() {
		// TODO Auto-generated method stub
		
	}

}














