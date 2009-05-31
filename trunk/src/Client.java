

import java.io.*;
import java.net.*;

public class Client {
	Lisener inData;
	private PrintWriter outData;
	Socket server;

	public void connectServer(Socket server) throws IOException {
		this.server = server;
		inData = new Lisener(server);
		outData = new PrintWriter(server.getOutputStream(), true);
		inData.start();
	}
	public Lisener getLisener()
	{
		return inData;
	}
	public void sendData(String data) {
		outData.println(data);
	}
}
