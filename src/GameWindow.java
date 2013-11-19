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

class GameWindow {
	JPanel JPanel_Main;
	PlayerWindow[] PlayerPane = new PlayerWindow[4];
	PlayerWindow Center;

	ImageIcon[] ImageCardBlack = new ImageIcon[13];
	ImageIcon[] ImageCardWhite = new ImageIcon[13];
	ImageIcon[] ImageCardBlackOpen = new ImageIcon[13];
	ImageIcon[] ImageCardWhiteOpen = new ImageIcon[13];
	ImageIcon ImageCardBlackUnknown, ImageCardWhiteUnknown;
	ImageIcon ImageCardBlackUnknownRollerover, ImageCardWhiteUnknownRollerover;

	public static final int CENTER = 5;
	public static final int NORMAL = 0;
	public static final int OTHERS = 4;

	GameProcess Process; // 게임 윈도우 전체 내에서 게임 진행을 맡을 클래스.

	int[] PlayerNumToWindowNum;
	// index 는 player 게임 순서, 값은 player 가 윈도우 상에 나타날 위치를 기록하는 배열.
	// PlayerNumToWindowNum[n] == 0 일때, n 은 player 자신의 게임 순서.

	private JPanel nevertouch; // 게임 윈도우의 최상위 frame panel.

	public GameWindow(JPanel main, Network net) {
		nevertouch = main;
		JPanel_Main = new JPanel() {
			ImageIcon BG = new ImageIcon(
					DavichiGUI.class.getResource("board.jpg"));

			public void paint(Graphics g) {
				g.drawImage(BG.getImage(), 0, 0, BG.getIconWidth(),
						BG.getIconHeight(), null);
				this.setOpaque(false);
				super.paint(g);
			}
		};
		JPanel_Main.setLayout(new BorderLayout());

		// 이미지 로딩
		for (int i = 0; i < 13; i++) {
			ImageCardBlack[i] = new ImageIcon(
					DavichiGUI.class.getResource("card/b" + i + ".gif"));
			ImageCardBlackOpen[i] = new ImageIcon(
					DavichiGUI.class.getResource("card/b" + i + "r.gif"));
			ImageCardWhite[i] = new ImageIcon(
					DavichiGUI.class.getResource("card/w" + i + ".gif"));
			ImageCardWhiteOpen[i] = new ImageIcon(
					DavichiGUI.class.getResource("card/w" + i + "r.gif"));
		}
		ImageCardBlackUnknown = new ImageIcon(
				DavichiGUI.class.getResource("card/bu.gif"));
		ImageCardWhiteUnknown = new ImageIcon(
				DavichiGUI.class.getResource("card/wu.gif"));
		ImageCardBlackUnknownRollerover = new ImageIcon(
				DavichiGUI.class.getResource("card/bur.gif"));
		ImageCardWhiteUnknownRollerover = new ImageIcon(
				DavichiGUI.class.getResource("card/wur.gif"));

		// 플레이어 와 NPC패널의 설정
		for (int i = 0; i < 4; i++)
			PlayerPane[i] = new PlayerWindow(i);
		Center = new NPC();

		main.add(JPanel_Main);

		Process = new GameProcess(this, net); // 게임 윈도우가 생성되면서 게임 프로세스도 하나 생성됨.
		net.setM_Game(Process);
	}

	public void setEnable(int target, boolean state) {
		System.out.println("[ GameWindow : setEnable ]");
		ArrayList<Block> blockState;
		switch (target) {
		case OTHERS:
			for (int i = 0; i < Process.getNumOfPlayer(); i++) {
				if (i != Process.myPlayOrder) { // 현재 player 를 제외하고 나머지 player들의
												// 경우
					blockState = Process.GetPlayerBlocksState(i);
					PlayerPane[PlayerNumToWindowNum[i]].update(blockState);
					// 플레이어 넘버와 화면번호의 매칭하여 update.
					PlayerPane[PlayerNumToWindowNum[i]].setEnable(blockState,
							state);
					// 0이 아래 부터 시계방향 순서대로
				}
			}
			break;
		case CENTER:
			blockState = Process.GetCenterBlocksState();
			// 바닥에 깔린 block 들의 상태를 받아와 저장.
			Center.setEnable(blockState, state);
			break;
		}
	}

