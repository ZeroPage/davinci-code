

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
		SendOb(temp);
	}
	public void SendOb(Object ob)
	{
		try
		{
			outOb.writeObject(ob);
			outOb.flush();
			outOb.flush();
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
		public void close() {
			stop();
			try
			{
				inOb.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_Taget.AddChatString("������ ���������ϴ�.");
		}
		public void dataEvent(DataHeader data)
		{
			//�Էµ� �������� ó���� ���⿡ �߰��Ұ�.
			String head = data.getHeadData();
			if(head.equals("chat"))
				m_Taget.AddChatString((String)data.getData());
			if(head.equals("game"))
				//if(m_Game.GC == null || !m_Game.GC.equals((Game)data.getData()))
					m_Game.setGC((Game)data.getData());
			if(head.equals("game2"))
					m_Game.setGC((GameData)data.getData());
			if(head.equals("pass"))
				if(m_Game.getPlayOrder() == ((Integer)data.getData()).intValue())
					m_Game.turn();
			if(head.equals("����-�׼���"))
				m_Game.setPlayOrder(((Integer)data.getData()).intValue());
			if(head.equals("���ο���"))
				m_Game.m_GUITaget.Setting(((Integer)data.getData()).intValue());
		}
		public void run()
		{
			while(connection.isConnected())
			{
				//�Է� ������ ���� �ʿ�. 
				try
				{
					dataEvent((DataHeader)inOb.readObject());
				} catch (IOException e)
				{
					close();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e)
				{
					close();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isServer()
	{
		return false;
	}

}
