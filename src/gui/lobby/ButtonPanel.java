package gui.lobby;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.DaVinciGUI;
import gui.listeners.ExitButtonListener;

import javax.swing.JButton;

public class ButtonPanel {
	private JButton connectButton;
	// null Object
	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	};

	public ButtonPanel(ConnectPanel connectPanel, DaVinciGUI gui) {

		connectButton = new JButton("접속");
		connectButton.setBounds(50, 130, 100, 30);
		connectButton.addActionListener(listener);
		connectPanel.append(connectButton);

		JButton cancelButton = new JButton("취소");
		cancelButton.setBounds(200, 130, 100, 30);
		cancelButton.addActionListener(new ExitButtonListener(gui));
		connectPanel.append(cancelButton);
	}

	public void setAction(ActionListener action) {
		connectButton.removeActionListener(listener);
		listener = action;
		connectButton.addActionListener(listener);
	}

}
