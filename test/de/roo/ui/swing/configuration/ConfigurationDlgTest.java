package de.roo.ui.swing.configuration;

import java.io.File;
import java.io.IOException;

import de.roo.configuration.Configuration;
import de.roo.configuration.IWritableConf;
import de.roo.logging.ConsoleLog;
import de.roo.logging.ILog;
import de.roo.ui.swing.configuration.ConfigurationEditor.IConfEditorListener;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConfigurationDlgTest {

	static IWritableConf conf;
	static final File confFile = new File("test.conf");
	
	public static void main(String[] args) {

		conf = Configuration.loadFromFileIfThere(confFile);
		ILog log = new ConsoleLog();
		ConfigurationEditor edit = new ConfigurationEditor(conf, log);
		edit.addListener(new ConfEditListenerImpl());
		edit.show(null);
		
	}
	
	
	static class ConfEditListenerImpl implements IConfEditorListener {

		@Override
		public void confDlgClosed() {
			try {
				Configuration.saveToFile(confFile, conf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
