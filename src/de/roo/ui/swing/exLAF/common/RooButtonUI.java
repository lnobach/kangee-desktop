package de.roo.ui.swing.exLAF.common;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIConstants;

public class RooButtonUI extends BasicButtonUI {

    public void installUI(JComponent c) {
    	AbstractButton b = (AbstractButton)c;
    	b.setContentAreaFilled(false);
    	b.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    	b.setFont(UIConstants.defaultFont);
    	super.installUI(c);
    }
    
    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	
    	AbstractButton b = (AbstractButton)c;
    	
    	if (b.isSelected()) {
    		GradientPaint gp = new GradientPaint(
    				0, 0, Colors.buttonOnGradient0,
    				0, b.getHeight(), Colors.buttonOnGradient1);
    			((Graphics2D)g).setPaint(gp);
        	g.fillRoundRect(2, 2, b.getWidth()-5, b.getHeight()-5, 7, 7);
    	}
    	super.paint(g, c);
    }
    
    public Dimension getPreferredSize(JComponent c) {
    	return super.getPreferredSize(c);
    }

    public Dimension getMinimumSize(JComponent c) {
    	return super.getMinimumSize(c);
    }
    
    protected void paintFocus(Graphics g, AbstractButton b,
            Rectangle viewRect, Rectangle textRect, Rectangle iconRect){
    	
    	g.setColor(Colors.buttonFocusFrame);
    	g.drawRoundRect(2, 2, b.getWidth()-5, b.getHeight()-5, 7, 7);
    	
    }



    protected void paintButtonPressed(Graphics g, AbstractButton b){
		GradientPaint gp = new GradientPaint(
				0, 0, Colors.buttonPressedGradient0,
				0, b.getHeight(), Colors.buttonPressedGradient1);
			((Graphics2D)g).setPaint(gp);
    	g.fillRoundRect(2, 2, b.getWidth()-5, b.getHeight()-5, 7, 7);
    }
	
}
