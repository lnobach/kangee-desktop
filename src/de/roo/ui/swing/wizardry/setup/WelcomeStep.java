package de.roo.ui.swing.wizardry.setup;

import java.awt.Component;

import javax.swing.JLabel;

import de.roo.BuildConstants;
import de.roo.engine.setup.ISetupContext;
import de.roo.ui.swing.wizardry.common.IRooWizardStep;
import de.roo.ui.swing.wizardry.common.IValidInputListener;

public class WelcomeStep implements IRooWizardStep {

	private ISetupContext ctx;

	public WelcomeStep(ISetupContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public String getCaption() {
		return "Welcome";
	}

	@Override
	public Component getComponent(IValidInputListener l) {
		JLabel comp =  new JLabel();
		comp.setText("<html>Welcome to the " + BuildConstants.PROD_FULL_NAME + ". <br> In the next steps, " +
		"the connectivity will be tested and " + BuildConstants.PROD_TINY_NAME + " will be configured.</html>");
		return comp;
	}

	@Override
	public String getShortDesc() {
		return "Welcome to " + BuildConstants.PROD_FULL_NAME;
	}

	@Override
	public boolean mayGoToNextStep() {
		return true;
	}

	@Override
	public void onCancel() {
		//Nothing to do
	}

	@Override
	public void onTransitToNextStep() {
		//Nothing to do
	}

	@Override
	public void onTransitToPreviousStep() {
		//Nothing to do
	}

	@Override
	public IRooWizardStep getNextStep() {
		return new Nickname(ctx);
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
