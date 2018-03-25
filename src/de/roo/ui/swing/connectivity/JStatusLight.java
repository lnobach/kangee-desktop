package de.roo.ui.swing.connectivity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class JStatusLight extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6496992901746970149L;

	private Color color;
	
	static final int PAD = 2;

	public JStatusLight(Color initColor) {
		this.setPreferredSize(new Dimension(20, 20));
		this.color = initColor;
	}
	
	public void setLightColor(Color c) {
		this.color = c;
		this.repaint();
	}
	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval(PAD, PAD, this.getHeight()-PAD, this.getWidth()-PAD);
	}
	
}
