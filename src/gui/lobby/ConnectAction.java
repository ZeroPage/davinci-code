package gui.lobby;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import network.Client;

public class ConnectAction implements ActionListener {

	private ConnectPanel connectPanel;

	public ConnectAction(ConnectPanel connectPanel) {
		this.connectPanel = connectPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String nickName = connectPanel.getNickName();
		if (connectPanel.getNickName().length() == 0) {
			JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "알림",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Client client = new Client(); // 클라이언트
		client.setName(nickName);
		client.setPortNum(connectPanel.getPortNum());
		client.connect(connectPanel.getAddress());

		connectPanel.onConnected(client);

	}

}
