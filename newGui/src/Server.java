import java.io.*;
import java.net.*;


public class Server extends Network
{
	ServerSocket server;
	Socket[] clients;
	PrintWriter[] outData;
	ServerListener[] inData;
	WaitingClient wait;

	int portNum;
	int maxClient;
	int clientNum;

	public Server()
	{
		portNum = 10000;
		maxClient = 3;
		wait = new WaitingClient();
	}

	public void Connect(String ip)
	{
		//ip는 상속되는  코드와의 호환을 위해. 의미없음.
		//이 클래스는 서버 이므로 접속을 하지 않고 서버를 생성.
		try
		{
			server = new ServerSocket(portNum, maxClient);
			wait.start();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void SendChatMsg(String msg)
	{
		for(int i=0; i<clientNum; i++)
			outData[i].println(myName + " : " + msg);
	}
	public void Close()
	{
		// TODO Auto-generated method stub

	}
	class WaitingClient extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					clients[clientNum] = server.accept();
					inData[clientNum] = new ServerListener();
					inData[clientNum].setClient(clients[clientNum]);
					outData[clientNum] = new PrintWriter(clients[clientNum].getOutputStream(), true);
					inData[clientNum].start();
					clientNum++;
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	class ServerListener extends Thread
	{
		Socket client;
		BufferedReader inMsg;

		public void setClient(Socket client)
		{
			this.client = client;
		}
		public void setinMsg() throws IOException
		{
			inMsg = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}
		public void run()
		{
			while(client.isConnected())
			{
				try
				{
					m_Taget.AddChatString(inMsg.readLine());
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
