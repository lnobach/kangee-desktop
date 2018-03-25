package de.roo.ui.swing.menu.triggers;

import javax.swing.Icon;

import de.roo.gfx.RooStaticIconBuffer;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.configuration.ConfigurationEditor;
import de.roo.ui.swing.configuration.ConfigurationEditor.IConfEditorListener;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ViewConf extends Trigger implements IConfEditorListener {

	private ConfigurationEditor e;
	private RooEngineGUI owner;

	public ViewConf(ConfigurationEditor e, RooEngineGUI owner) {
		this.e = e;
		e.addListener(this);
		this.owner = owner;
	}
	
	@Override
	public void execute() {
		e.show(owner.getMainWindow());
	}

	@Override
	public String getDesc() {
		return "Open Configuration...";
	}

	@Override
	public Icon getIcon() {
		return RooStaticIconBuffer.PROPS_ICON;
	}

	@Override
	public void confDlgClosed() {
		owner.reconfigure();
	}

}
