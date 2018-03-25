package de.roo.ui.swing.util;

import javax.swing.JTextArea;

import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LogBox extends JTextArea implements ILog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2169554638072204895L;

	private ILog subLog;

	public LogBox(ILog subLog) {
		this.subLog = subLog;
	}

	public void addLine(String line) {
		this.setText(this.getText() + line + '\n');
	}

	public void clear() {
		this.setText(null);
	}

	@Override
	public void dbg(Object src, Object msg) {
		if (subLog != null)
			subLog.dbg(src, msg);
		addLine(String.valueOf(msg));
	}

	@Override
	public void error(Object src, Object msg) {
		if (subLog != null)
			subLog.error(src, msg);
		addLine("Error:" + String.valueOf(msg));
	}

	@Override
	public void error(Object src, Object msg, Throwable ex) {
		if (subLog != null)
			subLog.error(src, msg, ex);
		addLine("Error:" + String.valueOf(msg) + ", " + ex.getLocalizedMessage());
	}

	@Override
	public void warn(Object src, Object msg) {
		if (subLog != null)
			subLog.warn(src, msg);
		addLine("Warning:" + String.valueOf(msg));
	}

	@Override
	public void warn(Object src, Object msg, Throwable ex) {
		if (subLog != null)
			subLog.warn(src, msg, ex);
		addLine("Warning:" + String.valueOf(msg) + ", " + ex.getLocalizedMessage());
	}

}
