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
	ObjectOutputStream OOutStream;		// �������� ��ü output ��Ʈ���� ������ ����.
	ObListener OInStream;				// client �� server �κ��� ������ �����͸� ��� ���� �� �ִ� input ��Ʈ���� ������ ����.

	public void Connect(String ip)		// �־��� IP �� portNum ��������� ����Ͽ� ������ �����ϴ� �޼ҵ�.
	{
		try {
			clientSocket = new Socket(ip, portNum);		// ���� ���Ͽ� ����.
			setOutOb();									// output ��Ʈ���� �����Ͽ� ������ �����͸� ������ �� �ְ� �غ��Ѵ�.
			setListen();								// input ��Ʈ���� �����Ͽ� �������� �����͸� ������ �� �ֵ��� �غ��Ѵ�.
		} catch(ConnectException e) {
			JOptionPane.showMessageDialog(null, "IP\t: "+ip+"\nPort\t: "+portNum+"\nȣ��Ʈ �Ǵ� port�� �ùٸ��� �ʽ��ϴ�.\n\n������ �����մϴ�.");
			System.exit(0);
			//TODO ���� ��� �ٸ� ������� ip �� port �� ���� �޾� ������ �� �ֵ��� �ؾ� ��.
		}
		catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public void SendChatMsg(String msg)	// server �� ��ȭ�޽����� ������ ������ �ٽ� �� �޽����� broadcasting ���ְ�,
	{									// �� broadcasting �Ǿ� �� �޽����� dataEvent() �޼ҵ忡�� ó���Ǿ� ä��â�� ä�ó����� ǥ�õȴ�.
		DataHeader temp = new DataHeader();
		temp.setFlag(DataHeader.CHAT);
		temp.setData(playerNickname + " : " + msg);
		SendOb(temp);
	}
	public void setOutOb() throws IOException	// server ���� output ��Ʈ���� �����ϴ� �޼ҵ�.
	{
		OOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	public void setListen() throws IOException	// input ��Ʈ������ �����͸� �б� �����ϵ��� �ϴ� �޼ҵ�.
	{
		OInStream = new ObListener();	// input ��Ʈ���� ��� ���� ������ ��ü �޸� ����.
		OInStream.setOInStream();		// input ��Ʈ���� ���Ͽ� ����.
		OInStream.start();				// ������ ����.
	}
	
	public void SendOb(Object ob)		// ���ڷ� ���� ��ü�� ������ �����ϴ� �޼ҵ�.
	{
		System.out.println("[ Client : SendOb ]");
		try	{
			OOutStream.writeObject(ob);		// ��ü output ��Ʈ���� ��ü�� �Ǿ� ����.
			OOutStream.flush();				// ��ü output ��Ʈ���� ������ ġ���.
		}
		catch (SocketException e) {	System.out.println("Server ���� ������ ����Ǿ����ϴ�.");	} 
		catch (IOException e)		{	e.printStackTrace(); }
	}
	public void Close()					// client �� server ������ ������ ��� �����ϴ� �޼ҵ�.
	{
		// client network �� ����Ƿ���,
		// 1. client �� ����ȴٴ� �޽����� ������ ����.
		// 2. ���� �����͸� ������ ����(DataHeader)
		// 3. ������ ���� input/output ��Ʈ���� ����.
		// 4. ���� ���� ����.
		// 5. client �� ���α׷� ����.
		// �� 5 ���� ������� ���α׷��� �����ؼ� server �� ����� �ٸ� client ���� ���¿�
		// ������ ���� �ʰ�, ������ ����ǰų� ����ǵ��� �ؾ� �Ѵ�.
		try	{
			SendChatMsg("���ӿ��� �����ϴ�.");
			OInStream.close();
			OOutStream.close();		// ��ü output ��Ʈ�� ����.
			clientSocket.close();
		} catch( SocketException e) {
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}

	class ObListener extends Thread				// server �κ��� ������ input��Ʈ���� �޾� �˸��� ������ �ϵ��� �ϴ� Ŭ����.
	{
		ObjectInputStream OInStream;
		boolean listenning = true;

		public void setOInStream() throws IOException {	// ��ü input ��Ʈ���� Ŭ���̾�Ʈ�� �������κ��� �����Ѵ�.
			OInStream = new ObjectInputStream(clientSocket.getInputStream());
		}
		public void close() {
			try {
				listenning = false;
				OInStream.close();	// input ��Ʈ�� ����.
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		public void dataEvent(DataHeader data) {
			//�Էµ� �������� ó���� ���⿡ �߰��Ұ�.
//			if( data.getSender().equals(playerNickname)) {	// ���� ���´� �����Ͱ� server �� ���� �����۵Ǿ���� ��� �̸� ���´�.
//				System.out.println("�����۵� ������ ����.");
//				return;
//			}
			System.out.println("[ Client : dataEvent ]");
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
				case DataHeader.MYORDER:	// �ش� client �� ������ ���޹����� �� ������ ����. 
					gameProcess.setPlayOrder(((Integer)data.getData()).intValue());
					break;
				case DataHeader.TOTALCOUNT:	// �� �ο����� ���޹����� �� ���ڴ�� ���� GUI �� ����.
					gameProcess.gameWndGUI.Setting(((Integer)data.getData()).intValue());
					break;
			}
		}
		public void run() {
			while(listenning) {
				//�Է� ������ ���� �ʿ�. 
				try {
					dataEvent((DataHeader)OInStream.readObject());
				} catch ( EOFException e ) {		// �����κ����� ��Ʈ���� ���ڱ� ����� ���
					JOptionPane.showMessageDialog(null, "�����κ��� ������ ����Ǿ����ϴ�.\n������ ���ؼ��� ���α׷��� ��������ֽñ� �ٶ��ϴ�.","���� ���� ����", JOptionPane.OK_OPTION);
					listenning = false;
				} 
				catch(SocketException e) {
					System.out.println("Server �� ����Ǿ����� �ʽ��ϴ�.");
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
