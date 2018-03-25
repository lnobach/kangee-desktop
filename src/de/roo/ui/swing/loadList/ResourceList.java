package de.roo.ui.swing.loadList;

import java.awt.Component;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.roo.model.uiview.IRooModel;
import de.roo.model.uiview.IRooModelListener;
import de.roo.model.uiview.IRooResource;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.exLAF.common.RooScrollPaneUI;
import de.roo.ui.swing.exLAF.main.RooMainResourceListUI;
import de.roo.ui.swing.resources.RooResourceView;
import de.roo.ui.swing.util.DropBoxLayout;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class ResourceList implements IRooModelListener {

	JPanel view;
	
	List<RooResourceView> subViews = new LinkedList<RooResourceView>();

	private RooEngineGUI eng;
	
	Helper helper = new Helper();

	private Frame owner;
	
	public ResourceList(IRooModel mdl, RooEngineGUI eng, Frame owner) {
		
		this.eng = eng;
		
		this.owner = owner;
		
		mdl.addListener(this);
		
		view = new JPanel();
		
		view.setUI(new RooMainResourceListUI(eng.getLog()));
		
		DropBoxLayout l = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
		l.setGap(5, 5);
		view.setLayout(l);
		
		if (mdl.getResourcesList().isEmpty()) {
			this.addHelper();
		} else for (IRooResource res : mdl.getResourcesList()) {
			addResourceView(res);
		}
	}

	public Component getComponent() {
		JScrollPane pane = new JScrollPane(view);
		pane.setUI(new RooScrollPaneUI());
		return pane;
	}

	private void addResourceView(IRooResource res) {
		removeHelper();
		RooResourceView resView = new RooResourceView(res, eng, owner);
		subViews.add(resView);
		view.add(resView);
	}
	
	private void removeHelper() {
		view.remove(helper.getComponent());
	}

	private void refresh() {
		view.revalidate();
		view.repaint();
	}
	
	@Override
	public void resourceAdded(IRooModel mdl, IRooResource res, int index) {
		addResourceView(res);
		refresh();
	}


	@Override
	public void resourceChanged(IRooModel mdl, IRooResource res, int index) {
		refresh();
	}

	@Override
	public void resourceRemoved(IRooModel mdl, IRooResource res, int index) {
		RooResourceView view2remove = subViews.remove(index);
		if (!view2remove.getResource().equals(res)) throw new AssertionError("View at Index (" + view2remove.getResource() + ") is not the view of the resource to remove: " + res );
		view.remove(view2remove);
		addHelper();
		refresh();
	}

	private void addHelper() {
		if (subViews.isEmpty()) view.add(helper.getComponent());
	}
	
}
