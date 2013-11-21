package gui;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import network.Network;

public class RoomWindow extends JFrame {
	private JPanel JPanel_Room = null;
	private ChatWindow chatWnd = null; // 게임 우측 채팅 윈도우
	private GameWindow gameWndGUI = null; // 게임 좌측 실제 게임 윈도우.

	public RoomWindow(JPanel main, Network network) { 
		// player 에 따른 네트워크 설정과 room 윈도우 설정.

		//TODO maybe apply observer
		network.setTaget(this);
		setJPanel_Room(new JPanel());
		getJPanel_Room().setLayout(new BorderLayout());

		gameWndGUI = new GameWindow(getJPanel_Room(), network);
		chatWnd = new ChatWindow(getJPanel_Room(), network, gameWndGUI, this); // 채팅윈도우를 생성하여 JPanel_Room 에 붙인다.
		chatWnd.SetButton(network.isServer());

		this.addWindowListener(new CloseListener());
		main.add(BorderLayout.CENTER, getJPanel_Room());
	}

	public void AddChatString(String msg) {
		chatWnd.StringAdd(msg); // 현재 player 의 채팅창에만 메시지를 출력한다.
	}

	public JPanel getJPanel_Room() {
		return JPanel_Room;
	}

	public void setJPanel_Room(JPanel jPanel_Room) {
		JPanel_Room = jPanel_Room;
	}
}
