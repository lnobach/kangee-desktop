package de.roo.ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import de.roo.BuildConstants;
import de.roo.gfx.RooStaticIconBuffer;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SetupFollowerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3240883110374794040L;
	
	public static final ImageIcon WIZARD_LOGO = new ImageIcon(SetupFollowerDialog.class.getResource("/de/roo/gfx/wizard-logo.png"));
	JLabel jobLabel;
	
	public SetupFollowerDialog(String initText) {
		this.setSize(700, 90);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle(BuildConstants.PROD_TINY_NAME + " - Connection Setup");
		this.setLayout(new GridBagLayout());
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		jobLabel = new JLabel(initText);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		
		this.add(new JLabel(WIZARD_LOGO), c);
		
		c.gridx = 1;
		c.weightx = 1;
		this.add(jobLabel, c);
	}
	
	public void setText(String text) {
		jobLabel.setText(text);
	}
	
}
