package de.roo.ui.plafEnv.linux;

import de.roo.logging.ConsoleLog;
import de.roo.logging.ILog;
import de.roo.ui.plafEnv.linux.LinuxNotifier;

public class LinuxNotificationTest {

	
	public static void main (String[] args) {
		
		ILog log = new ConsoleLog();
		LinuxNotifier notifier = new LinuxNotifier();
		
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				//Nothing to do
			}
			System.out.println("Sending notification number " + i + ".");
			notifier.makeNotification("Notification number " + i, log);
		}
		
	}
	
	
}
