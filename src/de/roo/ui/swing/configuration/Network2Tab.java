package de.roo.ui.swing.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import de.roo.BuildConstants;
import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.ui.swing.common.LanAddressChooser;
import de.roo.ui.swing.common.ServiceComboBox;
import de.roo.ui.swing.util.DropBoxLayout;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class Network2Tab implements IConfigTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1140992337952171413L;
	
	JPanel panel;
	
	JSpinner port;
	
	ServiceComboBox discoverySrv;
	JButton testDiscoverySrv;
	
	JRadioButton connTestingNo;
	JRadioButton connTestingYes;
	
	JRadioButton discoverYes;
	JRadioButton discoverNo;
	JTextField fixedAddr;
	
	JRadioButton lanConnAuto;
	JRadioButton lanConnCustom;
	JTextField fixedLanAddr;
	JRadioButton lanConnChoice;
	LanAddressChooser lanAddrChooser;

	private IConfigurationContext ctx;

	public Network2Tab(IConfigurationContext ctx) {
		
		this.ctx = ctx;
		
		panel = new JPanel();
		panel.setLayout(new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL));
		
		//=============== Panel "Port";
		
		JPanel portPanel = new JPanel();
		
		SpinnerModel model = new SpinnerNumberModel(10434, 0, 65536, 1);		
		port = new JSpinner(model);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 0;
		portPanel.add(new JLabel("The TCP port " + BuildConstants.PROD_TINY_NAME + " will listen on: "), c);
		
		c.weightx = 1;
		c.gridx = 1;
		portPanel.add(port, c);
		
		createTitledBorder(portPanel, "Port");
		panel.add(portPanel);
		
		/*
		
		//=============== Panel "Remote Service";
		JPanel remoteService = new JPanel();
		remoteService.setLayout(new GridBagLayout());
		
		discoverySrv = new ServiceComboBox(ctx.getConf(), ctx.getLog());
		testDiscoverySrv = new JButton("Test the service");
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		remoteService.add(new JLabel("Remote service for discovery and connectivity testing:"), c);
		
		c.gridy = 1;
		c.gridwidth = 1;
		remoteService.add(discoverySrv, c);
		
		c.gridx = 1;
		c.weightx = 0;
		remoteService.add(testDiscoverySrv, c);
		
		createTitledBorder(remoteService, "Remote Service");
		
		panel.add(remoteService);
		
		//=============== Panel "Connectivity Testing";
		JPanel connTesting = new JPanel();
		connTesting.setLayout(new GridBagLayout());
		
		connTestingNo = new JRadioButton("Don't do a connectivity test");
		connTestingYes = new JRadioButton("Do a connection test on startup");
		ButtonGroup connTestingGrp = new ButtonGroup();
		connTestingGrp.add(connTestingNo);
		connTestingGrp.add(connTestingYes);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		connTesting.add(connTestingNo, c);
		
		c.gridy = 1;
		connTesting.add(connTestingYes, c);
		
		createTitledBorder(connTesting, "Connectivity Testing");
		panel.add(connTesting);
		
		
		//=============== Panel "Internet Connectivity";
		
		JPanel inetConn = new JPanel();
		inetConn.setLayout(new GridBagLayout());
		
		discoverYes = new JRadioButton("Discover my internet address through a remote server");
		discoverNo = new JRadioButton("Use a fixed address instead");
		ButtonGroup inetConnGrp = new ButtonGroup();
		inetConnGrp.add(discoverYes);
		inetConnGrp.add(discoverNo);
		final JLabel uselanaddresslbl = new JLabel("Use LAN address: ");
		fixedAddr = new JTextField();
		ActionListener fixedAddrEnabler = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sel = discoverNo.isSelected();
				fixedAddr.setEnabled(sel);
				uselanaddresslbl.setEnabled(sel);
			}
		};
		discoverYes.addActionListener(fixedAddrEnabler);
		discoverNo.addActionListener(fixedAddrEnabler);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		inetConn.add(discoverYes, c);
		
		c.gridy = 1;
		inetConn.add(discoverNo, c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		inetConn.add(uselanaddresslbl, c);
		
		c.gridx = 1;
		c.weightx = 1;
		inetConn.add(fixedAddr, c);
		
		createTitledBorder(inetConn, "Internet connectivity");
		panel.add(inetConn);
		
		*/
		
		//=============== Panel "Local connectivity";
		
		JPanel lanConn = new JPanel();
		lanConn.setLayout(new GridBagLayout());
		
		lanConnAuto = new JRadioButton("Auto-determine my local address at startup");
		lanConnCustom = new JRadioButton("Use the following address as my LAN address");
		final JLabel custLanAddrLbl = new JLabel("Custom LAN address: ");
		fixedLanAddr = new JTextField();
		lanConnChoice = new JRadioButton("Use the following address from my devices");
		final JLabel choiceLanAddrLbl = new JLabel("Chosen LAN address: ");
		lanAddrChooser = new LanAddressChooser(ctx.getLog(), ctx.getConf(), true);
		ButtonGroup lanConnGrp = new ButtonGroup();
		lanConnGrp.add(lanConnAuto);
		lanConnGrp.add(lanConnCustom);
		lanConnGrp.add(lanConnChoice);
		ActionListener lanConnThingsEnabler = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sel1 = lanConnCustom.isSelected();
				custLanAddrLbl.setEnabled(sel1);
				fixedLanAddr.setEnabled(sel1);
				boolean sel2 = lanConnChoice.isSelected();
				choiceLanAddrLbl.setEnabled(sel2);
				lanAddrChooser.setEnabled(sel2);
			}
		};
		lanConnAuto.addActionListener(lanConnThingsEnabler);
		lanConnCustom.addActionListener(lanConnThingsEnabler);
		lanConnChoice.addActionListener(lanConnThingsEnabler);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		lanConn.add(lanConnAuto, c);
		
		c.gridy = 1;
		lanConn.add(lanConnCustom, c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		lanConn.add(custLanAddrLbl, c);
	
		c.gridx = 1;
		c.weightx = 1;
		lanConn.add(fixedLanAddr, c);
	
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		lanConn.add(lanConnChoice, c);
		
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0;
		lanConn.add(choiceLanAddrLbl, c);
		
		c.gridx = 1;
		c.weightx = 1;
		lanConn.add(lanAddrChooser, c);
		
		createTitledBorder(lanConn, "Local (LAN) connectivity");
		panel.add(lanConn);
		
		refreshSettings(true);
		
		lanConnThingsEnabler.actionPerformed(null);
		
	}
	
	void createTitledBorder(JComponent comp, String title) {
		Border border = BorderFactory.createTitledBorder(title);
		comp.setBorder(border);
	}

	@Override
	public JComponent getComponent() {
		return new JScrollPane(panel);
	}

	@Override
	public String getTitle() {
		return "Network 2";
	}

	
	/*
	 * 	ServiceComboBox discoverySrv;
	JButton testDiscoverySrv;
	
	JRadioButton connTestingNo;
	JRadioButton connTestingYes;
	
	JRadioButton discoverYes;
	JRadioButton discoverNo;
	JTextField fixedAddr;
	
	JRadioButton lanConnAuto;
	JRadioButton lanConnCustom;
	JTextField fixedLanAddr;
	JRadioButton lanConnChoice;
	LanAddressChooser lanAddrChooser;

	private IConfigurationContext ctx;
	 */
	
	@Override
	public void commitChanges() {
		
		IWritableConf conf = ctx.getConf();
		conf.setValue("Port", (Integer)port.getValue());
		
		if (lanConnAuto.isSelected()) {
			conf.setValue("Lan_Address", "auto");
			lanAddrChooser.commitChanges(true);
		} else if (lanConnCustom.isSelected()) {
			conf.setValue("Lan_Address", fixedLanAddr.getText());
			lanAddrChooser.commitChanges(true);
		} else {
			conf.setValue("Lan_Address", "auto");
			lanAddrChooser.commitChanges(false);
		}
		
	}

	@Override
	public void refreshSettings() {
		refreshSettings(false);
	}
	
	public void refreshSettings(boolean startup) {
		IConf conf = ctx.getConf();
		
		port.setValue(conf.getValueInt("Port", BuildConstants.DEFAULT_PORT));
		
		String lanAddr = conf.getValueString("Lan_Address", "auto");
		boolean lanAddrAuto = "auto".equals(lanAddr);
		if (!lanAddrAuto) fixedLanAddr.setText(lanAddr);
		boolean lanAddrSeqPrefSel = true;
		try {
			String lanAddrSeqPrefStr = conf.getValueString("Lan_Address_Seq_Pref" , "auto");
			int lanAddrSeqPrefInt = Integer.parseInt(lanAddrSeqPrefStr);
			lanAddrChooser.setSelectedIndex(lanAddrSeqPrefInt);
		} catch (NumberFormatException e) {
			lanAddrSeqPrefSel = false;
		} catch (IllegalArgumentException e) {
			lanAddrSeqPrefSel = false;
		}
		
		lanConnAuto.setSelected(lanAddrAuto && !lanAddrSeqPrefSel);
		lanConnCustom.setSelected(!lanAddrAuto && !lanAddrSeqPrefSel);
		lanConnChoice.setSelected(lanAddrAuto && lanAddrSeqPrefSel);
		
	}

}
