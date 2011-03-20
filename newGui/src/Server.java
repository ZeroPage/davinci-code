import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class Server extends Network
{
	ClientData[]	clients;	// server �� ������ client ���� ������ ����. client ���� ������ ����Ǿ��ִ�.
	WaitingClient	wait;		// ���� ������ ����� ����.

	final int maxClient = 3;	// ���ӿ��� server ���� �ִ� 4 �� ������ �� �ִ�.
	int clientNum;

	public Server()
	{
		System.out.println("[ Server : Constructor ]");
		clientNum	= 0;								//���� ������ client �� ���� �ʱ�ȭ.
		clients		= new ClientData[maxClient];		// client ������ ������ �޸� ����.
		wait		= new WaitingClient();						// server �� client �� ������ ��ٸ��� ����.
		for(int i = 0; i < maxClient; i++)
			clients[i] = new ClientData();		// client �ڷᱸ�� ����.
	}

	public void Connect(String ip)			// ���� ������ ���� client �� ��ٸ��� �����ϴ� �޼ҵ�.
	{
		//ip�� ��ӵǴ�  �ڵ���� ȣȯ�� ����. �ǹ̾���.
		//�� Ŭ������ ���� �̹Ƿ� ������ ���� �ʰ� ������ ����.
		wait.setServSock(portNum, maxClient);		// server �� ������ ���� Ŭ���̾�Ʈ�� ��ٸ�.
		wait.start();								// server socket ������ ����.
	}
	public void SendChatMsg(String msg)
	{
		DataHeader temp = new DataHeader();
		temp.setFlag(DataHeader.CHAT);
		temp.setData(playerNickname +" : "+ msg);
		SendOb(temp);
		
		myRoomWnd.AddChatString(playerNickname + " : " + msg);
	}
	public void SendOb(Object ob)		// ���޹��� ��ü�� server �� ������ ��� ��ü�� �����ϴ� �޼ҵ�.
	{
		System.out.println("[ Server : SendOb ]");
		for(int i = 0 ; i < clientNum ; i++)
		{
			try {	clients[i].SendOb(ob);	}	// client ���� ��ü�� �����Ѵ�.
			catch(SocketException e){	System.out.println("client "+i+" �� ���� ������ �����ϴ�."); 	}
			catch(IOException e)	{	e.printStackTrace(); } 
		}
	}

	public void Close()
	{
		SendChatMsg("������ ����Ǿ����ϴ�.");
		wait.listenning = false;
		for(int i = 0; i < clientNum; i++)
		{
			if(clients[i].getClientSocket().isConnected())
			{
				try {			clients[i].close();			}
				catch(SocketException e)	{	System.out.println("client "+i+" ���� �̹� ������ ����Ǿ����ϴ�.");	}
				catch (IOException e) 		{	e.printStackTrace();	}
			}
		}
		wait.close();
	}
	
	class ClientData	// server �� ������ client ���� ���������� input/output ������ 
	{
		Socket 				clientSock	= null;
		ObjectOutputStream	outOb		= null;
		ObServerListener	inOb		= null;
		
		public ClientData() {	// ������ ���� �޸� ����.
			clientSock = new Socket();
		}
		public void setClientData(Socket accepted) throws IOException		// client �� ���� ���ο� ��Ĺ�� ���ڷ� �޾� input/output ��Ʈ���� ����
		{																	// �� ������ �����صδ� �޼ҵ�.
			clientSock	= accepted;										// ���ڷ� ���޹��� client �� ������ ����.
			outOb		= new ObjectOutputStream(clientSock.getOutputStream());	// output�� client ���� ���۵�.
			inOb		= new ObServerListener(clientSock);						// client ���Լ� ���۹���.
			inOb.start();
		}
		public Socket getClientSocket() {
			return clientSock;
		}
		public void SendOb(Object ob) throws IOException		// client ���� �����͸� �����ϴ� �޼ҵ�.
		{
			outOb.writeObject(ob);
			outOb.flush();
		}
		public void close() throws IOException		// client���� ������ ���� �޼ҵ�.
		{
			outOb.close();
			inOb.close();
			clientSock.close();
		}
	}
	class WaitingClient extends Thread		// Server �� Client �� ������ ��ٸ� �� ����� Ŭ����.
	{
		ServerSocket	servSock	= null;
		boolean			listenning	= true;
		public void setServSock(int portNum, int maxClient)			// ���� ������ portNum �� ���ս�Ų��.
		{
			try {
				servSock = new ServerSocket(portNum, maxClient);	// ������ ��Ʈ��ȣ�� ���� ������ ����.
			}
			catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, "�Է��Ͻ� Port number : "+portNum+"\nPort �� 1 ~ 65535 ������ ���̾�� �մϴ�.\n�⺻�� 10000 �� ��Ʈ�� �����մϴ�.", "Port number ���", JOptionPane.OK_OPTION );
				setServSock(10000, maxClient);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void close() {
			try {
				listenning = false;
				servSock.close();
			}
			catch (IOException e) {	e.printStackTrace(); }
		}
		public void run() {					// ������ ������ ���� ���۽�ų ������ ���� �޼ҵ�.
			int current = 0;
			while(listenning) {						// ���� ������ Ŭ���̾�Ʈ�� ������ ������ ��� ��ٷ��� �ϱ� ������ while( true ).
				if( current <= maxClient ) {
					if( clients[current].getClientSocket().isConnected() ) {
						System.out.println("client "+current+" ������.");
						current++;
					}
				}

				try {	clients[current].setClientData(servSock.accept());	}
				catch(SocketException e)	{	listenning = false;	close();	}
				catch (IOException e) 		{	e.printStackTrace();		}
				clientNum++;			// client �� �����Ű�� �� �� ��ü client ���� �ϳ� ����.
			}
		}
	}

	class ObServerListener extends Thread	// server �� ������ �����͵��� �޾� ó���ϴ� Ŭ����.
	{
		Socket client = null;
		boolean listenning = true;
		ObjectInputStream OInStream = null;

		public ObServerListener(Socket client) throws IOException	// ���޹��� ���� ���ڿ� input ��Ʈ���� ����.
		{
			this.client = client;		
			OInStream = new ObjectInputStream(client.getInputStream());
		}
		public void close() {
			try {
				listenning = false;
				OInStream.close();		// ��ü input ��Ʈ���� ����.
			}
			catch (IOException e) {	e.printStackTrace(); }
			clientNum--;
		}
		public void dataEvent(DataHeader data) {
			//�������� �� �������� ó���� ���⼭ ����Ѵ�.
			// �⺻������ �������� �� �����ʹ� ��ε� ĳ������ �Ǿ� �ٽ� ������.
			// ��ε� ĳ������ �������� ���ǹ� �ȿ��� return�� ��ų��.
			
			System.out.println("[ Server : dataEvent ]");
			int flag = data.getFlag();
			switch(flag) {
				case DataHeader.CHAT:		// ������ ����� ��ȭ �̺�Ʈ�� ���.
					myRoomWnd.AddChatString((String)data.getData());
					break;
				case DataHeader.GAME:
					//if(gameProcess.gameControl == null || !gameProcess.gameControl.equals((Game)data.getData()))
					gameProcess.setGameEnv((Game)data.getData());
					break;
				case DataHeader.GAMEDATA:
					gameProcess.setGameEnv((GameData)data.getData());
					break;
				case DataHeader.PASS:		// �� �ѱ� �޽����� �ް�, client �ڽ��� �÷����� ���ʰ� �Ǹ� ���� �����Ѵ�.
					if(gameProcess.getPlayOrder() == ((Integer)data.getData()).intValue())
						gameProcess.turn();
					break;
			}
			SendOb(data);
		}
		public void run() {
			while(listenning)
			{
				try {		dataEvent((DataHeader)OInStream.readObject());	}	// ��� ����ϸ� ������ �����͸� ó��.
				catch (SocketException e)	{	listenning = false;	}			// ������ �������ٰ� �Ǵܵ� ���
				catch (EOFException e) 		{	listenning = false;	}			// ���� ���� ��Ʈ���� ����ÿ� �߻�. 
				catch (IOException e) {
					listenning = false;					
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) {
					listenning = false;
					e.printStackTrace();
				}
			}
		}
	}
	
	public void SendOrder()			// player �鿡�� �ڽŵ��� ���� ������ �����Ѵ�.
	{
		System.out.println("[ Server : SendOrder ]");
		for(int i = 0; i < clientNum ; i++)
			try {
				clients[i].SendOb( new DataHeader( DataHeader.MYORDER, Integer.valueOf(i+1)) );	
			}
			catch (IOException e) {				
				e.printStackTrace();
				System.out.println("Class : Server\t :: SendOrder() : IOException");
			}
	}
	public boolean isServer() {
		return true;
	}
}