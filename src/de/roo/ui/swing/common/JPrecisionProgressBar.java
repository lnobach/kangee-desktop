package de.roo.ui.swing.common;

import javax.swing.JComponent;

/**
 * Only works with a special look-and-feel.
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
public class JPrecisionProgressBar extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3193654082098588202L;
	
	double progress = 0d;

	private ProgressBarState state;
	
	public enum ProgressBarState {
		InProgress,
		Finished,
		Failed;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
		this.repaint();
	}
	
	public void setState(ProgressBarState state) {
		this.state = state;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public ProgressBarState getState() {
		return state;
	}
	
	public void setUI(PrecisionProgressBarUI newUI) {
		super.setUI(newUI);
	}
	
}
