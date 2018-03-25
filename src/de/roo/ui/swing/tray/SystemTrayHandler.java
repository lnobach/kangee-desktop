package de.roo.ui.swing.tray;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TrayIcon;

import de.roo.BuildConstants;
import de.roo.logging.ILog;
import de.roo.ui.swing.RooEngineGUI;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SystemTrayHandler {

	static SystemTray tray = SystemTray.getSystemTray();
	
	RooTrayIcon trayIcon;
	
	public SystemTrayHandler(final RooEngineGUI eng) throws RooTrayException {
		try {
			trayIcon = new RooTrayIcon(eng, tray.getTrayIconSize());
			trayIcon.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					eng.showWindow();
				}
				
			});
			tray.add(trayIcon);
		} catch (AWTException e) {
			throw new RooTrayException(e);
		}
		
	}
	
	public void close() {
		tray.remove(trayIcon);
	}
	
	public static boolean trayIsSupported() {
		return SystemTray.isSupported();
	}

	public void showNotification(String message, ILog log) {
		trayIcon.displayMessage(BuildConstants.PROD_TINY_NAME, message, TrayIcon.MessageType.INFO);
	}
	
}
