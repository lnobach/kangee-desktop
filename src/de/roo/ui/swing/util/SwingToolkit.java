package de.roo.ui.swing.util;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SwingToolkit {

	/**
	 * Forces a SWING component to be square.
	 * @param comp
	 */
	public static void forceSquare(JComponent comp, boolean minimize) {
		Dimension pref = comp.getPreferredSize();
		int maxPref = (int)Math.rint(minimize?Math.min(pref.getHeight(), pref.getWidth()):
			Math.max(pref.getHeight(), pref.getWidth()));
		comp.setPreferredSize(new Dimension(maxPref, maxPref));
	}

}
