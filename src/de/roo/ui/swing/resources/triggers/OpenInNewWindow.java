package de.roo.ui.swing.resources.triggers;

import javax.swing.Icon;

import de.roo.gfx.RooStaticIconBuffer;
import de.roo.model.uiview.IRooResource;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.ui.swing.resources.ResourcePrefs;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class OpenInNewWindow extends Trigger {

	private RooEngineGUI eng;
	private IRooResource res;
	
	ResourcePrefs prefs = null;

	public OpenInNewWindow(IRooResource res, RooEngineGUI eng) {
		this.res = res;
		this.eng = eng;
	}
	
	@Override
	public void execute() {
		if (prefs == null) prefs = new ResourcePrefs(res, eng);
		prefs.setVisible(true);
	}

	@Override
	public String getDesc() {
		return "Open in New Window";
	}

	@Override
	public Icon getIcon() {
		return RooStaticIconBuffer.PROPS_ICON;
	}
	
}
