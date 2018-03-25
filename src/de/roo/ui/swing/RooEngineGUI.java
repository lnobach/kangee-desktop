package de.roo.ui.swing;

import java.awt.Dialog;
import java.io.File;

import javax.swing.JOptionPane;

import de.roo.BuildConstants;
import de.roo.configuration.IWritableConf;
import de.roo.engine.RooEngine;
import de.roo.engine.UpdateCheckException;
import de.roo.engine.UpdateChecker;
import de.roo.engine.UpdateChecker.UpdateInfo;
import de.roo.engine.setup.ISetupContext;
import de.roo.engine.setup.ISetupFollower;
import de.roo.engine.setup.ISetupMethod;
import de.roo.engine.setup.SetupException;
import de.roo.engine.setup.standard2.Default2SetupMethod;
import de.roo.gfx.RooStaticIconBuffer;
import de.roo.logging.ILog;
import de.roo.srvApi.ServerException;
import de.roo.ui.plafEnv.PlatformSpecials;
import de.roo.ui.swing.ConnectionTestFailDialog;
import de.roo.ui.swing.configuration.ConfigurationEditor;
import de.roo.ui.swing.logging.LogViewer;
import de.roo.ui.swing.util.BasedirToolkit;
import de.roo.ui.swing.util.LookAndFeelManager;
import de.roo.ui.swing.wizardry.common.RooWizardry;
import de.roo.ui.swing.wizardry.setup.WelcomeStep;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class RooEngineGUI extends RooEngine {

	private static final String CONN_REMOTE_TEST_ENABLED = "ConnRemoteTestEnabled";
	
	RooFrame gui;
	SplashPainter splash;
	ConfigurationEditor edit = null;
	
	SetupUIFollower setupFollower;
	private LogViewer logViewer;
	
	boolean keepOpenInBackground = false;
	
	public RooEngineGUI(ILog log){
		super(log);
		splash = new SplashPainter();
		setupFollower = new SetupUIFollower(this.getLog(), splash);
	}

	public void setLogViewer(LogViewer viewer) {
		this.logViewer = viewer;
	}
	
	public LogViewer getLogViewer() {
		return logViewer;
	}
	
	public boolean init() {
		if (!super.init()) return false;
		getLog().dbg(this, "Setting up Look-And-Feel...");
		try {
			PlatformSpecials.initialize(this);
		} catch (Throwable e) {
			// Never hold up the application if anything goes wrong when installing platform-specific behavior.
			getLog().error(PlatformSpecials.class, 
					"Something went wrong while initializing platform-specific behavior.", e);
		}
		getLog().dbg(this, "Launching UI...");
		gui = new RooFrame(this);
		gui.setVisible(true);
		return true;
	}
	
	/**
	 * Returns whether Kangee should go on starting.
	 * @return
	 */
	protected boolean checkForUpdates() {
		UpdateChecker chk = new UpdateChecker(BuildConstants.UPDATE_CHECK_URL, BuildConstants.UPDATE_DOWNLOAD_URL);
		UpdateInfo info;
		if (getConfiguration().getValueBoolean("CheckForUpdatesStrict", false)) {
			try {
				info = chk.checkStrict(getLog());
			} catch (UpdateCheckException e) {
				getLog().error(this, "Problems while checking for an update.", e);
				JOptionPane.showMessageDialog(null, "Could not retrieve information about updates from server. See the log for details."
						, BuildConstants.PROD_TINY_NAME, JOptionPane.ERROR_MESSAGE, RooStaticIconBuffer.WND_ICON);
				return true;
			}
		} else {
			info = chk.check(getLog());
		}
		if (info != null && info.shouldUpdate()) {
			UpdateNotificationDialog dlg = new UpdateNotificationDialog(info, getLog());
			de.roo.ui.swing.UpdateNotificationDialog.Choice result = dlg.showAndWait();
			getConfiguration().setValue("CheckForUpdates", !dlg.shallDisableSubsequentUpdateChecks());
			if (result == de.roo.ui.swing.UpdateNotificationDialog.Choice.Close) return false;
		}
		return true;
	}
	
	protected void onBeforeSetup() {
		LookAndFeelManager.setLookAndFeelAuto(this.getLog(), this.getConfiguration());
		logViewer.setConf(getConfiguration());
		logViewer.setAutoShow(!getConfiguration().getValueBoolean("DisableLogAutoShow", false));
	}
	
	public void close() {
		if (gui != null) {
			gui.saveSettings();
			gui.setVisible(false);
		}
		super.close();
	}
	
	public boolean setup(ISetupContext ctx, ISetupFollower follower) {
		
		IWritableConf conf = getConfiguration();
		
		if (conf.getValueBoolean("configured", false) && conf.getValueBoolean("CheckForUpdates", true)) {
			if (!checkForUpdates()) return false;
		}
		
		ISetupMethod method = new Default2SetupMethod();
		
		while (true) {
			try {
				follower.setupStarted();
				method.setup(ctx, follower);
				follower.setupFinished();
				//setupFollower.closeWindow();
				return true;
			} catch (SetupException.NotConfigured e) {
				follower.setupFailed(e);
				//setupFollower.closeWindow();
				if (!RooWizardry.showLocked(new WelcomeStep(ctx), BuildConstants.PROD_TINY_NAME, (Dialog)null)) return false;
				ctx.getConf().setValue("configured", true);
				if (getConfiguration().getValueBoolean("CheckForUpdates", true)) {
					if (!checkForUpdates()) return false;
				}
			} catch (SetupException.BadConnectionTest e) {
				follower.setupFailed(e);
				ConnectionTestFailDialog dlg = new ConnectionTestFailDialog(e.getPort(), e.getLanAddr(), e.getWanAddress(), e.getPresentationURLSeen(), getLog(), !conf.getValueBoolean(CONN_REMOTE_TEST_ENABLED, true));
				ConnectionTestFailDialog.Choice c = dlg.showAndWait();
				conf.setValue(CONN_REMOTE_TEST_ENABLED, !dlg.shallDisableSubsequentConnTests());
				if (c == ConnectionTestFailDialog.Choice.Reconfigure) {
					if (!RooWizardry.showLocked(new WelcomeStep(ctx), BuildConstants.PROD_TINY_NAME, (Dialog)null)) return false;
				} else if (c == ConnectionTestFailDialog.Choice.Close) {
					return false;
				} else if (c == ConnectionTestFailDialog.Choice.StartKangee) {
					ctx.setUnsuccessfulAndOverridden();
					return true;
				}
			} catch (SetupException e) {
				follower.setupFailed(e);
				DefaultSetupFailDialog dlg = new DefaultSetupFailDialog(e, getLog());
				DefaultSetupFailDialog.Choice c = dlg.showAndWait();
				if (c == DefaultSetupFailDialog.Choice.Reconfigure) {
					if (!RooWizardry.showLocked(new WelcomeStep(ctx), BuildConstants.PROD_TINY_NAME, (Dialog)null)) return false;
				} else if (c == DefaultSetupFailDialog.Choice.Close) {
					return false;
				} else if (c == DefaultSetupFailDialog.Choice.StartKangee) {
					ctx.setUnsuccessfulAndOverridden();
					return true;
				}
			}
		}
	}
	
	protected void startRestartServer() throws ServerException {
		try {
			super.startRestartServer();
		} catch (ServerException e) {
			setupFollower.makeProblemDialog("The server could not be started. Is another instance of " + BuildConstants.PROD_TINY_NAME + " running?", e);
			throw e;
		}
	}

	public void closeMainWnd() {
		if (keepOpenInBackground) gui.setVisible(false);
		else System.exit(0);
	}
	
	public void setKeepOpenInBackground(boolean keepOpenInBackground) {
		this.keepOpenInBackground = keepOpenInBackground;
	}

	public void showWindow() {
		if (gui != null) gui.setVisible(true);
	}

	@Override
	protected File determineApplicationDir() {
		File result = BasedirToolkit.getApplicationBasedir(BuildConstants.PROD_TINY_NAME);
		if (result == null) result = new File("config").getAbsoluteFile();
		result.mkdirs();
		return result;
	}

	@Override
	public ISetupFollower getSetupFollower() {
		return setupFollower;
	}
	
	public RooFrame getMainWindow() {
		return gui;
	}
	
	public ConfigurationEditor getConfEditor() {
		if (edit == null) edit = new ConfigurationEditor(this.getConfiguration(), this.getLog());
		return edit;
	}

	@Override
	protected void onIllegalSecondInstance() {
		JOptionPane.showMessageDialog(null, "Another instance of " + BuildConstants.PROD_TINY_NAME + " is currently running. " +
		"Please use this instance or close it.");
	}
	
}
