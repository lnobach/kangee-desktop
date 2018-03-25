package de.roo.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import de.roo.BuildConstants;
import de.roo.configuration.IWritableConf;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;
import de.roo.ui.swing.util.ConfToolkit;
import de.roo.util.stream.StreamCopy;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class CreditsWindow extends JDialog {

	private static final String WINDOW_ID = "CreditsWindow";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1259706907262381122L;

	public static final ImageIcon WIZARD_LOGO = new ImageIcon(CreditsWindow.class.getResource("/de/roo/gfx/credits-logo.png"));
	public static final String licenseLoc = "/de/roo/LICENSE.txt";
	public static final String noticeLoc = "/de/roo/NOTICE.txt";

	public static final String topText =
		"<html><p><b>" + BuildConstants.PROD_FULL_NAME + "</b></p>" +
		"<p>Version " + BuildConstants.PROD_VER + ", Copyright (c) Leonhard Nobach";
	
	private IWritableConf conf;
	
	public CreditsWindow(IWritableConf conf, ILog log) {
		
		this.conf = conf;
		if (! ConfToolkit.loadSettings(this, conf, WINDOW_ID, log))
			this.setSize(600, 500);
		this.setTitle("About " + BuildConstants.PROD_TINY_NAME);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		this.setLayout(new BorderLayout());
		
		this.add(new JLabel(WIZARD_LOGO), BorderLayout.WEST);
		this.add(new JLabel(topText), BorderLayout.NORTH);
		
		JTextPane license = new JTextPane();
		license.setEditable(false);
		license.setText(getText(licenseLoc, log));

		JTextPane notice = new JTextPane();
		notice.setEditable(false);
		notice.setText(getText(noticeLoc, log));
		
		JTabbedPane tabPane = new JTabbedPane();
		
		tabPane.add("Contributions", new JScrollPane(notice));
		tabPane.add("License", new JScrollPane(license));
		
		this.add(tabPane, BorderLayout.CENTER);
		
		this.addWindowListener(this.new ListenerImpl());
		
	}
	
	class ListenerImpl extends WindowAdapter {
	
		@Override
		public void windowClosing(WindowEvent e) {
			saveSettings();
		}
		
	}

	void saveSettings() {
		ConfToolkit.saveSettings(this, conf, WINDOW_ID);
	}
	
	public String getText(String location, ILog log) {
		InputStream s = CreditsWindow.class.getResourceAsStream(location);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			new StreamCopy().copy(s, os);
		} catch (IOException e) {
			log.error(this, "Could not read license", e);
			return "Error retrieving license. See log for details.";
		}
		return os.toString();
	}
	
}
