package de.roo.ui.swing.menu.triggers;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.roo.ui.swing.logging.LogViewer;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ViewLog extends Trigger {

	public static final ImageIcon ICON = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/buttons/Log.png"));
	
	private LogViewer viewer;

	public ViewLog(LogViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void execute() {
		viewer.show();
	}

	@Override
	public String getDesc() {
		return "Show log";
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

}
