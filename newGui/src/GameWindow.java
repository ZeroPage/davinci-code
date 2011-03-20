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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GameWindow
{
	JPanel 				JPanel_Main;
	PlayerWindow [] 	PlayerPane = new PlayerWindow[4];
	PlayerWindow 		Center;
	
	ImageIcon [] ImageCardBlack			= new ImageIcon[13];
	ImageIcon [] ImageCardWhite			= new ImageIcon[13];
	ImageIcon [] ImageCardBlackOpen		= new ImageIcon[13];
	ImageIcon [] ImageCardWhiteOpen		= new ImageIcon[13];
	ImageIcon ImageCardBlackUnknown, 			ImageCardWhiteUnknown;
	ImageIcon ImageCardBlackUnknownRollerover,	ImageCardWhiteUnknownRollerover;
	
	public static final int CENTER = 5;
	public static final int NORMAL = 0;
	public static final int OTHERS = 4;
	
	GameProcess Process;				// ���� ������ ��ü ������ ���� ������ ���� Ŭ����.
	
	int [] PlayerNumToWindowNum;		// index �� player ���� ����, ���� player �� ������ �� ��Ÿ�� ��ġ�� ����ϴ� �迭.
										// PlayerNumToWindowNum[n] == 0 �϶�, n �� player �ڽ��� ���� ����. 
	
	private JPanel nevertouch;			// ���� �������� �ֻ��� frame panel.
	
	public GameWindow(JPanel main, Network net)
	{
		nevertouch	= main;
		JPanel_Main = new JPanel()
		{
			ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("board.jpg"));
			public void paint(Graphics g) {
				g.drawImage(BG.getImage(), 0, 0, BG.getIconWidth(), BG.getIconHeight(), null);
				this.setOpaque(false);
				super.paint(g);
			}
		};
		JPanel_Main.setLayout(new BorderLayout());

		//�̹��� �ε�
		for(int i = 0; i < 13; i++) {
			ImageCardBlack[i]		= new ImageIcon(DavichiGUI.class.getResource("card/b"+i+".gif"));
			ImageCardBlackOpen[i]	= new ImageIcon(DavichiGUI.class.getResource("card/b"+i+"r.gif"));
			ImageCardWhite[i]		= new ImageIcon(DavichiGUI.class.getResource("card/w"+i+".gif"));
			ImageCardWhiteOpen[i]	= new ImageIcon(DavichiGUI.class.getResource("card/w"+i+"r.gif"));
		}
		ImageCardBlackUnknown 			= new ImageIcon(DavichiGUI.class.getResource("card/bu.gif"));
		ImageCardWhiteUnknown			= new ImageIcon(DavichiGUI.class.getResource("card/wu.gif"));
		ImageCardBlackUnknownRollerover = new ImageIcon(DavichiGUI.class.getResource("card/bur.gif"));
		ImageCardWhiteUnknownRollerover = new ImageIcon(DavichiGUI.class.getResource("card/wur.gif"));
		
		//�÷��̾� �� NPC�г��� ����
		for(int i = 0; i < 4; i++)	PlayerPane[i] = new PlayerWindow(i);
		Center = new NPC();
		
		main.add(JPanel_Main);
		
		Process = new GameProcess(this, net);		// ���� �����찡 �����Ǹ鼭 ���� ���μ����� �ϳ� ������.
		net.setM_Game(Process);
	}
	public void setEnable(int target, boolean state)
	{
		ArrayList<Block> blockState;
		switch(target) {
			case OTHERS:
				for(int i = 0; i < Process.getNumOfPlayer(); i++)
				{
					if(i != Process.myPlayOrder) {											// ���� player �� �����ϰ� ������ player ���� ���
						blockState = Process.GetPlayerBlocksState(i);
						PlayerPane[PlayerNumToWindowNum[i]].update(blockState);				//�÷��̾� �ѹ��� ȭ���ȣ�� ��Ī�Ͽ� update.
						PlayerPane[PlayerNumToWindowNum[i]].setEnable(blockState, state);	//0�� �Ʒ� ���� �ð���� �������
					}
				}
				break;
			case CENTER:
				blockState = Process.GetCenterBlocksState();		// �ٴڿ� �� block ���� ���¸� �޾ƿ� ����.
				Center.setEnable(blockState, state);
				break;
		}
	}
	public void update() {				// ���� ������ ���� block( center �� player ���) �� ���¸� �����ϴ� �޼ҵ�.
		ArrayList<Block> blockState;
		int playerNum = Process.getNumOfPlayer();
		for(int of_Player = 0; of_Player < playerNum; of_Player++) {			// player �� ��ŭ
			blockState = Process.GetPlayerBlocksState(of_Player);			// player #i �� block ���¸� �޾ƿͼ�
			PlayerPane[PlayerNumToWindowNum[of_Player]].update(blockState);	// ���� �����쿡 ����.
		}
		blockState = Process.GetCenterBlocksState();						// �ٴڿ� �� block ���� ���¸� �޾ƿͼ�
		Center.update(blockState);		
	}
	public void start()		// player ����ŭ ���� �����쿡�� player ���� ��ġ�� �����ϰ� server ���� ������ �����ϴ� �޼ҵ�.
	{
		//ä��â�� �ִ� ���� ���� ��ư�� ������  �ޱ����� ��.
		if(Process.netObject.isServer())
		{
			Process.Start();					// ���� ���μ����� ����
			Setting(Process.getNumOfPlayer());	// ���� ������ ������ player ���� ��ġ�� �����Ѵ�. 
			Process.turn();						// server ���� ���� ����.
		}
		else {
			JOptionPane.showMessageDialog(null, "������ �ƴմϴ�.","�˸�", 2);
		}
	}
	public void Setting(int PlayerNum)		// player �� ȭ�鿡�� �ڽ��� �� �Ʒ�, �������� �ٸ� ��ġ�� �°� �迭�ϵ��� �ϴ� �޼ҵ�.
	{
		PlayerNumToWindowNum = new int[PlayerNum];	// �������� player ����ŭ �迭 ����.
		switch(PlayerNum) {
			case 2:		// player ���� 2 ���� ���
				if(Process.myPlayOrder == 0)		// player �ڽ��� ������ 0 ���̸�
				{
					PlayerNumToWindowNum[0] = 0;	// �ڽ��� �� �� ��ġ��,
					PlayerNumToWindowNum[1] = 2;	// ������ �� �������� �̵�.
					PlayerPane[0].playerOrder = 0;
					PlayerPane[2].playerOrder = 1;
					
				}
				else							// player �� play ������ 1 ���̸�
				{
					PlayerNumToWindowNum[0] = 2;	// ������ �� ������,
					PlayerNumToWindowNum[1] = 0;	// �ڽ��� �Ʒ������� ����.
					PlayerPane[2].playerOrder = 0;
					PlayerPane[0].playerOrder = 1;
				}
				break;
			case 3:		// player ���� 3 ���� ���
				if(Process.myPlayOrder == 0)
				{
					PlayerNumToWindowNum[0] = 0;
					PlayerNumToWindowNum[1] = 1;
					PlayerNumToWindowNum[2] = 3;
					PlayerPane[0].playerOrder = 0;
					PlayerPane[1].playerOrder = 1;
					PlayerPane[3].playerOrder = 2;
				}
				else if(Process.myPlayOrder == 1)
				{
					PlayerNumToWindowNum[0] = 3;
					PlayerNumToWindowNum[1] = 0;
					PlayerNumToWindowNum[2] = 1;
					PlayerPane[3].playerOrder = 0;
					PlayerPane[0].playerOrder = 1;
					PlayerPane[1].playerOrder = 2;
				}
				else if(Process.myPlayOrder == 2)
				{
					PlayerNumToWindowNum[0] = 1;
					PlayerNumToWindowNum[1] = 3;
					PlayerNumToWindowNum[2] = 0;
					PlayerPane[1].playerOrder = 0;
					PlayerPane[3].playerOrder = 1;
					PlayerPane[0].playerOrder = 2;
				}
				break;
			case 4:		// player ���� 4 ���� ���
				if(Process.myPlayOrder == 0)
				{
					PlayerNumToWindowNum[0] = 0;
					PlayerNumToWindowNum[1] = 1;
					PlayerNumToWindowNum[2] = 2;
					PlayerNumToWindowNum[3] = 3;
					PlayerPane[0].playerOrder = 0;
					PlayerPane[1].playerOrder = 1;
					PlayerPane[2].playerOrder = 2;
					PlayerPane[3].playerOrder = 3;
				}
				else if(Process.myPlayOrder == 1)
				{
					PlayerNumToWindowNum[0] = 3;
					PlayerNumToWindowNum[1] = 0;
					PlayerNumToWindowNum[2] = 1;
					PlayerNumToWindowNum[3] = 2;
					PlayerPane[3].playerOrder = 0;
					PlayerPane[0].playerOrder = 1;
					PlayerPane[1].playerOrder = 2;
					PlayerPane[2].playerOrder = 3;
				}
				else if(Process.myPlayOrder == 2)
				{
					PlayerNumToWindowNum[0] = 2;
					PlayerNumToWindowNum[1] = 3;
					PlayerNumToWindowNum[2] = 0;
					PlayerNumToWindowNum[3] = 1;
					PlayerPane[2].playerOrder = 0;
					PlayerPane[3].playerOrder = 1;
					PlayerPane[0].playerOrder = 2;
					PlayerPane[1].playerOrder = 3;
				}
				else if(Process.myPlayOrder == 3)
				{
					PlayerNumToWindowNum[0] = 1;
					PlayerNumToWindowNum[1] = 2;
					PlayerNumToWindowNum[2] = 3;
					PlayerNumToWindowNum[3] = 0;
					PlayerPane[1].playerOrder = 0;
					PlayerPane[2].playerOrder = 1;
					PlayerPane[3].playerOrder = 2;
					PlayerPane[0].playerOrder = 3;
				}
				break;
		}
	}
	public void RemoveAll()		// ���� �����츦 ��� ������ �ʰ� �ϴ� �޼ҵ�.
	{
		JPanel_Main.setVisible(false);
		for(int i = 0; i < 4; i++)	PlayerPane[i].m_Panel.setVisible(false);
		Center.m_Panel.setVisible(false);
	}

	class PlayerWindow implements ActionListener
	{
		JButton []	playerBlock;
		JPanel		m_Panel;
		int			m_WindowNum;
		int			playerOrder;
		final int	Size = 100;
		
		protected PlayerWindow() { //����Ŭ�������� �ʿ��Ѱ� �ٱ������� ���������� ��������. 
		}	// ���� ����.
		public PlayerWindow(int PlayerNum)
		{
			m_Panel		= new JPanel();		// ���� ������ player �� block �� ���̴� ��ġ
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER,-1,-1);
			playerOrder = PlayerNum;
			playerBlock = new JButton[13];		// �ش� player �� block ������ �ϰ� �� ��ư 13��.
			String lo	= "";
			switch(PlayerNum)	// PlayerNum �� ���� �ٸ� ������ �ڽ��� block �� ���� ��ġ�� �����Ѵ�.
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
			m_Panel.setOpaque(false);		// �������ϰ� �����ϴ� �޼ҵ忡 false�� �־� �����ϰ� ����.
		}
		
		public void setEnable(ArrayList<Block> blockState, boolean state)	// player �� ���� block ���� ���¸� state �� ������.
		{
			//NPC�� ���� �Լ� ���� �����ؾ� ��
			for(int i = 0; playerBlock[i] != null; i++)
			{
				playerBlock[i].setEnabled(state);
				playerBlock[i].setRolloverEnabled(state);
				
				if(blockState.get(i).isOpen()) {				// �̹� open �� ī���� ���,
					playerBlock[i].setEnabled(false);			// ī�� ������ �Ұ����ϵ��� ����.
					playerBlock[i].setRolloverEnabled(false);	// ���콺 �����ص� ǥ�õ��� �ʵ��� ����.
				}
			}
		}
		public void update(ArrayList<Block> blocks)		// block �� ���¿� ���� ����� open,unknown ���¿� �´� �̹����� block �� �������ش�.
		{
			for(int i = 0; i < blocks.size(); i++) {				//���� ������Ʈ �Ѵ�.
				if(playerBlock[i] == null) {						// ���� block �� ������ ���� ������ ���� ����.
					playerBlock[i] = new JStyleButton();
					playerBlock[i].addActionListener(this);
					m_Panel.add(playerBlock[i]);
					playerBlock[i].setMargin(new Insets(0,0,0,0));	// ��ư���̿� ������ �ִ� ����. �� ������ ���� ��� ����ũ�Ⱑ �⺻ũ��� ���µ�, �� ũ�Ⱑ �ʹ� ũ��.
				}
				int num = blocks.get(i).getNum();
				if(blocks.get(i).getColor() == 0) {					// i ��° block �� ���� �� ���
					if(blocks.get(i).isOpen() || blocks.get(i).isOwned()) {
						if(blocks.get(i).isOpen()) {							// �� block �� open �Ǿ����� ��쿡��
							playerBlock[i].setIcon(ImageCardBlackOpen[num]);	// �˷��� ǥ�ø� ���ְ�
						}
						else {													// �׷��� ������
							playerBlock[i].setIcon(ImageCardBlack[num]);		// �Ϲ� �̹����� ǥ���Ѵ�.
						}
					}
					else {														// open �Ǿ����� �ʰ� ���������� ���� ���
						playerBlock[i].setIcon(ImageCardBlackUnknown);			// �����Ǿ����� ���� block �� ��쿡�� �˷����� �ʾҴٴ� �̹����� �����ش�.
						playerBlock[i].setRolloverIcon(ImageCardBlackUnknownRollerover);
					}
				}
				else {			// ��� block �� ���
					if(blocks.get(i).isOpen() || blocks.get(i).isOwned()) {
						if(blocks.get(i).isOpen()) {
							playerBlock[i].setIcon(ImageCardWhiteOpen[num]);
						}
						else {
							playerBlock[i].setIcon(ImageCardWhite[num]);
						}
					}
					else {		// �����Ǿ����� ���� ��� �޸��� �����ش�.
						playerBlock[i].setIcon(ImageCardWhiteUnknown);
						playerBlock[i].setRolloverIcon(ImageCardWhiteUnknownRollerover);
					}
				}
				playerBlock[i].setRolloverEnabled(false);		// �⺻�����δ� ���콺�����ص� �ƹ��͵� �Ⱥ��̰� �����Ѵ�.
			}	
		}
		public void actionPerformed(ActionEvent e) {
			// �ش� PlayerWindow �� block �� ���õ� ���̴�. 
			// ������ �ȴٴ� ���� �� playerWindow �� �ش��ϴ� player �� block �� ����� ���̱� ������ askblock�� ȣ���Ѵ�.
			for(int btnIndex =0; playerBlock[btnIndex] != null; btnIndex++) {
				if(e.getSource() == playerBlock[btnIndex]) {
					Process.AskBlock(playerOrder, btnIndex, askNum());
					break;
				}
			}
		}
	}
	class NPC extends PlayerWindow {
		public NPC() {
			super();
			m_Panel = new JPanel();
			m_WindowNum = 5;
			playerBlock = new JButton[27];		// ��� 27���� block ��.
			JPanel_Main.add(BorderLayout.CENTER, m_Panel);
			m_Panel.setOpaque(false);
		}
		public void update(ArrayList<Block> blockState) {
			super.update(blockState);		// center �� ���� block ���� �ƹ��͵� �����Ǿ��� ���� ���� ������ ��� �޸����� �̹����� �����ȴ�.
			for(int i = blockState.size(); playerBlock[i] != null; i++) {
				//playerBlock[i].removeAll();
				m_Panel.remove(playerBlock[i]);
				m_Panel.repaint();
			}
		}
		@Override
		public void setEnable(ArrayList<Block> blockState, boolean state) {
			//���� �÷��̾� ������� ���� �����ؾ���. 
			for(int i = 0; playerBlock[i] != null; i++) {
				playerBlock[i].setEnabled(state);
				playerBlock[i].setRolloverEnabled(state);
			}
		}
		public void actionPerformed(ActionEvent e) {		// center���� ���õ� block �� index �� �Ѱ� block �� player ���� �����ϵ��� �Ѵ�.
			//����� ���õǸ� �̰��� ó���� ��� ���� �����ϱ� ���Ѱ��̴�.
			//moveblock�� ȣ���ؼ� ������� �������� �ϸ� �ȴ�.
			//�׸��� ���õȰ��� ���������.(������Ʈ�� ��������.)
			Process.gameWndGUI.setEnable(GameWindow.CENTER, false);	// block �� �������� �Ŀ��� �ٽ� center�� block �� �������� ���ϰ� ���´�.
			for(int i = 0; i < 26; i++) {
				if(playerBlock[i] != null) {
					if(e.getSource() == playerBlock[i]) {
						
						Process.moveBlock(i);
					}
				}
			}
		}
	}
	public int askNum()	{	// block �� ���ڸ� ���� ��ȭâ�� ����� ���ڸ� ��� �� �� ���� ��ȯ�Ѵ�.
		AskDlg AD = new AskDlg();		// ��ȭ���ڸ� ���� ���ڸ� �����ϰ�
 		return AD.getNum();				// �� ���ڸ� ��ȯ�Ѵ�.
	}
}