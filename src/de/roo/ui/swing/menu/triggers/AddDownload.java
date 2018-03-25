package de.roo.ui.swing.menu.triggers;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.roo.engine.FileHandler;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class AddDownload extends Trigger {

	public static final ImageIcon ICON = new ImageIcon(AddDownload.class.getResource("/de/roo/gfx/buttons/AddDL.png"));
	
	private FileHandler hdlr;

	public AddDownload(FileHandler hdlr) {
		this.hdlr = hdlr;
	}

	@Override
	public void execute() {
		hdlr.createNewDownload();
	}

	@Override
	public String getDesc() {
		return "Add Download";
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

}
