

import java.io.*;
import java.net.*;

public class Client {
	private Lisener inData;
	private PrintWriter outData;
	private String myName;

	public Lisener getLisener()
	{
		return inData;
	}
	public Lisener getInData() {
		return inData;
	}
	public void setInData(Lisener inData) {
		this.inData = inData;
	}
	public void setName(String name) {
		this.myName = name;
	}
	public void connectServer(Socket server) throws IOException {
		inData = new Lisener(server);
		outData = new PrintWriter(server.getOutputStream(), true);
		sendData("접속하였습니다.");
		inData.start();
	}
	public void sendData(String data) {
		outData.println(myName + " : " + data);
	}
}
