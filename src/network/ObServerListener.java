package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import core.Game;
import core.GameData;

class ObServerListener extends Thread // server 로 들어오는 데이터들을 받아 처리하는 클래스.
{
	boolean listenning = true;
	private ObjectInputStream inStream = null;
	private Server server;
	private ClientData clientData;

	// 전달받은 소켓 인자에 input 스트림을 연결.
	public ObServerListener(Socket client, Server server, ClientData clientData) throws IOException {
		this.clientData = clientData;
		this.server = server;
		inStream = new ObjectInputStream(client.getInputStream());
	}

	public void close() {
		try {
			listenning = false;
			inStream.close(); // 객체 input 스트림을 종료.
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.remove(clientData);
	}

	public void dataEvent(DataHeader data) {
		// 서버에게 온 데이터의 처리는 여기서 담당한다.
		// 기본적으로 서버에게 온 데이터는 브로드 캐스팅이 되어 다시 나간다.
		// 브로드 캐스팅을 막으려면 조건문 안에서 return을 시킬것.

		System.out.println("[ Server : dataEvent ]");
		int flag = data.getFlag();
		switch (flag) {
		case DataHeader.CHAT: // 데이터 헤더가 대화 이벤트일 경우.
			server.getMyRoomWnd().AddChatString((String) data.getData());
			break;
		case DataHeader.GAME:
			// if(gameProcess.gameControl == null ||
			// !gameProcess.gameControl.equals((Game)data.getData()))
			server.gameProcess.setGameEnv((Game) data.getData());
			break;
		case DataHeader.GAMEDATA:
			server.gameProcess.setGameEnv((GameData) data.getData());
			break;
		case DataHeader.PASS: // 턴 넘김 메시지를 받고서, client 자신이 플레이할 차례가 되면 턴을
								// 시행한다.
			if (server.gameProcess.getPlayOrder() == ((Integer) data.getData())
					.intValue())
				server.gameProcess.turn();
			break;
		}
		server.sendObject(data);
	}

	public void run() {
		while (listenning) {
			try {
				dataEvent((DataHeader) inStream.readObject());
			} // 계속 대기하며 들어오는 데이터를 처리.
			catch (SocketException e) {
				listenning = false;
			} // 연결이 끊어졌다고 판단될 경우
			catch (EOFException e) {
				listenning = false;
			} // 예상 못한 스트림의 종료시에 발생.
			catch (IOException e) {
				listenning = false;
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				listenning = false;
				e.printStackTrace();
			}
		}
	}
}
