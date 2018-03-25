package de.roo.ui.swing.resources;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import de.roo.configuration.IConf;
import de.roo.connectivity.ConnectivityToolkit;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.barcoding.CodeDisplay;
import de.roo.ui.swing.menu.TriggerMenuItem;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.util.FileUtils;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ResourcePopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 677867649396365859L;
	private CodeDisplay qrCodeDisplay;
	private URL resURL = null;
	private RooEngineGUI eng;
	private RooResourceView v;

	public ResourcePopupMenu(RooResourceView v, RooEngineGUI eng) {
		this.v = v;
		this.eng = eng;

		IConf conf = eng.getConfiguration();
		
		JPanel info = new JPanel();
		info.setLayout(new GridBagLayout());		
		
		qrCodeDisplay = new CodeDisplay(eng.getLog(), conf.getValueInt("BarcodeSizeXPopup", 200), conf.getValueInt("BarcodeSizeYPopup", 200));
		refreshCond();
		JLabel label = new JLabel(FileUtils.shortenFileName(v.getResource().getPlainFileName(), 40));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 3, 5);
		info.add(label, c);
		
		c.gridy = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		info.add(qrCodeDisplay, c);
		
		this.add(info);
		for (Trigger t : v.getExportableTriggers()) this.add(new TriggerMenuItem(t));
	}

	//TODO: CONNREFRESH
	public void refreshCond() {
		URL newResURL = ConnectivityToolkit.generateDownloadURL(eng.getConnStateProvider().getCurrentConnectivityInfo(), 
				eng.getConfiguration(),
				v.getResource(), eng.getLog(), v.isLocalMode());
		if (!newResURL.equals(resURL)) {
			resURL = newResURL;
			refresh();
		}
	}

	private void refresh() {
		qrCodeDisplay.displayURL(resURL);
	}

}
