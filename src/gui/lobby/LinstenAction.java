package gui.lobby;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.Severity;
import javax.swing.JOptionPane;

import network.PortNumberException;
import network.Server;

public class LinstenAction implements ActionListener {

	private ConnectPanel connectPanel;

	public LinstenAction(ConnectPanel connectPanel) {
		this.connectPanel = connectPanel;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String nickName = connectPanel.getNickName();
		
		if (connectPanel.getNickName().length() == 0) {
			JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "알림",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Server server = new Server(); // 서버
		server.setName(nickName);
		server.setPortNum(connectPanel.getPortNum());
		try {
			server.listen();
		} catch (PortNumberException e) {
			String msg = "입력하신 Port number : " + e.getPortNum()
					+ "\nPort 는 1 ~ 65535 사이의 값이어야 합니다.\n"
					+ "기본값 10000 번 포트로 연결합니다.";
			String title = "Port number 경고";
			JOptionPane.showMessageDialog(null, msg, title,
					JOptionPane.OK_OPTION);
			server.setPortNum(10000);
			try {server.listen();} catch (PortNumberException e1) {	e1.printStackTrace();}
		}
		connectPanel.onConnected(server);
	}

}
