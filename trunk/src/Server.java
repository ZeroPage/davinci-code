
import java.io.*;
import java.net.*;

public class Server extends Thread {
	private ServerSocket server;
	private int clientNum;
	private Socket[] clients;
	private PrintWriter[] outData;
	private SLisener[] inData;
	private DabichiGUI GUI;

	
	Server(int port) throws IOException {
		server = new ServerSocket(port, 3); //3������� ���� ����
		clients = new Socket[3];
		outData = new PrintWriter[3];
		inData = new SLisener[3];
		clientNum = 0;
	}
	public int getClientNum() {
		return clientNum;
	}
	public DabichiGUI getGUI()
	{
		return GUI;
	}
	public void setGUI(DabichiGUI gui)
	{
		GUI = gui;
	}
	public void readClient() throws IOException {
		clients[clientNum] = server.accept();
		inData[clientNum] = new SLisener(clients[clientNum], this);
		inData[clientNum].setGUI(GUI);
		setOutData();
		inData[clientNum].start();
		clientNum++;
	}
	public void setOutData() throws IOException {
		outData[clientNum] = new PrintWriter(clients[clientNum].getOutputStream(), true);
	}
	public void sendData(String data) {
		for(int i=0; i<clientNum; i++)
			outData[i].println(data);
	}
	
	public void run() {
		while(true) {
			try {
				readClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
