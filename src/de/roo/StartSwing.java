package de.roo;

import de.roo.logging.ConsoleLog;
import de.roo.logging.HeapLog;
import de.roo.logging.HistoryLog;
import de.roo.logging.LogTree;
import de.roo.logging.LogUncaughtExceptionHandler;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.logging.LogViewer;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class StartSwing {

	public static void main(String[] args) {
		
		LogTree log = new LogTree();
		log.addLog(new ConsoleLog());
		HistoryLog histLog = new HeapLog(256);
		log.addLog(histLog);
		LogViewer viewer = new LogViewer(histLog);
		viewer.setAutoShowOnError(LogViewer.ERROR);
		
		Thread.setDefaultUncaughtExceptionHandler(new LogUncaughtExceptionHandler(log, true));
		RooEngineGUI eng = new RooEngineGUI(log);
		eng.setLogViewer(viewer);
		if (!eng.init()) System.exit(1);
		
	}
	
}
