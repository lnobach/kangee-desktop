package de.roo.ui.swing.common;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;

import de.roo.BuildConstants;
import de.roo.gfx.RooStaticIconBuffer;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class PendingDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7126476000092646212L;

	static final long UPDATE_INTERVAL = 40;
	
	static final int PADDING = 50;
	
	int state = 0;
	
	JLabel pendingLabel;
	
	String pendingText;
	
	boolean terminated = false;
	
	public PendingDialog(Dialog owner, String pendingText) {
		super(owner);
		init(pendingText);
	}


	public PendingDialog(Frame owner, String pendingText) {
		super(owner);
		init(pendingText);
	}
	
	private void init(String pendingText) {
		this.setTitle(BuildConstants.PROD_TINY_NAME);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		this.pendingText = pendingText;
		this.add(pendingLabel = new JLabel(pendingText + "   "));
		
		Dimension prefSz = pendingLabel.getPreferredSize();
		this.setSize((int)prefSz.getWidth() + PADDING, (int)prefSz.getHeight() + PADDING);
		
		this.validate();
		this.setVisible(true);
	}
	
	public void close() {
		this.terminated = true;
		this.setVisible(false);
	}
	
	private void updateLabel() {
		String nextText;
		if (state == 0) {
			nextText = pendingText + ".  ";
			state = 1;
		} else if (state == 1) {
			nextText = pendingText + ".. ";
			state = 2;
		} else if (state == 2) {
			nextText = pendingText + "...";
			state = 3;
		} else {
			nextText = pendingText + "   ";
			state = 0;
		}
		
		pendingLabel.setText(nextText);
		repaint();
	}
	
	public class UpdateThread extends Thread {
		
		public void run() {
			while (!terminated) {
				try {
					Thread.sleep(UPDATE_INTERVAL);
					updateLabel();
				} catch (InterruptedException e) {
					//Nothing to do
				}
			}
		}
		
	}
	
}
