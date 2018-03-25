package de.roo.ui.swing.resources.triggers;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import de.roo.gfx.RooStaticIconBuffer;
import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IRooResource;
import de.roo.ui.swing.RooEngineGUI;
import de.roo.ui.swing.menu.triggers.Trigger;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class Close extends Trigger {

	private IRooResource res;
	private RooEngineGUI eng;
	private Component dialogParent;

	public Close(IRooResource res, RooEngineGUI eng, Component dialogParent) {
		this.res = res;
		this.eng = eng;
		this.dialogParent = dialogParent;
	}
	
	@Override
	public void execute() {
		close();
	}

	@Override
	public String getDesc() {
		return "Remove";
	}

	@Override
	public Icon getIcon() {
		return RooStaticIconBuffer.CLOSE_ICON;
	}
	
	public boolean close() {
		boolean pendingUpload = false;

		for (IAbstractLoad upl : res.getLoadsList()) {
			if (!upl.hasFinished())
				pendingUpload = true;
			break;
		}

		if (pendingUpload && !shallKillAllUploads()) {
			return false;
		}

		for (IAbstractLoad upl : res.getLoadsList()) {
			upl.terminate();
		}

		res.getParent().remove(res, eng.getLog());
		
		return true;
	}

	boolean shallKillAllUploads() {
		return JOptionPane.showConfirmDialog(dialogParent,
				"Terminate all pending uploads?", "Remove file",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;

	}
	
}
