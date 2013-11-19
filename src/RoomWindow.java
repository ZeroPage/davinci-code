import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class RoomWindow extends JFrame {
	JPanel JPanel_Room = null;
	Network myNetwork;
	ChatWindow chatWnd = null; // 게임 우측 채팅 윈도우
	GameWindow gameWndGUI = null; // 게임 좌측 실제 게임 윈도우.

	public RoomWindow(JPanel main, Network n) { // player 에 따른 네트워크 설정과 room 윈도우
												// 설정.
		myNetwork = n;

		JPanel_Room = new JPanel();
		JPanel_Room.setLayout(new BorderLayout());

		gameWndGUI = new GameWindow(JPanel_Room, myNetwork);
		chatWnd = new ChatWindow(JPanel_Room); // 채팅윈도우를 생성하여 JPanel_Room 에 붙인다.
		chatWnd.SetButton(myNetwork.isServer());

		this.addWindowListener(new myWindowListener());
		main.add(BorderLayout.CENTER, JPanel_Room);
	}

	class ChatWindow implements ActionListener {
		JPanel JPanel_Main;
		JButton JB_Send; // 보내기 버튼.
		JButton JB_NewGame;
		JButton JB_Exit;
		JButton JB_ChatClear;
		JButton JB_About;
		JTextArea JTA_Chat; // 대화내용이 쓰여지는 필드.
		JTextField JTF_ChatInput; // 사용자가 대화를 입력할 부분.

		public ChatWindow(JPanel main) {
			JPanel_Main = new JPanel();
			JPanel_Main.setLayout(null);
			JPanel_Main.setPreferredSize(new Dimension(200, 500));

			// 채팅창
			JTA_Chat = new JTextArea();
			JTA_Chat.setEditable(false);
			JTA_Chat.setFocusable(true);
			JTA_Chat.setLineWrap(true);
			JTA_Chat.setBackground(new Color(192, 192, 192));
			JScrollPane JSP_ChatScroll = new JScrollPane(JTA_Chat,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			JSP_ChatScroll.setAutoscrolls(true);
			JSP_ChatScroll.setBounds(0, 0, 200, 400);
			JPanel_Main.add(JSP_ChatScroll);

			// 채팅 입력창
			JTF_ChatInput = new JTextField();
			JTF_ChatInput.setBounds(0, 420, 200, 30);
			JTF_ChatInput.addActionListener(this);
			JPanel_Main.add(JTF_ChatInput);

			// 보내기 버튼
			JB_Send = new JButton("보내기");
			JB_Send.setBounds(0, 450, 100, 30);
			JB_Send.addActionListener(this);
			JPanel_Main.add(JB_Send);

			// 클리어 버튼
			JB_ChatClear = new JButton("클리어");
			JB_ChatClear.setBounds(100, 450, 100, 30);
			JB_ChatClear.addActionListener(this);
			JPanel_Main.add(JB_ChatClear);

			// 새게임
			JB_NewGame = new JButton("시작");
			JB_NewGame.setBounds(0, 480, 100, 30);
			JB_NewGame.addActionListener(this);
			JPanel_Main.add(JB_NewGame);

			// 나가기
			JB_Exit = new JButton("나가기");
			JB_Exit.setBounds(100, 480, 100, 30);
			JB_Exit.addActionListener(this);
			JPanel_Main.add(JB_Exit);

			// 룰 설명
			JB_About = new JButton("게임 방법");
			JB_About.setBounds(0, 510, 200, 30);
			JB_About.addActionListener(this);
			JPanel_Main.add(JB_About);

			main.add(BorderLayout.EAST, JPanel_Main);
		}// ChatWindow() end

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == JB_Send
					|| event.getSource() == JTF_ChatInput) {
				// 텍스트 전송
				if (JTF_ChatInput.getText().length() != 0) {
					myNetwork.SendChatMsg(JTF_ChatInput.getText());
					JTF_ChatInput.setText("");
					JTF_ChatInput.requestFocus();
				}
			}
			if (event.getSource() == JB_ChatClear) { // 클리어
				JTA_Chat.setText("");
				JTF_ChatInput.requestFocus();
			}
			if (event.getSource() == JB_NewGame) { // 새게임 시작
				myNetwork.SendChatMsg("게임을 새로 시작합니다.");
				// TODO 클라이언트들의 현재 상태를 종료한 후 새로 시작해야 한다.
				gameWndGUI.start();
			}
			if (event.getSource() == JB_Exit) { // 종료코드
				getWindowListeners()[0].windowClosing(new WindowEvent(
						getWindows()[0], 0));
			}
			if (event.getSource() == JB_About) { // 게임 설명
				JDialog some = new JDialog((Frame) getWindows()[0], "게임설명",
						true) {
					ImageIcon BG = new ImageIcon(
							DavichiGUI.class.getResource("About.jpg"));

					public void paint(Graphics g) {
						this.setSize(BG.getIconWidth(), BG.getIconHeight());
						g.drawImage(BG.getImage(), 0, 20, this.getWidth(),
								this.getHeight(), null);
						// this.getContentPane().
						// super.paint(g);
					}
				};
				some.setVisible(true);
			}
		}

		public void StringAdd(String msg) {
			JTA_Chat.append(msg + "\n");
			JTA_Chat.setCaretPosition(JTA_Chat.getDocument().getLength());
		}

		public void SetButton(boolean server) {
			if (!server) {
				JB_NewGame.setText("대기중");
			}
		}
	}

	public void AddChatString(String msg) {
		chatWnd.StringAdd(msg); // 현재 player 의 채팅창에만 메시지를 출력한다.
	}
}