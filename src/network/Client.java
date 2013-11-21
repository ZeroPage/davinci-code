package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

//import sun.java2d.Disposer;

public class Client extends Network {

	private Socket clientSocket;
	private ObjectOutputStream outStream; // 서버로의 객체 output 스트림을 연결할 변수.
	private NetworkListener reciver; // client 가 server 로부터 들어오는 데이터를 계속 받을 수 있는
								// input 스트림을

	// 연결할 변수.

	public void Connect(String ip) {
		// 주어진 IP 와 portNum 멤버변수를 사용하여 서버에 접속하는 메소드.
		try {
			clientSocket = new Socket(ip, portNum); // 서버 소켓에 연결.
			setOutstream(); // output 스트림을 설정하여 서버에 데이터를 전송할 수 있게 준비한다.
			setListen(); // input 스트림을 설정하여 서버에서 데이터를 수신할 수 있도록 준비한다.
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(null, "IP\t: " + ip + "\nPort\t: "
					+ portNum + "\n호스트 또는 port가 올바르지 않습니다.\n\n게임을 종료합니다.");
			System.exit(0);
			// TODO 종료 대신 다른 방법으로 ip 와 port 를 새로 받아 접속할 수 있도록 해야 함.
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void setOutstream() throws IOException {
		// server 로의 output 스트림을 연결하는 메소드.
		outStream = new ObjectOutputStream(clientSocket.getOutputStream());
	}

	public void setListen() throws IOException {
		// input 스트림으로 데이터를 읽기 시작하도록 하는 메소드.
		reciver = new NetworkListener(clientSocket, this); // input 스트림을 계속 읽을 스레드 객체
														// 메모리 생성.
		
	}

	public void sendObject(Object ob) {
		// 인자로 받은 객체를 서버로 전송하는 메소드.
		System.out.println("[ Client : SendOb ]");
		try {
			outStream.writeObject(ob); // 객체 output 스트림에 객체를 실어 보냄.
			outStream.flush(); // 객체 output 스트림을 깨끗이 치운다.
		} catch (SocketException e) {
			System.out.println("Server 와의 연결이 종료되었습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Close() {
		// client 와 server 사이의 연결을 모두 종료하는 메소드.
		// client network 이 종료되려면,
		// 1. client 가 종료된다는 메시지를 서버에 전달.
		// 2. 종료 데이터를 서버에 전달(DataHeader)
		// 3. 소켓을 통한 input/output 스트림을 종료.
		// 4. 소켓 연결 종료.
		// 5. client 의 프로그램 종료.
		// 위 5 개의 순서대로 프로그램을 종료해서 server 에 연결된 다른 client 들의 상태에
		// 영향을 주지 않고, 게임이 진행되거나 종료되도록 해야 한다.
		try {
			sendChatMessage("게임에서 나갑니다.");
			reciver.close();
			outStream.close(); // 객체 output 스트림 종료.
			clientSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isServer() {
		return false;
	}
}
