
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
		listen.start();
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
		outMsg.println(m_Name + " : " + msg);

	}
	public void Close()
	{
		SendChatMsg(m_Name + "���� ������ �����ϼ̽��ϴ�.");
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
		}

		public void run()
		{
			while(server.isConnected())
			{	try {
				//�Է� ������ ���� �ʿ�. 
				m_Taget.AddChatString(inMsg.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
}
