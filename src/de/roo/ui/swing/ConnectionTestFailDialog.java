package de.roo.ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ConnectionTestFailDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6711794960919544833L;

	public enum Choice { Retest, Reconfigure, StartKangee, Close }
	
	Choice choiceMade = null;
	
	Object lock = new Object();
	
	JCheckBox disableConnTest;

	public ConnectionTestFailDialog(int port, InetAddress lanAddr, InetAddress wanAddr, String presentationURLSeen, ILog log, boolean connTestDisabled) {
		this.setSize(500, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		
		this.setTitle("Something went wrong with the connection test.");
		
		final JButton retest = new JButton("Retest");
		final JButton reconfigure = new JButton("Reconfigure");
		final JButton startKangee = new JButton("Start Kangee");
		final JButton close = new JButton("Close");
		
		ActionListener l = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == retest) choiceMade = Choice.Retest;
				else if (e.getSource() == reconfigure) choiceMade = Choice.Reconfigure;
				else if (e.getSource() == startKangee) choiceMade = Choice.StartKangee;
				else choiceMade = Choice.Close;
				synchronized (lock) {
					lock.notify();
				}
			}
		};
		
		this.addWindowListener(this.new WindowListenerImpl());
		
		retest.addActionListener(l);
		reconfigure.addActionListener(l);
		startKangee.addActionListener(l);
		close.addActionListener(l);
		
		this.setLayout(new GridBagLayout());
		
		HTMLRichTextPane label = new HTMLRichTextPane(log);

		StringBuilder b = new StringBuilder();
		b.append("<html><center>The connection test was not successful. Kangee may not work " +
				"properly. This is a usual problem if you are behind a broadband DSL/Cable " +
				"router. You may have to set up the network manually by doing <b>port forwarding</b>.<br><br>");
				
		if (presentationURLSeen != null) b.append(" Kangee has detected such a DSL/Cable router, but has no permission " +
				"to make the appropriate settings for you, You can access it via <a href=\"" + presentationURLSeen + "\">" + presentationURLSeen
				+ "</a> to do it manually. <br><br>");
		
		b.append("Set the device to forward port <br><br><b>" + port + "</b><br><br> to the LAN address <br><br><b>" + lanAddr.getHostAddress()
				+ "</b><br><br> Please consult the device's " + "manual on how to proceed.</center></html>");
		
		label.setText(b.toString());
		
		disableConnTest = new JCheckBox("Do not make any subsequent connection tests.", connTestDisabled);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(label, c);
		
		c.gridy = 1;
		this.add(disableConnTest, c);
		
		c.gridy = 2;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(retest, c);
		
		c.gridx = 1;
		this.add(reconfigure, c);
		
		c.gridx = 2;
		this.add(startKangee, c);
		
		c.gridx = 3;
		this.add(close, c);
		
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
	
	public boolean shallDisableSubsequentConnTests() {
		return disableConnTest.isSelected();
	}
	
}
