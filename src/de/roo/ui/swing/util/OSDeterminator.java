package de.roo.ui.swing.util;

public class OSDeterminator {

	static OS os;
	
	public enum OS {
		Windows,
		Linux,
		Mac,
		Other;
	}
	
	public static OS getOS() {
		if (os == null) {
			String osProp = System.getProperty("os.name").toLowerCase();
			//System.out.println("OS property: " + osProp);
		    if (osProp.indexOf( "mac" ) >= 0) {
		    	os = OS.Mac;
		    	return os;
		    }
		    if (osProp.indexOf("win") >= 0) {
		    	os = OS.Windows;
		    	return os;
		    }
		    if (osProp.indexOf("nux") >= 0) {
		    	os = OS.Linux;
		    	return os;
		    }
		    if (osProp.indexOf("nix") >= 0) {
		    	os = OS.Linux;
		    	return os;
		    }
		}
		return OS.Other;
	}
	
}
