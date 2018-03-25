package de.roo.ui.swing.components;

import javax.swing.JFrame;

import de.roo.ui.swing.common.JPrecisionProgressBar;
import de.roo.ui.swing.exLAF.main.RooMainLoadProgressBarUI;
import de.roo.ui.swing.util.DropBoxLayout;

public class JPrecisionProgressBarTest {

	public static void main (String[] args) {
		
		JFrame frame = new JFrame();
		
		frame.setSize(400, 300);
		
		DropBoxLayout l = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
		l.setGap(20, 20);
		
		frame.setLayout(l);
		
		final JPrecisionProgressBar bar = new JPrecisionProgressBar();
		bar.setUI(new RooMainLoadProgressBarUI());
		
		frame.add(bar);
		
		frame.setVisible(true);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Thread t = new Thread() {
			
			public void run() {
				
				for (int i = 0; i<1000; i++) {
					bar.setProgress(0.001d*i);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						//Nothing to do
					}
				}
				
				System.exit(0);
				
			}
			
		};
		
		t.start();
		
	}
	
}
