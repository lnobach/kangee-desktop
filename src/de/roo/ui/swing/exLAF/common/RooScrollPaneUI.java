package de.roo.ui.swing.exLAF.common;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class RooScrollPaneUI extends BasicScrollPaneUI {

    public void installUI(JComponent c) {
    	super.installUI(c);
    	c.setBorder(BorderFactory.createEmptyBorder());
    	c.setOpaque(false);
    }
	
}
