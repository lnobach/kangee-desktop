package de.roo.ui.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.ui.swing.menu.triggers.Trigger.ITriggerStateListener;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class TriggerMenuItem extends JMenuItem implements ITriggerStateListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4126591990456974280L;
	
	private Trigger t;

	public TriggerMenuItem(Trigger t) {
		this.t = t;
		t.addListener(this);
		this.setEnabled(t.isEnabled());
		this.setText(t.getDesc());
		this.setIcon(t.getIcon());
		this.addActionListener(this);
	}

	@Override
	public void triggerStateChanged(Trigger trigger) {
		if (trigger == t) {
			this.setEnabled(t.isEnabled());
			this.setText(t.getDesc());
			this.setIcon(t.getIcon());
			this.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		t.execute();
	}
	
}
