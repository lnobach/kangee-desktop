package de.roo.ui.swing.exLAF.detail;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicPanelUI;

import de.roo.ui.swing.exLAF.UIToolkit;

public class RooDetailViewRequestDetailsUI extends BasicPanelUI {

	public void installUI(JComponent c) {
    	c.setOpaque(false);
    	c.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    	super.installUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	UIToolkit.paintRoundBG(c, g, Color.white, 10, 0);
    }
	
}
