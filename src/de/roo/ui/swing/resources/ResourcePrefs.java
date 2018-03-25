package de.roo.ui.swing.resources;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.roo.BuildConstants;
import de.roo.configuration.IWritableConf;
import de.roo.connectivity.ConnectivityToolkit;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.model.RooUploadResource;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IRooResource;
import de.roo.model.uiview.IRooResource.IResourceListener;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.barcoding.CodeDisplay;
import de.roo.ui.swing.exLAF.common.RooScrollPaneUI;
import de.roo.ui.swing.exLAF.detail.RooDetailViewBackgroundPaneUI;
import de.roo.ui.swing.resources.RooResourceView.IResourceViewListener;
import de.roo.ui.swing.util.ConfToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ResourcePrefs extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4109293749215674762L;
	private static final String WINDOW_ID = "ResourcePrefs";
	private IWritableConf conf;

	public ResourcePrefs(final IRooResource res, final RooEngineGUI eng) {
		
		this.conf = eng.getConfiguration();
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		
		if (!ConfToolkit.loadSettings(this, eng.getConfiguration(), WINDOW_ID, eng.getLog()))
			this.setSize(700, 500);
		
		String fname = res.getPlainFileName();
		
		this.setTitle("Details of " + ((res instanceof RooUploadResource)?"upload":"download") + ": " + fname + " - " + BuildConstants.PROD_TINY_NAME);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				saveSettings();
			}
			
		});
		
		JPanel cp = new JPanel();
		cp.setUI(new RooDetailViewBackgroundPaneUI(eng.getLog()));
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		final RooResourceView view = new RooResourceView(res, eng, this);
		view.getShowPrefsTrigger().setEnabled(false);
		res.addResourceListener(this.new ResourceListenerImpl());
		view.refresh();
		
		final CodeDisplay disp = new CodeDisplay(eng.getLog(), conf.getValueInt("BarcodeSizeXDetail", 200), conf.getValueInt("BarcodeSizeYDetail", 200));
		refreshBarcode(disp, res, view, eng);
		
		view.addListener(new IResourceViewListener() {
			
			@Override
			public void localModeToggled(RooResourceView view2, boolean localMode) {
				if (view == view2) {
					refreshBarcode(disp, res, view, eng);
				}
			}
		});
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(5, 5, 5, 3);
		cp.add(view, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(5, 2, 5, 5);
		cp.add(disp, c);

		JScrollPane sp = new JScrollPane(cp);
		sp.setUI(new RooScrollPaneUI());
		this.add(sp);
		
	}

	// TODO: CONNREFRESH
	private void refreshBarcode(CodeDisplay disp, IRooResource res,
			RooResourceView view, RooEngineGUI eng) {
		disp.displayURL(ConnectivityToolkit.generateDownloadURL(eng.getConnStateProvider().getCurrentConnectivityInfo(), 
				eng.getConfiguration(), res, eng.getLog(), view.isLocalMode()));
		disp.revalidate();
		disp.repaint();
	}
	
	public void saveSettings() {
		ConfToolkit.saveSettings(this, conf, WINDOW_ID);
	}
	
	public class ResourceListenerImpl implements IResourceListener {

		@Override
		public void loadAdded(IRooResource res, IAbstractLoad upl, int idx) {
			//Nothing to do
		}

		@Override
		public void loadRemoved(IRooResource res, IAbstractLoad upl, int idx) {
			
		}

		@Override
		public void resourceChanged(IRooResource resource) {
			//Nothing to do
		}

		@Override
		public void resourceRemoved(IRooResource resource) {
			ResourcePrefs.this.setVisible(false);
		}
		
	}
	
}
