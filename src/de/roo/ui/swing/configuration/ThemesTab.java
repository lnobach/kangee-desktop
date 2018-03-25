package de.roo.ui.swing.configuration;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import de.roo.ui.swing.themes.ThemeBrowser;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ThemesTab implements IConfigTab {

	ThemeBrowser b;
	private IConfigurationContext ctx;
	
	public ThemesTab(IConfigurationContext ctx) {
		this.ctx = ctx;
		b = new ThemeBrowser(ctx.getConf(), ctx.getLog());
	}

	@Override
	public void commitChanges() {
		b.commit(ctx.getConf());
	}

	@Override
	public JComponent getComponent() {
		return new JScrollPane(b.getComponent());
	}

	@Override
	public String getTitle() {
		return "Theme";
	}

	@Override
	public void refreshSettings() {
		b.load(ctx.getConf());
	}

}
