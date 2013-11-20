package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientData // server 가 관리할 client 들의 소켓정보와 input/output 정보를
{
	private Socket clientSocket = null;
	private ObjectOutputStream outStream = null;
	private ObServerListener inOb = null;
	private Server server;

	public ClientData(Server server, Socket socket) throws IOException { // 임의의 소켓 메모리 생성.
		this.server = server;
		clientSocket = new Socket();
		clientSocket = socket; // 인자로 전달받은 client 의 소켓을 저장.
		outStream = new ObjectOutputStream(socket.getOutputStream());
		// output이 client 에게 전송됨.
		inOb = new ObServerListener(socket, server); // client 에게서 전송받음.
	}

	public boolean isConnected(){
		return clientSocket.isConnected();
	}

	public void sendObject(Object object) throws IOException {
		// client 에게 데이터를 전송하는 메소드.
		outStream.writeObject(object);
		outStream.flush();
	}

	public void close() throws IOException {
		// client와의 연결을 끊는 메소드.
		outStream.close();
		inOb.close();
		clientSocket.close();
		server.remove(this);
	}
}
