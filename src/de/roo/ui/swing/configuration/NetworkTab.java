package de.roo.ui.swing.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.ui.swing.common.ServiceComboBox;
import de.roo.ui.swing.util.DropBoxLayout;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class NetworkTab implements IConfigTab {

	int restartReqs = 0;
	
	private static final String CONN_FIXED_ADDRESS = "ConnFixedAddress";
	private static final String CONN_REMOTE_TEST_ENABLED = "ConnRemoteTestEnabled";
	private static final String CONN_FORCE_STUN_OVER_ADAPTERS = "ConnForceSTUNOverAdapters";
	private static final String CONN_STUN_ENABLED = "ConnSTUNEnabled";
	private static final String CONN_P_MAP_AUTO_INCREMENT = "ConnPMapAutoIncrement";
	private static final String CONN_P_MAPPING_ENABLED = "ConnPMappingEnabled";
	private static final String CONN_P_MAP_QUERY_ENABLED = "ConnPMapQueryEnabled";
	private IConfigurationContext ctx;
	private JPanel panel;
	
	private JRadioButton fixedAddrNo;
	private JRadioButton fixedAddrYes;
	private JCheckBox connPMapQueryEnabledCB;
	private JCheckBox connPMappingEnabledCB;
	private JCheckBox connPMapAutoIncrementCB;
	private JCheckBox connSTUNEnabledCB;
	private JCheckBox connForceSTUNOverAdaptersCB;
	private JTextField fixedWANAddr;
	private JCheckBox connRemoteTestEnabledCB;
	private ServiceComboBox discoverySrv;
//	private JButton testDiscoverySrv;


	@Override
	public void commitChanges() {
		IWritableConf conf = ctx.getConf();
		
		conf.setValue(CONN_P_MAP_QUERY_ENABLED, connPMapQueryEnabledCB.isSelected());
		conf.setValue(CONN_P_MAPPING_ENABLED, connPMappingEnabledCB.isSelected());
		conf.setValue(CONN_P_MAP_AUTO_INCREMENT, connPMapAutoIncrementCB.isSelected());
		conf.setValue(CONN_STUN_ENABLED, connSTUNEnabledCB.isSelected());
		conf.setValue(CONN_FORCE_STUN_OVER_ADAPTERS, connForceSTUNOverAdaptersCB.isSelected());
		conf.setValue(CONN_FIXED_ADDRESS, fixedAddrYes.isSelected()?fixedWANAddr.getText():"");
		
		conf.setValue(CONN_REMOTE_TEST_ENABLED, connRemoteTestEnabledCB.isSelected());
		discoverySrv.commitChanges();
		
	}
	
	public NetworkTab(IConfigurationContext ctx)  {
		this.ctx = ctx;
		
		
		
		IConf conf = ctx.getConf();
		
		String fixedAddr = conf.getValueString(CONN_FIXED_ADDRESS, "");
		boolean fixedAddrEnabled = !fixedAddr.equals("");
		
		fixedAddrNo = new JRadioButton("Determine address dynamically", !fixedAddrEnabled);
		
		fixedAddrYes = new JRadioButton("Use a fixed address instead", fixedAddrEnabled);
		
		ButtonGroup fixedAddrGrp = new ButtonGroup();
		fixedAddrGrp.add(fixedAddrNo);
		fixedAddrGrp.add(fixedAddrYes);
		
		connPMapQueryEnabledCB = new JCheckBox("Try to use UPnP to get the public IP address", conf.getValueBoolean(CONN_P_MAP_QUERY_ENABLED, true));
		
		connPMappingEnabledCB = new JCheckBox("Try to use UPnP to additionally set up the connection (port mapping)", conf.getValueBoolean(CONN_P_MAPPING_ENABLED, true));
		
		connPMapAutoIncrementCB = new JCheckBox("Auto-increment port mapping if port is in use", conf.getValueBoolean(CONN_P_MAP_AUTO_INCREMENT, true));
		
		connSTUNEnabledCB = new JCheckBox("Try to use STUN to get the public IP address", conf.getValueBoolean(CONN_STUN_ENABLED, true));
		
		connForceSTUNOverAdaptersCB = new JCheckBox("Force STUN to override WAN addresses found on the local adapters", conf.getValueBoolean(CONN_FORCE_STUN_OVER_ADAPTERS, false));
		
		final JLabel custWanAddrLbl = new JLabel("Fixed WAN address: ");
		fixedWANAddr = new JTextField(fixedAddr);
		
		connRemoteTestEnabledCB = new JCheckBox("Test the connection via a remote server", conf.getValueBoolean(CONN_REMOTE_TEST_ENABLED, true));
		
		final JPanel upnpPanel = new JPanel();
		upnpPanel.setLayout(new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL));
		createTitledBorder(upnpPanel, "Port Mapping");
		upnpPanel.add(connPMappingEnabledCB);
		upnpPanel.add(connPMapAutoIncrementCB);
		
		final JPanel stunPanel = new JPanel();
		stunPanel.setLayout(new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL));
		createTitledBorder(stunPanel, "STUN");
		stunPanel.add(connForceSTUNOverAdaptersCB);
		
		final JPanel dynPanel = new JPanel();
		dynPanel.setLayout(new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL));
		createTitledBorder(dynPanel, "Settings for dynamic addressing");
		dynPanel.add(connPMapQueryEnabledCB);
		dynPanel.add(upnpPanel);
		dynPanel.add(connSTUNEnabledCB);
		dynPanel.add(stunPanel);
		
		final JPanel fixedAddrPanel = new JPanel();
		fixedAddrPanel.setLayout(new GridBagLayout());
		createTitledBorder(fixedAddrPanel, "Fixed Address");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		fixedAddrPanel.add(custWanAddrLbl, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		fixedAddrPanel.add(fixedWANAddr, c);
		
		JPanel remoteService = new JPanel();
		remoteService.setLayout(new GridBagLayout());
		
		discoverySrv = new ServiceComboBox(ctx.getConf(), ctx.getLog());
		//testDiscoverySrv = new JButton("Test the service");
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		remoteService.add(new JLabel("Remote service for discovery and connectivity testing:"), c);
		
		c.gridy = 1;
		c.gridwidth = 2;	//= 1
		remoteService.add(discoverySrv, c);
		
		//c.gridx = 1;
		//c.weightx = 0;
		//remoteService.add(testDiscoverySrv, c);
		
		createTitledBorder(remoteService, "Remote Service");
		
		panel = new JPanel();
		panel.setLayout(new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL));
		panel.add(fixedAddrNo);
		panel.add(dynPanel);
		panel.add(fixedAddrYes);
		panel.add(fixedAddrPanel);
		panel.add(connRemoteTestEnabledCB);
		panel.add(remoteService);
		
		ActionListener l = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean fixedSel = fixedAddrYes.isSelected();
				dynPanel.setEnabled(!fixedSel);
				connPMapQueryEnabledCB.setEnabled(!fixedSel);
				upnpPanel.setEnabled(!fixedSel);
				connPMappingEnabledCB.setEnabled(!fixedSel);
				connPMapAutoIncrementCB.setEnabled(!fixedSel);
				stunPanel.setEnabled(!fixedSel);
				connSTUNEnabledCB.setEnabled(!fixedSel);
				connForceSTUNOverAdaptersCB.setEnabled(!fixedSel);
				
				fixedAddrPanel.setEnabled(fixedSel);
				custWanAddrLbl.setEnabled(fixedSel);
				fixedWANAddr.setEnabled(fixedSel);
			}
		};
		
		fixedAddrNo.addActionListener(l);
		fixedAddrYes.addActionListener(l);
		l.actionPerformed(null);
		
	}
	
	@Override
	public JComponent getComponent() {
		return new JScrollPane(panel);
	}

	@Override
	public void refreshSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return "Network";
	}
	
	void createTitledBorder(JComponent comp, String title) {
		Border border = BorderFactory.createTitledBorder(title);
		comp.setBorder(border);
	}

}
