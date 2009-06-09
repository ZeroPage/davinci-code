import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GameWindow
{
	JPanel JPanel_Main;
	PlayerWindow [] PlayerPane = new PlayerWindow[4];
	PlayerWindow Center;
	
	ImageIcon [] ImageCardBlack = new ImageIcon[13];
	ImageIcon [] ImageCardWhite = new ImageIcon[13];
	ImageIcon [] ImageCardBlackRollover = new ImageIcon[13];
	ImageIcon [] ImageCardWhiteRollover = new ImageIcon[13];
	ImageIcon ImageCardBlackUnknown;
	ImageIcon ImageCardWhiteUnknown;
	
	GameProcess Process;
	
	int [] PlayerNumToWindowNum; 
	
	public GameWindow(JPanel main, Network n)
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

		//이미지 로딩
		for(int i = 0; i < 13; i++)
		{
			ImageCardBlack[i] = new ImageIcon(DavichiGUI.class.getResource("card/b"+i+".gif"));
			ImageCardBlackRollover[i] = new ImageIcon(DavichiGUI.class.getResource("card/b"+i+"r.gif"));
			ImageCardWhite[i] = new ImageIcon(DavichiGUI.class.getResource("card/w"+i+".gif"));
			ImageCardWhiteRollover[i] = new ImageIcon(DavichiGUI.class.getResource("card/w"+i+"r.gif"));
		}
		ImageCardBlackUnknown = new ImageIcon(DavichiGUI.class.getResource("card/bu.gif"));
		ImageCardWhiteUnknown = new ImageIcon(DavichiGUI.class.getResource("card/wu.gif"));
		
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
		PlayerPane[PlayerNumToWindowNum[playerNum]].setEnable(state);//0이 아래 부터 시계방향 순서대로
	}
	public void setCenterEnable(boolean state)
	{
		Center.setEnable(state);
	}
	public void update()
	{
		Block [] State;
		for(int i = 0; i < Process.getPlayerNum(); i++)//싸이즈 얻어오는거 게임 프로세스에 추가할것
		{
			State = Process.GetBlocksState(i);
			//PlayerPane[PlayerNumToWindowNum[i]].update(State);
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
			PlayerNumToWindowNum = new int[Process.getPlayerNum()];
			for(int i = 0; i < PlayerNumToWindowNum.length; i++)
			{
				PlayerNumToWindowNum[i] = (Process.playOrder + i )%PlayerNumToWindowNum.length;
			}
			Process.turn();
		}
		else
		{
			//update();
			JOptionPane.showMessageDialog(null, "방장이 아닙니다.","알림", 2);
		}
	}
	public void Setting(int PlayerNum)
	{
		PlayerNumToWindowNum = new int[PlayerNum];
		for(int i = 0; i < PlayerNumToWindowNum.length; i++)
		{
			PlayerNumToWindowNum[i] = (Process.playOrder + i )%PlayerNum;
		}
	}
	class PlayerWindow implements ActionListener
	{
		JButton [] m_Card;
		JPanel m_Panel;
		int m_WindowNum;
		int m_PlayerNum;
		final int Size = 150;
		
		protected PlayerWindow()
		{
			//하위클래스에서 필요한것 바깥에서는 위험함으로 쓰지말것.
		}
		public PlayerWindow(int PlayerNum)
		{
			m_Panel = new JPanel();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER,-1,-1);
			m_Panel.setLayout(layout);
			m_PlayerNum = PlayerNum;
			m_Card = new JButton[13];
			String lo = "";
			m_WindowNum = PlayerNum;
			switch(PlayerNum)
			{
				case 0:
					lo = BorderLayout.SOUTH;
					m_Panel.setPreferredSize(new Dimension(0,Size));
					break;
				case 1:
					lo = BorderLayout.WEST;
					m_Panel.setPreferredSize(new Dimension(Size,0));
					break;
				case 2:
					lo = BorderLayout.NORTH;
					m_Panel.setPreferredSize(new Dimension(0,Size));
					break;
				case 3:
					lo = BorderLayout.EAST;
					m_Panel.setPreferredSize(new Dimension(Size,0));
					break;
				default:
					break;
			}
			JPanel_Main.add(lo,m_Panel);
			m_Panel.setOpaque(false);
		}
		
		public void setEnable(boolean state)
		{
			for(int i = 0; m_Card[i] != null; i++)
			{
				//배열정보 얻어와서 열려진것은 선택 안되도록 해야함.
				m_Card[i].setEnabled(state);
			 	m_Card[i].setRolloverEnabled(state);
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
				if(State[i].getColor() == 0)
				{
					if(State[i].getOpen() || State[i].getOwn())
					{
						m_Card[i].setIcon(ImageCardBlack[State[i].getNum()]);
						m_Card[i].setDisabledIcon(ImageCardBlack[State[i].getNum()]);
						m_Card[i].setRolloverIcon(ImageCardBlackRollover[State[i].getNum()]);
					}
					else
					{
						//오픈 안되있는 경우 뒷면을 보여준다.
						m_Card[i].setIcon(ImageCardBlackUnknown);
						m_Card[i].setDisabledIcon(ImageCardBlackUnknown);
					}
				}
				else
				{
					if(State[i].getOpen() || State[i].getOwn())
					{
						m_Card[i].setIcon(ImageCardWhite[State[i].getNum()]);
						m_Card[i].setDisabledIcon(ImageCardWhite[State[i].getNum()]);
						m_Card[i].setRolloverIcon(ImageCardWhiteRollover[State[i].getNum()]);
					}
					else
					{
						//오픈 안되있는 경우 뒷면을 보여준다.
						m_Card[i].setIcon(ImageCardWhiteUnknown);
						m_Card[i].setDisabledIcon(ImageCardWhiteUnknown);
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
					Process.AskBlock(m_WindowNum, i,Integer.parseInt(JOptionPane.showInputDialog("몇번일까?")));
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
}