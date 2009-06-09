import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
		super("�ٺ�ġ �ڵ� - õ��� �Ǹ�");
		InitalGUI();
	}
	public void InitalGUI()
	{
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(1000, 600);//ũ�� ����.
		this.setResizable(false);
		
		//ȭ�� ����
		//�κ�ȭ�� ����* -> lobbywindow Ŭ���� ����
		//����ȭ�� ���� -> Roomwindow Ŭ���� ����
		LW = new LobbyWindow((JPanel)this.getContentPane());
		//RW = new RoomWindow((JPanel)this.getContentPane());
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
			//ConnetDlg CD = new ConnetDlg(this);
			CW.SetButton(NC.isServer());
			GW = new GameWindow(JPanel_Room, NC);
			
			main.add(BorderLayout.CENTER, JPanel_Room);
		}
		public RoomWindow(JPanel main, Network n)
		{			
			JPanel_Room = new JPanel();
			JPanel_Room.setLayout(new BorderLayout());
			CW = new ChatWindow(JPanel_Room);
			NC = n;
			//ConnetDlg CD = new ConnetDlg(this);
			CW.SetButton(NC.isServer());
			GW = new GameWindow(JPanel_Room, NC);
			
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
				
				//ä��â
				JTA_Chat = new JTextArea();
				JTA_Chat.setEditable(false);
				JTA_Chat.setFocusable(true);
				JTA_Chat.setLineWrap(true);
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
					
				main.add(BorderLayout.EAST, JPanel_Main);
			}
			public void actionPerformed(ActionEvent event)
			{
				if(event.getSource() == JB_Send || event.getSource() == JTF_ChatInput)
				{
					//�ؽ�Ʈ ����
					if(JTF_ChatInput.getText().length() !=0)
					{	
						NC.SendChatMsg(JTF_ChatInput.getText());
						JTF_ChatInput.setText("");
						JTF_ChatInput.requestFocus();
					}
				}
				if(event.getSource() == JB_ChatClear)
				{
					//Ŭ����
					JTA_Chat.setText("");
					JTF_ChatInput.requestFocus();
				}
				if(event.getSource() == JB_NewGame)
				{
					//������ ����
					GW.strat();
				}
				if(event.getSource() == JB_Exit)
				{
					//�����ڵ�
					getWindowListeners()[0].windowClosing(new WindowEvent(getWindows()[0], 0));
				}
			}
			public void StringAdd(String msg)
			{
				JTA_Chat.append(msg + "\n");
				JTA_Chat.setCaretPosition(JTA_Chat.getDocument().getLength());
			}
			public void SetButton(boolean server)
			{
				if(!server)
				{
					JB_NewGame.setText("����");
				}
			}
		}
		public void AddChatString(String msg)
		{
			CW.StringAdd(msg);
		}
	}
	class LobbyWindow extends mWindow
	{
		JTextField JTF_IPAdress;
		JTextField JTF_Nick;
		JButton JB_Connect;
		JButton JB_Cancel;
		JCheckBox JCB_Server;
		JTextField JTF_Port;
		JPanel JPanel_Lobby;
		
		Network TagetNetwork;
		
		ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("cover.gif"));
		
		LobbyWindowLisener Lisener = new LobbyWindowLisener();
		
		public LobbyWindow(JPanel main)
		{
			JPanel_Lobby = new JPanel()
			{
				public void paint(Graphics g)
				{
					g.drawImage(BG.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
					this.setOpaque(false);
					this.setPreferredSize(new Dimension(BG.getIconWidth(), BG.getIconHeight()));
					super.paint(g);
				}
			};
			JPanel_Lobby.setLayout(null);
			
			JPanel JPanel_Connect = new JPanel();
			JPanel_Connect.setOpaque(false);
			JPanel_Connect.setPreferredSize(new Dimension(380,300));
			
			JPanel_Connect.setLayout(null);
			JPanel_Connect.setBounds(600, 300, 380, 300);
			JPanel_Connect.setOpaque(false);
			
			JLabel temp = new JLabel("�г���");
			temp.setForeground(Color.red);
			temp.setBounds(0, 10, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JTF_Nick = new JTextField();
			JTF_Nick.setBounds(100, 10, 100, 30);
			JPanel_Connect.add(JTF_Nick);
			
			temp = new JLabel("����");
			temp.setForeground(Color.red);
			temp.setBounds(200, 10, 80, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JCB_Server = new JCheckBox();
			JCB_Server.setBounds(280, 19, 13, 13);
			JCB_Server.addItemListener(Lisener);
			JCB_Server.setMargin(new Insets(-2,-2,-2,-2));
			JPanel_Connect.add(JCB_Server);
			
			temp = new JLabel("IP");
			temp.setForeground(Color.red);
			temp.setBounds(0, 50, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JTF_IPAdress = new JTextField();
			JTF_IPAdress.setBounds(100, 50, 200, 30);
			JTF_IPAdress.addActionListener(Lisener);
			JPanel_Connect.add(JTF_IPAdress);
			
			temp = new JLabel("Port");
			temp.setForeground(Color.red);
			temp.setBounds(0, 90, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JTF_Port = new JTextField();
			JTF_Port.setBounds(100, 90, 50, 30);
			JTF_Port.setText("10000");
			JPanel_Connect.add(JTF_Port);
			
			JB_Connect = new JButton("����");
			JB_Connect.setBounds(50 , 130, 100, 30);
			JB_Connect.addActionListener(Lisener);
			JPanel_Connect.add(JB_Connect);
			
			
			JB_Cancel = new JButton("���");
			JB_Cancel.setBounds(200, 130, 100, 30);
			JB_Cancel.addActionListener(Lisener);
			JPanel_Connect.add(JB_Cancel);
			
			JPanel_Lobby.add(JPanel_Connect);
			JTF_Nick.requestFocus();
			
			JPanel_Lobby.add(JPanel_Connect);
			main.add(JPanel_Lobby);
		}
		class LobbyWindowLisener implements ActionListener, ItemListener
		{
			public void actionPerformed(ActionEvent event)
			{
				if(event.getSource() == JB_Connect || event.getSource() == JTF_IPAdress)
				{
					if(JTF_Nick.getText().length() == 0)
					{
						JOptionPane.showMessageDialog(null, "�г����� �Է��ϼ���.", "�˸�", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(JCB_Server.isSelected())
					{
						//����
						TagetNetwork = new Server();
					}
					else
					{
						//Ŭ��
						TagetNetwork = new Client();
					}
					TagetNetwork.setM_Name(JTF_Nick.getText());
					TagetNetwork.setPortNum(Integer.parseInt(JTF_Port.getText()));
					TagetNetwork.Connect(JTF_IPAdress.getText());
					//�������� �����ϰ� ��Ʈ��ũ�� ����.
					RW = new RoomWindow((JPanel) getRootPane().getContentPane(), TagetNetwork);
					RW.setNetwork(TagetNetwork);
					TagetNetwork.m_Taget = RW;
					getWindows()[0].setSize(1000,600);
					LW.JPanel_Lobby.setVisible(false);
					RW.JPanel_Room.setVisible(true);
					if(TagetNetwork.isServer())
					{
						TagetNetwork.SendChatMsg("������ �����Ͽ����ϴ�,");
					}
					else
					{
						TagetNetwork.SendChatMsg("�����Ͽ����ϴ�.");
					}
				}
				if(event.getSource() == JB_Cancel)
				{
					//this.setVisible(false);
					//JOptionPane.showMessageDialog(null, "������ �ϼž߸� �մϴ�.");
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
		public void AddChatString(String msg)
		{
			// TODO Auto-generated method stub
		}
	}
	class MainWindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			if(JOptionPane.showConfirmDialog(null, "���������Ͻðڽ��ϱ�?","����Ȯ��",JOptionPane.YES_NO_OPTION) == 0)
			{
				e.getWindow().setVisible(false); // Frame�� ȭ�鿡�� ������ �ʵ��� �Ѵ�.
			    e.getWindow().dispose(); // �޸𸮿��� �����Ѵ�.
			    System.exit(0); // ���α׷��� �����Ѵ�.
			}
			else
			{
				//���� ���� �ڵ�
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
		
		ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("cover.jpg"));
		
		public ConnetDlg(mWindow chat)
		{
			super((JFrame)null, "����â", true);
			this.setContentPane(new JPanel()
			{
				public void paint(Graphics g)
				{
					g.drawImage(BG.getImage(), 0, 0, BG.getIconWidth(), BG.getIconHeight(), null);
					this.setOpaque(false);
					this.setPreferredSize(new Dimension(BG.getIconWidth(), BG.getIconHeight()));
					//this.setSize(BG.getIconWidth(), BG.getIconHeight());
					super.paint(g);
				}
			});
			TagetChat = chat; 
			//TagetNetwork = Taget;
			
			this.setSize(new Dimension(BG.getIconWidth()+5, BG.getIconHeight()+20));
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
			
			JPanel JPanel_Connect = new JPanel();
			JPanel_Connect.setLayout(null);
			JPanel_Connect.setBounds(30, 350, 380, 300);
			JPanel_Connect.setOpaque(false);
			
			
			JLabel temp = new JLabel("�г���");
			temp.setForeground(Color.red);
			temp.setBounds(0, 10, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JTF_Nick = new JTextField();
			JTF_Nick.setBounds(100, 10, 100, 30);
			JPanel_Connect.add(JTF_Nick);
			
			temp = new JLabel("����");
			temp.setForeground(Color.red);
			temp.setBounds(200, 10, 80, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JCB_Server = new JCheckBox();
			JCB_Server.setBounds(280, 19, 13, 13);
			JCB_Server.addItemListener(this);
			JCB_Server.setMargin(new Insets(-2,-2,-2,-2));
			JPanel_Connect.add(JCB_Server);
			
			temp = new JLabel("IP");
			temp.setForeground(Color.red);
			temp.setBounds(0, 50, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JTF_IPAdress = new JTextField();
			JTF_IPAdress.setBounds(100, 50, 200, 30);
			JTF_IPAdress.addActionListener(this);
			JPanel_Connect.add(JTF_IPAdress);
			
			temp = new JLabel("Port");
			temp.setForeground(Color.red);
			temp.setBounds(0, 90, 100, 30);
			temp.setHorizontalAlignment(JLabel.CENTER);
			JPanel_Connect.add(temp);
			
			JTF_Port = new JTextField();
			JTF_Port.setBounds(100, 90, 50, 30);
			JTF_Port.setText("10000");
			JPanel_Connect.add(JTF_Port);
			
			JB_Connect = new JButton("����");
			JB_Connect.setBounds(50 , 130, 100, 30);
			JB_Connect.addActionListener(this);
			JPanel_Connect.add(JB_Connect);
			
			
			JB_Cancel = new JButton("���");
			JB_Cancel.setBounds(200, 130, 100, 30);
			JB_Cancel.addActionListener(this);
			JPanel_Connect.add(JB_Cancel);
			
			this.getContentPane().add(JPanel_Connect);
			JTF_Nick.requestFocus();
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == JB_Connect || event.getSource() == JTF_IPAdress)
			{
				if(JTF_Nick.getText().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "�г����� �Է��ϼ���.", "�˸�", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(JCB_Server.isSelected())
				{
					//����
					TagetNetwork = new Server();
				}
				else
				{
					//Ŭ��
					TagetNetwork = new Client();
				}
				TagetNetwork.setM_Name(JTF_Nick.getText());
				TagetNetwork.setM_Taget(TagetChat);
				TagetNetwork.setPortNum(Integer.parseInt(JTF_Port.getText()));
				TagetNetwork.Connect(JTF_IPAdress.getText());
				TagetChat.setNetwork(TagetNetwork);
				if(TagetNetwork.isServer())
				{
					TagetNetwork.SendChatMsg("������ �����Ͽ����ϴ�,");
				}
				else
				{
					TagetNetwork.SendChatMsg("�����Ͽ����ϴ�.");
				}
				
				this.setVisible(false);
			}
			if(event.getSource() == JB_Cancel)
			{
				//this.setVisible(false);
				//JOptionPane.showMessageDialog(null, "������ �ϼž߸� �մϴ�.");
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
			super(some, "���ڸ� �����ϼ���",true);
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
