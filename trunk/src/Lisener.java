
import java.io.*;
import java.net.*;

public class Lisener extends Thread {
	private Socket connection;
	private BufferedReader inMessage;
	private DabichiGUI GUI;

	public Lisener() {
	}
	public Lisener(Socket com) throws IOException 
	{
		setConnection(com);
		setInMessage();
	}
	public void setConnection(Socket connection) 
	{
		this.connection = connection;
	}
	public Socket getConnection()
	{
		return connection;
	}
	public BufferedReader getInMessage() {
		return inMessage;
	}
	public void setInMessage() throws IOException {
		inMessage = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}
	public void setGUI(DabichiGUI gui)
	{
		GUI = gui;
	} 
	public DabichiGUI getGUI()
	{
		return GUI;
	}
	public String readInMessage() {
		try {
			return inMessage.readLine();
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void printMessage() throws IOException {
		String temp = "";
		while(true) {
			if(!connection.isConnected() || temp.equals("exit")) {
				close();
				GUI.ChatLisener("접속이 종료되었습니다.");
				//System.out.println(temp);
				return;
			}
			temp = readInMessage();
			if(temp != null)
				//System.out.println(temp);
				GUI.ChatLisener(temp);
		}
	}
	public void close() throws IOException {
		inMessage.close();
		connection.close();
	}
	public void run() {
		try {
			printMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
