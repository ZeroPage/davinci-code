package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import core.Game;
import core.GameData;

class ObServerListener extends Thread {
	// server 로 들어오는 데이터들을 받아 처리하는 클래스.
	boolean listenning = true;
	private ObjectInputStream inStream = null;
	private Server server;

	// 전달받은 소켓 인자에 input 스트림을 연결.
	public ObServerListener(Socket clientSocket, Server server)
			throws IOException {
		this.server = server;
		inStream = new ObjectInputStream(clientSocket.getInputStream());
		this.start();
	}

	public void close() {
		try {
			listenning = false;
			inStream.close(); // 객체 input 스트림을 종료.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dataEvent(DataHeader data) {
		// 서버에게 온 데이터의 처리는 여기서 담당한다.
		// 기본적으로 서버에게 온 데이터는 브로드 캐스팅이 되어 다시 나간다.
		// 브로드 캐스팅을 막으려면 조건문 안에서 return을 시킬것.

		System.out.println("[ Server : dataEvent ]");
		int flag = data.getFlag();
		switch (flag) {
		case DataHeader.CHAT: // 데이터 헤더가 대화 이벤트일 경우.
			server.onChat((String) data.getData());
			break;
		case DataHeader.GAME:
			server.onGameData((Game) data.getData());
			break;
		case DataHeader.GAMEDATA:
			server.onGameData((GameData) data.getData());
			break;
		case DataHeader.PASS:
			server.onPass((Integer) data.getData());
			break;
		}
		
		//TODO 이거 한줄뺴고 똑같음
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
