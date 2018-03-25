package de.roo.ui.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.roo.ui.swing.exLAF.common.RooButtonUI;
import de.roo.ui.swing.menu.triggers.Trigger;
import de.roo.ui.swing.menu.triggers.Trigger.ITriggerStateListener;
import de.roo.ui.swing.util.SwingToolkit;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class TriggerToolbarButton extends JButton implements ITriggerStateListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4126591990456974280L;
	
	private Trigger t;
	private boolean showText;
	private boolean showIcon;

	
	public TriggerToolbarButton(Trigger t) {
		this(t, true, true);
	}
	
	public TriggerToolbarButton(Trigger t, boolean showText, boolean showIcon) {
		this.setUI(new RooButtonUI());
		this.showText = showText;
		this.showIcon = showIcon;
		this.t = t;
		this.setEnabled(t.isEnabled());
		t.addListener(this);
		if (showText) this.setText(t.getDesc());
		if (showIcon) this.setIcon(t.getIcon());
		this.setToolTipText(t.getDesc());
		this.addActionListener(this);
	}

	@Override
	public void triggerStateChanged(Trigger trigger) {
		if (trigger == t) {
			this.setEnabled(t.isEnabled());
			if (showText) this.setText(t.getDesc());
			if (showIcon) this.setIcon(t.getIcon());
			this.setToolTipText(t.getDesc());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		t.execute();
	}
	
}
