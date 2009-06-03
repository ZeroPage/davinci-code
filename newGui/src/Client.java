
import java.io.*;
import java.net.*;

public class Client extends Network
{
	Socket server;
	PrintWriter outMsg;
	Listener listen;

	public void setOutMsg() throws IOException
	{
		outMsg = new PrintWriter(server.getOutputStream(), true);
	}
	public void setListen() throws IOException
	{
		listen = new Listener();
		listen.setInMsg();
		listen.run();
	}
	public void Connect(String ip)
	{
		try
		{
			server = new Socket(ip, 10000);
			setOutMsg();
			setListen();
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SendChatMsg(String msg)
	{
		outMsg.println(myName + " : " + msg);

	}
	public void Close()
	{
		SendChatMsg(myName + "님이 연결을 종료하셨습니다.");
		try
		{
			listen.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class Listener extends Thread
	{
		BufferedReader inMsg;

		public void setInMsg() throws IOException {
			inMsg = new BufferedReader(new InputStreamReader(server.getInputStream()));
		}

		public void close() throws IOException {
			inMsg.close();
			server.close();
		}

		public void run() {
			while(server.isConnected())
			{
				try {
					m_Taget.AddChatString(inMsg.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
