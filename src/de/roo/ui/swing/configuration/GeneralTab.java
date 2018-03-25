package de.roo.ui.swing.configuration;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.roo.BuildConstants;
import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.ui.swing.util.DropBoxLayout;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class GeneralTab implements IConfigTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1140992337952171413L;

	JPanel panel;

	private IConfigurationContext ctx;
	
	JTextField nickname;
	JCheckBox checkForUpdates;
	JCheckBox disableTray;
	JCheckBox disableNotifications;
	
	JCheckBox useHostnames;
	JCheckBox copyHashToo;
	JCheckBox useDirectDownloads;
	
	public GeneralTab(IConfigurationContext ctx) {
		this.ctx = ctx;
		
		panel = new JPanel();
		panel.setLayout(new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL));
		
		//=============== Panel "General";
		
		JPanel gen = new JPanel();
		
		gen.setLayout(new BoxLayout(gen, BoxLayout.PAGE_AXIS));
		nickname = new JTextField();
		checkForUpdates = new JCheckBox("Check for updates for " + BuildConstants.PROD_TINY_NAME + " on every startup");
		disableTray = new JCheckBox("Don't use a tray icon, close " + BuildConstants.PROD_TINY_NAME + " directly instead");
		disableNotifications = new JCheckBox("Disable notifications");
		
		gen.add(new JLabel("Nickname: "));
		gen.add(nickname);
		gen.add(checkForUpdates);
		gen.add(disableTray);
		gen.add(disableNotifications);
		
		createTitledBorder(gen, "General");
		panel.add(gen);
	
		//=============== Panel "Resource Address Composition";
		
		JPanel resCmp = new JPanel();
		resCmp.setLayout(new BoxLayout(resCmp, BoxLayout.PAGE_AXIS));
		useHostnames = new JCheckBox("Use hostnames instead of IP addresses");
		copyHashToo = new JCheckBox("Copy the hash of the resource along with the resource address");
		useDirectDownloads = new JCheckBox("Do not show a download page, let the receiver download the resource directly instead.");
		
		resCmp.add(useHostnames);
		resCmp.add(copyHashToo);
		resCmp.add(useDirectDownloads);
		
		createTitledBorder(resCmp, "Resource Address Composition");
		panel.add(resCmp);
		
		refreshSettings();
		
	}
	
	void createTitledBorder(JComponent comp, String title) {
		Border border = BorderFactory.createTitledBorder(title);
		comp.setBorder(border);
	}

	@Override
	public void commitChanges() {
		
		IWritableConf conf = ctx.getConf();
		
		conf.setValue("Nickname", nickname.getText());
		conf.setValue("CheckForUpdates", checkForUpdates.isSelected());
		conf.setValue("Disable_Tray", disableTray.isSelected());
		conf.setValue("DisableNotifications", disableNotifications.isSelected());
		
		conf.setValue("Use_Hostnames", useHostnames.isSelected());
		conf.setValue("copyHashToo", copyHashToo.isSelected());
		conf.setValue("Use_Direct_Downloads", useDirectDownloads.isSelected());
		
	}

	@Override
	public JComponent getComponent() {
		return new JScrollPane(panel);
	}

	@Override
	public String getTitle() {
		return "General";
	}

	@Override
	public void refreshSettings() {
		IConf conf = ctx.getConf();
		
		nickname.setText(conf.getValueString("Nickname", ""));
		checkForUpdates.setSelected(conf.getValueBoolean("CheckForUpdates", true));
		disableTray.setSelected(conf.getValueBoolean("Disable_Tray", false));
		disableNotifications.setSelected(conf.getValueBoolean("DisableNotifications", false));
		
		useHostnames.setSelected(conf.getValueBoolean("Use_Hostnames", false));
		copyHashToo.setSelected(conf.getValueBoolean("copyHashToo", false));
		useDirectDownloads.setSelected(conf.getValueBoolean("Use_Direct_Downloads", false));
		
	}

}
