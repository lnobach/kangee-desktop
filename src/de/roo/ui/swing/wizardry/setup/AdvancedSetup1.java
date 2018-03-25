package de.roo.ui.swing.wizardry.setup;

import java.awt.Component;

import de.roo.BuildConstants;
import de.roo.configuration.IWritableConf;
import de.roo.engine.setup.ISetupContext;
import de.roo.logging.ILog;
import de.roo.ui.swing.configuration.IConfigTab;
import de.roo.ui.swing.configuration.IConfigurationContext;
import de.roo.ui.swing.configuration.NetworkTab;
import de.roo.ui.swing.wizardry.common.IRooWizardStep;
import de.roo.ui.swing.wizardry.common.IValidInputListener;

public class AdvancedSetup1 implements IRooWizardStep {


	private IConfigTab comp;
	private ISetupContext ctx;
	
	public AdvancedSetup1(final ISetupContext ctx) {
		this.ctx = ctx;
		comp = new NetworkTab(new IConfigurationContext() {
			
			@Override
			public ILog getLog() {
				return ctx.getLog();
			}
			
			@Override
			public IWritableConf getConf() {
				return ctx.getConf();
			}
			
			@Override
			public void confDlgClosed() {
				//Nothing to do
			}
		});
	}

	@Override
	public String getCaption() {
		return "Network Setup";
	}

	@Override
	public Component getComponent(IValidInputListener l) {
		return comp.getComponent();
	}

	@Override
	public String getShortDesc() {
		return BuildConstants.PROD_TINY_NAME + " may need to set up the connection to work properly and recommends " +
				"the following network setup configuration. It should work quite good. " +
				"If you are a pro, change it if desired.";
	}

	@Override
	public boolean mayGoToNextStep() {
		return true;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTransitToNextStep() {
		comp.commitChanges();
	}

	@Override
	public void onTransitToPreviousStep() {
		// TODO Auto-generated method stub

	}


	@Override
	public IRooWizardStep getNextStep() {
		return new CheckForUpdates(ctx);
	}

	@Override
	public boolean hasNextStep() {
		return true;
	}

	@Override
	public void onAppear() {
		// TODO Auto-generated method stub
		
	}
	
}
