
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class DabichiGUI extends JFrame
{
	private JPanel jpanel_mainFrame;
	private JButton JB_Send;
	private JButton JB_Claer;
	private JMenuBar JMB_MainMenu;
	private JPanel JPanel_GameFrame;
	private JTextArea JTA_Chat;
	private JTextField JTF_Chatinput;
	private JButton JB_New;
	private EventHandle handler;
	private JMenuItem JMI_File_Exit;
	private JMenuItem JMI_Info_About;
	private JMenuItem JMI_Info_Option;
	private JMenuItem JMI_File_Connet;
	private JButton JB_Exit;
	private Server m_Server;
	private Client m_Client;
	private static DabichiGUI MainGUI;
	private boolean isServer;
	
	public static void main(String[] args)
	{
		MainGUI = new DabichiGUI();
	}
	public DabichiGUI()
	{
		super("다빈치 코드 - 천사와 악마");
		InitalGUI();
		GameProcessor a = new GameProcessor();
	}
	public void InitalGUI()
	{
		try	{
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(820, 600);//크기 설정.
			this.setResizable(false);
			this.setLocation(200, 100);
			
			//메인 프레임 세팅;
			jpanel_mainFrame = new JPanel();
			
			jpanel_mainFrame.setLayout(null);
			//이벤트 처리기 추가.
			handler = new EventHandle();
			
			//아래로는 Gui의 추가 과정
			//메뉴.
			JMB_MainMenu = new JMenuBar();
			setJMenuBar(JMB_MainMenu);
			//파일메뉴
			JMenu JM_File = new JMenu("파일");//파일 메뉴단추;
			
			JMI_File_Connet = new JMenuItem("접속");
			JM_File.add(JMI_File_Connet);//file 메뉴에 부착;
			JMI_File_Connet.addActionListener(handler);
			
			JMI_File_Exit = new JMenuItem("닫기");
			JM_File.add(JMI_File_Exit);
			JMI_File_Exit.addActionListener(handler);
			
			JMB_MainMenu.add(JM_File);
			//정보메뉴
			JMenu JM_Info = new JMenu("정보");
			
			JMI_Info_About = new JMenuItem("이게임은?");
			JM_Info.add(JMI_Info_About);
			JMI_Info_About.addActionListener(handler);
			
			JMI_Info_Option = new JMenuItem("옵션");
			JM_Info.add(JMI_Info_Option);
			JMI_Info_Option.addActionListener(handler);
			
			JMB_MainMenu.add(JM_Info);
			
			//채팅 보내기 버튼
			JB_Send = new JButton("보내기");//생성
			jpanel_mainFrame.add(JB_Send);//추가
			JB_Send.setBounds(600, 350, 100, 30);//위치 설정.
			JB_Send.addActionListener(handler);//이벤트 처리기 추가.
			
			//클리어 버튼
			JB_Claer = new JButton("클리어");
			jpanel_mainFrame.add(JB_Claer);
			JB_Claer.setBounds(700, 350, 100, 30);
			JB_Claer.addActionListener(handler);
			
			//채팅창.
			JTA_Chat = new JTextArea();
			
			//JTA_Chat.setBounds(600,0,200,300);
			JTA_Chat.setEditable(false);
			JTA_Chat.setFocusable(true);
			
			JScrollPane JSP_ChatScroll = new JScrollPane(JTA_Chat,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			JTA_Chat.setLineWrap(true);
			JSP_ChatScroll.setAutoscrolls(true);
			jpanel_mainFrame.add(JSP_ChatScroll);
			JSP_ChatScroll.setBounds(600,0, 200, 300);
			
			
			//채팅 입력창
			JTF_Chatinput = new JTextField();
			jpanel_mainFrame.add(JTF_Chatinput);
			JTF_Chatinput.setBounds(600, 320, 200, 30);
			JTF_Chatinput.addActionListener(handler);
			//게임 창.
			JPanel_GameFrame = new JPanel();
			JPanel_GameFrame.setLayout(null);
			//JPanel_GameFrame.setBackground(Color.blue);
			
			jpanel_mainFrame.add(JPanel_GameFrame);
			JPanel_GameFrame.setBounds(0, 0, 590, 450);
			
			//게임 시작
			JB_New = new JButton("게임시작");
			jpanel_mainFrame.add(JB_New);
			JB_New.setBounds(600, 380, 100, 30);
			JB_New.addActionListener(handler);
			//나가기(접속종료)
			JB_Exit = new JButton("접속종료");
			jpanel_mainFrame.add(JB_Exit);
			JB_Exit.setBounds(700, 380, 100, 30);
			JB_Exit.addActionListener(handler);
						
			this.getContentPane().add(BorderLayout.CENTER, jpanel_mainFrame);
			
			this.setVisible(true);
			
			JTF_Chatinput.requestFocus();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private class EventHandle implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == JTF_Chatinput || e.getSource() ==JB_Send)
			{
				if(m_Server == null && m_Client == null)
				{
					JOptionPane.showMessageDialog(null, "접속이 되지 않았습니다.", "경고", 2);
				}
				else
				{
					if(JTF_Chatinput.getText().length() != 0)
					{
						//텍스트  전송;
						if(isServer)
						{
							//서버
							m_Server.sendData(JTF_Chatinput.getText());
							JTA_Chat.append(JTF_Chatinput.getText() + '\n');
						}
						else
						{
							//클라이언트
							m_Client.sendData(JTF_Chatinput.getText());
						}
						JTF_Chatinput.setText("");
						JTF_Chatinput.requestFocus();
					}
				}
			}
			if(e.getSource() == JB_Claer)
			{
				JTA_Chat.setText("");
				JTF_Chatinput.requestFocus();
			}
			if(e.getSource() == JMI_File_Exit)
			{
				if(JOptionPane.showConfirmDialog(null, "정말 종료하시겠습니까?","종료확인", 0) == 0)
				{
					System.exit(0);
				}
			}
			if(e.getSource() == JMI_Info_About)
			{
				JOptionPane.showMessageDialog(null, "다빈치 코드 온라인\nProject. D\nfor Java Term Project");
			}
			if(e.getSource() == JMI_Info_Option)
			{
			}
			if(e.getSource() == JMI_File_Connet)
			{
				ConnetDialog CD = new ConnetDialog();
			}
			if(e.getSource() == JB_Exit)
			{
				//접속종료코드
				JOptionPane.showMessageDialog(null, "접속이 종료되었습니다.");
			}
		}
	}
	public void ChatLisener(String chat)
	{
		JTA_Chat.append(chat + "\n");
		JTA_Chat.setCaretPosition(JTA_Chat.getDocument().getLength());//왜 그런지? 신의 한줄..
	}
	private class ConnetDialog extends JDialog
	{
		private JPanel JPanel_Connet;
		private JButton JB_Connet;
		private JTextField JTF_ipAddress;
		private JCheckBox JCB_Server;
		private JTextField JTF_Nick;
		private JButton JB_Cancel;
		private ConnetDialog CDMain;
		
		public ConnetDialog()
		{
			super(MainGUI ,"접속창", true);
			InitalGUI();
			setResizable(false);
			CDMain = this;
			this.setVisible(true);
		} 
		public void InitalGUI()
		{
			try
			{
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.setSize(350, 170);//크기 설정.
				this.setLocation(MainGUI.getLocation().x + MainGUI.getWidth()/2 - 350/2, MainGUI.getLocation().y + MainGUI.getHeight()/2 - 200/2);
				ConnetEventHandler CEH = new ConnetEventHandler();
				
				JPanel_Connet = new JPanel();
				
				JPanel_Connet.setLayout(null);
				
				//접속버튼
				JB_Connet = new JButton("접속");
				JPanel_Connet.add(JB_Connet);
				JB_Connet.setBounds(50 , 100, 100, 30);
				JB_Connet.addActionListener(CEH);
				//취소 버튼.
				JB_Cancel = new JButton("취소");
				JPanel_Connet.add(JB_Cancel);
				JB_Cancel.setBounds(200, 100, 100, 30);
				JB_Cancel.addActionListener(CEH);
				//ip어드레스
				JTF_ipAddress = new JTextField();
				JPanel_Connet.add(JTF_ipAddress);
				JTF_ipAddress.setBounds(100, 50, 200, 30);
				JTF_ipAddress.addActionListener(CEH);
				//서버의 체크 박스
				JCB_Server = new JCheckBox();
				JPanel_Connet.add(JCB_Server);
				JCB_Server.setBounds(280, 15, 20, 20);
				JCB_Server.addItemListener(CEH);
				//닉네임
				JTF_Nick = new JTextField();
				JPanel_Connet.add(JTF_Nick);
				JTF_Nick.setBounds(100, 10, 100, 30);
				JTF_Nick.addActionListener(CEH);
				//텍스트들
				JLabel JL_Nick = new JLabel("닉네임");
				JPanel_Connet.add(JL_Nick);
				JL_Nick.setBounds(0, 10, 100, 30);
				JL_Nick.setHorizontalAlignment(JLabel.CENTER);
				
				JLabel JL_IP = new JLabel("ip");
				JPanel_Connet.add(JL_IP);
				JL_IP.setBounds(0, 50, 100, 30);
				JL_IP.setHorizontalAlignment(JLabel.CENTER);
				
				JLabel JL_Server = new JLabel("서버");
				JPanel_Connet.add(JL_Server);
				JL_Server.setBounds(200, 10, 80, 30);
				JL_Server.setHorizontalAlignment(JLabel.CENTER);
				
				this.getContentPane().add(BorderLayout.CENTER, JPanel_Connet);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		private class ConnetEventHandler implements ActionListener, ItemListener, WindowListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource() == JB_Connet || e.getSource() == JTF_ipAddress || e.getSource() == JTF_Nick)
				{
					if(JCB_Server.isSelected())
					{
						//서버
						try
						{
							m_Server = new Server(10000);
							isServer = true;
							
							m_Server.setGUI(MainGUI);
							m_Server.start();
							CDMain.setVisible(false);
							JOptionPane.showMessageDialog(null, "접속완료");
						} catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else
					{					
						if(cheekIPAdress(JTF_ipAddress.getText()))
						{
							//접속 시도.
							try
							{
								m_Client.connectServer(new Socket(JTF_ipAddress.getText(), 10000));
								isServer = false;
								m_Client.getLisener().setGUI(MainGUI);
								CDMain.setVisible(false);
								JOptionPane.showMessageDialog(null, "접속완료");
							} catch (UnknownHostException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1)
							{
								//if(e1.getCause() == )
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "아이피가 형식에 맞지않습니다.","경고", 2);
						}
					}
				}
				if(e.getSource() == JB_Cancel)
				{
					CDMain.setVisible(false);
				}
			}
			private boolean cheekIPAdress(String ipAdress)
			{
				//아이피 체크 코드
				return true;
			}
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getSource() == JCB_Server)
				{
					if(e.getStateChange() == ItemEvent.SELECTED)
						JTF_ipAddress.setEnabled(false);
					else
						JTF_ipAddress.setEnabled(true);
					repaint();
				}
			}
			@Override
			public void windowActivated(WindowEvent e)
			{
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowClosed(WindowEvent e)
			{
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowClosing(WindowEvent e)
			{
				// TODO Auto-generated method stub
				//종료코드
			}
			@Override
			public void windowDeactivated(WindowEvent e)
			{
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowDeiconified(WindowEvent e)
			{
				// TODO Auto-generated method stub				
			}
			@Override
			public void windowIconified(WindowEvent e)
			{
				// TODO Auto-generated method stub
			}
			@Override
			public void windowOpened(WindowEvent e)
			{
				// TODO Auto-generated method stub
			}
		}
		
	}
	private class GameProcessor
	{
		private JPanel [] PeaLayout = new JPanel[100];
		private JButton [] Pea = new JButton[100];
		private ImageIcon [] Charicter = new ImageIcon[4];
		
		public GameProcessor()
		{
			//이미지 불러오기
			PeaLayout[0] = new JPanel(new CardLayout());
			//PeaLayout[0].add(temp, 0);
			ImageIcon temp  = new ImageIcon(".\\image\\1.jpg");
			Pea[0] = new JButton(temp);
			JPanel_GameFrame.add(Pea[0]);
			Pea[0].setBounds(0,0,temp.getIconWidth(),temp.getIconHeight());
			temp = new ImageIcon(".\\image\\2.jpg");
			Pea[1] = new JButton(temp);
			JPanel_GameFrame.add(Pea[1]);
			Pea[1].setBounds(100, 0, temp.getIconWidth(),temp.getIconHeight());
			
		}
	}
}