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

		//�̹��� �ε�
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
		
		//�÷��̾� �� NPC�г��� ����
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
		//�÷��̾� �ѹ��� ȭ���ȣ�� ��Ī
		Block [] Card = Process.GetBlocksState(playerNum);
		PlayerPane[PlayerNumToWindowNum[playerNum]].setEnable(Card, state);//0�� �Ʒ� ���� �ð���� �������
	}
	public void setCenterEnable(boolean state)
	{
		Block [] Card = Process.GetCenterBlocksState();
		Center.setEnable(Card, state);
	}
	public void update()
	{
		Block [] State;
		for(int i = 0; i < Process.getPlayerNum(); i++)//������ �����°� ���� ���μ����� �߰��Ұ�
		{
			State = Process.GetBlocksState(i);
			PlayerPane[PlayerNumToWindowNum[i]].update(State);
		}
		State = Process.GetCenterBlocksState();
		Center.update(State);
	}
	public void update(int PlayerNum)
	{
		//�÷��̾� �ѹ��� �ް� �� ��ȣ�� �÷��̾ ��� ������Ʈ �Ѵ�
		//���ʹ� updateCenter�� ����.
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
		
		//ä��â�� �ִ� ���� ���� ��ư�� ������  �ޱ����� ��.
		if(Process.m_NetTaget.isServer())
		{
			Process.Start();
			Setting(Process.getPlayerNum());
			Process.turn();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "������ �ƴմϴ�.","�˸�", 2);
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
			//����Ŭ�������� �ʿ��Ѱ� �ٱ������� ���������� ��������.
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
			//NPC�� ���� �Լ� ���� �����ؾ� ��
			for(int i = 0; m_Card[i] != null; i++)
			{
				//�迭���� ���ͼ� ���������� ���� �ȵǵ��� �ؾ���.
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
						//���� �ȵ��ִ� ��� �޸��� �����ش�.
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
						//���� �ȵ��ִ� ��� �޸��� �����ش�.
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
			//�÷��̾��� �а� ���õȰ��̴�. 
			for(int i =0; m_Card[i] != null; i++)
			{
				if(e.getSource() == m_Card[i])
				{
					//���õǸ� ������ �и� ����� ���̱� ������ askblock�� ȣ���Ѵ�.
					
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
			//���� �÷��̾� ������� ���� �����ؾ���. 
			for(int i = 0; m_Card[i] != null; i++)
			{
				//�迭���� ���ͼ� ���������� ���� �ȵǵ��� �ؾ���.
				m_Card[i].setEnabled(state);
				m_Card[i].setRolloverEnabled(state);
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
			super((JFrame) getWindows()[0], "���ڸ� �����ϼ���",true);
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