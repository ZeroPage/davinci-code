package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import network.Network;
import resource.ResourceManager;
import core.Block;
import core.GameProcess;

public class GameWindow {
	private JPanel mainPanel;
	private PlayerBoard[] playerBoard = new PlayerBoard[4];
	private PlayerBoard Center;

	public static final int CENTER = 5;
	public static final int NORMAL = 0;
	public static final int OTHERS = 4;

	private GameProcess gameProcess; // 게임 윈도우 전체 내에서 게임 진행을 맡을 클래스.

	int[] PlayerNumToWindowNum;

	// index 는 player 게임 순서, 값은 player 가 윈도우 상에 나타날 위치를 기록하는 배열.
	// PlayerNumToWindowNum[n] == 0 일때, n 은 player 자신의 게임 순서.

	public GameWindow(JPanel main, Network network) {
		ResourceManager resourceManager = ResourceManager.getInstance();
		mainPanel = new JBackgroundPanel(resourceManager.getGameBackground());
		mainPanel.setLayout(new BorderLayout());

		gameProcess = new GameProcess(this, network); 
		// 게임 윈도우가 생성되면서 게임 프로세스도 하나 생성됨.
		network.setGameProcess(gameProcess);

		// 플레이어 와 NPC패널의 설정
		for (int i = 0; i < 4; i++){
			playerBoard[i] = new PlayerBoard(i, mainPanel, gameProcess);
		}
		Center = new NPCBoard(mainPanel, gameProcess);

		main.add(mainPanel);
	}

	public void setEnable(int target, boolean state) {
		System.out.println("[ GameWindow : setEnable ]");
		ArrayList<Block> blockState;
		switch (target) {
		case OTHERS:
			for (int i = 0; i < gameProcess.getNumOfPlayer(); i++) {
				if (i != gameProcess.getMyPlayOrder()) {
					// 현재 player 를 제외하고 나머지 player들의 경우
					blockState = gameProcess.GetPlayerBlocksState(i);
					playerBoard[PlayerNumToWindowNum[i]].update(blockState);
					// 플레이어 넘버와 화면번호의 매칭하여 update.
					playerBoard[PlayerNumToWindowNum[i]].setEnable(blockState,
							state);
					// 0이 아래 부터 시계방향 순서대로
				}
			}
			break;
		case CENTER:
			blockState = gameProcess.GetCenterBlocksState();
			// 바닥에 깔린 block 들의 상태를 받아와 저장.
			Center.setEnable(blockState, state);
			break;
		}
	}

	public void update() {
		// 게임 윈도우 내의 block( center 와 player 모두) 들 상태를 갱신하는 메소드.
		System.out.println("[ GameWindow : update ]");
		ArrayList<Block> blockState;
		int playerNum = gameProcess.getNumOfPlayer();
		for (int of_Player = 0; of_Player < playerNum; of_Player++) {
			// player 수 만큼
			blockState = gameProcess.GetPlayerBlocksState(of_Player);
			// player #i 의 block 상태를 받아와서
			playerBoard[PlayerNumToWindowNum[of_Player]].update(blockState);
			// 게임 윈도우에 갱신.
		}
		blockState = gameProcess.GetCenterBlocksState();
		// 바닥에 깔린 block 들의 상태를 받아와서
		Center.update(blockState);
	}

	public void start() {
		// player 수만큼 게임 윈도우에서 player 들의 위치를 설정하고 server 부터 게임을 시작하는 메소드.
		// 채팅창에 있는 게임 시작 버튼의 동작을 받기위한 것.
		if (gameProcess.getNetObject().isServer()) {
			gameProcess.Start(); // 게임 프로세스를 시작
			Setting(gameProcess.getNumOfPlayer());
			// 게임 윈도우 내에서 player 들의 위치를 설정한다.
			gameProcess.turn(); // server 부터 게임 시작.
		} else {
			JOptionPane.showMessageDialog(null, "방장이 아닙니다.", "알림", 2);
		}
	}

