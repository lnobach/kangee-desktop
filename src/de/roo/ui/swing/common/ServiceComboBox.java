package de.roo.ui.swing.common;

import javax.swing.JComboBox;

import de.roo.BuildConstants;
import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ServiceComboBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8520060239806384658L;
	
	private IWritableConf conf;
	//private ILog log;

	public ServiceComboBox(IWritableConf conf, ILog log) {
		super(getDiscoveryURLs(conf, log));
		this.conf = conf;
		//this.log = log;
		this.setEditable(true);
		refreshSettings();
	}
	
	public static String[] getDiscoveryURLs(IConf conf, ILog log) {
		return BuildConstants.Default_Discovery_URLs;
	}
	
	
	public String getSelectedDiscoveryURL() {
		return (String)this.getEditor().getItem();
		//return (String)this.getSelectedItem();
	}
	
	public void setDiscoveryURL(String url) {
		this.setSelectedItem(url);
	}
	
	public void refreshSettings() {
		this.setSelectedItem(conf.getValueString(getDiscoveryURLConfKey(), BuildConstants.Default_Discovery_URLs[0]));
	}
	
	public void commitChanges() {
		//System.out.println("Setting value for service URL to " + getSelectedDiscoveryURL());
		this.
		conf.setValue(getDiscoveryURLConfKey(), getSelectedDiscoveryURL());
	}
	
	String getDiscoveryURLConfKey() {
		return "Discovery_URL";
	}
	
}
