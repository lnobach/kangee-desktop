package de.roo.ui.swing.loads;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IRooResource;
import de.roo.model.uiview.IRooResource.IResourceListener;
import de.roo.ui.swing.exLAF.main.RooMainLoadListUI;
import de.roo.ui.swing.util.DropBoxLayout;
import de.roo.configuration.IWritableConf;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class LoadList implements IResourceListener {

	JPanel view;
	
	List<LoadView> subViews = new LinkedList<LoadView>();

	private ILog log;

	private IWritableConf conf;
	
	public LoadList(IRooResource res, ILog log, IWritableConf conf) {
		
		this.log = log;
		this.conf = conf;
		
		view = new JPanel();
		view.setUI(new RooMainLoadListUI());
		DropBoxLayout layout = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
		layout.setGap(5, 5);
		view.setLayout(layout);
		
		int i=0;
		for (IAbstractLoad load : res.getLoadsList())
			loadAdded(res, load, i++);
		
		res.addResourceListener(this);
		
	}

	@Override
	public void resourceChanged(IRooResource resource) {
		refresh();
	}
	
	void refresh() {
		view.revalidate();
		view.repaint();
	}
	
	public Component getComponent() {
		return view;
	}

	@Override
	public void loadAdded(IRooResource res, IAbstractLoad upl, int idx) {
		LoadView uplView = new LoadView(upl, res, log, conf);
		subViews.add(uplView);
		view.add(uplView);
		refresh();
	}

	@Override
	public void loadRemoved(IRooResource res, IAbstractLoad upl, int idx) {
		LoadView view2remove = subViews.remove(idx);
		if (view2remove.getUpload() != upl) 
			throw new AssertionError("Upload to remove and the one in the view at its index are not the same.");
		view.remove(view2remove);
		refresh();
	}

	@Override
	public void resourceRemoved(IRooResource resource) {
		//Nothing to do
	}
	
}
