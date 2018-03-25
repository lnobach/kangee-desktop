package de.roo.ui.swing.exLAF.loads;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicPanelUI;

import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIToolkit;

public class RooLoadViewUI extends BasicPanelUI {
	
    public void installUI(JComponent c) {
    	c.setOpaque(false);
    	//((JLabel)c).setFont(UIConstants.defaultFont);
    	c.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    	super.installUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	
    	UIToolkit.paintRoundGradientBG(c, g, Colors.backgroundHighElement2Grad0, Colors.backgroundHighElement2Grad1, 10, 0);
    	
    	super.paint(g, c);
    	
    }
	
}
