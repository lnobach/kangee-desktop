package de.roo.ui.swing.connectivity;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.roo.BuildConstants;
import de.roo.configuration.IConf;
import de.roo.configuration.IWritableConf;
import de.roo.connectivity.DiscoveryException;
import de.roo.connectivity.RemoteDiscovery;
import de.roo.connectivity.RemoteDiscovery.CheckConnectivityState;
import de.roo.connectivity.RemoteDiscovery.DiscoveryInfo;
import de.roo.httpsrv.Server;
import de.roo.srv.RooPingHandler;
import de.roo.srvApi.ServerException;
import de.roo.ui.swing.common.ServiceComboBox;
import de.roo.ui.swing.wizardry.common.IValidInputListener;
import de.roo.util.ExceptionToolkit;
import de.roo.util.StringHasher;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConnectivityWidget {

	JPanel cntnr;
	JStatusLight light;
	JButton startBtn;
	JTextArea statusLabel;
	ServiceComboBox checkTargets;
	private ILog log;
	private int port;
	private boolean successful = false;
	private List<IValidInputListener> listeners = new LinkedList<IValidInputListener>();
	private IConf conf;
	private boolean serverWasReachable = false;
	public InetAddress discoveredAddress = null;
	
	String testRooID = StringHasher.hash(10);
	
	public ConnectivityWidget(int port, ILog log, IWritableConf conf) {
		this.log = log;
		this.port = port;
		this.conf = conf;
		
		cntnr = new JPanel();
		
		JLabel urlDesc = new JLabel("Use the following URL to test my connectivity status:");
		
		checkTargets = new ServiceComboBox(conf, log);
		startBtn = new JButton("Start connectivity test on port " + port);
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ConnectivityCheckThread().start();
			}
		});
		
		light = new JStatusLight(Color.GRAY);
		
		statusLabel = new JTextArea();
		statusLabel.setEditable(false);
		JScrollPane statusLabelSP = new JScrollPane(statusLabel);
		
		cntnr.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		cntnr.add(urlDesc, c);
		
		c.gridy = 1;
		cntnr.add(checkTargets, c);
		
		c.gridy = 2;
		cntnr.add(startBtn, c);
		
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		cntnr.add(light, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.weightx = 1;
		c.weighty = 1;
		cntnr.add(statusLabelSP, c);
		
	}
	
	public JPanel getComponent() {
		return cntnr;
	}
	
	
	class ConnectivityCheckThread extends Thread {

		public void run() {
			startTest();
		}
		
		void startTest() {
			try {
				
				Server srv = new Server(port, log, new RooPingHandler(log, testRooID), conf);
				try {
					addStatusLabelText("Starting connectivity test...");
					
					DiscoveryInfo info = RemoteDiscovery.discover(checkTargets.getSelectedDiscoveryURL(), port, true, testRooID);
					srv.stopServer();
					
					addStatusLabelText("Hostname: " + info.getDiscoveredAddr().getHostName() +"\n");
					addStatusLabelText("IP: " + info.getDiscoveredAddr().getHostAddress() + "\n");
					
					if (info.getCheckState() == CheckConnectivityState.OK) {
						light.setLightColor(Color.GREEN);
						addStatusLabelText("The connection test was successful.");
						successful = true;
					} else {
						light.setLightColor(Color.RED);
						addStatusLabelText("The connection test was not successful.\n" +
								"The server experienced the error: " + info.getCheckErrorStr());
					}
					serverWasReachable  = true;
					discoveredAddress = info.getDiscoveredAddr();
					inputChanged();
				} catch (DiscoveryException e) {
					srv.stopServer();
					e.printStackTrace();
					light.setLightColor(Color.RED);
					statusLabel.setText("The connection test server could not be contacted. \n" + ExceptionToolkit.getCauseChainMessagesCRLFSeparated(e));
					inputChanged();
				}
			} catch (ServerException e) {
				e.printStackTrace();
				light.setLightColor(Color.RED);
				statusLabel.setText("The local " + BuildConstants.PROD_TINY_NAME + " server could not be set up. \n" + ExceptionToolkit.getCauseChainMessagesCRLFSeparated(e));
				inputChanged();
			}
		}
		
	}
	
	void addStatusLabelText(String text) {
		statusLabel.setText(statusLabel.getText() + text + '\n');
	}
	
	private void inputChanged() {
		for (IValidInputListener l : listeners) l.inputChanged();
	}
	
	public boolean wasSuccessful() {
		return successful ;
	}
	
	public boolean serverWasReachable() {
		return serverWasReachable;
	}
	
	public void commitChanges() {
		checkTargets.commitChanges();
	}

	public void addValidInputListener(IValidInputListener l) {
		listeners.add(l);
	}
	
	public void removeValidInputListener(IValidInputListener l) {
		listeners.remove(l);
	}
	
	public InetAddress getDiscoveredAddress() {
		return discoveredAddress;
	}
	
}
