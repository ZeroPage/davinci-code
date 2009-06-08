import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import sun.awt.WindowClosingListener;

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
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(1000, 600);//크기 설정.
		this.setResizable(false);
		
		//화면 띄우기
		//로비화면 띄우기* -> lobbywindow 클래스 생성
		//게임화면 띄우기 -> Roomwindow 클래스 생성
		RW = new RoomWindow((JPanel)this.getContentPane());
		this.addWindowListener(new MainWindowListener());
	}
	class RoomWindow extends mWindow
	{
		JPanel JPanel_Room = null;
		ChatWindow CW = null;
		GameWindow GW = null;
		
		public RoomWindow(JPanel main)
		{			
			JPanel_Room = new JPanel();
			JPanel_Room.setLayout(new BorderLayout());
			CW = new ChatWindow(JPanel_Room);
			GW = new GameWindow(JPanel_Room, NC);
			ConnetDlg CD = new ConnetDlg(this);
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
				JB_NewGame = new JButton("시작");
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
					if(JTF_ChatInput.getText().length() !=0)
					{	
						NC.SendChatMsg(JTF_ChatInput.getText());
						JTF_ChatInput.setText("");
						JTF_ChatInput.requestFocus();
					}
				}
				if(event.getSource() == JB_ChatClear)
				{
					//클리어
					JTA_Chat.setText("");
					JTF_ChatInput.requestFocus();
				}
				if(event.getSource() == JB_NewGame)
				{
					//새게임 시작
				}
				if(event.getSource() == JB_Exit)
				{
					//종료코드
				}
			}
			public void StringAdd(String msg)
			{
				JTA_Chat.append(msg + "\n");
				JTA_Chat.setCaretPosition(JTA_Chat.getDocument().getLength());
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
	class MainWindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			if(JOptionPane.showConfirmDialog(null, "정말종료하시겠습니까?","종료확인",JOptionPane.YES_NO_OPTION) == 0)
			{
				e.getWindow().setVisible(false); // Frame을 화면에서 보이지 않도록 한다.
			    e.getWindow().dispose(); // 메모리에서 제거한다.
			    System.exit(0); // 프로그램을 종료한다.
			}
			else
			{
				//종료 막는 코드
				return;
			}
		}
	}
	class ConnetDlg extends JDialog implements ActionListener, ItemListener
	{
		JTextField JTF_IPAdress;
		JTextField JTF_Nick;
		JButton JB_Connect;
		JButton JB_Cancel;
		JCheckBox JCB_Server;
		JTextField JTF_Port;
		mWindow TagetChat;
		Network TagetNetwork;
		
		public ConnetDlg(mWindow chat)
		{
			super(some, "접속창", true);
			TagetChat = chat; 
			//TagetNetwork = Taget;
			setSize(350, 200);
			setLocation(getRootPane().getSize().width/2, getRootPane().getSize().height/2);
			setResizable(false);
			this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
				super.windowClosing(e);
				System.exit(0);
				}	
			});
			this.getContentPane().setLayout(null);
			
			JLabel temp = new JLabel("닉네임");
			temp.setBounds(0, 10, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			this.getContentPane().add(temp);
			
			JTF_Nick = new JTextField();
			JTF_Nick.setBounds(100, 10, 100, 30);
			this.getContentPane().add(JTF_Nick);
			
			temp = new JLabel("서버");
			temp.setBounds(200, 10, 80, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			this.getContentPane().add(temp);
			
			JCB_Server = new JCheckBox();
			JCB_Server.setBounds(280, 15, 20, 20);
			JCB_Server.addItemListener(this);
			this.getContentPane().add(JCB_Server);
			
			temp = new JLabel("IP");
			temp.setBounds(0, 50, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			this.getContentPane().add(temp);
			
			JTF_IPAdress = new JTextField();
			JTF_IPAdress.setBounds(100, 50, 200, 30);
			JTF_IPAdress.addActionListener(this);
			this.getContentPane().add(JTF_IPAdress);
			
			temp = new JLabel("Port");
			temp.setBounds(0, 90, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			this.getContentPane().add(temp);
			
			JTF_Port = new JTextField();
			JTF_Port.setBounds(100, 90, 50, 30);
			JTF_Port.setText("10000");
			this.getContentPane().add(JTF_Port);
			
			JB_Connect = new JButton("접속");
			JB_Connect.setBounds(50 , 130, 100, 30);
			JB_Connect.addActionListener(this);
			this.getContentPane().add(JB_Connect);
			
			JB_Cancel = new JButton("취소");
			JB_Cancel.setBounds(200, 130, 100, 30);
			JB_Cancel.addActionListener(this);
			this.getContentPane().add(JB_Cancel);
			
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent event)
		{
			
			if(event.getSource() == JB_Connect || event.getSource() == JTF_IPAdress)
			{
				if(JTF_Nick.getText().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "알림", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(JCB_Server.isSelected())
				{
					//서버
					TagetNetwork = new Server();
					TagetNetwork.setM_Name(JTF_Nick.getText());
					TagetNetwork.setM_Taget(TagetChat);
					TagetNetwork.Connect(JTF_IPAdress.getText());
					TagetNetwork.setPortNum(Integer.parseInt(JTF_Port.getText()));
					TagetChat.setNetwork(TagetNetwork);
					TagetChat.AddChatString("서버가 개설되었습니다.");
				}
				else
				{
					//클라
					TagetNetwork = new Client();
					TagetNetwork.setM_Name(JTF_Nick.getText());
					TagetNetwork.setM_Taget(TagetChat);
					TagetNetwork.Connect(JTF_IPAdress.getText());
					TagetChat.setNetwork(TagetNetwork);
					TagetNetwork.SendChatMsg("접속하였습니다.");
				}
				this.setVisible(false);
			}
			if(event.getSource() == JB_Cancel)
			{
				//this.setVisible(false);
				//JOptionPane.showMessageDialog(null, "접속을 하셔야만 합니다.");
				System.exit(0);
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
	class AskDlg extends JDialog implements ActionListener
	{
		JButton [] JB_Num = new JButton[13];
		int Num;
		public AskDlg()
		{
			super(some, "숫자를 선택하세요",true);
			this.setSize(300,400);
			this.setLayout(new BorderLayout(5,3));
			this.setLocation(getRootPane().getSize().width/2, getRootPane().getSize().height/2);
			this.setResizable(false);
			this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
				}	
			});
			
			for(int i = 0; i < 13; i++)
			{
				JB_Num[i] = new JButton(""+ i);
				JB_Num[i].addActionListener(this);
				this.getContentPane().add(JB_Num[i]);
			}
			
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent e)
		{
			for(int i = 0; i <13; i++)
			{
				if(e.getSource() == JB_Num[i])
				{
					Num = i;
					break;
				}
			}
		}
		public int getNum()
		{
			return Num;
		}
	}
}
