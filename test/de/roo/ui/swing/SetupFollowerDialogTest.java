package de.roo.ui.swing;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class SetupFollowerDialogTest {

	public static void main(String[] args) {
		
		SetupFollowerDialog dlg = new SetupFollowerDialog("Init-Text");
		dlg.setVisible(true);
		
		try {
			Thread.sleep(2000);
			
			dlg.setText("Setting up mega giga connection...");
			
			Thread.sleep(3000);
			
			dlg.setText("Setting up hyper");
			
			Thread.sleep(3000);
			
			dlg.setVisible(false);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
