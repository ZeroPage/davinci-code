package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import core.GameData;

public class Server extends Network {
	ClientData[] clients; // server 에 접속한 client 들을 저장할 변수. client 접속 소켓이
							// 저장되어있다.
	WaitingClient wait; // 서버 소켓이 저장될 변수.

	final int maxClient = 3; // 게임에는 server 까지 최대 4 명만 접속할 수 있다.
	private int clientNum;

	public Server() {
		System.out.println("[ Server : Constructor ]");
		clientNum = 0; // 현재 접속한 client 의 수를 초기화.
		clients = new ClientData[maxClient]; // client 정보를 저장할 메모리 생성.
		wait = new WaitingClient(); // server 가 client 의 접속을 기다리기 시작.
		for (int i = 0; i < maxClient; i++)
			clients[i] = new ClientData(this); // client 자료구조 생성.
	}

	public void Connect(String ip) // 서버 소켓을 열고 client 를 기다리기 시작하는 메소드.
	{
		// ip는 상속되는 코드와의 호환을 위해. 의미없음.
		// 이 클래스는 서버 이므로 접속을 하지 않고 서버를 생성.
		wait.setServSock(portNum, maxClient); // server 의 소켓을 열고 클라이언트를 기다림.
		wait.start(); // server socket 스레드 시작.
	}

	public void SendChatMsg(String msg) {
		DataHeader temp = new DataHeader(DataHeader.CHAT, playerNickname
				+ " : " + msg);
		sendObject(temp);

		getMyRoomWnd().AddChatString(playerNickname + " : " + msg);
	}
	
	@Override
	public void sendGameData(GameData gameData) {
		DataHeader data = new DataHeader(DataHeader.GAMEDATA, gameData);
		sendObject(data);
	}

	@Override
	public void sendObject(Object object) // 전달받은 객체를 server 에 접속한 모든 객체에 전달하는 메소드.
	{
		System.out.println("[ Server : SendOb ]");
		for (int i = 0; i < clientNum; i++) {
			try {
				clients[i].SendObject(object);
			} // client 에게 객체를 전달한다.
			catch (SocketException e) {
				System.out.println("client " + i + " 번 에서 반응이 없습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void Close() {
		SendChatMsg("서버가 종료되었습니다.");
		wait.listenning = false;
		for (int i = 0; i < clientNum; i++) {
			if (clients[i].getClientSocket().isConnected()) {
				try {
					clients[i].close();
				} catch (SocketException e) {
					System.out.println("client " + i + " 번은 이미 연결이 종료되었습니다.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		wait.close();
	}

	public int getClientNum() {
		return clientNum;
	}

	// Server 가 Client 의 접속을 기다릴 때 사용할 클래스.
	class WaitingClient extends Thread {

		ServerSocket servSock = null;
		boolean listenning = true;

		// 서버 소켓을 portNum 과결합시킨다.
		public void setServSock(int portNum, int maxClient) {
			try {
				// 지정된 포트번호로 서버 소켓을 연다.
				servSock = new ServerSocket(portNum, maxClient);
			} catch (IllegalArgumentException e) {
				JOptionPane
						.showMessageDialog(
								null,
								"입력하신 Port number : "
										+ portNum
										+ "\nPort 는 1 ~ 65535 사이의 값이어야 합니다.\n기본값 10000 번 포트로 연결합니다.",
								"Port number 경고", JOptionPane.OK_OPTION);
				setServSock(10000, maxClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void close() {
			try {
				listenning = false;
				servSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() { // 스레드 생성시 실제 동작시킬 내용을 적는 메소드.
			int current = 0;
			while (listenning) { // 서버 소켓은 클라이언트가 접속할 때까지 계속 기다려야 하기 때문에 while(
									// true ).
				if (current <= maxClient) {
					if (clients[current].getClientSocket().isConnected()) {
						System.out.println("client " + current + " 접속함.");
						current++;
					}
				}

				try {
					clients[current].setClientData(servSock.accept());
				} catch (SocketException e) {
					listenning = false;
					close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				clientNum++; // client 를 연결시키고 난 후 전체 client 수를 하나 증가.
			}
		}
	}

	public void SendOrder() // player 들에게 자신들의 게임 순서를 전송한다.
	{
		System.out.println("[ Server : SendOrder ]");
		for (int i = 0; i < clientNum; i++)
			try {
				clients[i].SendObject(new DataHeader(DataHeader.MYORDER, Integer
						.valueOf(i + 1)));
			} catch (IOException e) {
				e.printStackTrace();
				System.out
						.println("Class : Server\t :: SendOrder() : IOException");
			}
	}

	public boolean isServer() {
		return true;
	}

	public void increaseClient() {
		clientNum++;
	}

	public void decreaseClient() {
		clientNum--;
	}
}
