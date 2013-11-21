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
import javax.swing.JTextField;

import network.Client;
import network.Network;
import network.PortNumberException;
import network.Server;
import resource.ResourceManager;

class LobbyWindow implements ActionListener, ItemListener {
	private JTextField ipAddrTextField, nickTextField, portTextField;
	private JCheckBox serverCheckBox;
	private JButton connectButton, cancelButton;
	private JPanel lobbyPanel;

	private Network network;
	private DaVinciGUI gui;

	public LobbyWindow(DaVinciGUI daVinciGUI) {
		this.gui = daVinciGUI;
		// Lobby window 의 외형을 구현.
		ImageIcon BG = ResourceManager.getInstance().getLobbyBackground();
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
		daVinciGUI.append(lobbyPanel);
	}

	public void actionPerformed(ActionEvent event) {
		//TODO maybe apply Strategy pattern
		if (event.getSource() == connectButton
				|| event.getSource() == ipAddrTextField) {
			if (nickTextField.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "알림",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (serverCheckBox.isSelected()) {
				Server server = new Server(); // 서버
				server.setName(nickTextField.getText());
				server.setPortNum(Integer.parseInt(portTextField.getText()));
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
					try {
						server.listen();
					} catch (PortNumberException e1) {
						e1.printStackTrace();
						return;
					}
				}
				this.network = server;
			} else {
				Client client = new Client(); // 클라이언트
				client.setName(nickTextField.getText());
				client.setPortNum(Integer.parseInt(portTextField.getText()));
				client.connect(ipAddrTextField.getText());
				this.network = client;
			}

			new RoomWindow(gui, network);

			lobbyPanel.setVisible(false);

			if (network.isServer())
				network.sendChatMessage("서버를 개설하였습니다,");
			else
				network.sendChatMessage("접속하였습니다.");
		}
		if (event.getSource() == cancelButton) {
			System.exit(0);
		}
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
}
