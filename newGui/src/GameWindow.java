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
		
		//�÷��̾� �� NPC�г��� ����
		for(int i = 0; i < 4; i++)
		{
			players[i] = new PlayerWindow(i);
		}
		Center = new NPC();
		
		//�̹��� �ε�
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
		//�÷��̾� �ѹ��� ȭ���ȣ�� ��Ī
		Block [] card = Process.GetBlocksState(playerNum);
		players[NumfPtW(playerNum)].setEnable(state);//0�� �Ʒ� ���� �ð���� �������
	}
	public void setCenterEnable(boolean state)
	{
		Block [] card = Process.GetCenterBlocksState();
		Center.setEnable(state);
	}
	public void update(int PlayerNum)
	{
		//�÷��̾� �ѹ��� �ް� �� ��ȣ�� �÷��̾ ��� ������Ʈ �Ѵ�
		//���ʹ� updateCenter�� ����.
		Block [] state = Process.GetBlocksState(PlayerNum);
		players[NumfPtW(PlayerNum)].update(state);
	}
	public void CenterUpdate()
	{
		Block[] State = Process.GetCenterBlocksState();
		Center.update(State);
	}
	private int NumfPtW(int PlayerNum)
	{
		if(PlayerNum < 4)
		{
			PlayerNum += 4 - Process.playOrder;
			PlayerNum = PlayerNum % 4;
			return PlayerNum;
		}
		else
		{
			return 4; 	
		}
	}
	private int NumfWtP(int WindowNum)
	{
		WindowNum += Process.playOrder;;
		WindowNum = WindowNum % 4;
		return WindowNum;
	}
	public void strat()
	{
		//ä��â�� �ִ� ���� ���� ��ư�� ������  �ޱ����� ��.
		if(Process.m_NetTaget.isServer())
		{
			Process.Start();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "������ �ƴմϴ�.","�˸�", 2);
		}
	}
	class PlayerWindow implements ActionListener
	{
		JButton [] m_Card;
		JPanel m_Panel;
		int m_PlayerNum;
		
		protected PlayerWindow()
		{
			//����Ŭ�������� �ʿ��Ѱ� �ٱ������� ���������� ��������.
		}
		public PlayerWindow(int PlayerNum)
		{
			m_Panel = new JPanel();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER,-1,-1);
			m_Panel.setLayout(layout);
			m_Card = new JButton[13];
			m_PlayerNum = PlayerNum;
			String lo = "";
			int Size = 150;
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
				//�迭���� ���ͼ� ���������� ���� �ȵǵ��� �ؾ���.
				m_Card[i].setEnabled(state);
			 	m_Card[i].setRolloverEnabled(state);
			}
		}
		public void update(Block [] State)
		{
			//���� ������Ʈ �Ѵ�.
			for(int i = 0; i < State.length; i++)
			{
				if(m_Card[i] == null)
				{
					m_Card[i] = new JStyleButton();
					m_Card[i].addActionListener(this);
					m_Panel.add(m_Card[i]);
					//SetButtonLocation(m_Card[i], i);
					m_Card[i].setMargin(new Insets(0,0,0,0));//������ �ѱ���.. �÷ο췹�̾ƿ��� �Դ´�.
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
						//���� �ȵ��ִ� ��� �޸��� �����ش�.
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
						//���� �ȵ��ִ� ��� �޸��� �����ش�.
						m_Card[i].setIcon(ImageCardWhiteUnknown);
						m_Card[i].setDisabledIcon(ImageCardWhiteUnknown);
					}
				}
				m_Card[i].setRolloverEnabled(false);
			}	
		}
		public void actionPerformed(ActionEvent e)
		{
			//�÷��̾��� �а� ���õȰ��̴�. 
			for(int i =0; m_Card[i] != null; i++)
			{
				if(e.getSource() == m_Card[i])
				{
					//���õǸ� ������ �и� ����� ���̱� ������ askblock�� ȣ���Ѵ�.
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
			//����� ���õǸ� �̰��� ó���� ��� ���� �����ϱ� ���Ѱ��̴�.
			//moveblock�� ȣ���ؼ� ������� �������� �ϸ� �ȴ�.
			//�׸��� ���õȰ��� ���������.(������Ʈ�� ��������.)
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