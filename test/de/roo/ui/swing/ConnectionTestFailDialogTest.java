package de.roo.ui.swing;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.roo.configuration.DefaultConfiguration;
import de.roo.logging.ConsoleLog;
import de.roo.logging.ILog;
import de.roo.ui.swing.ConnectionTestFailDialog.Choice;
import de.roo.ui.swing.util.LookAndFeelManager;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConnectionTestFailDialogTest {

	public static void main(String[] args) {
		ILog log = new ConsoleLog();
		
		LookAndFeelManager.setLookAndFeelAuto(log, new DefaultConfiguration());
		ConnectionTestFailDialog dlg;
		try {
			dlg = new ConnectionTestFailDialog(0, InetAddress.getByName("192.168.0.1"), null, "http://www.bla.box", log, true);
			Choice choice = dlg.showAndWait();
			System.out.println("Choice was: " + choice + ", disable conn tests: " + dlg.shallDisableSubsequentConnTests());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
}
