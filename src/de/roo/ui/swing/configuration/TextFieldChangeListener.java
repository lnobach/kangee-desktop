package de.roo.ui.swing.configuration;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

public abstract class TextFieldChangeListener implements CaretListener {

	String oldText;
	private JTextComponent c;

	public TextFieldChangeListener(JTextComponent c) {
		this.c = c;
		oldText = c.getText();
	}
	
	@Override
	public void caretUpdate(CaretEvent e) {
		String newText = c.getText();
		if (!newText.equals(oldText)) onTextChange(oldText, newText);
		oldText = newText;
	}
	
	public abstract void onTextChange(String oldText, String newText);
	
}
