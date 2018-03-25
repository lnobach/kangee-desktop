package de.roo.ui.swing.exLAF;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class UIToolkit {
	
	public enum Orientation {
		Top,
		Left,
		Bottom,
		Right;
	}
	
	public static void paintGradientBG(Component comp, Graphics g, Color color1, Color color2) {
		GradientPaint gp = new GradientPaint(
				0, 0, color1,
				0, comp.getHeight(), color2);
			((Graphics2D)g).setPaint(gp);
			g.fillRect(0, 0, comp.getWidth(), comp.getHeight());
	}
	
	public static void paintRoundGradientBG(Component comp, Graphics g, Color color1, Color color2, int arcSize, int borderDistance) {
		GradientPaint gp = new GradientPaint(
				0, 0, color1,
				0, comp.getHeight(), color2);
			((Graphics2D)g).setPaint(gp);
			g.fillRoundRect(borderDistance, borderDistance, comp.getWidth()-2*borderDistance, comp.getHeight()-2*borderDistance, arcSize, arcSize);
	}
	
	public static void paintBorderShadow(Component comp, Graphics g, Color shadowClr, int virtHeight, int limit, Orientation o) {
		
		for (int i = 0; i < limit; i++) {
			
			double p = 1d - Math.atan2(i, virtHeight) / Math.PI * 2;
			int alpha = (int)Math.round(p*256-1);
			g.setColor(new Color(shadowClr.getRed(), shadowClr.getGreen(), shadowClr.getBlue(), alpha*shadowClr.getAlpha()/256));
			if (o == Orientation.Top) {
				g.drawLine(0, i, comp.getWidth(), i);
			} else if (o == Orientation.Left) {
				g.drawLine(i, 0, i, comp.getHeight());
			} else if (o == Orientation.Right) {
				int width = comp.getWidth();
				g.drawLine(width-i, 0, width-i, comp.getHeight());
			} else {
				int height = comp.getHeight();
				g.drawLine(0, height-i, comp.getWidth(), height-i);
			}
			
		}
		
		
		
	}

	public static void paintPlainBG(JComponent comp, Graphics g,
			Color c) {
		g.setColor(c);
		g.fillRect(0, 0, comp.getWidth(), comp.getHeight());
	}
	
	public static void paintRoundBG(Component comp, Graphics g, Color c, int arcSize, int borderDistance) {
		g.setColor(c);
		g.fillRoundRect(borderDistance, borderDistance, comp.getWidth()-2*borderDistance, comp.getHeight()-2*borderDistance, arcSize, arcSize);
	}
	
}



