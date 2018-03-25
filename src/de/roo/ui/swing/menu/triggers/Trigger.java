package de.roo.ui.swing.menu.triggers;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public abstract class Trigger {
	
	private boolean enabled = true;
	private List<ITriggerStateListener> listeners = new LinkedList<ITriggerStateListener>();

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		triggerStateChanged();
	}
	
	public void triggerStateChanged() {
		for (ITriggerStateListener l : listeners ) l.triggerStateChanged(this);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public abstract void execute();
	
	public abstract String getDesc();
	
	public abstract Icon getIcon();
	
	public interface ITriggerStateListener {
		
		public void triggerStateChanged(Trigger trigger);
		
	}
	
	public void addListener(ITriggerStateListener l) {
		listeners.add(l);
	}
	
	public void removeListener(ITriggerStateListener l) {
		listeners.remove(l);
	}
	
}
