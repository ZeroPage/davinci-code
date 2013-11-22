package gui.lobby;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class NickNamePanel {

	private JTextField nickTextField;

	public NickNamePanel(ConnectPanel connectPanel) {
		JLabel label = new JLabel("Nick Name");
		label.setForeground(Color.red);
		label.setBounds(0, 10, 100, 30);
		label.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.append(label);

		nickTextField = new JTextField();
		nickTextField.setBounds(100, 10, 100, 30);
		connectPanel.append(nickTextField);
	}

	public String getNickName() {
		return nickTextField.getText();
	}

}
