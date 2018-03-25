package de.roo.ui.swing.wizardry.setup;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.roo.engine.setup.ISetupContext;
import de.roo.ui.swing.util.DropBoxLayout;
import de.roo.ui.swing.wizardry.common.IRooWizardStep;
import de.roo.ui.swing.wizardry.common.IValidInputListener;

public class Nickname implements IRooWizardStep {

	private StepComponent comp;

	private ISetupContext ctx;
	
	public Nickname(ISetupContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public String getCaption() {
		return "Nickname";
	}

	@Override
	public Component getComponent(IValidInputListener l) {
		comp = new StepComponent(l);
		return comp;
	}

	@Override
	public String getShortDesc() {
		return "Choose a nickname that other people will see when you send " +
				"links to them. Choose it so that they will know who you are. " +
				"You may also leave the field blank.";
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
		ctx.getConf().setValue("Nickname", comp.getSelectedNickname());
	}

	@Override
	public void onTransitToPreviousStep() {
		// TODO Auto-generated method stub

	}
	
	class StepComponent extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7994999250219153593L;
		JTextField nickname;

		public StepComponent(IValidInputListener l) {
			
			DropBoxLayout ly = new DropBoxLayout(DropBoxLayout.MODE_FILL, DropBoxLayout.ORIENTATION_VERTICAL);
			ly.setForceFillSize(true);
			this.setLayout(ly);

			String nn = ctx.getConf().getValueString("Nickname", "");
			
			JLabel nnLbl = new JLabel("Nickname:");
			nickname = new JTextField(nn);
			
			this.add(nnLbl);
			this.add(nickname);
			
		}
		
		public String getSelectedNickname() {
			return nickname.getText();
		}
		
	}

	@Override
	public IRooWizardStep getNextStep() {
		return new ChoosePort(ctx);
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














