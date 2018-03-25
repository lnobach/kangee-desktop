package de.roo.ui.plafEnv;

import de.roo.logging.ILog;
import de.roo.ui.plafEnv.linux.LinuxNotifier;
import de.roo.ui.plafEnv.mac.MacOSXEnvironment;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.tray.RooTrayException;
import de.roo.ui.swing.tray.SystemTrayHandler;
import de.roo.ui.swing.util.OSDeterminator;
import de.roo.ui.swing.util.OSDeterminator.OS;

public class PlatformSpecials {

	/**
	 * Initializes platform-specific UI behavior for the application.
	 * The behavior may augment user experience, but should not be
	 * required by the application to work correctly.
	 * 
	 * @param eng
	 */
	public static void initialize(final RooEngineGUI eng) {
		
		final OS os = OSDeterminator.getOS();
		
		SystemTrayHandler trayhdlr = null;
		
		/* OSes with System Tray */
		
		boolean trayWanted = !eng.getConfiguration().getValueBoolean("Disable_Tray", false);
		if (trayWanted) {
			if (os == OS.Windows || os == OS.Linux || os == OS.Other) {
				//Not on Mac OS X, here, the dock is used.
				if (SystemTrayHandler.trayIsSupported()){  
					try {
						trayhdlr = new SystemTrayHandler(eng);
						eng.setKeepOpenInBackground(true);
					} catch (RooTrayException e) {
						eng.getLog().error(PlatformSpecials.class, "Could not create tray icon.", e);
					}
				}
			} else {
				//Mac OS X
				eng.setKeepOpenInBackground(true);
				//App can be stopped in the dock.
			}
			
		}
		
		final SystemTrayHandler trayhdlrFinal = trayhdlr;
		
		/* Mac OS X */
		if (os == OS.Mac) {
			new MacOSXEnvironment().initialize(eng, trayWanted);
		}
		
		/* Operating systems with notification support */
		
		if (os == OS.Linux || os == OS.Windows) {
			new AbstractNotifier(eng) {
				
				LinuxNotifier lnxNotify = null;
				
				@Override
				protected void makeNotification(String message, ILog log) {
					
					boolean notificationWanted = !eng.getConfiguration().getValueBoolean("DisableNotifications", false);
					
					if (notificationWanted) {
					
						log.dbg(this, "Making notification: '" + message + "'...");
						
						if (os == OS.Windows && trayhdlrFinal != null) {
							trayhdlrFinal.showNotification(message, log);
						} else if (os == OS.Linux) {
							if (lnxNotify == null) lnxNotify = new LinuxNotifier();
							lnxNotify.makeNotification(message, log);
						}
					}
						
				}
			};
		}
		
	}
	
}
