import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class Server extends Network
{
	ClientData[]	clients;	// server 에 접속한 client 들을 저장할 변수. client 접속 소켓이 저장되어있다.
	WaitingClient	wait;		// 서버 소켓이 저장될 변수.

	final int maxClient = 3;	// 게임에는 server 까지 최대 4 명만 접속할 수 있다.
	int clientNum;

	public Server()
	{
		System.out.println("[ Server : Constructor ]");
		clientNum	= 0;								//현재 접속한 client 의 수를 초기화.
		clients		= new ClientData[maxClient];		// client 정보를 저장할 메모리 생성.
		wait		= new WaitingClient();						// server 가 client 의 접속을 기다리기 시작.
		for(int i = 0; i < maxClient; i++)
			clients[i] = new ClientData();		// client 자료구조 생성.
	}

	public void Connect(String ip)			// 서버 소켓을 열고 client 를 기다리기 시작하는 메소드.
	{
		//ip는 상속되는  코드와의 호환을 위해. 의미없음.
		//이 클래스는 서버 이므로 접속을 하지 않고 서버를 생성.
		wait.setServSock(portNum, maxClient);		// server 의 소켓을 열고 클라이언트를 기다림.
		wait.start();								// server socket 스레드 시작. 
	}
	public void SendChatMsg(String msg)
	{
		DataHeader temp = new DataHeader();
		temp.setFlag(DataHeader.CHAT);
		temp.setData(playerNickname +" : "+ msg);
		SendOb(temp);
		
		myRoomWnd.AddChatString(playerNickname + " : " + msg);
	}
	public void SendOb(Object ob)		// 전달받은 객체를 server 에 접속한 모든 객체에 전달하는 메소드.
	{
		System.out.println("[ Server : SendOb ]");
		for(int i = 0 ; i < clientNum ; i++)
		{
			try {	clients[i].SendOb(ob);	}	// client 에게 객체를 전달한다.
			catch(SocketException e){	System.out.println("client "+i+" 번 에서 반응이 없습니다."); 	}
			catch(IOException e)	{	e.printStackTrace(); } 
		}
	}

	public void Close()
	{
		SendChatMsg("서버가 종료되었습니다.");
		wait.listenning = false;
		for(int i = 0; i < clientNum; i++)
		{
			if(clients[i].getClientSocket().isConnected())
			{
				try {			clients[i].close();			}
				catch(SocketException e)	{	System.out.println("client "+i+" 번은 이미 연결이 종료되었습니다.");	}
				catch (IOException e) 		{	e.printStackTrace();	}
			}
		}
		wait.close();
	}
	
	class ClientData	// server 가 관리할 client 들의 소켓정보와 input/output 정보를 
	{
		Socket 				clientSock	= null;
		ObjectOutputStream	outOb		= null;
		ObServerListener	inOb		= null;
		
		public ClientData() {	// 임의의 소켓 메모리 생성.
			clientSock = new Socket();
		}
		public void setClientData(Socket accepted) throws IOException		// client 와 맺은 새로운 소캣을 인자로 받아 input/output 스트림을 열고
		{																	// 그 소켓을 저장해두는 메소드.
			clientSock	= accepted;										// 인자로 전달받은 client 의 소켓을 저장.
			outOb		= new ObjectOutputStream(clientSock.getOutputStream());	// output이 client 에게 전송됨.
			inOb		= new ObServerListener(clientSock);						// client 에게서 전송받음.
			inOb.start();
		}
		public Socket getClientSocket() {
			return clientSock;
		}
		public void SendOb(Object ob) throws IOException		// client 에게 데이터를 전송하는 메소드.
		{
			outOb.writeObject(ob);
			outOb.flush();
		}
		public void close() throws IOException		// client와의 연결을 끊는 메소드.
		{
			outOb.close();
			inOb.close();
			clientSock.close();
		}
	}
	class WaitingClient extends Thread		// Server 가 Client 의 접속을 기다릴 때 사용할 클래스.
	{
		ServerSocket	servSock	= null;
		boolean			listenning	= true;
		public void setServSock(int portNum, int maxClient)			// 서버 소켓을 portNum 과 결합시킨다.
		{
			try {
				servSock = new ServerSocket(portNum, maxClient);	// 지정된 포트번호로 서버 소켓을 연다.
			}
			catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, "입력하신 Port number : "+portNum+"\nPort 는 1 ~ 65535 사이의 값이어야 합니다.\n기본값 10000 번 포트로 연결합니다.", "Port number 경고", JOptionPane.OK_OPTION );
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
		public void run() {					// 스레드 생성시 실제 동작시킬 내용을 적는 메소드.
			int current = 0;
			while(listenning) {						// 서버 소켓은 클라이언트가 접속할 때까지 계속 기다려야 하기 때문에 while( true ).
				if( current <= maxClient ) {
					if( clients[current].getClientSocket().isConnected() ) {
						System.out.println("client "+current+" 접속함.");
						current++;
					}
				}

				try {	clients[current].setClientData(servSock.accept());	}
				catch(SocketException e)	{	listenning = false;	close();	}
				catch (IOException e) 		{	e.printStackTrace();		}
				clientNum++;			// client 를 연결시키고 난 후 전체 client 수를 하나 증가.
			}
		}
	}

	class ObServerListener extends Thread	// server 로 들어오는 데이터들을 받아 처리하는 클래스.
	{
		Socket client = null;
		boolean listenning = true;
		ObjectInputStream OInStream = null;

		public ObServerListener(Socket client) throws IOException	// 전달받은 소켓 인자에 input 스트림을 연결.
		{
			this.client = client;		
			OInStream = new ObjectInputStream(client.getInputStream());
		}
		public void close() {
			try {
				listenning = false;
				OInStream.close();		// 객체 input 스트림을 종료.
			}
			catch (IOException e) {	e.printStackTrace(); }
			clientNum--;
		}
		public void dataEvent(DataHeader data) {
			//서버에게 온 데이터의 처리는 여기서 담당한다.
			// 기본적으로 서버에게 온 데이터는 브로드 캐스팅이 되어 다시 나간다.
			// 브로드 캐스팅을 막으려면 조건문 안에서 return을 시킬것.
			
			System.out.println("[ Server : dataEvent ]");
			int flag = data.getFlag();
			switch(flag) {
				case DataHeader.CHAT:		// 데이터 헤더가 대화 이벤트일 경우.
					myRoomWnd.AddChatString((String)data.getData());
					break;
				case DataHeader.GAME:
					//if(gameProcess.gameControl == null || !gameProcess.gameControl.equals((Game)data.getData()))
					gameProcess.setGameEnv((Game)data.getData());
					break;
				case DataHeader.GAMEDATA:
					gameProcess.setGameEnv((GameData)data.getData());
					break;
				case DataHeader.PASS:		// 턴 넘김 메시지를 받고서, client 자신이 플레이할 차례가 되면 턴을 시행한다.
					if(gameProcess.getPlayOrder() == ((Integer)data.getData()).intValue())
						gameProcess.turn();
					break;
			}
			SendOb(data);
		}
		public void run() {
			while(listenning)
			{
				try {		dataEvent((DataHeader)OInStream.readObject());	}	// 계속 대기하며 들어오는 데이터를 처리.
				catch (SocketException e)	{	listenning = false;	}			// 연결이 끊어졌다고 판단될 경우
				catch (EOFException e) 		{	listenning = false;	}			// 예상 못한 스트림의 종료시에 발생. 
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
	
	public void SendOrder()			// player 들에게 자신들의 게임 순서를 전송한다.
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