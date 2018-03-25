package de.roo.ui.swing;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import de.roo.BuildConstants;
import de.roo.engine.UpdateChecker.UpdateInfo;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;

public class UpdateNotificationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6711794960919544833L;

	public enum Choice { Start, Close }
	
	Choice choiceMade = null;
	
	Object lock = new Object();
	
	JCheckBox disableUpdateChecks;

	private ILog log;
	
	public UpdateNotificationDialog(UpdateInfo info, ILog log) {
		this.log = log;
		this.setSize(500, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		
		this.setTitle("An update for " + BuildConstants.PROD_TINY_NAME + " is available");
		
		final JButton start = new JButton("Start " + BuildConstants.PROD_TINY_NAME);
		final JButton close = new JButton("Close");
		
		ActionListener l = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == start) choiceMade = Choice.Start;
				else choiceMade = Choice.Close;
				synchronized (lock) {
					lock.notify();
				}
			}
		};

		start.addActionListener(l);
		close.addActionListener(l);
		
		this.setLayout(new GridBagLayout());
		
		JEditorPane label = new JEditorPane();
		label.setContentType("text/html");
		label.setEditable(false);
		label.setOpaque(false);
		label.addHyperlinkListener(new LinkListener());
		StringBuilder b = new StringBuilder();
		b.append("<html><center><p>There is an update for " + BuildConstants.PROD_TINY_NAME + " available.</p>" +
				"<p>You have version <b>" + info.getLocalVersion() + "</b> installed. Version <b>" + info.getNewestVersion() + "</b> is available.</p>" +
				"<p>You can download the current version of Kangee at <a href=\"" + info.getUpdateDownloadURL() + "\">" + info.getUpdateDownloadURL() + "</a>. " + 
				"For your security, it is recommended to have the latest version of " + BuildConstants.PROD_TINY_NAME + " installed.</center></html>");
		
		label.setText(b.toString());
		
		disableUpdateChecks = new JCheckBox("Do not make any subsequent checks for new versions of " + BuildConstants.PROD_TINY_NAME + ".");
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(label, c);
		
		c.gridy = 1;
		this.add(disableUpdateChecks, c);
		
		c.gridy = 2;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(start, c);
		
		c.gridx = 1;
		this.add(close, c);
		
	}
	
	class LinkListener implements HyperlinkListener {
	    public void hyperlinkUpdate(HyperlinkEvent evt) {
	        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	        	try {
					Desktop.getDesktop().browse(evt.getURL().toURI());
				} catch (IOException e) {
					log.warn(this, "I/O problems while clicked on a link. ", e);
				} catch (URISyntaxException e) {
					log.warn(this, "URI syntax problems while clicked on a link. ", e);
				}
	        }
	    }
	}

	
	public Choice showAndWait() {
		this.setVisible(true);
		synchronized(lock) {
			while (choiceMade == null) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					//Nothing to do
				}
			}
			this.setVisible(false);
			this.dispose();
			return choiceMade;
		}
	}
	
	public boolean shallDisableSubsequentUpdateChecks() {
		return disableUpdateChecks.isSelected();
	}
}
