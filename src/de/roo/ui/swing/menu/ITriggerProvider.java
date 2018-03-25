package de.roo.ui.swing.menu;

import java.util.List;

import de.roo.ui.swing.menu.triggers.Trigger;

/**
 * An element that may have triggers and wants to share it with others.
 * @author Leo Nobach
 *
 */
public interface ITriggerProvider {

	public List<Trigger> getExportableTriggers();
	
}
