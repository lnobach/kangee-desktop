package de.roo.ui.swing.exLAF.main;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicPanelUI;

import de.roo.ui.swing.exLAF.UIConstants;

public class RooMainLoadListUI extends BasicPanelUI {

    public void installUI(JComponent c) {
    	c.setOpaque(false);
    	c.setBorder(BorderFactory.createEmptyBorder());
    	((JPanel)c).setFont(UIConstants.defaultFont);
    	super.installUI(c);
    }
	
}
