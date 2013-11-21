package network;

import core.Game;
import core.GameData;
import core.GameProcess;
import gui.RoomWindow;

//서버와 클라이언트가 각각 서버소켓과 소켓을 사용하여 서로 통신할 때,
//소켓을 통해 output 스트림과 input 스트림을 연결해놓으면,
//서로가 데이터를 주고받을 때 사용할 메소드가 매우 간편하기 때문에
//일단 소켓을 연결한 후 input/output 스트림이 연결되어있다면
//서버와 클라이언트가 서로 통신하도록 하는데 매우 쉽다.
abstract public class Network {
	private RoomWindow roomWnd; // 채팅 메세지를 받으면 전달할 RoomWindow.
	protected Network network;
	protected GameProcess gameProcess; // 해당 네트워크에서 진행중인 게임 프로세스.
	protected String playerNickname; // 해당 네트워크를 연 player 의 이름
	protected int portNum = 10000; // 해당 네트워크가 접속되어있는 서버의 포트.

	public void setTaget(RoomWindow target) {
		setMyRoomWnd(target);
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public void setGameProcess(GameProcess game) {
		gameProcess = game;
	}

	public void setName(String name) {
		playerNickname = name;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	abstract public boolean isServer(); // 서버인지 아닌지 리턴.

	abstract public void Connect(String ip); // 지정된 ip로 접속하는 함수

	abstract public void sendObject(Object ob); // 접속된 네트워크에 채팅 오브젝트를 날리는 함수

	abstract public void Close(); // 모든접속을 끊는 함수. 쓰레드 종료필수

	public RoomWindow getMyRoomWnd() {
		return roomWnd;
	}

	public void setMyRoomWnd(RoomWindow myRoomWnd) {
		this.roomWnd = myRoomWnd;
	}
	
	public void sendChatMessage(String msg) {
		// 접속된 네트워크에 채팅 msg를 날리는 함수
		DataHeader temp = new DataHeader(DataHeader.CHAT, playerNickname
				+ " : " + msg);
		sendObject(temp);
	}

	public void sendGameData(GameData gameData) {
		DataHeader data = new DataHeader(DataHeader.GAMEDATA, gameData);
		sendObject(data);
	}

	public void sendPass(int nextPlayer) {
		DataHeader data = new DataHeader(DataHeader.PASS, nextPlayer);
		sendObject(data);
	}

	public void onGameData(GameData gameData) {
		gameProcess.setGameEnv(gameData);
	}

	public void onGameData(Game data) {
		gameProcess.setGameEnv(data);
	}

	public void onChat(String message) {
		getMyRoomWnd().AddChatString(message);
	}

	public void onPass(int order) {
		// 턴 넘김 메시지를 받고서, 자신이 플레이할 차례가 되면 턴을 시행한다.
		if (gameProcess.getPlayOrder() == order)
			gameProcess.turn();
	}
}
