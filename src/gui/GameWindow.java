package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import resource.ResourceManager;
import network.Network;
import core.Block;
import core.GameProcess;

public class GameWindow {
	private JPanel mainPanel;
	private PlayerWindow[] PlayerPane = new PlayerWindow[4];
	private PlayerWindow Center;

	public static final int CENTER = 5;
	public static final int NORMAL = 0;
	public static final int OTHERS = 4;

	private GameProcess Process; // 게임 윈도우 전체 내에서 게임 진행을 맡을 클래스.
	private ResourceManager resourceManager;

	int[] PlayerNumToWindowNum;

	// index 는 player 게임 순서, 값은 player 가 윈도우 상에 나타날 위치를 기록하는 배열.
	// PlayerNumToWindowNum[n] == 0 일때, n 은 player 자신의 게임 순서.

	public GameWindow(JPanel main, Network network) {
		mainPanel = new JPanel() {
			private ImageIcon BG = ResourceManager.getInstance().getGameBackground();

			public void paint(Graphics g) {
				g.drawImage(BG.getImage(), 0, 0, BG.getIconWidth(),
						BG.getIconHeight(), null);
				this.setOpaque(false);
				super.paint(g);
			}
		};
		mainPanel.setLayout(new BorderLayout());

		resourceManager = ResourceManager.getInstance();

		// 플레이어 와 NPC패널의 설정
		for (int i = 0; i < 4; i++)
			PlayerPane[i] = new PlayerWindow(i);
		Center = new NPC();

		main.add(mainPanel);

		Process = new GameProcess(this, network); // 게임 윈도우가 생성되면서 게임 프로세스도 하나
													// 생성됨.
		network.setM_Game(Process);
	}