	public void update() {
		// 게임 윈도우 내의 block( center 와 player 모두) 들 상태를 갱신하는 메소드.
		System.out.println("[ GameWindow : update ]");
		ArrayList<Block> blockState;
		int playerNum = Process.getNumOfPlayer();
		for (int of_Player = 0; of_Player < playerNum; of_Player++) {
			// player 수 만큼
			blockState = Process.GetPlayerBlocksState(of_Player);
			// player #i 의 block 상태를 받아와서
			PlayerPane[PlayerNumToWindowNum[of_Player]].update(blockState);
			// 게임 윈도우에 갱신.
		}
		blockState = Process.GetCenterBlocksState();
		// 바닥에 깔린 block 들의 상태를 받아와서
		Center.update(blockState);
	}

	public void start() {
		// player 수만큼 게임 윈도우에서 player 들의 위치를 설정하고 server 부터 게임을 시작하는 메소드.
		// 채팅창에 있는 게임 시작 버튼의 동작을 받기위한 것.
		if (Process.netObject.isServer()) {
			Process.Start(); // 게임 프로세스를 시작
			Setting(Process.getNumOfPlayer());
			// 게임 윈도우 내에서 player 들의 위치를 설정한다.
			Process.turn(); // server 부터 게임 시작.
		} else {
			JOptionPane.showMessageDialog(null, "방장이 아닙니다.", "알림", 2);
		}
	}

