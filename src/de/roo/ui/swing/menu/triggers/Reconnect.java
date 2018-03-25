package de.roo.ui.swing.menu.triggers;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.roo.connectivity.IConnStateListener;
import de.roo.connectivity.IConnStateProvider;
import de.roo.connectivity.IConnStateProvider.ConnectivityState;

public class Reconnect extends Trigger {

	public static final ImageIcon ICON = new ImageIcon(AddUpload.class.getResource("/de/roo/gfx/buttons/RecheckConn.png"));
	
	private IConnStateProvider prov;

	public Reconnect(IConnStateProvider prov) {
		this.prov = prov;
		
		this.setEnabled(prov.getCurrentConnectivityState() != ConnectivityState.SettingUp);
		
		prov.addListener(new IConnStateListener() {
			
			@Override
			public void connectivityStateChanged(IConnStateProvider src,
					ConnectivityState state) {
				Reconnect.this.setEnabled(state != ConnectivityState.SettingUp);
			}
			
			@Override
			public void connectivityJobChanged(IConnStateProvider prov,
					String connectivityJobDesc) {
				//Nothing to do
			}
		});
		
	}
	
	@Override
	public void execute() {
		new Thread("ForceConnResetThread"){
			
			public void run() {
				prov.forceReset();
			}
			
		}.start();
	}

	@Override
	public String getDesc() {
		return "Reset Connectivity";
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

}
