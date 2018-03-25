package de.roo.ui.swing.loads.triggers;

import javax.swing.Icon;

import de.roo.gfx.RooStaticIconBuffer;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IAbstractLoad.LoadState;
import de.roo.model.uiview.IRooResource;
import de.roo.ui.swing.menu.triggers.Trigger;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class StopClose extends Trigger {

	private IAbstractLoad load;
	private IRooResource res;
	
	boolean stop = true;

	public StopClose(IRooResource res, IAbstractLoad load) {
		this.res = res;
		this.load = load;
	}

	@Override
	public void execute() {
		if (load.getState() == LoadState.SUCCESS
				|| load.getState() == LoadState.FAILED
				|| load.getState() == LoadState.CANCELLED) {
			closeLoad();
			return;
		}
		
		terminateLoad();
	}

	@Override
	public String getDesc() {
		if (stop) return "Stop";
		return "Close";
	}

	@Override
	public Icon getIcon() {
		if (stop) return RooStaticIconBuffer.STOP_ICON;
		return RooStaticIconBuffer.CLOSE_ICON;
	}
	
	public void setStateStop(boolean stop) {
		this.stop = stop;
		this.triggerStateChanged();
	}
	
	public void closeLoad() {
		res.removeLoad(load);
	}

	public void terminateLoad() {
		load.terminate();
	}
	
}
