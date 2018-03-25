package de.roo.ui.swing.exLAF.resources;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIToolkit;

public class RooResourceViewUI extends PanelUI {

	public RooResourceViewUI() {
	}
	
    public void installUI(JComponent c) {
    	c.setOpaque(false);
    	super.installUI(c);
    }
    
    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	//bgart.paintBackgroundArt(g, c.getWidth(), c.getHeight(), log);
    	//UIToolkit.paintBorderShadow(c, g, new Color(0, 0, 0, 120), 5, 200, Orientation.Top);
    	//UIToolkit.paintBorderShadow(c, g, new Color(0, 0, 0, 120), 5, 200, Orientation.Bottom);
    	UIToolkit.paintRoundGradientBG(c, g, Colors.backgroundHighElementGrad0, Colors.backgroundHighElementGrad1, 10, 0);
    	super.paint(g, c);
    }
	
}
