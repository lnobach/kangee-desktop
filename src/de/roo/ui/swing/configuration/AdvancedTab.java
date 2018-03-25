package de.roo.ui.swing.configuration;

import javax.swing.JComponent;

import de.roo.configuration.ConfTypes;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class AdvancedTab implements IConfigTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1140992337952171413L;
	
	WeakTypingConfEditor edit;
	
	public AdvancedTab(IConfigurationContext ctx) {
		edit = new WeakTypingConfEditor(ctx, ConfTypes.getTypesMap());
	}

	@Override
	public void commitChanges() {
		//Nothing to do
	}

	@Override
	public JComponent getComponent() {
		return edit.getComponent();
	}

	@Override
	public String getTitle() {
		return "Advanced";
	}

	@Override
	public void refreshSettings() {
		edit.refresh();
	}
	

}
