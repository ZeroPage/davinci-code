import java.io.*;
import java.net.*;

public class Server extends Network
{
	ClientData[] clients;
	WaitingClient wait;

	int maxClient, clientNum;

	public Server()
	{
		maxClient = 3;
		clientNum= 0;
		clients = new ClientData[maxClient];
		for(int i=0; i<maxClient; i++)
			clients[i] = new ClientData();
		wait = new WaitingClient();
	}

	public void Connect(String ip)
	{
		//ip는 상속되는  코드와의 호환을 위해. 의미없음.
		//이 클래스는 서버 이므로 접속을 하지 않고 서버를 생성.
		wait.setObServer(portNum, maxClient);
		wait.start();
	}
	public void SendChatMsg(String msg)
	{
		DataHeader temp = new DataHeader();
		temp.setHeadData("chat");
		temp.setData(m_Name + " : " + msg);
		BroadCasting(temp);
		m_Taget.AddChatString(m_Name + " : " + msg);
	}
	public void SendOb(Object ob)
	{
		BroadCasting(ob);
	}
	public void Close()
	{
		wait.stop();
		for(int i=0; i<clientNum; i++)
		{
			if(clients[i].getConnect().isConnected())
			{
				try
				{
					clients[i].close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void BroadCasting(Object ob)
	{
		for(int i=0; i<maxClient; i++)
		{
			if(clients[i].getConnect().isConnected())
				try
			{
					clients[i].SendOb(ob);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	class ClientData
	{
		Socket connect;
		ObjectOutputStream outOb;
		ObServerListener inOb;
		public ClientData()
		{
			connect = new Socket();
		}
		public void setClientData(Socket client) throws IOException
		{
			connect = client;
			outOb = new ObjectOutputStream(connect.getOutputStream());
			inOb = new ObServerListener(connect);
			inOb.start();
		}
		public Socket getConnect()
		{
			return connect;
		}
		public void SendOb(Object ob) throws IOException
		{
			outOb.writeObject(ob);
			outOb.flush();
			outOb.flush();
		}
		public void close() throws IOException
		{
			outOb.close();
			inOb.close();
		}
	}
	class WaitingClient extends Thread
	{
		ServerSocket ObServer;
		public void setObServer(int portNum, int maxClient)
		{
			try
			{
				ObServer = new ServerSocket(portNum, maxClient);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void run()
		{
			while(true)
			{
				for(int i=0; i<maxClient; i++)
				{
					if(!clients[i].getConnect().isConnected())
					{
						try
						{
							clients[i].setClientData(ObServer.accept());
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						clientNum++;
					}
				}
			}
		}
	}
	class ObServerListener extends Thread
	{
		Socket client;
		ObjectInputStream inOb;

		public ObServerListener(Socket client) throws IOException
		{
			this.client = client;
			inOb = new ObjectInputStream(client.getInputStream());
		}
		public void close()
		{
			stop();
			try
			{
				inOb.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SendChatMsg("접속이 끉어졌습니다.");
			clientNum--;
		}
		public void dataEvent(DataHeader data)
		{
			//서버에게 온 데이터의 처리는 여기서 담당한다.
			// 기본적으로 서버에게 온 데이터는 브로드 캐스팅이 되어 다시 나간다.
			// 브로드 캐스팅을 막으려면 조건문 안에서 return을 시킬것.
			String head = data.getHeadData();
			if(head.equals("chat"))
				m_Taget.AddChatString((String)data.getData());
			if(head.equals("game"))
			{
				//if(m_Game.GC == null || !m_Game.GC.equals((Game)data.getData()))
					m_Game.setGC((Game)data.getData());
					//return;
			}
			if(head.equals("game2"))
				m_Game.setGC((GameData)data.getData());
			if(head.equals("pass"))
				if(m_Game.getPlayOrder() == ((Integer)data.getData()).intValue())
					m_Game.turn();
			BroadCasting(data);
		}
		public void run()
		{
			while(client.isConnected())
			{
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
	public boolean isServer()
	{
		return true;
	}
	public void SendOrder()
	{
		int temp = 1;
		for(int i=0; i<clientNum; i++)
			if(clients[i].getConnect().isConnected())
			{
				try
				{
					clients[i].SendOb(new DataHeader("서버-네순서", Integer.valueOf(temp)));
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				temp++;
			}
	}
}