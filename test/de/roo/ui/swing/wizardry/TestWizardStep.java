package de.roo.ui.swing.wizardry;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import de.roo.ui.swing.wizardry.common.IRooWizardStep;
import de.roo.ui.swing.wizardry.common.IValidInputListener;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class TestWizardStep implements IRooWizardStep {

	static final int MAX_STEP_NO = 10;
	
	private int stepNo;
	private String testStr = null;
	
	JTextArea area;

	public TestWizardStep(int stepNo) {
		this.stepNo = stepNo;
		area = new JTextArea("Test step #" + stepNo + " text");
	}

	public TestWizardStep(int stepNo, String testStr) {
		this(stepNo);
		this.testStr = testStr;
	}

	@Override
	public String getCaption() {
		return "Test step #" + stepNo;
	}

	@Override
	public Component getComponent(final IValidInputListener l) {
		area.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				l.inputChanged();
			}
		});
		
		return area;
	}

	@Override
	public String getShortDesc() {
		return "Test step #" + stepNo + " description";
	}

	@Override
	public boolean mayGoToNextStep() {
		if (testStr == null) return true;
		return (area.getText().equalsIgnoreCase(testStr));
	}

	@Override
	public void onCancel() {
		log("Cancel noticed by " + this);
	}

	@Override
	public void onTransitToNextStep() {
		log("Transit to next step noticed by " + this);
	}

	@Override
	public void onTransitToPreviousStep() {
		log("Transit to previous step noticed by " + this);
	}

	public String toString() {
		return this.getCaption();
	}
	
	public void log(String log) {
		System.out.println(log);
	}

	@Override
	public IRooWizardStep getNextStep() {
		if (stepNo >= MAX_STEP_NO) return null;
		if (stepNo %3 == 0) return new TestWizardStep(stepNo +1, String.valueOf(stepNo +1));
		return new TestWizardStep(stepNo + 1);
	}

	@Override
	public boolean hasNextStep() {
		return stepNo < MAX_STEP_NO;
	}

	@Override
	public void onAppear() {
		// TODO Auto-generated method stub
		
	}
	
}
