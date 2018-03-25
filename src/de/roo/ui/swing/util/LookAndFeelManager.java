package de.roo.ui.swing.util;

import java.util.Arrays;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.roo.configuration.IConf;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LookAndFeelManager {
	
	static final String confKey = "SwingLookAndFeel";
	
	public static void setLookAndFeelAuto(ILog log, IConf conf) {
		logAllLaFs(log);
		String sel = conf.getValueString(confKey, "auto");
		if ("auto".equals(sel)) trySetLookAndFeel(getRecommendedLaF(log), log);
		else try {
			UIManager.setLookAndFeel(sel);
		} catch (UnsupportedLookAndFeelException e) {
			fallbackAuto(log, sel, e);
		} catch (ClassNotFoundException e) {
			fallbackAuto(log, sel, e);
		} catch (InstantiationException e) {
			fallbackAuto(log, sel, e);
		} catch (IllegalAccessException e) {
			fallbackAuto(log, sel, e);
		}
		
	}
	
	private static void fallbackAuto(ILog log, String sel, Throwable cause) {
		log.warn(LookAndFeelManager.class, "Could not load look-and-feel " + sel + " Trying to load recommended look-and-feel", cause);
		trySetLookAndFeel(getRecommendedLaF(log), log);
	}
	
	public static void trySetLookAndFeel(String sel, ILog log) {
		try {
			UIManager.setLookAndFeel(sel);
			log.dbg(LookAndFeelManager.class, "Successfully loaded look-and-feel " + sel);
		} catch (UnsupportedLookAndFeelException e) {
			err(log, sel, e);
		} catch (ClassNotFoundException e) {
			err(log, sel, e);
		} catch (InstantiationException e) {
			err(log, sel, e);
		} catch (IllegalAccessException e) {
			err(log, sel, e);
		}
	}
	
	static void err(ILog log, String sel, Throwable cause) {
		log.error(LookAndFeelManager.class, "Could not load look-and-feel " + sel + ". Will continue with look and feel set by default.", cause);
	}

	public static String getRecommendedLaF(ILog log) {
		return UIManager.getSystemLookAndFeelClassName();
	}

	public static void logAllLaFs(ILog log) {
		StringBuffer buf = new StringBuffer();
		buf.append("The following look-and-feels were found: \n");
		for (UIManager.LookAndFeelInfo info : getAllLookAndFeels()) {
			buf.append(info);
			buf.append("\n");
		}
		log.dbg(LookAndFeelManager.class, buf);
	}
	
	public static List<UIManager.LookAndFeelInfo> getAllLookAndFeels() {
		return Arrays.asList(UIManager.getInstalledLookAndFeels());
	}

}
