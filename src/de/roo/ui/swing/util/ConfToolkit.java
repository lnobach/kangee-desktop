package de.roo.ui.swing.util;

import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JFrame;

import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConfToolkit {

	public static void saveSettings(JDialog f, IWritableConf conf, String windowIdentifier) {
		saveRectangle(f.getBounds(), conf, windowIdentifier);
	}
	
	public static void saveSettings(JFrame f, IWritableConf conf, String windowIdentifier) {
		saveRectangle(f.getBounds(), conf, windowIdentifier);
	}

	public static void saveRectangle(Rectangle r, IWritableConf conf,
			String rIdentifier) {
		String ra = r.x + "," + r.y + "," + r.width + "," + r.height;
		conf.setValue(rIdentifier, ra);
	}
	
	public static boolean loadSettings(JDialog f, IConf conf, String windowIdentifier, ILog log) {
		Rectangle r = loadRectangle(conf, windowIdentifier, log);
		if (r != null) {
			f.setBounds(r);
			return true;
		}
		return false;
	}
	
	public static boolean loadSettings(JFrame f, IConf conf, String windowIdentifier, ILog log) {
		Rectangle r = loadRectangle(conf, windowIdentifier, log);
		if (r != null) {
			f.setBounds(r);
			return true;
		}
		return false;
	}

	private static Rectangle loadRectangle(IConf conf, String rIdentifier, ILog log) {
		String ra = conf.getValueString(rIdentifier, null);
		if (ra == null) return null;
		String[] tokens = ra.split(",");
		if (tokens.length < 4) {
			log.warn(ConfToolkit.class, "Could not load rectangle settings for rectangle "
					+ rIdentifier + ", since there are only " + tokens.length + "fields in the config key.");
			return null;
		}
		try {
			Rectangle r = new Rectangle(Integer.parseInt(tokens[0]), 
					Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]));
			return r;
		} catch (NumberFormatException e) {
			log.warn(ConfToolkit.class, "Could not load rectangle settings for rectangle "
					+ rIdentifier + ", since there is a number format problem.", e);
			return null;
		}
	}
	
}
