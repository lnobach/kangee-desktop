package de.roo.ui.swing.loadList;

import java.awt.Component;

import javax.swing.JLabel;

import de.roo.ui.swing.exLAF.common.RooBarcodeDisplayHelperUI;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class Helper {

	JLabel helpText;

	
	public Helper() {
		helpText = new JLabel("Click \"Add Files\" or drag them into this window to share them.");
		helpText.setUI(new RooBarcodeDisplayHelperUI());
	}
	
	public Component getComponent() {
		return helpText;
	}
	
}
