package de.roo.ui.swing.wizardry.common;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.roo.gfx.RooStaticIconBuffer;

/**
 * 
 * @author Leo Nobach (dev@getkangee.com)
 *
 */
class RooWizardDialog extends JDialog implements ActionListener, WindowListener {
	
	static final Insets DEFAULT_CONT_INSETS = new Insets(6, 6, 6, 6);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2987157625380983529L;
	
	public static final ImageIcon WIZARD_LOGO = new ImageIcon(RooWizardDialog.class.getResource("/de/roo/gfx/wizard-logo.png"));
	
	//Component shownStepEncSP = null;
	Component shownStep;
	
	ContainerPanel cont = new ContainerPanel();
	
	JButton nextFinishBtn;
	JButton prevBtn;
	JButton cancelBtn;
	TopPanel tp;

	private RooWizardSequenceHandler hdlr;
	
	RooWizardDialog(RooWizardSequenceHandler hdlr, Dialog parent) {
		super(parent);
		init(hdlr);
	}
	
	RooWizardDialog(RooWizardSequenceHandler hdlr, Frame parent) {
		super(parent);
		init(hdlr);
	}

	private void init(RooWizardSequenceHandler hdlr) {
		this.hdlr = hdlr;
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setIconImage(RooStaticIconBuffer.WND_ICON.getImage());
		this.setLayout(new BorderLayout());
		refreshTitle();
		this.addWindowListener(this);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		tp = new TopPanel();
		this.add(tp, BorderLayout.NORTH);
		
		JPanel btnPane = new JPanel();
		btnPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		prevBtn = new JButton("...Previous");
		prevBtn.setVisible(false);
		prevBtn.addActionListener(this);
		btnPane.add(prevBtn);
		
		nextFinishBtn = new JButton();
		nameNextBtn();
		checkNextBtnEnabled();
		nextFinishBtn.addActionListener(this);
		btnPane.add(nextFinishBtn);
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(this);
		btnPane.add(cancelBtn);
		
		this.add(btnPane, BorderLayout.SOUTH);
		
		this.add(cont, BorderLayout.CENTER);
		
		refreshComponent();
	}

	private void refreshComponent() {
		setCurrentComponent(hdlr.getCurrentStep().getComponent(new ValidInputListener()));
	}
	
	private void setCurrentComponent(Component comp) {
		
		cont.setComponent(comp);
		
		/*
		if (shownStep != null) this.remove(shownStep);
		//shownStepEncSP = new JScrollPane(comp);
		this.add(shownStep = comp, BorderLayout.CENTER);
		*/
	}
	
	private void refresh() {
		refreshTitle();
		prevBtn.setVisible(!hdlr.isFirstStep());
		nameNextBtn();
		checkNextBtnEnabled();
		refreshComponent();
		tp.refresh();
		this.invalidate();
		this.validate();
		this.repaint();
	}
	
	private void nameNextBtn() {
		nextFinishBtn.setText(hdlr.isLastStep()?"Finish":"Next...");
	}
	
	private void checkNextBtnEnabled() {
		nextFinishBtn.setEnabled(hdlr.getCurrentStep().mayGoToNextStep());
	}
	
	private void refreshTitle() {
		String title = hdlr.getTitle() + " - " + hdlr.getCurrentStep().getCaption();
		this.setTitle(title);
	}
	
	class TopPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3087350603179257527L;
		
		JLabel taskCaption;
		JLabel desc;
		
		public TopPanel() {
			this.setBackground(SystemColor.white);
			this.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.weightx = 0;
			c.weighty = 0;
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			JLabel logo = new JLabel();
			logo.setIcon(WIZARD_LOGO);
			this.add(logo, c);
			
			c.weightx = 1;
			c.gridx = 1;
			taskCaption = new JLabel(hdlr.getCurrentStep().getCaption());
			taskCaption.setFont(taskCaption.getFont().deriveFont(Font.BOLD));
			this.add(taskCaption, c);
			
			c.gridy = 1;
			c.gridx = 0;
			c.gridwidth = 2;
			desc = new JLabel("<html>" + hdlr.getCurrentStep().getShortDesc() + "<html>");
			desc.setBorder(BorderFactory.createLineBorder(SystemColor.LIGHT_GRAY, 4));
			desc.setOpaque(true);
			desc.setBackground(SystemColor.lightGray);
			desc.setFont(desc.getFont().deriveFont(Font.PLAIN));
			this.add(desc, c);
			
		}
		
		public void refresh() {
			taskCaption.setText("<html>" + hdlr.getCurrentStep().getCaption() + "<html>");
			desc.setText("<html>" + hdlr.getCurrentStep().getShortDesc() + "<html>");
		}
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == prevBtn) {
			hdlr.previousStep();
			refresh();
		} else if (src == nextFinishBtn) {
			hdlr.nextStep();
			if (hdlr.hasFinished()) this.setVisible(false);
			else refresh();
		} else if (src == cancelBtn) {
			cancel();
		}
	}
	
	void cancel() {
		hdlr.cancel();
		this.setVisible(false);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		//Nothing to do
	}

	@Override
	public void windowClosed(WindowEvent e) {
		//Nothing to do
	}

	@Override
	public void windowClosing(WindowEvent e) {
		cancel();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		//Nothing to do
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		//Nothing to do
	}

	@Override
	public void windowIconified(WindowEvent e) {
		//Nothing to do
	}

	@Override
	public void windowOpened(WindowEvent e) {
		//Nothing to do
	}
	
	class ValidInputListener implements IValidInputListener {

		@Override
		public void inputChanged() {
			checkNextBtnEnabled();
		}
		
	}
	
	class ContainerPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3270161205363118057L;

		public ContainerPanel() {
			this.setLayout(new BorderLayout());
		}
		
		@Override
		public Insets getInsets() {
			return DEFAULT_CONT_INSETS;
		}
		
		public void setComponent(Component comp) {
			if (shownStep != null) this.remove(shownStep);
			this.add(shownStep = comp, BorderLayout.CENTER);
		}
		
	};
}





