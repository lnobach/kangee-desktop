package de.roo.ui.swing.logging;

import de.roo.logging.ILog;
import de.roo.logging.ObjectLog.LogEntry;
import de.roo.ui.swing.HTMLRichTextPane;
import de.roo.util.ExceptionToolkit;
import de.roo.util.HTMLToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LogEntryViewer extends HTMLRichTextPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1697664173238702012L;

	public LogEntryViewer(ILog log, LogEntry e) {
		super(log);
		showEntry(e);
	}
	
	public void showEntry(LogEntry e) {
		if (e != null) this.setText(getHTMLCode(e));
		else this.setText(null);
	}

	private String getHTMLCode(LogEntry e) {
		String result = "<html><b>Source:</b><br />" + e.getSource()
			+ "<br /><b>Message:</b><br /> " + enc(e.getMessage());
		if (e.getException() != null) {
			result += "<br /><b>Exception:</b><br /> <font color=\"FF0000\">" + enc(ExceptionToolkit.getStackTraceString(e.getException())) + "</font>";
		
		}
		result += "</html>";
		return result;
	}
	
	String enc(Object source) {
		return HTMLToolkit.encodeSpecialChars(String.valueOf(source));
	}

}
