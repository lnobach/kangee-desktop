package de.roo.ui.swing.exLAF.detail;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

import de.roo.logging.ILog;
import de.roo.ui.swing.exLAF.BackgroundArt;
import de.roo.ui.swing.exLAF.UIToolkit;
import de.roo.ui.swing.exLAF.UIToolkit.Orientation;

public class RooDetailViewBackgroundPaneUI extends PanelUI {

	private BackgroundArt bgart;
	private ILog log;

	public RooDetailViewBackgroundPaneUI(ILog log) {
		bgart = new BackgroundArt();
		this.log = log;
	}
	
    public void installUI(JComponent c) {
    	super.installUI(c);
    }
    
    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	bgart.paintBackgroundArt(g, c.getWidth(), c.getHeight(), log);
    	UIToolkit.paintBorderShadow(c, g, new Color(0, 0, 0, 120), 5, 200, Orientation.Top);
    	//UIToolkit.paintBorderShadow(c, g, new Color(0, 0, 0, 120), 5, 200, Orientation.Bottom);
    	super.paint(g, c);
    }
	
}
