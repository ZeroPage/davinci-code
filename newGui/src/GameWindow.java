import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
			ImageCardBlack[i] = new ImageIcon(DavichiGUI.class.getResource("b"+i+".gif"));
			ImageCardBlackRollover[i] = new ImageIcon(DavichiGUI.class.getResource("b"+i+"r.gif"));
			ImageCardWhite[i] = new ImageIcon(DavichiGUI.class.getResource("w"+i+".gif"));
			ImageCardWhiteRollover[i] = new ImageIcon(DavichiGUI.class.getResource("w"+i+"r.gif"));
		}
		
		main.add(JPanel_Main);
		
		Process = new GameProcess(this, n);
	}
	public void setEnable(int playerNum, boolean state)
	{
		//�÷��̾� �ѹ��� ȭ���ȣ�� ��Ī
		players[0].setEnable(state);//0�� �Ʒ� ���� �ð���� �������
	}
	public void CenterEnable(boolean state)
	{
		
	}
	public void update(int playerNum)
	{
		//�÷��̾� �ѹ��� �ް� �� ��ȣ�� �÷��̾ ��� ������Ʈ �Ѵ�
		Block [] state = Process.GetBlocksState();
		
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
					//�ѿ��� ���� ���ð���
					
				}
				else
				{
					//�ѿ��� ���� ���� �Ұ�.
				}
			}
		}
		public void AddCard(int CardNum)
		{
			
		}
		public void update(Block [] State)
		{
			//���� ������Ʈ �Ѵ�.
			for(int i = 0; i < State.length; i++)
			{
				if(m_Card[i] != null)
				{
					
					if(State[i].getColor() == 0)
					{
						if(State[i].getOpen())
						{
							m_Card[i].setIcon(ImageCardBlack[State[i].getNum()]);
						}
						else
						{
							//m_Card[i].setIcon()�ȵ����� �̹���
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
							//m_Card[i].setIcon()�ȵ����� �̹���
						}
					}
				}
			}
		}
		public void actionPerformed(ActionEvent e)
		{
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
			//����� ���õǸ� �̰��� ó���� ��� ���� �����ϱ� ���Ѱ��̴�.
			//moveblock�� ȣ���ؼ� ������� �������� �ϸ� �ȴ�.
			//�׸��� ���õȰ��� ���������.
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