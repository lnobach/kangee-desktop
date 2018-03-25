package de.roo.ui.swing.configuration;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConfigurationEditor implements IConfigurationContext {

	private IWritableConf conf;
	ConfigurationWindow wnd = null;
	private ILog log;

	public ConfigurationEditor(IWritableConf conf, ILog log) {
		this.conf = conf;
		this.log = log;
	}
	
	public void show(JFrame parent) {
		if (wnd == null) {
			wnd = new ConfigurationWindow(parent, this, null);
			wnd.setVisible(true);
		}
	}

	@Override
	public IWritableConf getConf() {
		return conf;
	}

	@Override
	public void confDlgClosed() {
		wnd = null;
		for (IConfEditorListener l : listeners) l.confDlgClosed();
	}

	@Override
	public ILog getLog() {
		return log;
	}
	
	List<IConfEditorListener> listeners = new LinkedList<IConfEditorListener>();
	
	
	public void addListener(IConfEditorListener l) {
		listeners.add(l);
	}
	
	public void removeListener(IConfEditorListener l) {
		listeners.remove(l);
	}
	
	public interface IConfEditorListener {
		
		public void confDlgClosed();
		
	}
	
}
