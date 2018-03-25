package de.roo.ui.swing.exLAF.main;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicPanelUI;

import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIToolkit;

public class RooMainToolbarUI extends BasicPanelUI {

    public void installUI(JComponent c) {
    	//c.setPreferredSize(new Dimension(32, 32));
    	super.installUI(c);
    }
    
    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    }
    
    public void paint(Graphics g, JComponent c) {
    	UIToolkit.paintGradientBG(c, g, Colors.backgroundHighElementGrad0, Colors.backgroundHighElementGrad1);
    	super.paint(g, c);
    }
	
}
