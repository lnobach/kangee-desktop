package de.roo.ui.swing;

import javax.swing.JOptionPane;

import de.roo.BuildConstants;
import de.roo.engine.setup.ISetupFollower;
import de.roo.engine.setup.SetupException;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SetupUIFollower implements ISetupFollower {

	private ILog log;
	private SplashPainter splash;
	SetupFollowerDialog wnd = null;

	public SetupUIFollower(ILog log, SplashPainter splash) {
		this.log = log;
		this.splash = splash;
	}

	public void makeProblemDialog(String dialog) {
		displayErrorOKDialog(dialog);
	}

	public void makeProblemDialog(String dialog, Throwable ex) {
		displayErrorOKDialog(dialog, ex);
	}

	@Override
	public void setCurrentJob(String jobText) {
		
		log.dbg(this, "Current job: " + jobText);
		if (wnd == null) {
			splash.writeJob(jobText);
		} else {
			wnd.setText(jobText);
		}
	}
	
	private void displayErrorOKDialog(String dialogText, Throwable e) {
		log.error(this, dialogText, e);
		displayDialogRaw(dialogText + ": " + e.getMessage());
		
	}

	public void displayErrorOKDialog(String dialogText) {
		log.error(this, dialogText);
		displayDialogRaw(dialogText);
	}

	private void displayDialogRaw(String dialogText) {
		JOptionPane.showMessageDialog(null, dialogText, BuildConstants.PROD_TINY_NAME, JOptionPane.ERROR_MESSAGE, RooStaticIconBuffer.WND_ICON);
	}

	@Override
	public void setupFinished() {
		
		if (wnd != null) wnd.setVisible(false);
		wnd = null;
	}

	@Override
	public void setupStarted() {
		
		if (!splash.isSplashActive()) {
			wnd = new SetupFollowerDialog("");
			wnd.setVisible(true);
		}
	}

	@Override
	public void setupFailed(SetupException e) {
		
		if (wnd != null) wnd.setVisible(false);
		wnd = null;
	}

}
