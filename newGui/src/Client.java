

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
			String head = data.getHeadData();
			if(head.equals("chat"))
				m_Taget.AddChatString((String)data.getData());
			else if(head.equals("game-playerNum"))
			{
				m_Game.setPlayerNum(((Integer)data.getData()).intValue());
			}
			else if(head.equals("playOrder-int"))
			{
				m_Game.setPlayOrder(((Integer)data.getData()).intValue());
			}
			else if(head.equals("get-Block"))
			{
				m_Game.getDC().getBlocks().remove((Block)data.getData());
			}
			else if(head.equals("ask-int[]"))
			{
				int[] temp = ((int[])data.getData());
				if(m_Game.getPlayOrder() == temp[0])
					m_Game.getDC().getPlayers().get(temp[0]).checkBlock(temp[1], temp[2]);
				m_Taget.AddChatString("System : " + temp[0] + "번 플레" +
						"이어의 " + temp[1] + "번째 Bl" +
								"ock을" + temp[2] + "로 지목" +
										"하였습니다.");		
			}
			else if(head.equals("askOX-boolean"))
			{
				m_Taget.AddChatString("System : " + ((Boolean)data.getData()).booleanValue() + "입니다.");
			}
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

	@Override
	public boolean isServer()
	{
		return false;
	}

}
