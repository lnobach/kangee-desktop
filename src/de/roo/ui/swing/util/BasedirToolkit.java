package de.roo.ui.swing.util;

import java.io.File;

/**
 * Toolkit to get a proper base directory
 * 
 * @author Leo Nobach
 *
 */
public class BasedirToolkit {

	/**
	 * Returns the basedir for application data that shall be used.
	 * May not exist, has to be made.
	 * @param appName
	 * @return
	 */
	public static File getApplicationBasedir(String appName) {
		//Windows Vista, 7
		File basedir = basedir(System.getenv("LOCALAPPDATA"), appName);
		if (basedir != null) return basedir;
		//Windows XP
		basedir = basedir(System.getenv("APPDATA"), appName);
		if (basedir != null) return basedir;
		
		String userHomeBasedir = System.getProperty("user.home");
		String os = System.getProperty("os.name").toLowerCase();
		//Mac
	    if (os.indexOf( "mac" ) >= 0) {
	    	basedir = basedir(new File(userHomeBasedir, "Library").getAbsolutePath(), appName);
	    	if (basedir != null) return basedir;
	    }
		//*nix
		basedir = basedir(userHomeBasedir, "." + appName.toLowerCase());
		return basedir;
	}
	
	static File basedir(String basedirTrunk, String subdir) {
		if (basedirTrunk == null) return null;
		File basedir = new File(basedirTrunk);
		if (!basedir.exists() || !basedir.isDirectory()) return null;
		return new File(basedir, subdir);
	}
	
}
