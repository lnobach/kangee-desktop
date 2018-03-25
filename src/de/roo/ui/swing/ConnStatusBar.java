package de.roo.ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.roo.connectivity.IConnStateListener;
import de.roo.connectivity.IConnStateProvider;
import de.roo.connectivity.IConnStateProvider.ConnectivityState;
import de.roo.ui.swing.exLAF.common.RooLabelUI;
import de.roo.ui.swing.exLAF.main.RooMainConnStatusBarUI;
import de.roo.ui.swing.menu.triggers.AddUpload;

public class ConnStatusBar {

	public static final ImageIcon UNAVAILABLE = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/status/status-unavailable.png"));
	public static final ImageIcon AVAILABLE = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/status/status-available.png"));
	public static final ImageIcon UNKNOWN = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/status/status-unknown.png"));
	public static final ImageIcon PENDING1 = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/status/status-pending1.png"));
	public static final ImageIcon PENDING2 = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/status/status-pending2.png"));
	
	
	private IConnStateProvider prov;
	private ComponentImpl comp;
	
	public ConnStatusBar(IConnStateProvider prov) {
		
		this.prov = prov;
		
		this.comp = new ComponentImpl();
		
		comp.setUI(new RooMainConnStatusBarUI());
		
		setFromConnState(prov.getCurrentConnectivityState());
		
		prov.addListener(new IConnStateListener() {
			
			@Override
			public void connectivityStateChanged(IConnStateProvider src,
					ConnectivityState state) {
				setFromConnState(state);
			}
			
			@Override
			public void connectivityJobChanged(IConnStateProvider prov,
					String connectivityJobDesc) {
				comp.setText("Setting up: " + connectivityJobDesc);
			}
		});
		
	}
	
	public JComponent getComponent() {
		return comp;
	}
	
	protected void setFromConnState(ConnectivityState state) {
		if (state == ConnectivityState.Available) {
			comp.setText("Available.");
			comp.setIcon(AVAILABLE);
		} else if (state == ConnectivityState.ProbablyAvailable) {
			comp.setText("Probably Available.");
			comp.setIcon(UNKNOWN);
		} else if (state == ConnectivityState.Unavailable || state == ConnectivityState.UnavailableError) {
			comp.setText("Unavailable.");
			comp.setIcon(UNAVAILABLE);
		} else {
			comp.setText("Setting up...");
			comp.setIcon(PENDING1);
		}
	}
	
	class ComponentImpl extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9013366377462930139L;
		
		JLabel statusIcon;
		JLabel textLbl;
		
		public ComponentImpl() {
			
			this.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 0;
			c.weighty = 1;
			this.add(statusIcon = new JLabel(UNKNOWN), c);
			c.weightx = 1;
			c.gridx = 1;
			this.add(textLbl = new JLabel("???"), c);
			textLbl.setUI(new RooLabelUI());
			
		}
		
		public void setText(String text) {
			textLbl.setText(text);
		}
		
		public void setIcon(ImageIcon icon) {
			statusIcon.setIcon(icon);
		}
	}
	
}
