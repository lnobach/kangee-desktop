package de.roo.ui.swing.barcoding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.roo.barcoding.Encoder;
import de.roo.barcoding.IBarcodeImage;
import de.roo.configuration.IConf;
import de.roo.logging.ILog;
import de.roo.ui.swing.common.Hyperlink;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class CodeDisplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1557774678826633560L;
	private int dotscale = 1;
	private IBarcodeImage bimg;

	static final String BC_HELP_URL = "http://getkangee.com/barcode";
	static final String BC_HELP_TEXT = "What's this?";
	
	Object treeLock = new Object();
	private ILog log;
	private int barcodeSizeX;
	private int barcodeSizeY;

	public CodeDisplay(String str, ILog log, int barcodeSizeX, int barcodeSizeY) {
		this(Encoder.encode(str, log, barcodeSizeX, barcodeSizeY), log, barcodeSizeX, barcodeSizeY);
	}
	

	
	public CodeDisplay(IBarcodeImage ba, ILog log, int barcodeSizeX, int barcodeSizeY) {
		this(log, barcodeSizeX, barcodeSizeY);
		this.bimg = ba;
	}
	
	public CodeDisplay(ILog log, int barcodeSizeX, int barcodeSizeY) {
		this.log = log;
		this.barcodeSizeX = barcodeSizeX;
		this.barcodeSizeY = barcodeSizeY;
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(2, 2, 1, 2);
		this.add(this.new Main(), c);
		
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2,5,5,5);
		this.add(new Hyperlink(BC_HELP_TEXT, BC_HELP_URL, log, new Color(0x888888)), c);
		
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
		super.paint(g);
	}
	
	class Main extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1182785389487404173L;

		public void paint(Graphics g) {
			synchronized(treeLock) {
				if (bimg == null) return;
				String errStr = bimg.getErrorMsg();
				
				g.setColor(Color.black);
				if (errStr == null) {
					int height = bimg.getHeight();
					int width = bimg.getWidth();
					for (int i = 0; i < width; i++) {
						for (int j = 0; j < height; j++) {
							if (bimg.isBlack(i, j)) g.fillRect(i*dotscale, j*dotscale, dotscale, dotscale);
						}
					}
				} else {
					g.drawString(errStr, 0, this.getHeight() -3);
				}
			}
		}
		
		public Dimension getMinimumSize() {
			synchronized(treeLock) {
				if (bimg == null) return new Dimension(0,0);
				int width = bimg.getWidth()*dotscale;
				int height = bimg.getHeight()*dotscale;
				return new Dimension(width, height);
			}
		}
		
		public Dimension getPreferredSize() {
			return getMinimumSize();
		}
	}
	
	public void displayBarcodeImage(IBarcodeImage bm) {
		synchronized(treeLock) {
			this.bimg = bm;
		}
	}
	
	public void displayString(String str) {
		displayBarcodeImage(Encoder.encode(str, log, barcodeSizeX, barcodeSizeY));
	}
	
	public void displayURL(URL url) {
		displayString(url.toExternalForm());
	}
	
}
