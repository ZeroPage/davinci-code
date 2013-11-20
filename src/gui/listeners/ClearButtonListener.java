package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClearButtonListener implements ActionListener {
	private JTextArea chatTextArea;
	private JTextField chatInputTextField;

	public ClearButtonListener(JTextArea chatTextArea, JTextField inputTextField) {
		this.chatTextArea = chatTextArea;
		this.chatInputTextField = inputTextField;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		chatTextArea.setText("");
		chatInputTextField.requestFocus();
	}

}
