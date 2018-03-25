package de.roo.ui.swing.resources;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import de.roo.connectivity.ConnectivityToolkit;
import de.roo.connectivity.IConnStateListener;
import de.roo.connectivity.IConnStateProvider;
import de.roo.connectivity.IConnStateProvider.ConnectivityState;
import de.roo.model.ILocalModeProvider;
import de.roo.model.RooDownloadResource;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IRooDownloadResource;
import de.roo.model.uiview.IRooResource;
import de.roo.model.uiview.IRooResource.IResourceListener;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.exLAF.common.RooButtonUI;
import de.roo.ui.swing.exLAF.common.RooLabelUI;
import de.roo.ui.swing.exLAF.resources.RooResourceURLFieldUI;
import de.roo.ui.swing.exLAF.resources.RooResourceViewUI;
import de.roo.ui.swing.loads.LoadList;
import de.roo.ui.swing.menu.ITriggerProvider;
import de.roo.ui.swing.menu.TriggerToolbarButton;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.ui.swing.resources.triggers.Close;
import de.roo.ui.swing.resources.triggers.CopyLink;
import de.roo.ui.swing.resources.triggers.SaveDownload;
import de.roo.ui.swing.resources.triggers.OpenInNewWindow;
import de.roo.ui.swing.util.SwingToolkit;
import de.roo.util.FileUtils;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooResourceView extends JPanel implements ILocalModeProvider, ITriggerProvider {

	public static final ImageIcon LOCAL_ICON = new ImageIcon(RooResourceView.class.getResource("/de/roo/gfx/buttons/Local.png"));
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4899413267544504803L;

	JButton closeBtn;
	JButton copyLinkBtn;

	JTextField urlField;
	
	boolean localMode = false;

	private IRooResource res;

	private JToggleButton localMdBtn;

	private RooEngineGUI eng;

	private JLabel fileLabel;
	
	private ResourceIcon icon;
	
	JButton prefsBtn;
	
	Trigger saveTo;
	Trigger copyLink;
	Trigger showPrefs;
	Trigger close;
	
	IConnStateListener connL = new IConnStateListener() {
		
		@Override
		public void connectivityStateChanged(IConnStateProvider src,
				ConnectivityState state) {
			urlFieldUI.setConnectivityState(state);
			urlField.setText(getURL().toExternalForm());
		}
		
		@Override
		public void connectivityJobChanged(IConnStateProvider prov,
				String connectivityJobDesc) {
			//Nothing to do
		}
	};

	private RooResourceURLFieldUI urlFieldUI;

	public RooResourceView(IRooResource res, RooEngineGUI eng, Component owner) {

		boolean isDL = res instanceof RooDownloadResource;
		
		this.res = res;
		this.eng = eng;

		this.setUI(new RooResourceViewUI());
		
		res.addResourceListener(this.new ResourceListenerImpl());
		
		eng.getConnStateProvider().addListener(connL);

		this.setLayout(new GridBagLayout());

		ActionListener l = new Listener();
		
		copyLink = new CopyLink(res, eng, this);
		showPrefs = new OpenInNewWindow(res, eng);
		close = new Close(res, eng, this);
		
		closeBtn = new TriggerToolbarButton(close, false, true);
		copyLinkBtn = new TriggerToolbarButton(copyLink, false, true);

		localMdBtn = new JToggleButton(LOCAL_ICON);
		localMdBtn.setUI(new RooButtonUI());
		localMdBtn.addActionListener(l);
		localMdBtn.setToolTipText("Local (LAN) mode");
		
		JButton saveToBtn = null;
		if (isDL) {
			saveTo = new SaveDownload((IRooDownloadResource)res, eng.getLog(), owner, eng.getConfiguration());
			saveTo.setEnabled(res.getFile() != null);
			saveToBtn = new TriggerToolbarButton(saveTo, false, true);
		}
		
		addPopupMenu(eng);
		
		prefsBtn = new TriggerToolbarButton(showPrefs, false, true);

		urlField = new JTextField(getURL().toExternalForm());
		urlField.setEditable(false);
		urlField.setUI(urlFieldUI = new RooResourceURLFieldUI(eng.getConnStateProvider().getCurrentConnectivityState()));

		fileLabel = new JLabel(getFileLabel());
		fileLabel.setUI(new RooLabelUI());
		fileLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		
		icon = new ResourceIcon(eng.getIcons(), res.getFile(), res instanceof RooDownloadResource, eng.getLog());
		
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.gridheight = 2;
		this.add(icon, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = 1;
		this.add(fileLabel, c);

		c.weightx = 0;
		if (isDL) {
			c.gridx = 2;
			this.add(saveToBtn);
		}
		
		c.gridx = 3;
		this.add(localMdBtn, c);
		
		c.gridx = 4;
		this.add(closeBtn, c);

		c.gridy = 1;
		c.gridx = 1;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(urlField, c);

		c.gridx = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		this.add(copyLinkBtn, c);
		
		c.gridx = 4;
		this.add(prefsBtn, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.gridwidth = 5;
		this.add(new LoadList(res, eng.getLog(), eng.getConfiguration()).getComponent(), c);

	}

	protected String getFileLabel() {
		return FileUtils.shortenFileName(res.getPlainFileName(), 40);
	}

	private void addPopupMenu(RooEngineGUI eng) {
		final ResourcePopupMenu popupMenu = new ResourcePopupMenu(this, eng);

		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenu.refreshCond();
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenu.refreshCond();
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
	}

	public class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == localMdBtn) switchLocalMode();
		}

	}
	
	public URL getURL() {
		/*
		System.out.println("Without local mode: " + ConnectivityToolkit.generateDownloadURL(
				eng.getConnStateProvider().getCurrentConnectivityInfo(), eng.getConfiguration(), res,
				eng.getLog(), false));
		
		System.out.println("With local mode: " + ConnectivityToolkit.generateDownloadURL(
				eng.getConnStateProvider().getCurrentConnectivityInfo(), eng.getConfiguration(), res,
				eng.getLog(), true));
		*/
		return ConnectivityToolkit.generateDownloadURL(
				eng.getConnStateProvider().getCurrentConnectivityInfo(), eng.getConfiguration(), res,
				eng.getLog(), localMode);
	}
	
	public void switchLocalMode() {
		localMode = !localMode;
		//eng.getLog().dbg(this, "Switching local mode of " + res + " to " + localMode);
		localMdBtn.setSelected(localMode);
		urlField.setText(getURL().toExternalForm());
		this.refresh();
		notifyLocalModeToggled(localMode);
	}
	
	void notifyLocalModeToggled(boolean localMode) {
		for (IResourceViewListener l : listeners) l.localModeToggled(this, localMode);
	}
	
	List<IResourceViewListener> listeners = new LinkedList<IResourceViewListener>();
	
	public void addListener(IResourceViewListener l) {
		listeners.add(l);
	}
	
	public void removeListener(IResourceViewListener l) {
		listeners.remove(l);
	}
	
	public static interface IResourceViewListener {
		public void localModeToggled(RooResourceView view, boolean localMode);
	}
	
	void refresh() {
		urlField.setText(getURL().toExternalForm());
		fileLabel.setText(getFileLabel());
		icon.reset(res.getFile());
		if (res instanceof RooDownloadResource) saveTo.setEnabled(((IRooDownloadResource)res).canSave());

		this.repaint();
	}

	public IRooResource getResource() {
		return res;
	}
	
	@Override
	public boolean isLocalMode() {
		return localMode;
	}
	
	class ResourceListenerImpl implements IResourceListener {

		@Override
		public void loadAdded(IRooResource res, IAbstractLoad upl, int idx) {
			//Nothing to do
		}

		@Override
		public void loadRemoved(IRooResource res, IAbstractLoad upl, int idx) {
			//Nothing to do
		}

		@Override
		public void resourceChanged(IRooResource resource) {
			refresh();
		}

		@Override
		public void resourceRemoved(IRooResource resource) {
			eng.getConnStateProvider().removeListener(connL);
		}
		
	}

	@Override
	public List<Trigger> getExportableTriggers() {
		List<Trigger> l = new ArrayList<Trigger>(4);
		if (saveTo != null) l.add(saveTo);
		l.add(copyLink);
		l.add(showPrefs);
		l.add(close);
		return l;
	}
	
	public Trigger getShowPrefsTrigger() {
		return showPrefs;
	}

}
