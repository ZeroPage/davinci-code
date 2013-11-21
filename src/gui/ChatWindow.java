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

import core.GameProcess;
import network.Network;

public class ChatWindow {// implements ActionListener {
	private JPanel mainPanel;
	private JButton sendBtn; // 보내기 버튼.
	private JButton newGameButton;
	private JButton exitButton;
	private JButton clearButton;
	private JButton aboutButton;
	private JTextArea chatTextArea; // 대화내용이 쓰여지는 필드.
	private JTextField chatInputTextField; // 사용자가 대화를 입력할 부분.

	public ChatWindow(JPanel main, Network network, GameProcess gameProcess,
			DaVinciGUI gui) {
		
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
		chatInputTextField.addActionListener(new ChatSendListener(
				chatInputTextField, network));
		mainPanel.add(chatInputTextField);

		// 보내기 버튼
		sendBtn = new JButton("보내기");
		sendBtn.setBounds(0, 450, 100, 30);
		sendBtn.addActionListener(new ChatSendListener(chatInputTextField,
				network));
		mainPanel.add(sendBtn);

		// 클리어 버튼
		clearButton = new JButton("클리어");
		clearButton.setBounds(100, 450, 100, 30);
		clearButton.addActionListener(new ClearButtonListener(chatTextArea,
				chatInputTextField));
		mainPanel.add(clearButton);

		// 새게임
		newGameButton = new JButton("시작");
		newGameButton.setBounds(0, 480, 100, 30);
		newGameButton.addActionListener(new StartButtonListener(network,
				gameProcess));
		mainPanel.add(newGameButton);

		// 나가기
		exitButton = new JButton("나가기");
		exitButton.setBounds(100, 480, 100, 30);
		exitButton.addActionListener(new ExitButtonListener(gui));
		mainPanel.add(exitButton);

		// 룰 설명
		aboutButton = new JButton("게임 방법");
		aboutButton.setBounds(0, 510, 200, 30);
		aboutButton.addActionListener(new AboutButtonListener(gui));
		mainPanel.add(aboutButton);

		main.add(BorderLayout.EAST, mainPanel);
		
		//TODO maybe apply observer
		network.setTaget(this);
		
		if (!network.isServer()) {
			newGameButton.setText("대기중");
		}
	}

	public void chatMessage(String msg) {
		chatTextArea.append(msg + "\n");
		chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
	}
}