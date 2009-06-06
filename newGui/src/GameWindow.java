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
	JPanel [] JPanel_Player;
	PlayerWindow [] players = new PlayerWindow[4];
	
	ImageIcon [] ImageCardBlack = new ImageIcon[13];
	ImageIcon [] ImageCardWhite = new ImageIcon[13];
	ImageIcon [] ImageCardBlackRollover = new ImageIcon[13];
	ImageIcon [] ImageCardWhiteRollover = new ImageIcon[13];
	
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
			players[i] = new PlayerWindow(i);
		}
		main.add(JPanel_Main);
		
	}
	public void setEnable(boolean state)
	{
		players[0].setEnable(state);//0이 아래 부터 시계방향 순서대로
	}
	class PlayerWindow implements ActionListener
	{
		JButton [] m_Card = new JButton[13];
		JPanel m_Panel;
		int m_PlayerNum;
		
		public PlayerWindow(int PlayerNum)
		{
			m_Panel = new JPanel();
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
		public void AddCard(boolean isJoker)
		{
			
		}
		public void actionPerformed(ActionEvent e)
		{
			for(int i =0; m_Card[i] != null; i++)
			{
			}
		}
	}
}