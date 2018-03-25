package de.roo.ui.swing.configuration;

import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public interface IConfigurationContext {

	public IWritableConf getConf();
	
	public void confDlgClosed();

	public ILog getLog();
	
}
