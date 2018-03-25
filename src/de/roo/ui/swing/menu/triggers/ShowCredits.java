package de.roo.ui.swing.menu.triggers;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;
import de.roo.ui.swing.CreditsWindow;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ShowCredits extends Trigger {
	
	public static final ImageIcon ICON = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/buttons/Credits.png"));
	
	CreditsWindow w = null;
	private IWritableConf conf;
	private ILog log;
	
	public ShowCredits(IWritableConf conf, ILog log) {
		this.conf = conf;
		this.log = log;
	}
	
	@Override
	public void execute() {
		if (w == null) w = new CreditsWindow(conf, log);
		w.setVisible(true);
	}

	@Override
	public String getDesc() {
		return "About Kangee";
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

}
