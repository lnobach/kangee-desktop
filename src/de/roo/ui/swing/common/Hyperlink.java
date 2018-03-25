package de.roo.ui.swing.common;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class Hyperlink extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4688587521209501107L;
	private final String targetAddr;
	private ILog log;

	public Hyperlink(String label, final String targetAddr, ILog log, Color color) {
		this.setText(label);
		this.setForeground(color);
		this.setOpaque(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.targetAddr = targetAddr;
		this.log = log;
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				try {
					Desktop.getDesktop().browse(new URI(targetAddr));
				} catch (IOException e1) {
					err(e1);
				} catch (URISyntaxException e1) {
					err(e1);
				}
			}
		});
	}
	
	public void err(Throwable ex) {
		log.error(this, "Error while handling URL " + targetAddr, ex);
	}
	
}
