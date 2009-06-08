import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GameWindow
{
	JPanel JPanel_Main;
	PlayerWindow [] players = new PlayerWindow[4];
	PlayerWindow Center;
	
	ImageIcon [] ImageCardBlack = new ImageIcon[13];
	ImageIcon [] ImageCardWhite = new ImageIcon[13];
	ImageIcon [] ImageCardBlackRollover = new ImageIcon[13];
	ImageIcon [] ImageCardWhiteRollover = new ImageIcon[13];
	ImageIcon ImageCardBlackUnknown;
	ImageIcon ImageCardWhiteUnknown;
	
	GameProcess Process;
	
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
		
		//플레이어 와 NPC패널의 설정
		for(int i = 0; i < 4; i++)
		{
			players[i] = new PlayerWindow(i);
		}
		Center = new NPC();
		
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
		
		main.add(JPanel_Main);
		
		Process = new GameProcess(this, n);
	}
	public void setEnable(int playerNum, boolean state)
	{
		//플레이어 넘버와 화면번호의 매칭
		players[NumMatching(playerNum)].setEnable(state);//0이 아래 부터 시계방향 순서대로
	}
	public void setCenterEnable(boolean state)
	{
		Center.setEnable(state);
	}
	public void update(int PlayerNum)
	{
		//플레이어 넘버를 받고 그 번호의 플레이어를 즉시 업데이트 한다
		//센터는 updateCenter를 쓴다.
		Block [] state = Process.GetBlocksState(PlayerNum);
		players[NumMatching(PlayerNum)].update(state);
	}
	public void CenterUpdate()
	{
		Block[] State = Process.GetCenterBlocksState();
		Center.update(State);
	}
	private int NumMatching(int PlayerNum)
	{
		if(PlayerNum < 4)
		{
			PlayerNum -= Process.playOrder;
			return PlayerNum;
		}
		else
		{
			return 4; 	
		}
	}
	public void strat()
	{
		//채팅창에 있는 게임 시작 버튼의 동작을  받기위한 것.
		if(Process.m_NetTaget.isServer())
		{
			Process.Start();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "방장이 아닙니다.","알림", 2);
		}
	}
	class PlayerWindow implements ActionListener
	{
		JButton [] m_Card;
		JPanel m_Panel;
		int m_PlayerNum;
		
		protected PlayerWindow()
		{
			//하위클래스에서 필요한것 바깥에서는 위험함으로 쓰지말것.
		}
		public PlayerWindow(int PlayerNum)
		{
			m_Panel = new JPanel();
			m_Card = new JButton[13];
			m_PlayerNum = PlayerNum;
			String lo = "";
			switch(PlayerNum)
			{
				case 0:
					lo = BorderLayout.SOUTH;
					break;
				case 1:
					lo = BorderLayout.WEST;
					break;
				case 2:
					lo = BorderLayout.NORTH;
					break;
				case 3:
					lo = BorderLayout.EAST;
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
				m_Card[i].setEnabled(state);
				if(state)
				{
					//롤오버 설정 선택가능
					
				}
				else
				{
					//롤오버 설정 선택 불가.
				}
			}
		}
		public void AddCard(int CardNum)
		{
			
		}
		public void update(Block [] State)
		{
			//블럭을 업데이트 한다.
			for(int i = 0; i < State.length; i++)
			{
				if(m_Card[i] == null)
				{
					m_Card[i] = new JButton();
				}
				if(State[i].getColor() == 0)
				{
					if(State[i].getOpen())
					{
						m_Card[i].setIcon(ImageCardBlack[State[i].getNum()]);
					}
					else
					{
						//오픈 안되있는 경우 뒷면을 보여준다.
						m_Card[i].setIcon(ImageCardBlackUnknown);
					}
				}
				else
				{
					if(State[i].getOpen())
					{
						m_Card[i].setIcon(ImageCardWhite[State[i].getNum()]);
					}
					else
					{
						//오픈 안되있는 경우 뒷면을 보여준다.
						m_Card[i].setIcon(ImageCardWhiteUnknown);
					}
				}
			}	
		}
		public void actionPerformed(ActionEvent e)
		{
			//플레이어의 패가 선택된것이다. 
			for(int i =0; m_Card[i] != null; i++)
			{
				if(e.getSource() == m_Card[i])
				{
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
			m_PlayerNum = 5;
			m_Card = new JButton[26];
			JPanel_Main.add(BorderLayout.CENTER,m_Panel);
			m_Panel.setOpaque(false);
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