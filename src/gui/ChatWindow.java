package gui;

import gui.listeners.AboutButtonListener;
import gui.listeners.ChatSendListener;
import gui.listeners.ClearButtonListener;
import gui.listeners.ExitButtonListener;
import gui.listeners.StartButtonListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.Network;

public class ChatWindow {//implements ActionListener {
	private JPanel mainPanel;
	private JButton sendBtn; // 보내기 버튼.
	private JButton newGameButton;
	private JButton exitButton;
	private JButton clearButton;
	private JButton aboutButton;
	private JTextArea chatTextArea; // 대화내용이 쓰여지는 필드.
	private JTextField chatInputTextField; // 사용자가 대화를 입력할 부분.
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
		chatInputTextField.addActionListener(new ChatSendListener(getInputTextField(), getNetwork()));
		mainPanel.add(chatInputTextField);

		// 보내기 버튼
		sendBtn = new JButton("보내기");
		sendBtn.setBounds(0, 450, 100, 30);
		sendBtn.addActionListener(new ChatSendListener(getInputTextField(), getNetwork()));
		mainPanel.add(sendBtn);

		// 클리어 버튼
		clearButton = new JButton("클리어");
		clearButton.setBounds(100, 450, 100, 30);
		clearButton.addActionListener(new ClearButtonListener(getChatTextArea(), getInputTextField()));
		mainPanel.add(clearButton);

		// 새게임
		newGameButton = new JButton("시작");
		newGameButton.setBounds(0, 480, 100, 30);
		newGameButton.addActionListener(new StartButtonListener(getNetwork(), getGameWndGUI()));
		mainPanel.add(newGameButton);

		// 나가기
		exitButton = new JButton("나가기");
		exitButton.setBounds(100, 480, 100, 30);
		exitButton.addActionListener(new ExitButtonListener(getRoomWindow()));
		mainPanel.add(exitButton);

		// 룰 설명
		aboutButton = new JButton("게임 방법");
		aboutButton.setBounds(0, 510, 200, 30);
		aboutButton.addActionListener(new AboutButtonListener(getRoomWindow()));
		mainPanel.add(aboutButton);

		main.add(BorderLayout.EAST, mainPanel);
	}// ChatWindow() end
	
	private GameWindow getGameWndGUI() {
		return gameWndGUI;
	}

	private Network getNetwork() {
		return network;
	}

	public RoomWindow getRoomWindow() {
		return roomWindow;
	}
	
	public JTextField getInputTextField() {
		return chatInputTextField;
	}
	
	public JTextArea getChatTextArea() {
		return chatTextArea;
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