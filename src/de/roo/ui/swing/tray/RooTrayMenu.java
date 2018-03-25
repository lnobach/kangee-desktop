package de.roo.ui.swing.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.roo.BuildConstants;
import de.roo.ui.swing.RooEngineGUI;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooTrayMenu extends PopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8705609230568252317L;
	private MenuItem closeItem;
	private MenuItem showItem;
	private RooEngineGUI eng;

	public RooTrayMenu(RooEngineGUI eng) {
		this.eng = eng;
		
		ActionListener l = this.new Listener();
		
		showItem = new MenuItem("Show " + BuildConstants.PROD_TINY_NAME + " window");
		showItem.addActionListener(l);
		this.add(showItem);
		
		closeItem = new MenuItem("Close " + BuildConstants.PROD_TINY_NAME);
		closeItem.addActionListener(l);
		this.add(closeItem);
		
	}
	
	class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object s = e.getSource();
			if (s == closeItem) {
				System.exit(0);
			} else if (s == showItem) {
				eng.showWindow();
			}
		}
		
	}

}
