package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientData // server 가 관리할 client 들의 소켓정보와 input/output 정보를
{
	private Socket clientSocket = null;
	private ObjectOutputStream outOb = null;
	private ObServerListener inOb = null;
	private Server server;

	public ClientData(Server server) { // 임의의 소켓 메모리 생성.
		clientSocket = new Socket();
		this.server = server;
	}

	public void setClientData(Socket accepted) throws IOException {
		// client 와 맺은 새로운 소캣을 인자로 받아 input/output 스트림을 열고
		// 그 소켓을 저장해두는 메소드.
		clientSocket = accepted; // 인자로 전달받은 client 의 소켓을 저장.
		outOb = new ObjectOutputStream(clientSocket.getOutputStream());
		// output이 client 에게 전송됨.
		inOb = new ObServerListener(clientSocket, server); // client 에게서 전송받음.
		inOb.start();
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void SendObject(Object object) throws IOException {
		// client 에게 데이터를 전송하는 메소드.
		outOb.writeObject(object);
		outOb.flush();
	}

	public void close() throws IOException {
		// client와의 연결을 끊는 메소드.
		outOb.close();
		inOb.close();
		clientSocket.close();
	}
}
