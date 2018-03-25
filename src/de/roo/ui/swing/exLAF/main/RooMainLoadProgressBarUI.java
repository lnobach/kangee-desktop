package de.roo.ui.swing.exLAF.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import de.roo.ui.swing.common.JPrecisionProgressBar;
import de.roo.ui.swing.common.PrecisionProgressBarUI;
import de.roo.ui.swing.exLAF.Colors;

public class RooMainLoadProgressBarUI extends PrecisionProgressBarUI {
	
	static final int BG_FG_PADDING = 2;
	
    public void installUI(JComponent c) {
    	super.installUI(c);
    	c.setOpaque(false);
    	c.setPreferredSize(new Dimension(10, 10));
    }
    
    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	
    	JPrecisionProgressBar bar = ((JPrecisionProgressBar)c);
    	
    	int usableAreaOriginX = 0;
    	int usableAreaOriginY = 0;
    	int usableHeight = c.getHeight();
    	int usableWidth = c.getWidth();
    	
    	paintProgressBarBackground(g, c, usableAreaOriginX, usableAreaOriginY, usableWidth, usableHeight);
    	
    	int usableAreaFGOriginX = usableAreaOriginX + BG_FG_PADDING;
    	int usableAreaFGOriginY = usableAreaOriginY + BG_FG_PADDING;
    	int usableFGHeight = usableHeight - 2*BG_FG_PADDING;
    	int usableFGWidth = (int) Math.round((usableWidth - 2*BG_FG_PADDING)*bar.getProgress());
    	
    	paintProgressBarForeground(g, c, usableAreaFGOriginX, usableAreaFGOriginY, usableFGWidth, usableFGHeight);
    	
    	super.paint(g, c);
    }
    
	private void paintProgressBarBackground(Graphics g, JComponent c, int usableAreaOriginX,
			int usableAreaOriginY, int usableWidth, int usableHeight) {
		GradientPaint gp = new GradientPaint(
				0, usableAreaOriginY, Colors.progressBarBGGradient0,
				0, usableHeight, Colors.progressBarBGGradient1);
		((Graphics2D)g).setPaint(gp);
		
		g.fillRoundRect(usableAreaOriginX, usableAreaOriginY, usableWidth, usableHeight, usableHeight, usableHeight);
	
	}
	
	private void paintProgressBarForeground(Graphics g, JComponent c, int usableAreaOriginX,
			int usableAreaOriginY, int usableWidth, int usableHeight) {
		GradientPaint gp = new GradientPaint(
				0, usableAreaOriginY, Colors.progressBarFGGradient0,
				0, usableHeight, Colors.progressBarFGGradient1);
		((Graphics2D)g).setPaint(gp);
		g.fillRoundRect(usableAreaOriginX, usableAreaOriginY, usableWidth, usableHeight, usableHeight, usableHeight);
	}


	
}
