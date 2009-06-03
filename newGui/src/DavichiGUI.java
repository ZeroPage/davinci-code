import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class DavichiGUI extends JFrame
{
	RoomWindow RW = null;
	LobbyWindow LW = null;
	static DavichiGUI some = null;
	public static void main(String [] args)
	{
		DavichiGUI aa = new DavichiGUI();
		aa.setVisible(true);
		some = aa;
	}
	public DavichiGUI() 
	{
		super("다빈치 코드 - 천사와 악마");
		InitalGUI();
	}
	public void InitalGUI()
	{
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(1000, 600);//크기 설정.
		this.setResizable(false);
		
		//화면 띄우기
		//로비화면 띄우기* -> lobbywindow 클래스 생성
		//게임화면 띄우기 -> Roomwindow 클래스 생성
		RW = new RoomWindow((JPanel)this.getContentPane());
	}
	class RoomWindow extends mWindow
	{
		JPanel JPanel_Room = null;
		ChatWindow CW = null;
		GameWindow GW = null;
		Network NC = null;
		
		public RoomWindow(JPanel main)
		{			
			JPanel_Room = new JPanel();
			JPanel_Room.setLayout(new BorderLayout());
			CW = new ChatWindow(JPanel_Room);
			GW = new GameWindow(JPanel_Room);
			//ConnetDlg CD = new ConnetDlg();
			
			main.add(BorderLayout.CENTER, JPanel_Room);
		}
		class ChatWindow implements ActionListener
		{
			JPanel JPanel_Main;
			JTextArea JTA_Chat;
			JTextField JTF_ChatInput;
			JButton JB_Send;
			JButton JB_NewGame;
			JButton JB_Exit;
			JButton JB_ChatClear;
			Network m_Network;
			
			public ChatWindow(JPanel main)
			{
				JPanel_Main = new JPanel();
				JPanel_Main.setLayout(null);
				JPanel_Main.setPreferredSize(new Dimension(200,500));
				
				//채팅창
				JTA_Chat = new JTextArea();
				JTA_Chat.setEditable(false);
				JTA_Chat.setFocusable(true);
				JTA_Chat.setLineWrap(true);
				JScrollPane JSP_ChatScroll = new JScrollPane(JTA_Chat,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				JSP_ChatScroll.setAutoscrolls(true);
				JSP_ChatScroll.setBounds(0,0, 200, 400);
				JPanel_Main.add(JSP_ChatScroll);
				
				//채팅 입력창
				JTF_ChatInput = new JTextField();
				JTF_ChatInput.setBounds(0, 420, 200, 30);
				JTF_ChatInput.addActionListener(this);
				JPanel_Main.add(JTF_ChatInput);
				
				//보내기 버튼
				JB_Send = new JButton("보내기");
				JB_Send.setBounds(0, 450, 100, 30);
				JB_Send.addActionListener(this);
				JPanel_Main.add(JB_Send);
				
				//클리어 버튼 
				JB_ChatClear = new JButton("클리어");
				JB_ChatClear.setBounds(100, 450, 100, 30);
				JB_ChatClear.addActionListener(this);
				JPanel_Main.add(JB_ChatClear);
				
				//새게임
				JB_NewGame = new JButton("새게임");
				JB_NewGame.setBounds(0, 480, 100, 30);
				JB_NewGame.addActionListener(this);
				JPanel_Main.add(JB_NewGame);
				
				//나가기
				JB_Exit = new JButton("나가기");
				JB_Exit.setBounds(100, 480, 100, 30);
				JB_Exit.addActionListener(this);
				JPanel_Main.add(JB_Exit);
					
				main.add(BorderLayout.EAST, JPanel_Main);
			}
			public void actionPerformed(ActionEvent event)
			{
				if(event.getSource() == JB_Send || event.getSource() == JTF_ChatInput)
				{
					//텍스트 전송
					JTF_ChatInput.setText("");
					JTF_ChatInput.requestFocus();
				}
				if(event.getSource() == JB_ChatClear)
				{
					//클리어
					JTA_Chat.setText("");
					JTF_ChatInput.requestFocus();
				}
				if(event.getSource() == JB_NewGame);
				{
					//새게임 시작
					m_Network = new Server();
					m_Network.setM_Taget(RW);
					m_Network.setM_Name("server");
					m_Network.Connect("1");
				}
				if(event.getSource() == JB_Exit)
				{
					//종료코드
					m_Network = new Client();
					m_Network.setM_Taget(RW);
					m_Network.setM_Name("client");
					m_Network.Connect(JOptionPane.showInputDialog("put ip"));
				}
			}
			public void StringAdd(String msg)
			{
				JTA_Chat.append(msg + "\n");
				JTA_Chat.setCaretPosition(JTA_Chat.getDocument().getLength());
			}
		}
		class GameWindow
		{
			JPanel JPanel_Main;
			JPanel [] JPanel_Player;
			
			ImageIcon [] ImageCardBlack = new ImageIcon[13];
			ImageIcon [] ImageCardWhite = new ImageIcon[13];
			                     
			public GameWindow(JPanel main)
			{
				JPanel_Main = new JPanel()
				{
					ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("board2.jpg"));
					public void paint(Graphics g)
					{
						g.drawImage(BG.getImage(), 0, 0, BG.getIconWidth(), BG.getIconHeight(), null);
						this.setOpaque(false);
						super.paint(g);
					}
				};
				JPanel_Main.setLayout(new BorderLayout());
				
				JPanel_Player = new JPanel[4];
				for(int i = 0; i < 4; i++)
				{
					JPanel_Player[i] = new JPanel();
					JPanel_Player[i].setOpaque(false);
				}
				JPanel_Main.add(BorderLayout.NORTH, JPanel_Player[0]);
				JPanel_Main.add(BorderLayout.EAST, JPanel_Player[1]);
				JPanel_Main.add(BorderLayout.SOUTH, JPanel_Player[2]);
				JPanel_Main.add(BorderLayout.WEST, JPanel_Player[3]);
				
				JStyleButton temp = new JStyleButton(new ImageIcon(DavichiGUI.class.getResource("1.gif")));
				JPanel_Player[2].add(temp);
				
				main.add(JPanel_Main);
			}
		}
		public void AddChatString(String msg)
		{
			CW.StringAdd(msg);
		}
	}
	class LobbyWindow extends mWindow
	{
		public void AddChatString(String msg)
		{
			// TODO Auto-generated method stub
		}
	}
	class ConnetDlg extends JDialog implements ActionListener, ItemListener
	{
		JTextField JTF_IPAdress;
		JTextField JTF_Nick;
		JButton JB_Connect;
		JButton JB_Cancel;
		JCheckBox JCB_Server;
		
		public ConnetDlg()
		{
			super(some, "접속창", true);
			setSize(350, 200);
			setResizable(false);
			this.getContentPane().setLayout(null);
			
			JTF_Nick = new JTextField();
			JTF_Nick.setBounds(100, 10, 100, 30);
			this.getContentPane().add(JTF_Nick);
			
			JCB_Server = new JCheckBox();
			JCB_Server.setBounds(280, 15, 20, 20);
			JCB_Server.addItemListener(this);
			this.getContentPane().add(JCB_Server);
			
			JTF_IPAdress = new JTextField();
			JTF_IPAdress.setBounds(100, 50, 200, 30);
			JTF_IPAdress.addActionListener(this);
			this.getContentPane().add(JTF_IPAdress);
			
			JB_Connect = new JButton("접속");
			JB_Connect.setBounds(50 , 100, 100, 30);
			JB_Connect.addActionListener(this);
			this.getContentPane().add(JB_Connect);
			
			JB_Cancel = new JButton("취소");
			JB_Cancel.setBounds(200, 100, 100, 30);
			JB_Cancel.addActionListener(this);
			this.getContentPane().add(JB_Cancel);
			
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == JB_Connect || event.getSource() == JTF_IPAdress)
			{
				//접속과정
				JOptionPane.showMessageDialog(null, "접속이 완료되었습니다.","알림", 3);
				this.setVisible(false);
			}
			if(event.getSource() == JB_Cancel)
			{
				//this.setVisible(false);
			}
		}
		public void itemStateChanged(ItemEvent event)
		{
			if(event.getSource() == JCB_Server)
			{
				if(JCB_Server.isSelected())
				{
					JTF_IPAdress.setEnabled(false);
				}
				else
				{
					JTF_IPAdress.setEnabled(true);
				}
			}
		}
	}
}