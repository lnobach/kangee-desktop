package de.roo.ui.swing.wizardry;

import java.awt.Dialog;

import de.roo.ui.swing.wizardry.common.RooWizardry;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class WizardryTest {

	public static void main(String[] args) {
		
		boolean result = RooWizardry.showLocked(new TestWizardStep(0), "TestWizard", (Dialog)null);
		System.out.println("Dialog returned: " + result);
		
	}
	
}
