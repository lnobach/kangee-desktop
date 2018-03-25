package de.roo.ui.swing.logging;

import de.roo.logging.ConsoleLog;
import de.roo.logging.HeapLog;
import de.roo.logging.HistoryLog;
import de.roo.logging.ILog;
import de.roo.logging.LogTree;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LogViewerTest {

	public static void main(String[] args) {
		
		HistoryLog hLog = new HeapLog(1024);
		LogViewer viewer = new LogViewer(hLog);
		viewer.setAutoShowOnError(LogViewer.ERROR);

		ILog cLog = new ConsoleLog();
		
		LogTree log = new LogTree();
		log.addLog(hLog);
		log.addLog(cLog);
		
		writeSomething(log);
		
	}
	
	public static void writeSomething(ILog log) {
		log.dbg("Source1", "Blablabla");
		log.dbg("Source2", "Blablabla2");
		log.warn("Source1", "Warning");
		log.error("Source3", "Schmalzt");
		log.error("Source3", "Errooorrr", new RuntimeException("Blaaaargh"));
		sleepRandom();
		log.dbg("S1", "erster zeitversetzter Fehler");
		sleepRandom();
		log.dbg("S2", "Blablablubbla");
		sleepRandom();
		log.warn("S1", "Tils");
		log.error("S3", "Tuls");
		sleepRandom();
		log.error("S3", "Errooorrr2", new RuntimeException("Blaaaargh2"));
		sleepRandom();
		log.dbg("C1", "erster zeitversetzter Fehler");
		sleepRandom();
		log.dbg("C2", "Blablablubbla");
		sleepRandom();
		log.warn("C1", "Tils");
		log.error("C3", "Tuls");
		sleepRandom();
		log.error("C3", "Errooorrr2", new RuntimeException("Blaaaargh3"));
		sleepRandom();
		log.dbg("U1", "erster zeitversetzter Fehler");
		sleepRandom();
		log.dbg("U2", "Blablablubbla");
		sleepRandom();
		log.warn("U1", "Tils");
		log.error("U3", "Tuls");
		sleepRandom();
		log.error("U3", "Errooorrr2", new RuntimeException("Blaaaargh4"));
		log.dbg("Ende", "Ende");
	}
	
	public static void sleepRandom() {
		try {
			Thread.sleep((long)(Math.random() * 2000));
		} catch (InterruptedException e) {
			//Nothing to do
		}
	}
	
}
