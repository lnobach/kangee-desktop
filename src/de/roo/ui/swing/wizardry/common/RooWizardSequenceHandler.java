package de.roo.ui.swing.wizardry.common;

import java.util.Stack;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
class RooWizardSequenceHandler {
	
	private IRooWizardStep currentStep;
	private Stack<IRooWizardStep> prevSteps = new Stack<IRooWizardStep>();
	private boolean cancelled = false;
	private String title;
	private boolean finished;

	RooWizardSequenceHandler(IRooWizardStep startStep, String title) {
		currentStep = startStep;
		this.title = title;
	}
	
	public IRooWizardStep getCurrentStep() {
		return currentStep;
	}
	
	public void nextStep() {
		if (!getCurrentStep().mayGoToNextStep()) throw new IllegalStateException("May not jump to next step, since the current one is not completed");
		IRooWizardStep nextStep = currentStep.getNextStep();
		if (isLastStep()) {
			finish();
		} else {
			if (nextStep == null)  throw new IllegalStateException("Last step reached. There is no next step.");
			nextStep.onAppear();
		}
		currentStep.onTransitToNextStep();
		prevSteps.push(currentStep);
		currentStep = nextStep;
	}
	
	private void finish() {
		finished = true;
		synchronized(this) {
			this.notify();
		}
	}

	public void previousStep() {
		if (prevSteps.isEmpty())  throw new IllegalStateException("First step. There is no previous step.");
		getCurrentStep().onTransitToPreviousStep();
		currentStep = prevSteps.pop();
	}
	
	public boolean isFirstStep() {
		return prevSteps.isEmpty();
	}
	
	public void cancel() {
		getCurrentStep().onCancel();
		finished = true;
		cancelled = true;
		synchronized(this) {
			this.notify();
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean isLastStep() {
		return !currentStep.hasNextStep();
	}

	public boolean hasFinished() {
		return finished;
	}
	
	public boolean wasCancelled() {
		return cancelled;
	}
}
