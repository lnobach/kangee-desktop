package de.roo.ui.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SplashPainter {

	SplashScreen splash;
	static final Color BGCOLOR = new Color(0x432e1e);
	static final Color FONTCOLOR = Color.WHITE;
	
	public SplashPainter() {
		splash = SplashScreen.getSplashScreen();
	}
	
	public void writeJob(String text) {
		if (splash == null || !splash.isVisible()) return;
		Graphics2D g = (Graphics2D)splash.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setColor(BGCOLOR);
		g.fillRect(225, 75, 240, 20);
		g.setColor(FONTCOLOR);
		g.drawString(text, 230, 90);
		
		splash.update();
	}
	
	public boolean isSplashActive() {
		return SplashScreen.getSplashScreen() != null;
	}
	
}
