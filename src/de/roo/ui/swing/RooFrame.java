package de.roo.ui.swing;

import java.awt.BorderLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TooManyListenersException;

import javax.swing.JFrame;

import de.roo.BuildConstants;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.ui.swing.loadList.ResourceList;
import de.roo.ui.swing.menu.ToolbarBuilder;
import de.roo.ui.swing.util.ConfToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooFrame extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3442017785837170652L;

	ResourceList list;
	
	ConnStatusBar statusBar;
	
	private static final String WINDOW_ID = "MainWindow";

	private RooEngineGUI eng;
	
	public RooFrame(RooEngineGUI eng) {
		
		this.eng = eng;
		
		this.setTitle(BuildConstants.PROD_TINY_NAME);
		if (!ConfToolkit.loadSettings(this, eng.getConfiguration(), WINDOW_ID, eng.getLog()))
			this.setSize(500, 600);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		this.addWindowListener(this);
		
		ToolbarBuilder tb = new ToolbarBuilder(eng, this);
		
		this.getContentPane().add(tb.getComponent(), BorderLayout.NORTH);
		
		list = new ResourceList(eng.getModel(), eng, this);
		this.getContentPane().add(list.getComponent(), BorderLayout.CENTER);
		
		statusBar = new ConnStatusBar(eng.getConnStateProvider());
		this.getContentPane().add(statusBar.getComponent(), BorderLayout.SOUTH);
		
		
		try {
			DropTarget t = new DropTarget();
			t.addDropTargetListener(new RooDropListener(eng.getFileHandler(), eng.getLog()));
			this.setDropTarget(t);
		} catch (TooManyListenersException e) {
			eng.getLog().error(this, "Error while setting up drag-and-drop", e);
		}
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		eng.closeMainWnd();
	}

	void saveSettings() {
		ConfToolkit.saveSettings(this, eng.getConfiguration(), WINDOW_ID);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
