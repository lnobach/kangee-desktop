package de.roo.ui.swing.logging;

import de.roo.configuration.IWritableConf;
import de.roo.logging.HistoryLog;
import de.roo.logging.ListenableLog;
import de.roo.logging.ListenableLog.ILogListener;
import de.roo.logging.ObjectLog.LogEntry;
import de.roo.logging.ObjectLog.LogType;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LogViewer {
	
	private HistoryLog log;
	private Listener ourLogListener = null;
	
	public static final int DEBUG = 2;
	public static final int WARNING = 1;
	public static final int ERROR = 0;

	public static final int LOWEST_SEVERITY = DEBUG;
	
	LogViewerWindow currentWnd = null;
	private IWritableConf conf = null;
	private boolean enableAutoShow = true;
	
	public LogViewer(HistoryLog log) {
		this.log = log;
	}
	
	public void setAutoShow(boolean autoShow) {
		this.enableAutoShow = autoShow;
	}
	
	public void setAutoShowOnError(int severitylevel) {
		if (ourLogListener != null) {
			log.removeListener(ourLogListener);
		}
		if (severitylevel <= LOWEST_SEVERITY) {
			log.addListener(this.new Listener(severitylevel));
		} else ourLogListener = null;
	}
	
	class Listener implements ILogListener {

		private int l;

		Listener(int l) {
			this.l = l;
		}
		
		@Override
		public void newEntryArrived(LogEntry e, ListenableLog log) {
			boolean shallShow = l >= DEBUG && e.getType() == LogType.Debug ||
					l >= WARNING && e.getType() == LogType.Warn ||
					l >= ERROR && e.getType() == LogType.Error;
			if (enableAutoShow && shallShow) show();
			add(e);
		}
		
	}
	
	public void show() {
		if (currentWnd == null) {
			currentWnd = new LogViewerWindow(log, this, conf);
		}
	}
	
	private void add(LogEntry e) {
		if (currentWnd != null) currentWnd.addEntry(e);
	}
	
	void windowClosed() {
		currentWnd.saveSettings();
		currentWnd.dispose();
		currentWnd = null;
	}
	
	public void setConf(IWritableConf conf) {
		this.conf  = conf;
	}
	
}
