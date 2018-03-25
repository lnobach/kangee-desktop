package de.roo.ui.swing.configuration;

import javax.swing.JComponent;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public interface IConfigTab {

	public JComponent getComponent();
	
	public void commitChanges();
	
	public void refreshSettings();
	
	public String getTitle();
	
}
