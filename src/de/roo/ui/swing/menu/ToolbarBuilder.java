package de.roo.ui.swing.menu;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.roo.configuration.IWritableConf;
import de.roo.engine.FileHandler;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.exLAF.main.RooMainToolbarUI;
import de.roo.ui.swing.menu.triggers.AddDownload;
import de.roo.ui.swing.menu.triggers.AddUpload;
import de.roo.ui.swing.menu.triggers.Reconnect;
import de.roo.ui.swing.menu.triggers.ShowCredits;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.ui.swing.menu.triggers.ViewConf;
import de.roo.ui.swing.menu.triggers.ViewLog;
import de.roo.ui.swing.util.DropBoxLayout;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ToolbarBuilder {
	
	//JToolBar tb;
	JPanel tb;  //Using JPanel instead of the dedicated JToolBar, 
				//as tool bars arise cross-platform UI compatibility issues
	
	public ToolbarBuilder(RooEngineGUI eng, Component parent) {

		FileHandler hndlr = eng.getFileHandler();
		ILog log = eng.getLog();
		final IWritableConf conf = eng.getConfiguration();
		
		Trigger upload = new AddUpload(hndlr, log, parent, conf);
		Trigger download = new AddDownload(hndlr);
		Trigger reconnect = new Reconnect(eng.getConnStateProvider());
		Trigger showLog = new ViewLog(eng.getLogViewer());
		Trigger viewConf = new ViewConf(eng.getConfEditor(), eng);
		Trigger showCredits = new ShowCredits(conf, log);
		

		
		tb = new JPanel();
		tb.setUI(new RooMainToolbarUI());
		
		tb.setLayout(new DropBoxLayout(DropBoxLayout.MODE_WRAP, DropBoxLayout.ORIENTATION_HORIZONTAL));
		
		tb.add(new TriggerToolbarButton(upload));
		tb.add(new TriggerToolbarButton(download));
		tb.add(new TriggerToolbarButton(reconnect, false, true));
		tb.add(new TriggerToolbarButton(viewConf, false, true));
		tb.add(new TriggerToolbarButton(showLog, false, true));
		tb.add(new TriggerToolbarButton(showCredits, false, true));
		
	}

	public Component getComponent() {
		return tb;
	}
	
}
