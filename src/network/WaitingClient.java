package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

//Server 가 Client 의 접속을 기다릴 때 사용할 클래스.
public class WaitingClient extends Thread {

	private ServerSocket servSock = null;
	boolean listenning = true;
	private Server server;

	public WaitingClient(Server server) {
		this.server = server;
	}

	// 서버 소켓을 portNum과 결합시킨다.
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
		while (listenning) {
			try {
				Socket socket = servSock.accept();
				ClientData clientData = new ClientData(server, socket);
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