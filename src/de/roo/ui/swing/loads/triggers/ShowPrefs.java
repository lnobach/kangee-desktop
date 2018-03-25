package de.roo.ui.swing.loads.triggers;

import javax.swing.Icon;

import de.roo.configuration.IWritableConf;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.ui.swing.loads.LoadPrefs;
import de.roo.ui.swing.menu.triggers.Trigger;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ShowPrefs extends Trigger {

	private IAbstractLoad load;
	private ILog log;
	private IWritableConf conf;
	private LoadPrefs prefs;

	public ShowPrefs(IAbstractLoad load, ILog log, IWritableConf conf) {
		this.load = load;
		this.log = log;
		this.conf = conf;
	}

	@Override
	public void execute() {
		if (prefs == null) prefs = new LoadPrefs(load, log, conf);
		prefs.setVisible(true);
		
	}

	@Override
	public String getDesc() {
		return "Show Preferences";
	}

	@Override
	public Icon getIcon() {
		return RooStaticIconBuffer.PROPS_ICON;
	}
	
}
