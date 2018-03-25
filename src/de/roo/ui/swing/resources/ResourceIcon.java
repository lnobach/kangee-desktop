package de.roo.ui.swing.resources;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import de.roo.icons.IRooIcon;
import de.roo.icons.Icons2;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ResourceIcon extends JComponent {

	public static final ImageIcon DL_ICON = new ImageIcon(RooResourceView.class.getResource("/de/roo/gfx/buttons/Download.png"));
	public static final ImageIcon UL_ICON = new ImageIcon(RooResourceView.class.getResource("/de/roo/gfx/buttons/Upload.png"));
	
	static final int TYPE_ICON_OFFSET_X = 6;
	static final int TYPE_ICON_OFFSET_Y = 4;
	
	static final Map<File, BufferedImage> bufferedMIMETypes = new HashMap<File, BufferedImage>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -427581104332046792L;

	BufferedImage image = null;

	private ILog log;
	private boolean download;
	private Icons2 icons;

	public ResourceIcon(Icons2 icons, File f, boolean download, ILog log) {
		this.log = log;
		this.download = download;
		this.icons = icons;
		reset(f);
	}

	public void reset(File f) {
		BufferedImage imageBuf = bufferedMIMETypes.get(f);
		if (imageBuf == null) {
			imageBuf = bufferImage(f);
			bufferedMIMETypes.put(f, imageBuf);
		}
		this.image = imageBuf;
	}
	
	private BufferedImage bufferImage(File f) {
		BufferedImage image = null;
		IRooIcon icon = icons.getIconForFile(f).getIconSmallerEqual(Icons2.S_48x48);
		if (icon == null)
			log.error(this, "Icon is null!");
		else
			try {
				image = ImageIO.read(new ByteArrayInputStream(icon.getBytes()));
			} catch (IOException e) {
		log.error(this, "I/O error during icon loading.", e);
		}
		return image;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
		Image resTypeImg = (download?DL_ICON:UL_ICON).getImage();
		g.drawImage(resTypeImg, TYPE_ICON_OFFSET_X, TYPE_ICON_OFFSET_Y, null);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(), image.getHeight());
	}

}
