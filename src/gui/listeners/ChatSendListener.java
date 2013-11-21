package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import network.Network;

public class ChatSendListener implements ActionListener {
	private JTextField chatInputTextField;
	private Network network;

	public ChatSendListener(JTextField inputTextField, Network network) {
		this.chatInputTextField = inputTextField;
		this.network = network;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (chatInputTextField.getText().length() != 0) {
			network.sendChatMessage(chatInputTextField.getText());
			chatInputTextField.setText("");
			chatInputTextField.requestFocus();
		}
	}

}
