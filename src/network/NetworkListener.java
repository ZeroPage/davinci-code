package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

class NetworkListener extends Thread {
	// server 로부터 들어오는 input스트림을 받아 알맞은 동작을 하도록
	// 하는 클래스.
	ObjectInputStream inputStream;
	boolean isListening = true;
	Network network;

	public NetworkListener(Socket clientSocket, Network server) throws IOException {
		this.network = server;
		inputStream = new ObjectInputStream(clientSocket.getInputStream());
		this.start();
	}

	public void close() {
		try {
			isListening = false;
			inputStream.close(); // input 스트림 종료.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (isListening) {
			// 입력 데이터 조건 필요.
			try {
				network.dataEvent((DataHeader) inputStream.readObject());
			} catch (EOFException e) { // 서버로부터의 스트림이 갑자기 끊기는 경우
				JOptionPane.showMessageDialog(null,
						"서버로부터 연결이 종료되었습니다.\n연결을 위해서는 프로그램을 재시작해주시기 바랍니다.",
						"서버 연결 종료", JOptionPane.OK_OPTION);
				isListening = false;
			} catch (SocketException e) {
				System.out.println("Server 가 연결되어있지 않습니다.");
				isListening = false;
			} catch (IOException e) {
				isListening = false;
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// close();
				e.printStackTrace();
			}
		}
	}
}