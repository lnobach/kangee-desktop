package de.roo.ui.swing.exLAF.main;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIToolkit;

public class RooMainConnStatusBarUI extends PanelUI {

    public void installUI(JComponent c) {
    	super.installUI(c);
    }
    
    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	
    	UIToolkit.paintGradientBG(c, g, Colors.backgroundHighElementGrad0, Colors.backgroundHighElementGrad1);
    	
    }
	
}
