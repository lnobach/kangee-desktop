package de.roo.ui.swing.configuration;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.roo.BuildConstants;
import de.roo.logging.ILog;
import de.roo.ui.swing.util.ConfToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConfigurationWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6527949809808011781L;
	static final String WINDOW_ID = "ConfigurationWindow";
	
	private IConfigurationContext ctx;
	
	int currentTabIndex = 0;
	JTabbedPane tabPane;
	List<IConfigTab> tabs = new LinkedList<IConfigTab>();

	public ConfigurationWindow(JFrame parent, final IConfigurationContext ctx, ILog log) {
		super(parent);
		this.setTitle(BuildConstants.PROD_TINY_NAME + " Settings");
		this.ctx = ctx;
		if (!ConfToolkit.loadSettings(this, ctx.getConf(), WINDOW_ID, log))
			this.setSize(600, 600);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 tabs.get(currentTabIndex).commitChanges();
				 saveSettings();
				 ConfigurationWindow.this.setVisible(false);
				 ConfigurationWindow.this.dispose();
				 ctx.confDlgClosed();
			 }
		});
		
		tabPane = new JTabbedPane();
		
		ListenerImpl l = this.new ListenerImpl();
		
		tabPane.addChangeListener(l);
		
		initTabs(tabs, ctx);
		
		for (IConfigTab tab : tabs) 
			tabPane.addTab(tab.getTitle(), tab.getComponent());
		
		tabPane.setSelectedIndex(0);
		
		this.add(tabPane);
		
	}
	
	protected static void initTabs(List<IConfigTab> tabs, IConfigurationContext ctx) {
		tabs.add(new GeneralTab(ctx));
		tabs.add(new ThemesTab(ctx));
		tabs.add(new NetworkTab(ctx));
		tabs.add(new Network2Tab(ctx));
		tabs.add(new AdvancedTab(ctx));
	}

	protected void saveSettings() {
		ConfToolkit.saveSettings(this, ctx.getConf(), WINDOW_ID);
	}
	
	class ListenerImpl implements ChangeListener {

		@Override
		public synchronized void stateChanged(ChangeEvent e) {
			tabs.get(currentTabIndex).commitChanges();
			currentTabIndex = tabPane.getSelectedIndex();
			tabs.get(currentTabIndex).refreshSettings();
		}
		
	}
	
}
