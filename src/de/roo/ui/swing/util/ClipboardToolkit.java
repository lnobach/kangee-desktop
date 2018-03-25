package de.roo.ui.swing.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ClipboardToolkit {

	public static void copyToClipboard(URL url) {
		copyToClipboard(url.toExternalForm());
	}
	
	public static void copyToClipboard(String text) {
		StringSelection stringSelection = new StringSelection(text);
	    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
	    c.setContents(stringSelection, new Owner());
	}
	
	static class Owner implements ClipboardOwner {

		@Override
		public void lostOwnership(Clipboard clipboard, Transferable contents) {
			//Nothing to do
		}
		
	}
	
}
