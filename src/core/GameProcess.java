package core;

import gui.AskDlg;
import gui.GameWindow;

import java.util.ArrayList;

import network.DataHeader;
import network.Network;
import network.Server;

public class GameProcess {
	private GameWindow gameWnd;
	private Network network;
	private int myPlayOrder = 0; // 자신의 플레이 순서.
	int onlyDraw = 4; // player 들은 block 을 4 개를 먼저 가지고 시작한다.

	Game gameEnv;

	public GameProcess(GameWindow gameWnd, Network network) {
		// 게임 프로세스 객체에 게임 GUI와 네트워크를 설정.
		this.gameWnd = gameWnd; // 프로세스를 생성한 game window를 가리키기위한 변수.
		this.network = network;
	}

	public void Start() { // server 가 게임을 시작할 때, 자신의 게임 컨트롤를 생성하고, client 들에게 총
							// play 인원과 서버가 생성한 게임 컨트롤을 전달하고 자신부터 게임을 시작하는 메소드.
		System.out.println("[ GameProcess : Start ]");
		gameEnv = new Game(((Server) getNetObject()).getClientNum() + 1);
		((Server) getNetObject()).SendOrder();
		getNetObject().sendObject(
				new DataHeader(DataHeader.TOTALCOUNT, gameEnv.getPlayers()
						.size()));
		getNetObject().sendObject(new DataHeader(DataHeader.GAME, gameEnv));
		// 모든 client 들에게 server가 생성한 게임 컨트롤을 전달한다.

		if (gameEnv.getPlayers().size() == 4)
			onlyDraw = 3; // player 수가 4 명일 경우, 3 개만 가지고 게임을 시작.
	}

	public void turn() {
		System.out.println("[ GameProcess : turn ]");
		getGameWndGUI().update();
		getGameWndGUI().setEnable(GameWindow.CENTER, true);
		// player 자신의 턴이 되기 전까지는 center block 을 가져올 수 없다.
		getNetObject().SendChatMsg("턴 입니다.");
	}

	public void Next() {
		// 다음 플레이어에게 턴을 넘겨준다. 게임 윈도우의 모든 입력은 블록 처리 되어 있으므로 자동으로
		// 대기상태가 된다.
		System.out.println("[ GameProcess : Next ]");
		getGameWndGUI().setEnable(GameWindow.OTHERS, false);
		// 현재 player 가 다른 block 들을 클릭하지 못하도록 설정.
		getGameWndGUI().setEnable(GameWindow.CENTER, false);
		// center 의 block 들도 선택하지 못하도록 설정.
		getNetObject().sendObject(
				new DataHeader(DataHeader.PASS, ((Integer
						.valueOf((getMyPlayOrder() + 1)) % (gameEnv
						.getPlayers().size())))));
		// 그 후 다음 player 에게 턴을 넘김.
	}

	public void End() {
		// 게임이 끝났을때의 호출 레디버튼을 활성화 해주고 모든 이미지를 안보이게 지정한뒤 승자를 표시해준다.
		getNetObject().SendChatMsg("이겼습니다.");
		getGameWndGUI().RemoveAll();
		getGameWndGUI().update();
		gameEnv = null;
	}

	public void moveBlock(int blockIndex) {
		// center 에 있는 block 들을 player 에게 옮기는함수.
		System.out.println("[ GameProcess : moveBlock ]");
		Player me = gameEnv.getPlayer(getMyPlayOrder());

		if (gameEnv.getFloorBlocks().get(blockIndex).getNum() == 12) {
			// 선택한 block이 Joker일 경우 diag 대화상자의 버튼 색을 변경하고, joker를 놓을 장소를
			// player에게 물어본다.
			Block target = gameEnv.getFloorBlocks().get(blockIndex);
			AskDlg diag = new AskDlg(target.getColor());
			int num = diag.getNum();
			target.setSortingNum(num);
		}

		me.getBlock(gameEnv.getFloorBlocks(), blockIndex);
		// 이 block 을 player에게 전달.
		getGameWndGUI().update();
		getNetObject().sendGameData(new GameData(gameEnv));

		if (me.getHand().size() <= onlyDraw) {
			Next(); // 현재가 차례인 player 가 block 을 다 받을 때까지 block 만 가져가고 턴을 계속 돌린다.
		} else {
			// player 가 block 을 다 가져가고나면 상대방의 block 을 추측하기 시작한다.
			getGameWndGUI().setEnable(GameWindow.OTHERS, true);
			// 추측하기 위해 다른 player들의 block 을 선택가능하게 설정한다.
		}
	}

	public ArrayList<Block> GetPlayerBlocksState(int playerNum) {
		return gameEnv.getPlayer(playerNum).getHand();
	}// player의 block 들을 반환한다.(상태를 반환하는 것임.)

	public ArrayList<Block> GetCenterBlocksState() {
		return gameEnv.getFloorBlocks();
	} // 가운데 블럭들을 리턴한다.(상태를 반환한다고 봐도 타당함)

	public int getNumOfPlayer() {
		return gameEnv.getPlayers().size();
	} // play 중인 player 들의 수를 반환한다.

	public void AskBlock(int playerOrder, int index, int num) {
		// 해당 player 에게 block 의 숫자를 물어보는 메소드.
		System.out.println("[ GameProcess : AskBlock ]");
		Player me = gameEnv.getPlayer(getMyPlayOrder());
		Player target = gameEnv.getPlayer(playerOrder);

		me.askBlock(target, this, index, num); // 현재 player 가 대상이 되는 Player 에게
												// block 을 물어봄.
		getGameWndGUI().update();
	}

	public void setGameEnv(Game gc) {
		System.out.println("[ GameProcess : setGameEnv(Game gc) ]");
		gameEnv = gc;
		getGameWndGUI().update();
	}

	public void setPlayOrder(int order) {
		setMyPlayOrder(order);
	}

	public int getPlayOrder() {
		return getMyPlayOrder();
	}

	public Game getGameEnv() {
		return gameEnv;
	}

	public void setGameEnv(GameData newBlockState) {
		// 전달받은 인자에 새로운 block 들의 분배 정보가 들어있어, 그 정보로 현재의 block 게임 컨트롤의 block 상태를
		// 갱신하는 메소드.
		System.out
				.println("[ GameProcess : setGameEnv(GameData newBlockState ]");
		gameEnv.setFloor(newBlockState.getFloor()); // center block 들을 새로 설정함.

		for (int i = 0; i < newBlockState.getPlayers().size(); i++) {
			// 인자로 받은 게임 컨트롤러의 player 수만큼 돌며
			if (i == getMyPlayOrder()) {
				for (int j = 0; j < newBlockState.getBlocksOfPlayer(i).size(); j++) {
					newBlockState.getBlocksOfPlayer(i).get(j).setOwn(true);
				}
			} else {
				for (int j = 0; j < newBlockState.getBlocksOfPlayer(i).size(); j++) {
					newBlockState.getBlocksOfPlayer(i).get(j).setOwn(false);
				}
			}
			gameEnv.getPlayer(i).hand = newBlockState.getBlocksOfPlayer(i);
		}
		for (int i = 0; i < gameEnv.getPlayers().size(); i++)
			gameEnv.getPlayer(i).sortBlock();
		getGameWndGUI().update();
	}

	public int getMyPlayOrder() {
		return myPlayOrder;
	}

	public void setMyPlayOrder(int myPlayOrder) {
		this.myPlayOrder = myPlayOrder;
	}

	public Network getNetObject() {
		return network;
	}

	public GameWindow getGameWndGUI() {
		return gameWnd;
	}
}
