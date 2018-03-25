package de.roo.ui.swing.wizardry.common;

import java.awt.Component;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public interface IRooWizardStep {
	
	public Component getComponent(IValidInputListener l);
	
	public boolean mayGoToNextStep();
	
	public IRooWizardStep getNextStep();
	
	public boolean hasNextStep();
	
	public String getCaption();
	
	public String getShortDesc();
	
	public void onAppear();
	
	public void onCancel();
	
	public void onTransitToNextStep();
	
	public void onTransitToPreviousStep();
	
	
}
