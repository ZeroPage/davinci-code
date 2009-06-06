

import java.io.*;
import java.net.*;

public class Client extends Network
{
	Socket connection;
	ObjectOutputStream outOb;
	ObListener Oblisten;

	public void setOutOb() throws IOException
	{
		outOb = new ObjectOutputStream(connection.getOutputStream());
	}
	public void setListen() throws IOException
	{
		Oblisten = new ObListener();
		Oblisten.setInOb();
		Oblisten.start();
	}
	public void Connect(String ip)
	{
		try
		{
			connection = new Socket(ip, 10000);
			setOutOb();
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
		DataHeader temp = new DataHeader();
		temp.setHeadData("chat");
		temp.setData(m_Name + " : " + msg);
		SendOb(0, temp);
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
		try
		{
			Oblisten.close();
			outOb.close();
			connection.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class ObListener extends Thread
	{
		ObjectInputStream inOb;

		public void setInOb() throws IOException {
			inOb = new ObjectInputStream(connection.getInputStream());
		}
		public void close() throws IOException {
			inOb.close();
		}
		public void dataEvent(DataHeader data)
		{
			if(data.getHeadData().equals("chat"))
				m_Taget.AddChatString((String)data.getData());
		}
		public void run()
		{
			while(connection.isConnected())
			{
				//입력 데이터 조건 필요. 
				try
				{
					dataEvent((DataHeader)inOb.readObject());
					
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
