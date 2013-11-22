package gui.lobby;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddressPanel {

	private JTextField portTextField;
	private JTextField ipAddrTextField;
	//Null Object
	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	};

	public AddressPanel(ConnectPanel connectPanel) {
		JLabel label = new JLabel("IP");
		label.setForeground(Color.red);
		label.setBounds(0, 50, 100, 30);
		label.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.append(label);

		ipAddrTextField = new JTextField();
		ipAddrTextField.setBounds(100, 50, 200, 30);
		ipAddrTextField.setText("127.0.0.1");
		connectPanel.append(ipAddrTextField);

		label = new JLabel("Port");
		label.setForeground(Color.red);
		label.setBounds(0, 90, 100, 30);
		label.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.append(label);

		portTextField = new JTextField();
		portTextField.setBounds(100, 90, 50, 30);
		portTextField.setText("10000");
		connectPanel.append(portTextField);
	}

	public int getPortNum() {
		return Integer.parseInt(portTextField.getText());
	}

	public String getAddress() {
		return ipAddrTextField.getText();
	}

	public void enableAddressField() {
		ipAddrTextField.setEnabled(true);
	}

	public void disableAddressField() {
		ipAddrTextField.setEnabled(false);
	}

	public void setAction(ActionListener action) {
		ipAddrTextField.removeActionListener(listener);
		listener = action;
		ipAddrTextField.addActionListener(listener);
	}

}
