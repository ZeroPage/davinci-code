package network;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends Network {
	private ArrayList<ClientHandler> clients;
	ClientListener wait; // 서버 소켓이 저장될 변수.

	final static int MAX_CLIENT = 3; // 게임에는 server 까지 최대 4 명만 접속할 수 있다.

	public Server() {
		System.out.println("[ Server : Constructor ]");
		clients = new ArrayList<ClientHandler>(); // client 정보를 저장할 메모리 생성.
		wait = new ClientListener(this); // server 가 client 의 접속을 기다리기 시작.
	}
	public void listen() throws PortNumberException {
		wait.startServerSock(portNum); // server 의 소켓을 열고 클라이언트를 기다림.
		wait.start(); // server socket 스레드 시작.		
	}
	
	@Override
	public void sendChatMessage(String msg) {
		super.sendChatMessage(msg);
		chatWindow.chatMessage(msg);
	}
	
	@Override
	public void sendObject(Object object) // 전달받은 객체를 server 에 접속한 모든 객체에 전달하는 메소드.
	{
		System.out.println("[ Server : SendOb ]");
		for (int i = 0; i < clients.size(); i++) {
			try {
				clients.get(i).sendObject(object);
			} // client 에게 객체를 전달한다.
			catch (SocketException e) {
				System.out.println("client " + i + " 번 에서 반응이 없습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void Close() {
		sendChatMessage("서버가 종료되었습니다.");
		wait.listenning = false;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).isConnected()) {
				try {
					clients.get(i).close();
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
		return clients.size();
	}

	public void sendOrder() // player 들에게 자신들의 게임 순서를 전송한다.
	{
		System.out.println("[ Server : SendOrder ]");
		for (int i = 0; i < clients.size(); i++)
			try {
				clients.get(i).sendObject(new DataHeader(DataHeader.MYORDER, Integer
						.valueOf(i + 1)));
			} catch (IOException e) {
				e.printStackTrace();
				System.out
						.println("Class : Server\t :: SendOrder() : IOException");
			}
	}
	public void remove(ClientHandler clientData) {
		clients.remove(clientData);
	}
	public void register(ClientHandler clientData){
		clients.add(clientData);
	}

	public boolean isServer() {
		return true;
	}
	
	@Override
	public void dataEvent(DataHeader data) {
		super.dataEvent(data);
		this.sendObject(data);
	}

	
}
