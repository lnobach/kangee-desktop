package de.roo.ui.plafEnv.mac;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.roo.model.RooModel;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IAbstractLoad.LoadState;
import de.roo.model.uiview.IRooResource;
import de.roo.ui.plafEnv.PlatformNotSupportedException;
import de.roo.ui.plafEnv.mac.MacOSXDockAPI.IMacOSXDockIconClickedListener;
import de.roo.ui.swing.RooEngineGUI;

public class MacOSXEnvironment {

	private RooEngineGUI eng;
	
	MacOSXDockAPI dock;

	public void initialize(final RooEngineGUI eng, boolean trayWanted) {
		
		this.eng = eng;
		
		try {
			
			dock = new MacOSXDockAPI();
			RooModel mdl = eng.getModel();

			new AllLoadsListener(mdl) {
				
				@Override
				public void onLoadRemoved(IRooResource res, IAbstractLoad upl, int idx) {
					try {
						onLoadCountChanged();
					} catch (Throwable e) {
						eng.getLog().warn("Error while handling Mac-specific behavior", e);
					}
				}
				
				@Override
				public void onLoadAdded(IRooResource res, IAbstractLoad upl, int idx) {
					try {
						onLoadCountChanged();
					} catch (Throwable e) {
						eng.getLog().warn("Error while handling Mac-specific behavior", e);
					}
				}

				@Override
				public void onLoadStateChange(IAbstractLoad l) {
					try {
						onLoadCountChanged();
					} catch (Throwable e) {
						eng.getLog().warn("Error while handling Mac-specific behavior", e);
					}
				}
			};
			
			PopupMenu menu = new PopupMenu();
			MenuItem item = new MenuItem("Show Kangee Window");
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					eng.showWindow();
				}
			});
			menu.add(item);
			dock.setDockMenu(menu);
			
			dock.addIconClickedListener(new IMacOSXDockIconClickedListener() {
				
				@Override
				public void iconClicked() {
					eng.showWindow();
				}
			});
			
		} catch (PlatformNotSupportedException e) {
			eng.getLog().warn(this, "Could not load the Mac OS X Dock API." + e);
		}
		
	}
	


	public void onLoadCountChanged() {
		int loadCount = 0;
		
		RooModel mdl = eng.getModel();
		for (int i = 0; i < mdl.getNumResources(); i++) {
			IRooResource res = mdl.getResourceAt(i);
			for (int j = 0; j < res.getNumLoads(); j++) {
				IAbstractLoad l = res.getLoadAt(j);
				if (l.getState() == LoadState.RUNNING) loadCount++;
			}
		}
		
		eng.getLog().dbg(this, "Setting load count badge to " + loadCount);
		
		dock.setBadge(loadCount <= 0? null:String.valueOf(loadCount));
	}
	
}











