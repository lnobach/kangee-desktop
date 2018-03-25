package de.roo.ui.swing;

import de.roo.engine.UpdateCheckException;
import de.roo.engine.UpdateChecker;
import de.roo.engine.UpdateChecker.UpdateInfo;
import de.roo.logging.ConsoleLog;
import de.roo.logging.ILog;
import de.roo.ui.swing.UpdateNotificationDialog.Choice;

public class UpdateNotificationDialogTest {

	public static void main(String[] args) {
		
		try {
			UpdateChecker chk = new UpdateChecker("http://getkangee.com/api/updateTest.xml", "http://getkangee.com/getkangee");
			ILog log = new ConsoleLog();
			UpdateInfo info;
			info = chk.checkStrict(log);
			System.out.println("Update info generated: " + info);
			UpdateNotificationDialog dlg = new UpdateNotificationDialog(info, log);
			Choice result = dlg.showAndWait();
			System.out.println("Choice was: " + result + ". Shall disable subsequent tests: " + dlg.shallDisableSubsequentUpdateChecks());
		} catch (UpdateCheckException e) {
		}
		
	}
	
}
