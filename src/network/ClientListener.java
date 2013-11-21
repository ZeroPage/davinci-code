package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

//Server 가 Client 의 접속을 기다릴 때 사용할 클래스.
public class ClientListener extends Thread {

	private ServerSocket serverSocket = null;
	boolean listenning = true;
	private Server server;

	public ClientListener(Server server) {
		this.server = server;
	}

	public void startServerSock(int portNum) throws PortNumberException {
		try {
			// 지정된 포트번호로 서버 소켓을 연다.
			serverSocket = new ServerSocket(portNum, Server.MAX_CLIENT);
		} catch (IllegalArgumentException e) {
			throw new PortNumberException(portNum, e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			listenning = false;
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() { // 스레드 생성시 실제 동작시킬 내용을 적는 메소드.
		while (listenning) {
			try {
				Socket socket = serverSocket.accept();
				ClientHandler clientData = new ClientHandler(server, socket);
				server.register(clientData);
				System.out.println("client " + server.getClientNum() + " 접속함.");
			} catch (SocketException e) {
				listenning = false;
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}