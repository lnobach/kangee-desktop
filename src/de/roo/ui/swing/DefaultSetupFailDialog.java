package de.roo.ui.swing;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import de.roo.BuildConstants;
import de.roo.engine.setup.SetupException;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class DefaultSetupFailDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6711794960919544833L;

	public enum Choice {Reconfigure, StartKangee, Close }
	
	Choice choiceMade = null;
	
	Object lock = new Object();

	private ILog log;
	
	public DefaultSetupFailDialog(SetupException e, ILog log) {
		this.log = log;
		this.setSize(500, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		
		this.setTitle("Something went wrong while setting up " + BuildConstants.PROD_TINY_NAME);
		
		final JButton reconfigure = new JButton("Reconfigure");
		final JButton startKangee = new JButton("Start Kangee");
		final JButton close = new JButton("Close");
		
		ActionListener l = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == reconfigure) choiceMade = Choice.Reconfigure;
				else if (e.getSource() == startKangee) choiceMade = Choice.StartKangee;
				else choiceMade = Choice.Close;
				synchronized (lock) {
					lock.notify();
				}
			}
		};
		
		this.addWindowListener(this.new WindowListenerImpl());
		
		reconfigure.addActionListener(l);
		startKangee.addActionListener(l);
		close.addActionListener(l);
		
		this.setLayout(new GridBagLayout());
		
		JEditorPane label = new JEditorPane();
		label.setContentType("text/html");
		label.setEditable(false);
		label.setOpaque(false);
		label.addHyperlinkListener(new LinkListener());
		StringBuilder b = new StringBuilder();
		b.append("<html><center>There was an error while setting up " + BuildConstants.PROD_TINY_NAME + ": " + 
		e.getMessage() + "</center></html>");
		
		label.setText(b.toString());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(label, c);
		
		c.gridy = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(reconfigure, c);
		
		c.gridx = 1;
		this.add(startKangee, c);
		
		c.gridx = 2;
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
	
	class WindowListenerImpl implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			choiceMade = Choice.Close;
			synchronized (lock) {
				lock.notify();
			}
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
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
	
}