	public void Setting(int PlayerNum) {
		// player 의 화면에서 자신은 맨 아래, 나머지는 다른 위치에 맞게 배열하도록 하는 메소드.
		PlayerNumToWindowNum = new int[PlayerNum]; // 게임중인 player 수만큼 배열 생성.
		switch (PlayerNum) {
		case 2: // player 수가 2 명일 경우
			if (Process.myPlayOrder == 0) {
				// player 자신의 순서가 0 번이면
				PlayerNumToWindowNum[0] = 0; // 자신은 맨 밑 위치로,
				PlayerNumToWindowNum[1] = 2; // 상대방은 맨 위쪽으로 이동.
				PlayerPane[0].playerOrder = 0;
				PlayerPane[2].playerOrder = 1;

			} else {
				// player 의 play 순서가 1 번이면
				PlayerNumToWindowNum[0] = 2; // 상대방을 위 쪽으로,
				PlayerNumToWindowNum[1] = 0; // 자신을 아래쪽으로 설정.
				PlayerPane[2].playerOrder = 0;
				PlayerPane[0].playerOrder = 1;
			}
			break;
		case 3: // player 수가 3 명일 경우
			if (Process.myPlayOrder == 0) {
				PlayerNumToWindowNum[0] = 0;
				PlayerNumToWindowNum[1] = 1;
				PlayerNumToWindowNum[2] = 3;
				PlayerPane[0].playerOrder = 0;
				PlayerPane[1].playerOrder = 1;
				PlayerPane[3].playerOrder = 2;
			} else if (Process.myPlayOrder == 1) {
				PlayerNumToWindowNum[0] = 3;
				PlayerNumToWindowNum[1] = 0;
				PlayerNumToWindowNum[2] = 1;
				PlayerPane[3].playerOrder = 0;
				PlayerPane[0].playerOrder = 1;
				PlayerPane[1].playerOrder = 2;
			} else if (Process.myPlayOrder == 2) {
				PlayerNumToWindowNum[0] = 1;
				PlayerNumToWindowNum[1] = 3;
				PlayerNumToWindowNum[2] = 0;
				PlayerPane[1].playerOrder = 0;
				PlayerPane[3].playerOrder = 1;
				PlayerPane[0].playerOrder = 2;
			}
			break;
		case 4: // player 수가 4 명일 경우
			if (Process.myPlayOrder == 0) {
				PlayerNumToWindowNum[0] = 0;
				PlayerNumToWindowNum[1] = 1;
				PlayerNumToWindowNum[2] = 2;
				PlayerNumToWindowNum[3] = 3;
				PlayerPane[0].playerOrder = 0;
				PlayerPane[1].playerOrder = 1;
				PlayerPane[2].playerOrder = 2;
				PlayerPane[3].playerOrder = 3;
			} else if (Process.myPlayOrder == 1) {
				PlayerNumToWindowNum[0] = 3;
				PlayerNumToWindowNum[1] = 0;
				PlayerNumToWindowNum[2] = 1;
				PlayerNumToWindowNum[3] = 2;
				PlayerPane[3].playerOrder = 0;
				PlayerPane[0].playerOrder = 1;
				PlayerPane[1].playerOrder = 2;
				PlayerPane[2].playerOrder = 3;
			} else if (Process.myPlayOrder == 2) {
				PlayerNumToWindowNum[0] = 2;
				PlayerNumToWindowNum[1] = 3;
				PlayerNumToWindowNum[2] = 0;
				PlayerNumToWindowNum[3] = 1;
				PlayerPane[2].playerOrder = 0;
				PlayerPane[3].playerOrder = 1;
				PlayerPane[0].playerOrder = 2;
				PlayerPane[1].playerOrder = 3;
			} else if (Process.myPlayOrder == 3) {
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

	public void RemoveAll() {
		// 게임 윈도우를 모두 보이지 않게 하는 메소드.
		JPanel_Main.setVisible(false);
		for (int i = 0; i < 4; i++)
			PlayerPane[i].m_Panel.setVisible(false);
		Center.m_Panel.setVisible(false);
	}

	class PlayerWindow implements ActionListener {
		JButton[] playerBlock;
		JPanel m_Panel;
		int m_WindowNum;
		int playerOrder;
		final int Size = 100;

		protected PlayerWindow() { // 하위클래스에서 필요한것 바깥에서는 위험하므로 쓰지말것.
		} // 내용 없음.

		public PlayerWindow(int PlayerNum) {
			m_Panel = new JPanel(); // 게임 내에서 player 의 block 이 놓이는 위치
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER, -1, -1);
			playerOrder = PlayerNum;
			playerBlock = new JButton[13]; // 해당 player 의 block 역할을 하게 될 버튼 13개.
			String lo = "";
			switch (PlayerNum) {
			// PlayerNum 에 따라 다른 사람들과 자신의 block 이 놓일 위치를 설정한다.
			case 0:
				lo = BorderLayout.SOUTH;
				m_Panel.setPreferredSize(new Dimension(0, Size));
				break;
			case 1:
				lo = BorderLayout.WEST;
				m_Panel.setPreferredSize(new Dimension(Size, 0));
				layout.setAlignment(FlowLayout.LEADING);
				break;
			case 2:
				lo = BorderLayout.NORTH;
				m_Panel.setPreferredSize(new Dimension(0, Size));
				break;
			case 3:
				lo = BorderLayout.EAST;
				m_Panel.setPreferredSize(new Dimension(Size, 0));
				layout.setAlignment(FlowLayout.TRAILING);
				break;
			default:
				break;
			}
			m_Panel.setLayout(layout);
			JPanel_Main.add(lo, m_Panel);
			m_Panel.setOpaque(false); // 불투명하게 설정하는 메소드에 false를 주어 투명하게 만듬.
		}

		public void setEnable(ArrayList<Block> blockState, boolean state) {
			// player가 가진 block 들의 상태를 state로 설정함.
			System.out.println("[ PlayerWindow : setEnable ]");
			// NPC에 같은 함수 같이 변경해야 함
			for (int i = 0; playerBlock[i] != null; i++) {
				playerBlock[i].setEnabled(state);
				playerBlock[i].setRolloverEnabled(state);

				if (blockState.get(i).isOpen()) { // 이미 open 된 카드일 경우,
					playerBlock[i].setEnabled(false); // 카드 선택이 불가능하도록 설정.
					playerBlock[i].setRolloverEnabled(false); // 마우스 오버해도 표시되지
																// 않도록 설정.
				}
			}
		}

		public void update(ArrayList<Block> blocks) {
			// block 의 상태에 따라 색깔과 open,unknown 상태에 맞는 이미지로 block 을 갱신해준다.
			for (int i = 0; i < blocks.size(); i++) // 블럭을 업데이트 한다.
			{
				if (playerBlock[i] == null) {
					// 아직 block 이 생성된 적이 없으면 새로 생성.
					playerBlock[i] = new JStyleButton();
					playerBlock[i].addActionListener(this);
					m_Panel.add(playerBlock[i]);
					playerBlock[i].setMargin(new Insets(0, 0, 0, 0));
					// 버튼사이에 여백을 넣는 문구. 이 문장이 없을 경우 여백크기가 기본크기로 들어가는데, 그 크기가 너무
					// 크다.
				}
				int num = blocks.get(i).getNum();
				if (blocks.get(i).getColor() == 0) // i 번째 block 이 검정 일 경우
				{
					if (blocks.get(i).isOpen() || blocks.get(i).isOwned()) {
						if (blocks.get(i).isOpen()) {
							// 그 block 이 open 되어있을 경우 알려진 표시를 해주고
							playerBlock[i].setIcon(ImageCardBlackOpen[num]);
						} else {
							// 그렇지 않으면 일반 이미지를 표시한다.
							playerBlock[i].setIcon(ImageCardBlack[num]);
						}
					} else { // open 되어있지 않고 소유되지도 않은 경우
						playerBlock[i].setIcon(ImageCardBlackUnknown);
						// 소유되어있지 않은 block일 경우에는 알려지지 않았다는 이미지를 보여준다.
						playerBlock[i]
								.setRolloverIcon(ImageCardBlackUnknownRollerover);
					}
				} else { // 흰색 block 일 경우
					if (blocks.get(i).isOpen() || blocks.get(i).isOwned()) {
						if (blocks.get(i).isOpen()) {
							playerBlock[i].setIcon(ImageCardWhiteOpen[num]);
						} else {
							playerBlock[i].setIcon(ImageCardWhite[num]);
						}
					} else { // 소유되어있지 않은 경우 뒷면을 보여준다.
						playerBlock[i].setIcon(ImageCardWhiteUnknown);
						playerBlock[i]
								.setRolloverIcon(ImageCardWhiteUnknownRollerover);
					}
				}
				playerBlock[i].setRolloverEnabled(false);
				// 기본적으로는 마우스오버해도 아무것도 안보이게 설정한다.
			}
		}

		public void actionPerformed(ActionEvent e) {
			// 해당 PlayerWindow 의 block 이 선택된 것이다.
			// 선택이 된다는 것은 이 playerWindow 에 해당하는 player 의 block 을 물어보는 것이기 때문에
			// askblock을 호출한다.
			for (int btnIndex = 0; playerBlock[btnIndex] != null; btnIndex++) {
				if (e.getSource() == playerBlock[btnIndex]) {
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
			playerBlock = new JButton[27]; // 모두 27개의 block 들.
			JPanel_Main.add(BorderLayout.CENTER, m_Panel);
			m_Panel.setOpaque(false);
		}

		public void update(ArrayList<Block> blockState) {
			System.out.println("[ NPC : update ]");
			super.update(blockState);
			// center 가 가진 block 들은 아무것도 소유되어진 것이 없기 때문에 모두 뒷면으로 이미지가 설정된다.
			for (int i = blockState.size(); playerBlock[i] != null; i++) {
				// playerBlock[i].removeAll();
				m_Panel.remove(playerBlock[i]);
				m_Panel.repaint();
			}
		}

		@Override
		public void setEnable(ArrayList<Block> blockState, boolean state) {
			// 위의 플레이어 윈도우와 같이 변경해야함.
			System.out.println("[ NPC : setEnable ]");
			for (int i = 0; playerBlock[i] != null; i++) {
				playerBlock[i].setEnabled(state);
				playerBlock[i].setRolloverEnabled(state);
			}
		}

		public void actionPerformed(ActionEvent e) {
			// center에서 선택된 block 의 index 를 넘겨 block 을 player 에게 전달하도록 한다.
			// 가운데는 선택되면 이것은 처음에 가운데 블럭을 선택하기 위한것이다.
			// moveblock을 호출해서 가운데것을 가져가게 하면 된다.
			// 그리고 선택된것은 빼어버린다.(업데이트가 나으려나.)
			Process.gameWndGUI.setEnable(GameWindow.CENTER, false);
			// block 을 가져가고난 후에는 다시 center의 block 을 선택하지 못하게 막는다.
			for (int i = 0; i < 26; i++) {
				if (playerBlock[i] != null) {
					if (e.getSource() == playerBlock[i]) {
						Process.moveBlock(i);
					}
				}
			}
		}
	}

	public int askNum() { // block 의 숫자를 묻는 대화창을 만들어 숫자를 물어본 뒤 그 수를 반환한다.
		AskDlg AD = new AskDlg(); // 대화상자를 열어 숫자를 선택하고
		return AD.getNum(); // 그 숫자를 반환한다.
	}
}
