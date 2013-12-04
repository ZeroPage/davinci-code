package core;

import gui.AskDlg;
import gui.GameWindow;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import network.Network;
import network.Server;

public class GameProcess {
	private GameWindow gameWindow = null;
	private Network network;
	private int myPlayOrder = 0; // 자신의 플레이 순서.
	private int onlyDraw = 4; // player 들은 block 을 4 개를 먼저 가지고 시작한다.

	private Game game;

	public GameProcess(Network network) {
		// 게임 프로세스 객체에 게임 GUI와 네트워크를 설정.
		this.network = network;
		network.setGameProcess(this);
	}

	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	public void start() {
		if (!network.isServer()) {
			// FIXME GameWindow로 옯길것
			JOptionPane.showMessageDialog(null, "방장이 아닙니다.", "알림", 2);
		}
		Server server = (Server) network;
		server.sendChatMessage("게임을 새로 시작합니다.");
		// server 가 게임을 시작할 때, 자신의 게임 컨트롤를 생성하고, client 들에게 총
		// play 인원과 서버가 생성한 게임 컨트롤을 전달하고 자신부터 게임을 시작하는 메소드.
		System.out.println("[ GameProcess : Start ]");
		int playerNum = server.getClientNum() + 1;

		game = new Game(playerNum);
		server.sendOrder();
		server.sendTotalCount(playerNum);

		server.sendGameData(new GameData(game));

		// TODO Maybe use strategy
		if (game.getPlayers().size() == 4) {
			onlyDraw = 3; // player 수가 4 명일 경우, 3 개만 가지고 게임을 시작.
		}
		gameWindow.setting(getNumOfPlayer());
		turn();
	}

	public void turn() {
		System.out.println("[ GameProcess : turn ]");
		gameWindow.update();
		gameWindow.setEnable(GameWindow.CENTER, true);
		// player 자신의 턴이 되기 전까지는 center block 을 가져올 수 없다.
		network.sendChatMessage("턴 입니다.");
	}

	public void next() {
		// 다음 플레이어에게 턴을 넘겨준다. 게임 윈도우의 모든 입력은 블록 처리 되어 있으므로 자동으로
		// 대기상태가 된다.
		System.out.println("[ GameProcess : Next ]");
		gameWindow.setEnable(GameWindow.OTHERS, false);
		// 현재 player 가 다른 block 들을 클릭하지 못하도록 설정.
		gameWindow.setEnable(GameWindow.CENTER, false);
		// center 의 block 들도 선택하지 못하도록 설정.
		network.sendPass((getMyPlayOrder() + 1) % game.getPlayers().size());
		// 그 후 다음 player 에게 턴을 넘김.
	}

	public void End() {
		// 게임이 끝났을때의 호출 레디버튼을 활성화 해주고 모든 이미지를 안보이게 지정한뒤 승자를 표시해준다.
		network.sendChatMessage("이겼습니다.");
		gameWindow.RemoveAll();
		gameWindow.update();
		game = null;
	}

	public void centerBlockSelete(int blockIndex) {
		// center 에 있는 block 들을 player 에게 옮기는함수.
		System.out.println("[ GameProcess : moveBlock ]");
		// disable center Panel
		gameWindow.setEnable(GameWindow.CENTER, false);

		Player me = game.getPlayer(getMyPlayOrder());

		if (game.getFloorBlocks().get(blockIndex).getNum() == 12) {
			// 선택한 block이 Joker일 경우 diag 대화상자의 버튼 색을 변경하고, joker를 놓을 장소를
			// player에게 물어본다.
			Block target = game.getFloorBlocks().get(blockIndex);
			AskDlg diag = new AskDlg(target.getColor());
			int num = diag.getNum();
			target.setSortingNum(num);
		}

		me.getBlock(game.getFloorBlocks(), blockIndex);
		// 이 block 을 player에게 전달.
		gameWindow.update();
		network.sendGameData(new GameData(game));

		if (me.getHand().size() <= onlyDraw) {
			next(); // 현재가 차례인 player 가 block 을 다 받을 때까지 block 만 가져가고 턴을 계속 돌린다.
		} else {
			// player 가 block 을 다 가져가고나면 상대방의 block 을 추측하기 시작한다.
			// 추측하기 위해 다른 player들의 block 을 선택가능하게 설정한다.
			gameWindow.setEnable(GameWindow.OTHERS, true);
		}
	}

	public ArrayList<Block> GetPlayerBlocksState(int playerNum) {
		return game.getPlayer(playerNum).getHand();
	}// player의 block 들을 반환한다.(상태를 반환하는 것임.)

	public ArrayList<Block> GetCenterBlocksState() {
		return game.getFloorBlocks();
	} // 가운데 블럭들을 리턴한다.(상태를 반환한다고 봐도 타당함)

	public int getNumOfPlayer() {
		return game.getPlayers().size();
	}

	public void AskBlock(int playerOrder, int index, int num) {
		// 해당 player 에게 block 의 숫자를 물어보는 메소드.
		System.out.println("[ GameProcess : AskBlock ]");
		Player me = game.getPlayer(getMyPlayOrder());
		Player target = game.getPlayer(playerOrder);

		me.askBlock(target, this, index, num);
		// 현재 player 가 대상이 되는 Player 에게 block 을 물어봄.
		gameWindow.update();
	}

	public void setPlayOrder(int order) {
		this.myPlayOrder = order;
	}

	public int getPlayOrder() {
		return getMyPlayOrder();
	}

	public void setGameEnv(GameData gameState) {
		// 전달받은 인자에 새로운 block 들의 분배 정보가 들어있어,
		// 그 정보로 현재의 block 게임 컨트롤의 block 상태를 갱신하는 메소드.
		if (game == null) {// 처음 시작할경우
			game = new Game(gameState.getPlayers().size());
		}
		System.out.println("[ GameProcess : setGameEnv(GameData blockState ]");
		game.setFloor(gameState.getFloor()); // center block 들을 새로 설정함.

		for (int i = 0; i < gameState.getPlayers().size(); i++) {
			// 인자로 받은 게임 컨트롤러의 player 수만큼 돌며
			if (i == getMyPlayOrder()) {
				for (int j = 0; j < gameState.getBlocksOfPlayer(i).size(); j++) {
					gameState.getBlocksOfPlayer(i).get(j).setOwn(true);
				}
			} else {
				for (int j = 0; j < gameState.getBlocksOfPlayer(i).size(); j++) {
					gameState.getBlocksOfPlayer(i).get(j).setOwn(false);
				}
			}
			game.getPlayer(i).hand = gameState.getBlocksOfPlayer(i);
		}
		for (int i = 0; i < game.getPlayers().size(); i++)
			game.getPlayer(i).sortBlock();
		gameWindow.update();
	}

	public int getMyPlayOrder() {
		return myPlayOrder;
	}

	public void sendChatMsg(String msg) {
		network.sendChatMessage(msg);
	}

	public boolean isServer() {
		return network.isServer();
	}

	public void updateBlock() {
		gameWindow.update();
		network.sendGameData(new GameData(game));
	}

	public void enableOthers() {
		gameWindow.setEnable(GameWindow.OTHERS, true);
	}

	public void setting(int playerNum) {
		gameWindow.setting(playerNum);
	}

	public void sendGameData() {
		network.sendGameData(new GameData(game));
	}

	public boolean isEnd() {
		return game.isEnd();
	}
}
