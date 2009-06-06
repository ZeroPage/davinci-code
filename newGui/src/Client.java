
import java.io.*;
import java.net.*;

public class Client extends Network
{
	Socket server, Observer;
	PrintWriter outMsg;
	ObjectOutputStream outOb;
	Listener listen;
	ObListener Oblisten;
	
	public void setOutMsg() throws IOException
	{
		outMsg = new PrintWriter(server.getOutputStream(), true);
	}
	public void setOutOb() throws IOException
	{
		outOb = new ObjectOutputStream(Observer.getOutputStream());
	}
	public void setListen() throws IOException
	{
		listen = new Listener();
		listen.setInMsg();
		listen.start();
	}
	public void setOblisten() throws IOException
	{
		Oblisten = new ObListener();
		Oblisten.setInOb();
		Oblisten.start();
	}
	public void Connect(String ip)
	{
		try
		{
			server = new Socket(ip, portNum);
			Observer = new Socket(ip, portNum);
			setOutMsg();
			setOutOb();
			setListen();
			setOblisten();
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
		SendData(m_Name + " : " + msg);
	}
	public void SendData(String data)
	{
		outMsg.println(data);
	}
	public void SendOb(int sel, Object ob)
	{
		try
		{
			outOb.writeObject(ob);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Close()
	{
		SendChatMsg(m_Name + "님이 연결을 종료하셨습니다.");
		try
		{
			listen.close();
			outMsg.close();
			server.close();
			Oblisten.close();
			outOb.close();
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
				//입력 데이터 조건 필요. 
				m_Taget.AddChatString(inMsg.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
	class ObListener extends Thread
	{
		ObjectInputStream inOb;

		public void setInOb() throws IOException {
			inOb = new ObjectInputStream(server.getInputStream());
		}
		public void close() throws IOException {
			inOb.close();
		}

		public void run()
		{
			Object temp = null;
			while(server.isConnected())
			{
				//입력 데이터 조건 필요. 
				try
				{
					temp = inOb.readObject();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
