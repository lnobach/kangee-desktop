package de.roo.ui.swing.configuration;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import de.roo.configuration.DefaultConfiguration;
import de.roo.configuration.IWritableConf;
import de.roo.logging.ConsoleLog;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class WeakTypeConfEditorTest {

	public static void main(String[] args) {
	
		ILog log = new ConsoleLog();
		
		Map<String, String> inputVals = new HashMap<String, String>();
		inputVals.put("BooleanTest", "true");
		inputVals.put("StringTest", "bla");
		inputVals.put("IntegerTest", "12345");
	
		IWritableConf conf = new DefaultConfiguration(inputVals);
		
		Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		types.put("BooleanTest", Boolean.class);
		types.put("IntegerTest", Integer.class);
		
		JFrame f = new JFrame("Blargh");
		f.setSize(400, 400);
		WeakTypingConfEditor e = new WeakTypingConfEditor(new ConfigurationContextImpl(conf, log), types);
		
		f.setLayout(new BorderLayout());
		f.add(e.getComponent(), BorderLayout.CENTER);
		
		f.setVisible(true);
		
	}
	
	static class ConfigurationContextImpl implements IConfigurationContext {

		public IWritableConf conf;
		private ILog log;
		
		public ConfigurationContextImpl(IWritableConf conf, ILog log) {
			this.conf = conf;
			this.log = log;
		}
		
		@Override
		public void confDlgClosed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IWritableConf getConf() {
			return conf;
		}

		@Override
		public ILog getLog() {
			return log;
		}
		
	}
	
}
