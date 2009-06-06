import java.io.*;
import java.net.*;


public class Server extends Network
{
	ClientData[] clients;
	WaitingClient wait;

	int portNum, maxClient, clientNum;

	public Server()
	{
		portNum = 10000;
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
		wait.setServer(portNum, maxClient);
		wait.start();
	}
	public void SendChatMsg(String msg)
	{
		BroadCasting(m_Name + " : " + msg);
		m_Taget.AddChatString(m_Name + " : " + msg);
	}
	/*public void SendOb(int sel, Object ob)
	{
		// TODO Auto-generated method stub
		try
		{
			clients[sel].SendOb(ob);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
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
	public void BroadCasting(String msg)
	{
		for(int i=0; i<maxClient; i++)
		{
			if(clients[i].getConnect().isConnected())
				clients[i].SendData(msg);
		}
	}
	class ClientData
	{
		Socket connect;
		PrintWriter outPort;
		//ObjectOutputStream outOb;
		ServerListener inPort;

		public ClientData()
		{
			connect = new Socket();
		}
		public void setClientData(Socket client) throws IOException
		{
			connect = client;
			outPort = new PrintWriter(connect.getOutputStream(), true);
			//outOb = new ObjectOutputStream(connect.getOutputStream());
			inPort = new ServerListener(connect);
			inPort.start();
		}
		public Socket getConnect()
		{
			return connect;
		}
		public void SendData(String data)
		{
			outPort.println(data);
		}
/*		public void SendOb(Object ob) throws IOException
		{
			outOb.writeObject(ob);
		}*/
		public void close() throws IOException
		{
			outPort.close();
			//outOb.close();
			inPort.close();
		}
	}
	class WaitingClient extends Thread
	{
		ServerSocket server;
		public void setServer(int portNum, int maxClient)
		{
			try
			{
				server = new ServerSocket(portNum, maxClient);
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
							clients[i].setClientData(server.accept());
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
	class ServerListener extends Thread
	{
		Socket client;
		BufferedReader inMsg;
		//ObjectInputStream inOb;

		public ServerListener(Socket client) throws IOException
		{
			this.client = client;
			inMsg = new BufferedReader(new InputStreamReader(client.getInputStream()));
			//inOb = new ObjectInputStream(client.getInputStream());
		}
		public void close() throws IOException
		{
			stop();
			inMsg.close();
			//inOb.close();
		}
		public void run()
		{
			String inString;
			while(client.isConnected())
			{
				try
				{
					inString = inMsg.readLine();
					//입력 데이터 조건필요.
					m_Taget.AddChatString(inString);
					BroadCasting(inString);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}