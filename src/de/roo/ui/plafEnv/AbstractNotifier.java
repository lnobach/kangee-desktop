package de.roo.ui.plafEnv;

import de.roo.logging.ILog;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IAbstractLoad.LoadState;
import de.roo.model.uiview.IRooResource;
import de.roo.srv.Upload;
import de.roo.ui.plafEnv.mac.AllLoadsListener;
import de.roo.ui.swing.RooEngineGUI;

public abstract class AbstractNotifier {

	private ILog log;

	public AbstractNotifier(RooEngineGUI eng) {
		this.log = eng.getLog();
		new AllLoadsListener(eng.getModel()) {
			
			@Override
			public void onLoadRemoved(IRooResource res, IAbstractLoad upl, int idx) {
				//Nothing to notify
			}
			
			@Override
			public void onLoadAdded(IRooResource res, IAbstractLoad upl, int idx) {
				onLoadChangedState(upl);
			}

			@Override
			public void onLoadStateChange(IAbstractLoad l) {
				onLoadChangedState(l);
			}
		};
	}
	
	protected void onLoadChangedState(IAbstractLoad l) {
		if (l.getState() == LoadState.RUNNING) {
			makeNotification(printIP(l)
					+ " started " + ((l instanceof Upload)?"downloading":"uploading")
					+ " \"" + l.getResource().getPlainFileName() + "\"", log);
		} else if (l.getState() == LoadState.SUCCESS) {
			makeNotification(printIP(l)
					+ " successfully finished " + ((l instanceof Upload)?"downloading":"uploading")
					+ " \"" + l.getResource().getPlainFileName() + "\"", log);
		} else if (l.getState() == LoadState.CANCELLED) {
			makeNotification(printIP(l)
					+ " cancelled " + ((l instanceof Upload)?"downloading":"uploading")
					+ " \"" + l.getResource().getPlainFileName() + "\"", log);
		} else if (l.getState() == LoadState.FAILED) {
			makeNotification(printIP(l)
					+ " failed " + ((l instanceof Upload)?"downloading":"uploading")
					+ " \"" + l.getResource().getPlainFileName() + "\"", log);
		}
	}
	
	private String printIP(IAbstractLoad l) {
		return l.getHTTPRequest().getRequesterInfo().getRequesterIP().getHostName();
	}

	protected abstract void makeNotification(final String message, final ILog log);
	
}
