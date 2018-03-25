package de.roo.ui.swing;

import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import de.roo.logging.ILog;
import de.roo.ui.swing.exLAF.UIConstants;

public class HTMLRichTextPane extends JEditorPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4007442952457099131L;
	private ILog log;
	
	public HTMLRichTextPane(ILog log) {
		this.log = log;
		this.setContentType("text/html");
		
		Font font = UIConstants.defaultFont;
        ((HTMLDocument)this.getDocument()).getStyleSheet().addRule("body{font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize() + "pt; padding: 5px; }");
		
		this.setEditable(false);
		this.setOpaque(false);
		
		this.addHyperlinkListener(new LinkListener());
	}
	
	class LinkListener implements HyperlinkListener {
	    public void hyperlinkUpdate(HyperlinkEvent evt) {
	        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	        	try {
					Desktop.getDesktop().browse(evt.getURL().toURI());
				} catch (IOException e) {
					log.warn(this, "I/O problems while clicked on a link. ", e);
				} catch (URISyntaxException e) {
					log.warn(this, "URI syntax problems while clicked on a link. ", e);
				}
	        }
	    }
	}

}