	public void Setting(int PlayerNum) {
		// player 의 화면에서 자신은 맨 아래, 나머지는 다른 위치에 맞게 배열하도록 하는 메소드.
		PlayerNumToWindowNum = new int[PlayerNum]; // 게임중인 player 수만큼 배열 생성.
		switch (PlayerNum) {
		case 2: // player 수가 2 명일 경우
			if (gameProcess.getMyPlayOrder() == 0) {
				// player 자신의 순서가 0 번이면
				PlayerNumToWindowNum[0] = 0; // 자신은 맨 밑 위치로,
				PlayerNumToWindowNum[1] = 2; // 상대방은 맨 위쪽으로 이동.
				playerBoard[0].playerOrder = 0;
				playerBoard[2].playerOrder = 1;

			} else {
				// player 의 play 순서가 1 번이면
				PlayerNumToWindowNum[0] = 2; // 상대방을 위 쪽으로,
				PlayerNumToWindowNum[1] = 0; // 자신을 아래쪽으로 설정.
				playerBoard[2].playerOrder = 0;
				playerBoard[0].playerOrder = 1;
			}
			break;
		case 3: // player 수가 3 명일 경우
			if (gameProcess.getMyPlayOrder() == 0) {
				PlayerNumToWindowNum[0] = 0;
				PlayerNumToWindowNum[1] = 1;
				PlayerNumToWindowNum[2] = 3;
				playerBoard[0].playerOrder = 0;
				playerBoard[1].playerOrder = 1;
				playerBoard[3].playerOrder = 2;
			} else if (gameProcess.getMyPlayOrder() == 1) {
				PlayerNumToWindowNum[0] = 3;
				PlayerNumToWindowNum[1] = 0;
				PlayerNumToWindowNum[2] = 1;
				playerBoard[3].playerOrder = 0;
				playerBoard[0].playerOrder = 1;
				playerBoard[1].playerOrder = 2;
			} else if (gameProcess.getMyPlayOrder() == 2) {
				PlayerNumToWindowNum[0] = 1;
				PlayerNumToWindowNum[1] = 3;
				PlayerNumToWindowNum[2] = 0;
				playerBoard[1].playerOrder = 0;
				playerBoard[3].playerOrder = 1;
				playerBoard[0].playerOrder = 2;
			}
			break;
		case 4: // player 수가 4 명일 경우
			if (gameProcess.getMyPlayOrder() == 0) {
				PlayerNumToWindowNum[0] = 0;
				PlayerNumToWindowNum[1] = 1;
				PlayerNumToWindowNum[2] = 2;
				PlayerNumToWindowNum[3] = 3;
				playerBoard[0].playerOrder = 0;
				playerBoard[1].playerOrder = 1;
				playerBoard[2].playerOrder = 2;
				playerBoard[3].playerOrder = 3;
			} else if (gameProcess.getMyPlayOrder() == 1) {
				PlayerNumToWindowNum[0] = 3;
				PlayerNumToWindowNum[1] = 0;
				PlayerNumToWindowNum[2] = 1;
				PlayerNumToWindowNum[3] = 2;
				playerBoard[3].playerOrder = 0;
				playerBoard[0].playerOrder = 1;
				playerBoard[1].playerOrder = 2;
				playerBoard[2].playerOrder = 3;
			} else if (gameProcess.getMyPlayOrder() == 2) {
				PlayerNumToWindowNum[0] = 2;
				PlayerNumToWindowNum[1] = 3;
				PlayerNumToWindowNum[2] = 0;
				PlayerNumToWindowNum[3] = 1;
				playerBoard[2].playerOrder = 0;
				playerBoard[3].playerOrder = 1;
				playerBoard[0].playerOrder = 2;
				playerBoard[1].playerOrder = 3;
			} else if (gameProcess.getMyPlayOrder() == 3) {
				PlayerNumToWindowNum[0] = 1;
				PlayerNumToWindowNum[1] = 2;
				PlayerNumToWindowNum[2] = 3;
				PlayerNumToWindowNum[3] = 0;
				playerBoard[1].playerOrder = 0;
				playerBoard[2].playerOrder = 1;
				playerBoard[3].playerOrder = 2;
				playerBoard[0].playerOrder = 3;
			}
			break;
		}
	}

	public void RemoveAll() {
		// 게임 윈도우를 모두 보이지 않게 하는 메소드.
		mainPanel.setVisible(false);
		for (int i = 0; i < 4; i++)
			playerBoard[i].m_Panel.setVisible(false);
		Center.m_Panel.setVisible(false);
	}

	public int askNum() { // block 의 숫자를 묻는 대화창을 만들어 숫자를 물어본 뒤 그 수를 반환한다.
		AskDlg AD = new AskDlg(); // 대화상자를 열어 숫자를 선택하고
		return AD.getNum(); // 그 숫자를 반환한다.
	}
}
