package de.roo.ui.plafEnv.linux;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.roo.logging.ILog;
import de.roo.util.stream.StreamCopy;

public class LinuxNotifier {

	static final String NOTIFY_SEND_COMMAND = "notify-send";
	static final File relIconPath = new File("icon.png");
	private String relIconPathStr;
	
	public LinuxNotifier() {
		relIconPathStr = relIconPath.exists()?relIconPath.getAbsolutePath():"gtk-add";
	}
	
	public void makeNotification(final String message, final ILog log) {
		new Thread("NotifyTrigger") {
			
			public void run() {
				try {
					String[] cmdArray = new String[] {
							NOTIFY_SEND_COMMAND,
							"--icon=" + relIconPathStr,
							message
					};
					//System.out.println("Command is: " + Arrays.toString(cmdArray));
					Process rt = Runtime.getRuntime().exec(cmdArray);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					new StreamCopy().copy(rt.getInputStream(), os);
					os.close();
					int exitCode = rt.waitFor();
					if (exitCode != 0) {
						log.warn(LinuxNotifier.this, "Could not make notification '" + message
								+ "'. Process returned exit code " + exitCode
								+ ". Output was: " + os.toString());
					}
				} catch (IOException e) {
					log.warn(LinuxNotifier.this, "Could not make notification '" + message + "'", e);
				} catch (InterruptedException e) {
					log.warn(LinuxNotifier.this, "Could not make notification '" + message + "'", e);
				}
			}
			
		}.start();
	}
	
}
