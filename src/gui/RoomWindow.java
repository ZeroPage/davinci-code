package gui;
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import network.Network;

public class RoomWindow extends JFrame {
	private JPanel roomPanel = null;
	private ChatWindow chatWnd = null; // 게임 우측 채팅 윈도우
	private GameWindow gameWindow = null; // 게임 좌측 실제 게임 윈도우.

	public RoomWindow(DaVinciGUI gui, Network network) { 
		// player 에 따른 네트워크 설정과 room 윈도우 설정.

		//TODO maybe apply observer
		network.setTaget(this);
		roomPanel = new JPanel();
		roomPanel.setLayout(new BorderLayout());

		gameWindow = new GameWindow(roomPanel, network);
		chatWnd = new ChatWindow(roomPanel, network, gameWindow, gui); // 채팅윈도우를 생성하여 JPanel_Room 에 붙인다.
		chatWnd.SetButton(network.isServer());

		//gui.add(BorderLayout.CENTER, roomPanel);
		gui.append(roomPanel);
	}

	public void addChatString(String msg) {
		chatWnd.stringAdd(msg); // 현재 player 의 채팅창에만 메시지를 출력한다.
	}

	public void enable(){
		roomPanel.setVisible(true);
	}
}
