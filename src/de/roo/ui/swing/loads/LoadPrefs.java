package de.roo.ui.swing.loads;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.roo.configuration.IWritableConf;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IAbstractLoad.LoadState;
import de.roo.srv.Upload;
import de.roo.srvApi.IRequest;
import de.roo.ui.swing.common.RequestInfoPanel;
import de.roo.ui.swing.exLAF.detail.RooDetailViewBackgroundPaneUI;
import de.roo.ui.swing.util.ConfToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LoadPrefs extends JDialog {

	private static final String WINDOW_ID = "LoadPrefs";
	private IWritableConf conf;
	
	public LoadPrefs(final IAbstractLoad upl, ILog log, IWritableConf conf) {
		
		this.conf = conf;
		
		IRequest req = upl.getHTTPRequest();
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		
		if (!ConfToolkit.loadSettings(this, conf, WINDOW_ID, log))
			this.setSize(500, 500);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				saveSettings();
			}
			
		});
		
		JPanel cp = new JPanel();
		cp.setUI(new RooDetailViewBackgroundPaneUI(log));
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		String fname = upl.getResource().getPlainFileName();
		String peername = req.getRequesterInfo().getRequesterIP().getHostName();
		
		if (upl instanceof Upload) this.setTitle("Upload of " + fname + " to " + peername);
		else this.setTitle("Download of " + fname + " from " + peername);
		
		LoadView view = new LoadView(upl, upl.getResource(), log, conf);
		view.getPrefsButton().setEnabled(false);
		view.getCloseButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (upl.getState() == LoadState.SUCCESS
						|| upl.getState() == LoadState.FAILED
						|| upl.getState() == LoadState.CANCELLED) {
					LoadPrefs.this.setVisible(false);
				}
			}
			
		});
		view.updateElements();
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 3, 5);
		cp.add(view, c);
		
		c.gridy = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 5, 5, 5);
		cp.add(new RequestInfoPanel(req), c);
		
		this.add(cp);
	}

	public void saveSettings() {
		ConfToolkit.saveSettings(this, conf, WINDOW_ID);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1470687240917572721L;

}
