package de.roo.ui.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.roo.engine.FileHandler;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooDropListener implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -869334952019843629L;
	private ILog log;
	private FileHandler hdlr;

	public RooDropListener(FileHandler hdlr, ILog log) {
		this.log = log;
		this.hdlr = hdlr;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// Nothing to do
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// Nothing to do
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// Nothing to do
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drop(DropTargetDropEvent dtde) {

		Transferable t = dtde.getTransferable();

		//printAllDataFlavors(t);
		try {
			for (DataFlavor f : t.getTransferDataFlavors()) {
				if (f.isFlavorJavaFileListType()) {
					log.dbg(this, "Accepted file list flavor for" + f);
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					@SuppressWarnings("rawtypes")
					List<File> data = (List)t.getTransferData(f);
					addAsFiles(data);
					return;
				}

				if (f.getRepresentationClass().equals(String.class)) {
					try {
						log.dbg(this, "Accepted string flavor for " + t);
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						String data = (String) t.getTransferData(f);
						addAsURLs(data);
						return;
					} catch (InvalidDnDOperationException e) {
						log.error(this, "Error while handling string flavor: ", e);
					}
				}
			}
		} catch (UnsupportedFlavorException e) {
			log.error(this, "Error while determining how to handle the dropped element. Ignoring it.", e);
		} catch (IOException e) {
			log.error(this, "I/O Error while determining how to handle the dropped element.", e);
		}
		log.error(this, "No flavor found that can be used");

	}

	private void addAsFiles(List<File> files) {
		hdlr.publishFiles(files);
	}

	/*
	private void printAllDataFlavors(Transferable t) {
		for (DataFlavor f : t.getTransferDataFlavors()) {
			log.dbg(this, "Supp. Flavor: " + f);
		}
	}
	*/

	
	private void addAsURLs(List<String> urlStrs) {
		List<URL> urls = new ArrayList<URL>(urlStrs.size());
		for (String urlStr : urlStrs) {
			try {
				URL url = new URL(urlStr);
				urls.add(url);
			} catch (MalformedURLException e) {
				log.warn(this, "URL that was dropped is malformed. Ignoring. URL was: " + urlStr );
			}
		}
		hdlr.publishFilesFromURLs(urls);
	}
	
	private void addAsURLs(String allURLs) {
		String[] tokens = allURLs.split("\r\n");
		List<String> urlStrs = Arrays.asList(tokens);
		addAsURLs(urlStrs);
	}
	
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// Nothing to do
	}

}
