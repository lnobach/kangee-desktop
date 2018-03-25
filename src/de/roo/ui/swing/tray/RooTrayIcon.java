package de.roo.ui.swing.tray;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.TrayIcon;

import de.roo.BuildConstants;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.ui.swing.RooEngineGUI;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooTrayIcon extends TrayIcon {

	public RooTrayIcon(final RooEngineGUI eng, Dimension iconSize) {
		super(getTrayIconImage(iconSize), BuildConstants.PROD_TINY_NAME, new RooTrayMenu(eng));
	}

	private static Image getTrayIconImage(Dimension iconSize) {
		return RooStaticIconBuffer.WND_ICON.getImage().getScaledInstance((int)iconSize.getWidth(), 
				(int)iconSize.getHeight(), 
				java.awt.Image.SCALE_SMOOTH);
	}

}
