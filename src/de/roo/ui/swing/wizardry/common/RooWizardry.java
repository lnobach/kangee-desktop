package de.roo.ui.swing.wizardry.common;

import java.awt.Dialog;
import java.awt.Frame;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooWizardry {

	
	public static void showAsync(IRooWizardStep startStep, String title, Dialog owner) {
		RooWizardSequenceHandler hdlr = new RooWizardSequenceHandler(startStep, title);
		new RooWizardDialog(hdlr, owner).setVisible(true);
	}
	
	public static void showAsync(IRooWizardStep startStep, String title, Frame owner) {
		RooWizardSequenceHandler hdlr = new RooWizardSequenceHandler(startStep, title);
		new RooWizardDialog(hdlr, owner).setVisible(true);
	}
	
	public static boolean showLocked(IRooWizardStep startStep, String title, Dialog owner) {
		RooWizardSequenceHandler hdlr = new RooWizardSequenceHandler(startStep, title);
		RooWizardDialog dlg = new RooWizardDialog(hdlr, owner);
		dlg.setVisible(true);
		waitForFinish(hdlr);
		dlg.dispose();
		return !hdlr.wasCancelled();
	}
	
	public static boolean showLocked(IRooWizardStep startStep, String title, Frame owner) {
		RooWizardSequenceHandler hdlr = new RooWizardSequenceHandler(startStep, title);
		new RooWizardDialog(hdlr, owner).setVisible(true);
		RooWizardDialog dlg = new RooWizardDialog(hdlr, owner);
		dlg.setVisible(true);
		waitForFinish(hdlr);
		dlg.dispose();
		return !hdlr.wasCancelled();
	}
	
	static void waitForFinish(RooWizardSequenceHandler hdlr) {
		while (!hdlr.hasFinished()) {
			try {
				synchronized(hdlr) {
					hdlr.wait();
				}
			} catch (InterruptedException e) {
				//Interrupted, nothing to do;
			}
		}
	}
	
}
