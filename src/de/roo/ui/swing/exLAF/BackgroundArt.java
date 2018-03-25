package de.roo.ui.swing.exLAF;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.roo.logging.ILog;

public class BackgroundArt {

	Image bgBuf = null;
	
	public void paintBackgroundArt(Graphics g, int width, int height, ILog log) {
		
		try {
			g.drawImage(getBGFor(width, height), 0, 0, width, height, 0, 0, width, height, null);
		} catch (IOException e) {
			log.warn(this, "Could not paint background art.", e);
		}
		
	}
	
	public Image getBGFor(int width, int height) throws IOException{
		if (bgBuf == null ||
		bgBuf.getHeight(null) < height ||
		bgBuf.getWidth(null) < width) initBG(width, height);
		return bgBuf;
	}
	
	void initBG(int w, int h) throws IOException {
		
		int bufW = getBufC(w);
		int bufH = getBufC(h);
		
		BufferedImage tmpBuf = new BufferedImage(bufW, bufH, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tmpBuf.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Composite original = g2d.getComposite();
		
		/*
		Image paw1 = ImageIO.read(UIToolkit.class.getResourceAsStream("/de/roo/gfx/bg/paw1.png"));
		paintImagesRandomly(g2d, paw1, bufW, bufH, 40000);
		*/
		
		Image paw2 = ImageIO.read(UIToolkit.class.getResourceAsStream("/de/roo/gfx/bg/paw2.png"));
		paintImagesRandomly(g2d, paw2, bufW, bufH, 10000);
		
		/*
		Image paw3 = ImageIO.read(UIToolkit.class.getResourceAsStream("/de/roo/gfx/bg/paw3.png"));
		paintImagesRandomly(g2d, paw3, bufW, bufH, 20000);
		*/
		
		//g2d.setComposite(original);
		
		
		
		
		
		bgBuf = new BufferedImage(bufW, bufH, BufferedImage.TYPE_INT_ARGB);
		Graphics final_g = bgBuf.getGraphics();
		Graphics2D final_g2d = (Graphics2D)final_g;
		
		Composite original = final_g2d.getComposite();
		
		final_g2d.setColor(Colors.backgroundLowElement);
		final_g2d.fillRect(0, 0, bufW, bufH);
		
		final_g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
		
		final_g2d.drawImage(tmpBuf, 0, 0, bufW, bufH, 0, 0, bufW, bufH, null);
		
		final_g2d.setComposite(original);
		
		
	}
	
	
	private void paintImagesRandomly(Graphics2D g2d, Image img2paint, int bufW, int bufH, int densityInv) {
		int imgWidth = img2paint.getWidth(null);
		int imgHeight = img2paint.getHeight(null);
		int elems = Math.round((bufW+imgWidth)*(bufH+imgWidth)/densityInv);		
		for (int i=0; i<elems; i++) {
			paintImageRandomly(g2d, img2paint, bufW, bufH, imgWidth, imgHeight);
		}
	}
	
	private void paintImageRandomly(Graphics2D g2d, Image img2paint, int bufW, int bufH, int imgWidth, int imgHeight) {
		
		int xCoord = (int)Math.round((imgWidth+bufW)*Math.random())-imgWidth;
		int yCoord = (int)Math.round((imgHeight+bufH)*Math.random())-imgHeight;
		double theta = Math.PI/3*(Math.random() - 0.5d);
		//float alpha = (float) (Math.random()*.2d);
		
		AffineTransform tr = new AffineTransform();
		tr.rotate(theta);
		tr.translate(xCoord, yCoord);
		
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.drawImage(img2paint, tr, null);
	}
	
	private int getBufC(int c) {
		int res = 1;
		while (res < c) res = res << 1;
		return res;
	}
	
}
