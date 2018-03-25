package de.roo.ui.swing.exLAF.common;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;

import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIConstants;

public class RooBarcodeDisplayHelperUI extends BasicLabelUI {

    public void installUI(JComponent c) {
    	c.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	c.setFont(UIConstants.defaultFont);
    	c.setForeground(Colors.textFontDarkBG);
    	super.installUI(c);
    }
	
}
