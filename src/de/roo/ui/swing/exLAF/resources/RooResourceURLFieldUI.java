package de.roo.ui.swing.exLAF.resources;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;

import de.roo.connectivity.IConnStateProvider.ConnectivityState;
import de.roo.ui.swing.exLAF.Colors;
import de.roo.ui.swing.exLAF.UIConstants;

public class RooResourceURLFieldUI extends BasicTextFieldUI {

	private ConnectivityState state;
	private JComponent component;

	public RooResourceURLFieldUI(ConnectivityState initConnectivityState){
		super();
		this.state = initConnectivityState;
	}
	
    public void installUI(JComponent c) {
    	//JTextField f = (AbstractButton)c;
    	//b.setContentAreaFilled(false);
    	//b.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    	c.setOpaque(false);
    	c.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    	((JTextField)c).setFont(UIConstants.defaultFont);
    	super.installUI(c);
    	this.component = c;
    	//c.setPreferredSize(new Dimension(20, 20));
    }
	
	@Override
	protected String getPropertyPrefix() {
		return "TextField";
	}
	
	public void setConnectivityState(ConnectivityState state) {
		if (!this.state.equals(state)) {
			this.state = state;
			setToConnState();
		}
	}
	
	private void setToConnState(){
		boolean unavailable = state == ConnectivityState.SettingUp || state == ConnectivityState.Unavailable
				|| state == ConnectivityState.UnavailableError;
				
		System.out.println("Setting resource view to connection state: " + state);
		
		Font font = UIConstants.defaultFont;
		Map attributes = font.getAttributes();
		if (unavailable) attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		else attributes.remove(TextAttribute.STRIKETHROUGH);
		component.setFont(new Font(attributes));
		component.repaint();
	}
	
	
	public void paintSafely(Graphics g) {
		
		GradientPaint gp = new GradientPaint(
				0, 0, Colors.textFieldBGGradient0,
				0, component.getHeight(), Colors.textFieldBGGradient1);
			((Graphics2D)g).setPaint(gp);
			g.fillRoundRect(5, 5, component.getWidth()-10, component.getHeight()-10, 10, 10);
		
		super.paintSafely(g);
	}

}













