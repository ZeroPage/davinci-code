package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import network.Client;
import network.Network;
import network.Server;
import resource.ResourceManager;

class LobbyWindow implements ActionListener, ItemListener {
	JTextField ipAddrTextField, nickTextField, portTextField;
	JCheckBox serverCheckBox;
	JButton connectButton, cancelButton;
	JPanel lobbyPanel;
	JRootPane motherPane;

	Network network; 
	// 네트워크에 접속할 때의 player의 상태를 저장할 변수.
	// server 역할을 하는 player일 경우 myNetworkType = Server();
	// client 인 player 일 경우 myNetworkType = Client();

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectButton || event.getSource() == ipAddrTextField) {
			if (nickTextField.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "알림",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (serverCheckBox.isSelected())
				network = new Server(); // 서버
			else
				network = new Client(); // 클라이언트

			network.setName(nickTextField.getText()); // 대상 네트워크 객체에 nickname
													// 설정.
			network.setPortNum(Integer.parseInt(portTextField.getText()));
			// 포트 설정.
			network.Connect(ipAddrTextField.getText());
			// server 에 접속.

			network.setMyRoomWnd(new RoomWindow((JPanel) motherPane
					.getContentPane(), network));
			// 룸윈도우 생성하고 네트워크와 연결.

			lobbyPanel.setVisible(false);
			network.getMyRoomWnd().getJPanel_Room().setVisible(true);

			if (network.isServer())
				network.sendChatMessage("서버를 개설하였습니다,");
			else
				network.sendChatMessage("접속하였습니다.");
		}
		if (event.getSource() == cancelButton) {
			System.exit(0);
		}
	}

	public LobbyWindow(JPanel main, JRootPane mother) {
		// Lobby window 의 외형을 구현.
		motherPane = mother;

		final ImageIcon BG = ResourceManager.getInstance().getLobbyBackground();
		lobbyPanel = new JBackgroundPanel(BG, JBackgroundPanel.MODE_STRECH);

		lobbyPanel.setLayout(null);

		JPanel connectPanel = new JPanel();
		connectPanel.setOpaque(false);
		connectPanel.setPreferredSize(new Dimension(380, 300));

		connectPanel.setLayout(null);
		connectPanel.setBounds(600, 300, 380, 300);
		connectPanel.setOpaque(false);

		JLabel temp = new JLabel("Nick Name");
		temp.setForeground(Color.red);
		temp.setBounds(0, 10, 100, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.add(temp);

		nickTextField = new JTextField();
		nickTextField.setBounds(100, 10, 100, 30);
		connectPanel.add(nickTextField);

		temp = new JLabel("Server");
		temp.setForeground(Color.red);
		temp.setBounds(200, 10, 80, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.add(temp);

		serverCheckBox = new JCheckBox();
		serverCheckBox.setBounds(280, 19, 13, 13);
		serverCheckBox.addItemListener(this);
		serverCheckBox.setMargin(new Insets(-2, -2, -2, -2));
		connectPanel.add(serverCheckBox);

		temp = new JLabel("IP");
		temp.setForeground(Color.red);
		temp.setBounds(0, 50, 100, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.add(temp);

		ipAddrTextField = new JTextField();
		ipAddrTextField.setBounds(100, 50, 200, 30);
		ipAddrTextField.setText("127.0.0.1");
		ipAddrTextField.addActionListener(this);
		connectPanel.add(ipAddrTextField);

		temp = new JLabel("Port");
		temp.setForeground(Color.red);
		temp.setBounds(0, 90, 100, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.add(temp);

		portTextField = new JTextField();
		portTextField.setBounds(100, 90, 50, 30);
		portTextField.setText("10000");
		connectPanel.add(portTextField);

		connectButton = new JButton("접속");
		connectButton.setBounds(50, 130, 100, 30);
		connectButton.addActionListener(this);
		connectPanel.add(connectButton);

		cancelButton = new JButton("취소");
		cancelButton.setBounds(200, 130, 100, 30);
		cancelButton.addActionListener(this);
		connectPanel.add(cancelButton);

		lobbyPanel.add(connectPanel);
		nickTextField.requestFocus();

		lobbyPanel.add(connectPanel);
		main.add(lobbyPanel);
	}

	public void itemStateChanged(ItemEvent event) {
		// Server 항목에 체크할 경우 IP 입력필드가 비활성화됨.
		if (event.getSource() == serverCheckBox) {
			if (serverCheckBox.isSelected()) {
				ipAddrTextField.setEnabled(false);
			} else {
				ipAddrTextField.setEnabled(true);
			}
		}
	}

	public void AddChatString(String msg) { // DavichiGUI 에서 종료메시지를 보내기 위해 작성됨.
		network.sendChatMessage(msg); // 모든 player 들에게 메시지를 전송한다.
	}
}
