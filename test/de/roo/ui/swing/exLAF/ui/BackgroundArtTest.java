package de.roo.ui.swing.exLAF.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import de.roo.logging.ConsoleLog;
import de.roo.ui.swing.exLAF.BackgroundArt;

public class BackgroundArtTest {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setBounds(200, 200, 600, 600);
		
		final BackgroundArt bgArt = new BackgroundArt();
		
		frame.add(new JComponent() {
			
			public void paint(Graphics g) {
				
				bgArt.paintBackgroundArt((Graphics2D) g, this.getWidth(), this.getHeight(), new ConsoleLog());
				
			}
			
		});
		
		frame.setVisible(true);
		
		
	}
	
}
