import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GameWindow
{
	JPanel JPanel_Main;
	PlayerWindow [] PlayerPane = new PlayerWindow[4];
	PlayerWindow Center;
	
	ImageIcon [] ImageCardBlack = new ImageIcon[13];
	ImageIcon [] ImageCardWhite = new ImageIcon[13];
	ImageIcon [] ImageCardBlackOpen = new ImageIcon[13];
	ImageIcon [] ImageCardWhiteOpen = new ImageIcon[13];
	ImageIcon ImageCardBlackUnknown;
	ImageIcon ImageCardWhiteUnknown;
	ImageIcon ImageCardBlackUnknownRollerover;
	ImageIcon ImageCardWhiteUnknownRollerover;
	
	GameProcess Process;
	
	int [] PlayerNumToWindowNum; 
	
	public GameWindow(JPanel main, Network n)
	{
		JPanel_Main = new JPanel()
		{
			ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("board.jpg"));
			public void paint(Graphics g)
			{
				g.drawImage(BG.getImage(), 0, 0, BG.getIconWidth(), BG.getIconHeight(), null);
				this.setOpaque(false);
				super.paint(g);
			}
		};
		JPanel_Main.setLayout(new BorderLayout());

		//이미지 로딩
		for(int i = 0; i < 13; i++)
		{
			ImageCardBlack[i] = new ImageIcon(DavichiGUI.class.getResource("card/b"+i+".gif"));
			ImageCardBlackOpen[i] = new ImageIcon(DavichiGUI.class.getResource("card/b"+i+"r.gif"));
			ImageCardWhite[i] = new ImageIcon(DavichiGUI.class.getResource("card/w"+i+".gif"));
			ImageCardWhiteOpen[i] = new ImageIcon(DavichiGUI.class.getResource("card/w"+i+"r.gif"));
		}
		ImageCardBlackUnknown = new ImageIcon(DavichiGUI.class.getResource("card/bu.gif"));
		ImageCardWhiteUnknown = new ImageIcon(DavichiGUI.class.getResource("card/wu.gif"));
		ImageCardBlackUnknownRollerover = new ImageIcon(DavichiGUI.class.getResource("card/bur.gif"));
		ImageCardWhiteUnknownRollerover = new ImageIcon(DavichiGUI.class.getResource("card/wur.gif"));
		
		//플레이어 와 NPC패널의 설정
		for(int i = 0; i < 4; i++)
		{
			PlayerPane[i] = new PlayerWindow(i);
		}
		Center = new NPC();
		
		main.add(JPanel_Main);
		
		Process = new GameProcess(this, n);
		n.setM_Game(Process);
	}
	public void setEnable(int playerNum, boolean state)
	{
		//플레이어 넘버와 화면번호의 매칭
		Block [] Card = Process.GetBlocksState(playerNum);
		PlayerPane[PlayerNumToWindowNum[playerNum]].setEnable(Card, state);//0이 아래 부터 시계방향 순서대로
	}
	public void setCenterEnable(boolean state)
	{
		Block [] Card = Process.GetCenterBlocksState();
		Center.setEnable(Card, state);
	}
	public void update()
	{
		Block [] State;
		for(int i = 0; i < Process.getPlayerNum(); i++)//싸이즈 얻어오는거 게임 프로세스에 추가할것
		{
			State = Process.GetBlocksState(i);
			PlayerPane[PlayerNumToWindowNum[i]].update(State);
		}
		State = Process.GetCenterBlocksState();
		Center.update(State);
	}
	public void update(int PlayerNum)
	{
		//플레이어 넘버를 받고 그 번호의 플레이어를 즉시 업데이트 한다
		//센터는 updateCenter를 쓴다.
		Block [] state = Process.GetBlocksState(PlayerNum);
		PlayerPane[PlayerNumToWindowNum[PlayerNum]].update(state);
	}
	public void CenterUpdate()
	{
		Block[] State = Process.GetCenterBlocksState();
		Center.update(State);
	}
	public void strat()
	{
		
		//채팅창에 있는 게임 시작 버튼의 동작을  받기위한 것.
		if(Process.m_NetTaget.isServer())
		{
			Process.Start();
			Setting(Process.getPlayerNum());
			Process.turn();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "방장이 아닙니다.","알림", 2);
		}
	}
	public void Setting(int PlayerNum)
	{
		PlayerNumToWindowNum = new int[PlayerNum];
		switch(PlayerNum)
		{
			case 2:
				if(Process.playOrder == 0)
				{
					PlayerNumToWindowNum[0] = 0;
					PlayerNumToWindowNum[1] = 2;
					PlayerPane[0].m_PlayerNum = 0;
					PlayerPane[2].m_PlayerNum = 1;
					
				}
				else
				{
					PlayerNumToWindowNum[0] = 2;
					PlayerNumToWindowNum[1] = 0;
					PlayerPane[2].m_PlayerNum = 0;
					PlayerPane[0].m_PlayerNum = 1;
				}
				break;
			case 3:
				if(Process.playOrder == 0)
				{
					PlayerNumToWindowNum[0] = 0;
					PlayerNumToWindowNum[1] = 1;
					PlayerNumToWindowNum[2] = 3;
					PlayerPane[0].m_PlayerNum = 0;
					PlayerPane[1].m_PlayerNum = 1;
					PlayerPane[3].m_PlayerNum = 2;
				}
				else if(Process.playOrder == 1)
				{
					PlayerNumToWindowNum[0] = 3;
					PlayerNumToWindowNum[1] = 0;
					PlayerNumToWindowNum[2] = 1;
					PlayerPane[3].m_PlayerNum = 0;
					PlayerPane[0].m_PlayerNum = 1;
					PlayerPane[1].m_PlayerNum = 2;
				}
				else if(Process.playOrder == 2)
				{
					PlayerNumToWindowNum[0] = 1;
					PlayerNumToWindowNum[1] = 3;
					PlayerNumToWindowNum[2] = 0;
					PlayerPane[1].m_PlayerNum = 0;
					PlayerPane[3].m_PlayerNum = 1;
					PlayerPane[0].m_PlayerNum = 2;
				}
				break;
			case 4:
				if(Process.playOrder == 0)
				{
					PlayerNumToWindowNum[0] = 0;
					PlayerNumToWindowNum[1] = 1;
					PlayerNumToWindowNum[2] = 2;
					PlayerNumToWindowNum[3] = 3;
					PlayerPane[0].m_PlayerNum = 0;
					PlayerPane[1].m_PlayerNum = 1;
					PlayerPane[2].m_PlayerNum = 2;
					PlayerPane[3].m_PlayerNum = 3;
				}
				else if(Process.playOrder == 1)
				{
					PlayerNumToWindowNum[0] = 3;
					PlayerNumToWindowNum[1] = 0;
					PlayerNumToWindowNum[2] = 1;
					PlayerNumToWindowNum[3] = 2;
					PlayerPane[3].m_PlayerNum = 0;
					PlayerPane[0].m_PlayerNum = 1;
					PlayerPane[1].m_PlayerNum = 2;
					PlayerPane[2].m_PlayerNum = 3;
				}
				else if(Process.playOrder == 2)
				{
					PlayerNumToWindowNum[0] = 2;
					PlayerNumToWindowNum[1] = 3;
					PlayerNumToWindowNum[2] = 0;
					PlayerNumToWindowNum[3] = 1;
					PlayerPane[2].m_PlayerNum = 0;
					PlayerPane[3].m_PlayerNum = 1;
					PlayerPane[0].m_PlayerNum = 2;
					PlayerPane[1].m_PlayerNum = 3;
				}
				else if(Process.playOrder == 3)
				{
					PlayerNumToWindowNum[0] = 1;
					PlayerNumToWindowNum[1] = 2;
					PlayerNumToWindowNum[2] = 3;
					PlayerNumToWindowNum[3] = 0;
					PlayerPane[1].m_PlayerNum = 0;
					PlayerPane[2].m_PlayerNum = 1;
					PlayerPane[3].m_PlayerNum = 2;
					PlayerPane[0].m_PlayerNum = 3;
				}
				break;
		}
	}
	public void RemoveAll()
	{
		for(int i =0; i < 4; i++)
		{
			PlayerPane[i].remove();
		}
		Center.remove();
	}
	public int JokerInput(int [] index)
	{
		JokerWindow JI = new JokerWindow(index);
		return JI.m_index;
	}
	class JokerWindow implements ActionListener
	{
		JPanel JPanel_Joker;
		int m_index = 0;
		JButton [] JB_Cheek;
		         
		public JokerWindow(int [] index) 
		{
			JPanel_Joker = new JPanel(new FlowLayout());
			JPanel_Joker.setOpaque(false);
			JPanel_Joker.setPreferredSize(new Dimension(800,150));
			JPanel_Joker.setLocation(0, 400);
			
			ImageIcon ucheek = new ImageIcon(DavichiGUI.class.getResource("ucheek.gif"));
			ImageIcon cheek = new ImageIcon(DavichiGUI.class.getResource("cheek.gif"));
			
			int length = 0;
			for(length =0; PlayerPane[0].m_Card[length] != null; length++);
			JB_Cheek = new JStyleButton[length];
			for(int i = 0; i < length; i++)
			{
				JB_Cheek[i] = new JStyleButton(ucheek);
				JB_Cheek[i].addActionListener(this);
				JB_Cheek[i].setEnabled(true);
			}
			for(int i = 0; i < index.length; i++)
			{
				JB_Cheek[index[i]].setIcon(cheek);
			}
			JPanel_Joker.setVisible(true);
		}
		public void actionPerformed(ActionEvent event)
		{
			for(int i = 0; i < JB_Cheek.length; i++)
			{
				if(event.getSource() == JB_Cheek[i])
				{
					m_index = i;
					JPanel_Joker.setVisible(false);
					break;
				}
			}
		}
		
	}
	class PlayerWindow implements ActionListener
	{
		JButton [] m_Card;
		JPanel m_Panel;
		int m_WindowNum;
		int m_PlayerNum;
		final int Size = 100;
		
		protected PlayerWindow()
		{
			//하위클래스에서 필요한것 바깥에서는 위험함으로 쓰지말것.
		}
		public PlayerWindow(int PlayerNum)
		{
			m_Panel = new JPanel();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER,-1,-1);
			m_PlayerNum = PlayerNum;
			m_Card = new JButton[13];
			String lo = "";
			switch(PlayerNum)
			{
				case 0:
					lo = BorderLayout.SOUTH;
					m_Panel.setPreferredSize(new Dimension(0,Size));
					break;
				case 1:
					lo = BorderLayout.WEST;
					m_Panel.setPreferredSize(new Dimension(Size,0));
					layout.setAlignment(FlowLayout.LEADING);
					break;
				case 2:
					lo = BorderLayout.NORTH;
					m_Panel.setPreferredSize(new Dimension(0,Size));
					break;
				case 3:
					lo = BorderLayout.EAST;
					m_Panel.setPreferredSize(new Dimension(Size,0));
					layout.setAlignment(FlowLayout.TRAILING);
					break;
				default:
					break;
			}
			m_Panel.setLayout(layout);
			JPanel_Main.add(lo,m_Panel);
			m_Panel.setOpaque(false);
		}
		
		public void setEnable(Block [] Card, boolean state)
		{
			//NPC에 같은 함수 가팅 변경해야 함
			for(int i = 0; m_Card[i] != null; i++)
			{
				//배열정보 얻어와서 열려진것은 선택 안되도록 해야함.
				m_Card[i].setEnabled(state);
				m_Card[i].setRolloverEnabled(state);
				
				if(Card[i].getOpen())
				{
					m_Card[i].setEnabled(false);
					m_Card[i].setRolloverEnabled(false);
				}
			}
		}
		public void remove()
		{
			for(int i =0; i < m_Card.length; i++)
			{
				m_Panel.remove(m_Card[i]);
			}
		}
		public void update(Block [] State)
		{
			//블럭을 업데이트 한다.
			for(int i = 0; i < State.length; i++)
			{
				if(m_Card[i] == null)
				{
					m_Card[i] = new JStyleButton();
					m_Card[i].addActionListener(this);
					m_Panel.add(m_Card[i]);
					//SetButtonLocation(m_Card[i], i);
					m_Card[i].setMargin(new Insets(0,0,0,0));//마법의 한구문.. 플로우레이아웃이 먹는다.
				}
				int num = State[i].getNum();
				if(num < 0)
				{
					num = 12;
				}
				if(State[i].getColor() == 0)
				{
					if(State[i].getOpen() || State[i].getOwn())
					{
						if(State[i].getOpen())
						{
							m_Card[i].setIcon(ImageCardBlackOpen[num]);
						}
						else
						{
							m_Card[i].setIcon(ImageCardBlack[num]);
						}
					}
					else
					{
						//오픈 안되있는 경우 뒷면을 보여준다.
						m_Card[i].setIcon(ImageCardBlackUnknown);
						m_Card[i].setRolloverIcon(ImageCardBlackUnknownRollerover);
					}
				}
				else
				{
					if(State[i].getOpen() || State[i].getOwn())
					{
						if(State[i].getOpen())
						{
							m_Card[i].setIcon(ImageCardWhiteOpen[num]);
						}
						else
						{
							m_Card[i].setIcon(ImageCardWhite[num]);
						}
					}
					else
					{
						//오픈 안되있는 경우 뒷면을 보여준다.
						m_Card[i].setIcon(ImageCardWhiteUnknown);
						m_Card[i].setRolloverIcon(ImageCardWhiteUnknownRollerover);
					}
				}
				m_Card[i].setRolloverEnabled(false);
			}	
		}
		private void SetButtonLocation(JButton button, int i)
		{
			int row = i / 13;
			int cal = i % 13;
			button.setLocation(cal * 50, row *72);
		}
		public void actionPerformed(ActionEvent e)
		{
			//플레이어의 패가 선택된것이다. 
			for(int i =0; m_Card[i] != null; i++)
			{
				if(e.getSource() == m_Card[i])
				{
					//선택되면 상대방의 패를 물어보는 것이기 때문에 askblock을 호출한다.
					
					Process.AskBlock(m_PlayerNum, i,askNum());
					break;
				}
			}
		}
	}
	class NPC extends PlayerWindow
	{
		public NPC()
		{
			super();
			m_Panel = new JPanel();
			m_WindowNum = 5;
			m_Card = new JButton[27];
			JPanel_Main.add(BorderLayout.CENTER, m_Panel);
			m_Panel.setOpaque(false);
		}
		public void update(Block [] State)
		{
			super.update(State);
			for(int i = State.length; m_Card[i] != null; i++)
			{
				//m_Card[i].removeAll();
				m_Panel.remove(m_Card[i]);
				m_Panel.repaint();
			}
		}
		@Override
		public void setEnable(Block[] Card, boolean state)
		{
			//위의 플레이어 윈도우와 같이 변경해야함. 
			for(int i = 0; m_Card[i] != null; i++)
			{
				//배열정보 얻어와서 열려진것은 선택 안되도록 해야함.
				m_Card[i].setEnabled(state);
				m_Card[i].setRolloverEnabled(state);
			}
		}
		public void actionPerformed(ActionEvent e)
		{
			//가운데는 선택되면 이것은 처음에 가운데 블럭을 선택하기 위한것이다.
			//moveblock을 호출해서 가운데것을 가져가게 하면 된다.
			//그리고 선택된것은 빼어버린다.(업데이트가 나으려나.)
			for(int i = 0; i < 26; i++)
			{
				if(m_Card[i] != null)
				{
					if(e.getSource() == m_Card[i])
					{
						Process.moveBlock(i);
					}
				}
			}
		}
	}
	private int askNum()
	{
		AskDlg AD = new AskDlg();
		return AD.getNum();
	}
	class AskDlg extends JDialog implements ActionListener
	{
		JButton [] JB_Num = new JButton[13];
		int Num;
		public AskDlg()
		{
			super((JFrame) getWindows()[0], "숫자를 선택하세요",true);
			this.setSize(300,400);
			this.setLayout(new GridLayout(5,3));
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
					setVisible(false);
					break;
				}
			}
			
		}
		public int getNum()
		{
			if(Num == 12)
			{
				Num = -1;
			}
			return Num;
		}
	}
}