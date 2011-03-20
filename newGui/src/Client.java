import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

//import sun.java2d.Disposer;

public class Client extends Network
{
	Socket clientSocket;
	ObjectOutputStream OOutStream;		// 서버로의 객체 output 스트림을 연결할 변수.
	ObListener OInStream;				// client 가 server 로부터 들어오는 데이터를 계속 받을 수 있는 input 스트림을 연결할 변수.

	public void Connect(String ip)		// 주어진 IP 와 portNum 멤버변수를 사용하여 서버에 접속하는 메소드.
	{
		try {
			clientSocket = new Socket(ip, portNum);		// 서버 소켓에 연결.
			setOutOb();									// output 스트림을 설정하여 서버에 데이터를 전송할 수 있게 준비한다.
			setListen();								// input 스트림을 설정하여 서버에서 데이터를 수신할 수 있도록 준비한다.
		} catch(ConnectException e) {
			JOptionPane.showMessageDialog(null, "IP\t: "+ip+"\nPort\t: "+portNum+"\n호스트 또는 port가 올바르지 않습니다.\n\n게임을 종료합니다.");
			System.exit(0);
			//TODO 종료 대신 다른 방법으로 ip 와 port 를 새로 받아 접속할 수 있도록 해야 함.
		}
		catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public void SendChatMsg(String msg)	// server 로 대화메시지를 보내면 서버가 다시 그 메시지를 broadcasting 해주고,
	{									// 그 broadcasting 되어 온 메시지는 dataEvent() 메소드에서 처리되어 채팅창에 채팅내용이 표시된다.
		DataHeader temp = new DataHeader();
		temp.setFlag(DataHeader.CHAT);
		temp.setData(playerNickname + " : " + msg);
		SendOb(temp);
	}
	public void setOutOb() throws IOException	// server 로의 output 스트림을 연결하는 메소드.
	{
		OOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	public void setListen() throws IOException	// input 스트림으로 데이터를 읽기 시작하도록 하는 메소드.
	{
		OInStream = new ObListener();	// input 스트림을 계속 읽을 스레드 객체 메모리 생성.
		OInStream.setOInStream();		// input 스트림을 소켓에 연결.
		OInStream.start();				// 스레드 실행.
	}
	
	public void SendOb(Object ob)		// 인자로 받은 객체를 서버로 전송하는 메소드.
	{
		System.out.println("[ Client : SendOb ]");
		try	{
			OOutStream.writeObject(ob);		// 객체 output 스트림에 객체를 실어 보냄.
			OOutStream.flush();				// 객체 output 스트림을 깨끗이 치운다.
		}
		catch (SocketException e) {	System.out.println("Server 와의 연결이 종료되었습니다.");	} 
		catch (IOException e)		{	e.printStackTrace(); }
	}
	public void Close()					// client 와 server 사이의 연결을 모두 종료하는 메소드.
	{
		// client network 이 종료되려면,
		// 1. client 가 종료된다는 메시지를 서버에 전달.
		// 2. 종료 데이터를 서버에 전달(DataHeader)
		// 3. 소켓을 통한 input/output 스트림을 종료.
		// 4. 소켓 연결 종료.
		// 5. client 의 프로그램 종료.
		// 위 5 개의 순서대로 프로그램을 종료해서 server 에 연결된 다른 client 들의 상태에
		// 영향을 주지 않고, 게임이 진행되거나 종료되도록 해야 한다.
		try	{
			SendChatMsg("게임에서 나갑니다.");
			OInStream.close();
			OOutStream.close();		// 객체 output 스트림 종료.
			clientSocket.close();
		} catch( SocketException e) {
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}

	class ObListener extends Thread				// server 로부터 들어오는 input스트림을 받아 알맞은 동작을 하도록 하는 클래스.
	{
		ObjectInputStream OInStream;
		boolean listenning = true;

		public void setOInStream() throws IOException {	// 객체 input 스트림을 클라이언트의 소켓으로부터 연결한다.
			OInStream = new ObjectInputStream(clientSocket.getInputStream());
		}
		public void close() {
			try {
				listenning = false;
				OInStream.close();	// input 스트림 종료.
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		public void dataEvent(DataHeader data) {
			//입력된 데이터의 처리는 여기에 추가할것.
//			if( data.getSender().equals(playerNickname)) {	// 내가 보냈던 데이터가 server 에 의해 재전송되어오는 경우 이를 막는다.
//				System.out.println("재전송된 데이터 막힘.");
//				return;
//			}
			System.out.println("[ Client : dataEvent ]");
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
				case DataHeader.MYORDER:	// 해당 client 의 순서를 전달받으면 그 순서로 세팅. 
					gameProcess.setPlayOrder(((Integer)data.getData()).intValue());
					break;
				case DataHeader.TOTALCOUNT:	// 총 인원수를 전달받으면 그 숫자대로 게임 GUI 를 세팅.
					gameProcess.gameWndGUI.Setting(((Integer)data.getData()).intValue());
					break;
			}
		}
		public void run() {
			while(listenning) {
				//입력 데이터 조건 필요. 
				try {
					dataEvent((DataHeader)OInStream.readObject());
				} catch ( EOFException e ) {		// 서버로부터의 스트림이 갑자기 끊기는 경우
					JOptionPane.showMessageDialog(null, "서버로부터 연결이 종료되었습니다.\n연결을 위해서는 프로그램을 재시작해주시기 바랍니다.","서버 연결 종료", JOptionPane.OK_OPTION);
					listenning = false;
				} 
				catch(SocketException e) {
					System.out.println("Server 가 연결되어있지 않습니다.");
					listenning = false;
				} 
				catch (IOException e) {
					listenning = false;
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
//					close();
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isServer() {
		return false;
	}
}
