package de.roo.ui.swing.exLAF.common;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

import de.roo.ui.swing.exLAF.UIConstants;

public class RooLabelUI extends BasicLabelUI {

    public void installUI(JComponent c) {
    	c.setOpaque(false);
    	((JLabel)c).setFont(UIConstants.defaultFont);
    	super.installUI(c);
    }
	
}
