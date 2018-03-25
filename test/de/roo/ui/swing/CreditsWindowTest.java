package de.roo.ui.swing;

import de.roo.configuration.DefaultConfiguration;
import de.roo.logging.ConsoleLog;
import de.roo.logging.ILog;
import de.roo.ui.swing.util.LookAndFeelManager;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class CreditsWindowTest {

	public static void main(String[] args) {
		ILog log = new ConsoleLog();
		LookAndFeelManager.trySetLookAndFeel(LookAndFeelManager.getRecommendedLaF(log), log);
		new CreditsWindow(new DefaultConfiguration(), log).setVisible(true);
	}
	
}
