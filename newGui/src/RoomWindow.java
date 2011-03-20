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

class RoomWindow extends JFrame
{
		JPanel		JPanel_Room	= null;
		Network		myNetwork;
		ChatWindow	chatWnd 	= null;		// ���� ���� ä�� ������
		GameWindow	gameWndGUI	= null;		// ���� ���� ���� ���� ������.
		
		public RoomWindow(JPanel main, Network n) {		// player �� ���� ��Ʈ��ũ ������ room ������ ����.
			myNetwork = n;
			
			JPanel_Room = new JPanel();
			JPanel_Room.setLayout(new BorderLayout());
			
			gameWndGUI	= new GameWindow(JPanel_Room, myNetwork);
			chatWnd		= new ChatWindow(JPanel_Room);		// ä�������츦 �����Ͽ� JPanel_Room �� ���δ�.
			chatWnd.SetButton(myNetwork.isServer());	
			
			this.addWindowListener(new myWindowListener());
			main.add(BorderLayout.CENTER, JPanel_Room);
		}
		class ChatWindow implements ActionListener {
			JPanel		JPanel_Main;
			JButton 	JB_Send;		// ������ ��ư.
			JButton 	JB_NewGame;
			JButton 	JB_Exit;
			JButton 	JB_ChatClear;
			JButton 	JB_About;
			JTextArea 	JTA_Chat;		// ��ȭ������ �������� �ʵ�.
			JTextField 	JTF_ChatInput;	// ����ڰ� ��ȭ�� �Է��� �κ�.
			
			public ChatWindow(JPanel main) {
				JPanel_Main = new JPanel();
				JPanel_Main.setLayout(null);
				JPanel_Main.setPreferredSize(new Dimension(200,500));
				
				//ä��â
				JTA_Chat = new JTextArea();
				JTA_Chat.setEditable(false);
				JTA_Chat.setFocusable(true);
				JTA_Chat.setLineWrap(true);
				JTA_Chat.setBackground(new Color(192,192,192));
				JScrollPane JSP_ChatScroll = new JScrollPane(JTA_Chat,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				JSP_ChatScroll.setAutoscrolls(true);
				JSP_ChatScroll.setBounds(0,0, 200, 400);
				JPanel_Main.add(JSP_ChatScroll);
				
				//ä�� �Է�â
				JTF_ChatInput = new JTextField();
				JTF_ChatInput.setBounds(0, 420, 200, 30);
				JTF_ChatInput.addActionListener(this);
				JPanel_Main.add(JTF_ChatInput);
				
				//������ ��ư
				JB_Send = new JButton("������");
				JB_Send.setBounds(0, 450, 100, 30);
				JB_Send.addActionListener(this);
				JPanel_Main.add(JB_Send);
				
				//Ŭ���� ��ư 
				JB_ChatClear = new JButton("Ŭ����");
				JB_ChatClear.setBounds(100, 450, 100, 30);
				JB_ChatClear.addActionListener(this);
				JPanel_Main.add(JB_ChatClear);
				
				//������
				JB_NewGame = new JButton("����");
				JB_NewGame.setBounds(0, 480, 100, 30);
				JB_NewGame.addActionListener(this);
				JPanel_Main.add(JB_NewGame);
				
				//������
				JB_Exit = new JButton("������");
				JB_Exit.setBounds(100, 480, 100, 30);
				JB_Exit.addActionListener(this);
				JPanel_Main.add(JB_Exit);
				
				//�� ����
				JB_About = new JButton("���� ���");
				JB_About.setBounds(0,510,200,30);
				JB_About.addActionListener(this);
				JPanel_Main.add(JB_About);
					
				main.add(BorderLayout.EAST, JPanel_Main);
			}// ChatWindow() end
			public void actionPerformed(ActionEvent event) {
				if(event.getSource() == JB_Send || event.getSource() == JTF_ChatInput) {
					//�ؽ�Ʈ ����
					if(JTF_ChatInput.getText().length() !=0) {	
						myNetwork.SendChatMsg(JTF_ChatInput.getText());
						JTF_ChatInput.setText("");
						JTF_ChatInput.requestFocus();
					}
				}
				if(event.getSource() == JB_ChatClear) {				//Ŭ����
					JTA_Chat.setText("");
					JTF_ChatInput.requestFocus();
				}
				if(event.getSource() == JB_NewGame) {				//������ ����
					myNetwork.SendChatMsg("������ ���� �����մϴ�.");
					//TODO Ŭ���̾�Ʈ���� ���� ���¸� ������ �� ���� �����ؾ� �Ѵ�.
					gameWndGUI.start();
				}
				if(event.getSource() == JB_Exit) {					//�����ڵ�
					getWindowListeners()[0].windowClosing(new WindowEvent(getWindows()[0], 0));
				}
				if(event.getSource() == JB_About) {					//���� ����
					JDialog some = new JDialog( (Frame) getWindows()[0],"���Ӽ���", true ) {
						ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("About.jpg"));
						public void paint(Graphics g) {
							this.setSize(BG.getIconWidth(), BG.getIconHeight());
							g.drawImage(BG.getImage(), 0,20, this.getWidth(), this.getHeight(), null);
							//this.getContentPane().
							//super.paint(g);
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
				if(!server) {
					JB_NewGame.setText("�����");
				}
			}
		}
		public void AddChatString(String msg) {
			chatWnd.StringAdd(msg);			// ���� player �� ä��â���� �޽����� ����Ѵ�.
		}
	}