	public void setEnable(int target, boolean state) {
		System.out.println("[ GameWindow : setEnable ]");
		ArrayList<Block> blockState;
		switch (target) {
		case OTHERS:
			for (int i = 0; i < Process.getNumOfPlayer(); i++) {
				if (i != Process.getMyPlayOrder()) {
					// 현재 player 를 제외하고 나머지 player들의 경우
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
		if (Process.getNetObject().isServer()) {
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
			if (Process.getMyPlayOrder() == 0) {
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
			if (Process.getMyPlayOrder() == 0) {
				PlayerNumToWindowNum[0] = 0;
				PlayerNumToWindowNum[1] = 1;
				PlayerNumToWindowNum[2] = 3;
				PlayerPane[0].playerOrder = 0;
				PlayerPane[1].playerOrder = 1;
				PlayerPane[3].playerOrder = 2;
			} else if (Process.getMyPlayOrder() == 1) {
				PlayerNumToWindowNum[0] = 3;
				PlayerNumToWindowNum[1] = 0;
				PlayerNumToWindowNum[2] = 1;
				PlayerPane[3].playerOrder = 0;
				PlayerPane[0].playerOrder = 1;
				PlayerPane[1].playerOrder = 2;
			} else if (Process.getMyPlayOrder() == 2) {
				PlayerNumToWindowNum[0] = 1;
				PlayerNumToWindowNum[1] = 3;
				PlayerNumToWindowNum[2] = 0;
				PlayerPane[1].playerOrder = 0;
				PlayerPane[3].playerOrder = 1;
				PlayerPane[0].playerOrder = 2;
			}
			break;
		case 4: // player 수가 4 명일 경우
			if (Process.getMyPlayOrder() == 0) {
				PlayerNumToWindowNum[0] = 0;
				PlayerNumToWindowNum[1] = 1;
				PlayerNumToWindowNum[2] = 2;
				PlayerNumToWindowNum[3] = 3;
				PlayerPane[0].playerOrder = 0;
				PlayerPane[1].playerOrder = 1;
				PlayerPane[2].playerOrder = 2;
				PlayerPane[3].playerOrder = 3;
			} else if (Process.getMyPlayOrder() == 1) {
				PlayerNumToWindowNum[0] = 3;
				PlayerNumToWindowNum[1] = 0;
				PlayerNumToWindowNum[2] = 1;
				PlayerNumToWindowNum[3] = 2;
				PlayerPane[3].playerOrder = 0;
				PlayerPane[0].playerOrder = 1;
				PlayerPane[1].playerOrder = 2;
				PlayerPane[2].playerOrder = 3;
			} else if (Process.getMyPlayOrder() == 2) {
				PlayerNumToWindowNum[0] = 2;
				PlayerNumToWindowNum[1] = 3;
				PlayerNumToWindowNum[2] = 0;
				PlayerNumToWindowNum[3] = 1;
				PlayerPane[2].playerOrder = 0;
				PlayerPane[3].playerOrder = 1;
				PlayerPane[0].playerOrder = 2;
				PlayerPane[1].playerOrder = 3;
			} else if (Process.getMyPlayOrder() == 3) {
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
		mainPanel.setVisible(false);
		for (int i = 0; i < 4; i++)
			PlayerPane[i].m_Panel.setVisible(false);
		Center.m_Panel.setVisible(false);
	}

	class PlayerWindow {
		
		GUIBlock [] playerBlock;
		
		JPanel m_Panel;
		int m_WindowNum;
		int playerOrder;
		final int windowSize = 100;

		protected PlayerWindow() { // 하위클래스에서 필요한것 바깥에서는 위험하므로 쓰지말것.
		} // 내용 없음.

		public PlayerWindow(int PlayerNum) {
			m_Panel = new JPanel(); // 게임 내에서 player 의 block 이 놓이는 위치
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER, -1, -1);
			playerOrder = PlayerNum;

			playerBlock = new PlayerBlock[13];
			
			String lo = "";
			switch (PlayerNum) {
			// PlayerNum 에 따라 다른 사람들과 자신의 block 이 놓일 위치를 설정한다.
			case 0:
				lo = BorderLayout.SOUTH;
				m_Panel.setPreferredSize(new Dimension(0, windowSize));
				break;
			case 1:
				lo = BorderLayout.WEST;
				m_Panel.setPreferredSize(new Dimension(windowSize, 0));
				layout.setAlignment(FlowLayout.LEADING);
				break;
			case 2:
				lo = BorderLayout.NORTH;
				m_Panel.setPreferredSize(new Dimension(0, windowSize));
				break;
			case 3:
				lo = BorderLayout.EAST;
				m_Panel.setPreferredSize(new Dimension(windowSize, 0));
				layout.setAlignment(FlowLayout.TRAILING);
				break;
			default:
				break;
			}
			m_Panel.setLayout(layout);
			mainPanel.add(lo, m_Panel);
			m_Panel.setOpaque(false); // 불투명하게 설정하는 메소드에 false를 주어 투명하게 만듬.
		}

		public void setEnable(ArrayList<Block> blockState, boolean state) {
			// player가 가진 block 들의 상태를 state로 설정함.
			System.out.println("[ PlayerWindow : setEnable ]");
			// NPC에 같은 함수 같이 변경해야 함
			for (int i = 0; playerBlock[i] != null; i++) {
				playerBlock[i].setEnabled(state);
				
				if (blockState.get(i).isOpen()) { // 이미 open 된 카드일 경우,
					playerBlock[i].setEnabled(false); // 카드 선택이 불가능하도록 설정.
				}
			}
		}

		public void update(ArrayList<Block> blocks) {
			
			for(int i = 0; i < blocks.size(); i++){
				if(playerBlock[i] == null){
					playerBlock[i] = new PlayerBlock(Process, playerOrder, i);
					m_Panel.add(playerBlock[i]);
				}
				playerBlock[i].update(blocks.get(i));
			}
		}
	}

	class NPC extends PlayerWindow {
		public NPC() {
			super();
			m_Panel = new JPanel();
			m_WindowNum = 5;
			playerBlock = new NPCBlock[27]; // 모두 27개의 block 들.
			mainPanel.add(BorderLayout.CENTER, m_Panel);
			m_Panel.setOpaque(false);
		}

		public void update(ArrayList<Block> blocks) {
			System.out.println("[ NPC : update ]");
			for(int i = 0; i < blocks.size(); i++){
				if(playerBlock[i] == null){
					//TODO maybe apply abstract factory
					playerBlock[i] = new NPCBlock(Process, playerOrder, i);
					m_Panel.add(playerBlock[i]);
				}
				playerBlock[i].update(blocks.get(i));
			}
			// center 가 가진 block 들은 아무것도 소유되어진 것이 없기 때문에 모두 뒷면으로 이미지가 설정된다.
			for (int i = blocks.size(); playerBlock[i] != null; i++) {
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

		//TODO 이걸 분리 해야 함.
		public void actionPerformed(ActionEvent e) {
			// center에서 선택된 block 의 index 를 넘겨 block 을 player 에게 전달하도록 한다.
			// 가운데는 선택되면 이것은 처음에 가운데 블럭을 선택하기 위한것이다.
			// moveblock을 호출해서 가운데것을 가져가게 하면 된다.
			// 그리고 선택된것은 빼어버린다.(업데이트가 나으려나.)
			Process.getGameWndGUI().setEnable(GameWindow.CENTER, false);
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
