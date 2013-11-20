package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.Network;
import resource.ResourceManager;

public class ChatWindow implements ActionListener {
	private JPanel mainPanel;
	private JButton sendBtn; // 보내기 버튼.
	private JButton newGameButton;
	private JButton exitButton;
	JButton clearButton;
	JButton aboutButton;
	JTextArea chatTextArea; // 대화내용이 쓰여지는 필드.
	JTextField chatInputTextField; // 사용자가 대화를 입력할 부분.
	private Network network;
	private GameWindow gameWndGUI;
	private RoomWindow roomWindow;

	public ChatWindow(JPanel main, Network network, GameWindow gameWndGUI,
			RoomWindow roomWindow) {
		this.network = network;
		this.gameWndGUI = gameWndGUI;
		this.roomWindow = roomWindow;
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(200, 500));

		// 채팅창
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		chatTextArea.setFocusable(true);
		chatTextArea.setLineWrap(true);
		chatTextArea.setBackground(new Color(192, 192, 192));
		JScrollPane chatScrollPanel = new JScrollPane(chatTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPanel.setAutoscrolls(true);
		chatScrollPanel.setBounds(0, 0, 200, 400);
		mainPanel.add(chatScrollPanel);

		// 채팅 입력창
		chatInputTextField = new JTextField();
		chatInputTextField.setBounds(0, 420, 200, 30);
		chatInputTextField.addActionListener(this);
		mainPanel.add(chatInputTextField);

		// 보내기 버튼
		sendBtn = new JButton("보내기");
		sendBtn.setBounds(0, 450, 100, 30);
		sendBtn.addActionListener(this);
		mainPanel.add(sendBtn);

		// 클리어 버튼
		clearButton = new JButton("클리어");
		clearButton.setBounds(100, 450, 100, 30);
		clearButton.addActionListener(this);
		mainPanel.add(clearButton);

		// 새게임
		newGameButton = new JButton("시작");
		newGameButton.setBounds(0, 480, 100, 30);
		newGameButton.addActionListener(this);
		mainPanel.add(newGameButton);

		// 나가기
		exitButton = new JButton("나가기");
		exitButton.setBounds(100, 480, 100, 30);
		exitButton.addActionListener(this);
		mainPanel.add(exitButton);

		// 룰 설명
		aboutButton = new JButton("게임 방법");
		aboutButton.setBounds(0, 510, 200, 30);
		aboutButton.addActionListener(this);
		mainPanel.add(aboutButton);

		main.add(BorderLayout.EAST, mainPanel);
	}// ChatWindow() end

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == sendBtn
				|| event.getSource() == chatInputTextField) {
			// 텍스트 전송
			if (chatInputTextField.getText().length() != 0) {
				network.SendChatMsg(chatInputTextField.getText());
				chatInputTextField.setText("");
				chatInputTextField.requestFocus();
			}
		}
		if (event.getSource() == clearButton) { // 클리어
			chatTextArea.setText("");
			chatInputTextField.requestFocus();
		}
		if (event.getSource() == newGameButton) { // 새게임 시작
			network.SendChatMsg("게임을 새로 시작합니다.");
			// TODO 클라이언트들의 현재 상태를 종료한 후 새로 시작해야 한다.
			gameWndGUI.start();
		}
		if (event.getSource() == exitButton) { // 종료코드
			roomWindow.getWindowListeners()[0].windowClosing(new WindowEvent(
					roomWindow.getWindows()[0], 0));
		}
		if (event.getSource() == aboutButton) { // 게임 설명
			JDialog helpDialog = new JDialog(roomWindow.getFrames()[0], "게임설명",
					true) {
				private static final long serialVersionUID = 1L;
				ImageIcon BG = ResourceManager.getInstance().getHelp();

				public void paint(Graphics g) {
					this.setSize(BG.getIconWidth(), BG.getIconHeight());
					g.drawImage(BG.getImage(), 0, 20, this.getWidth(),
							this.getHeight(), null);
					// this.getContentPane().
					// super.paint(g);
				}
			};
			helpDialog.setBounds(0, 0, 100, 100);
			helpDialog.setVisible(true);
		}
	}

	public void StringAdd(String msg) {
		chatTextArea.append(msg + "\n");
		chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
	}

	public void SetButton(boolean server) {
		if (!server) {
			newGameButton.setText("대기중");
		}
	}
}