package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import core.Game;
import core.GameData;

class ObListener extends Thread {
	// server 로부터 들어오는 input스트림을 받아 알맞은 동작을 하도록
	// 하는 클래스.
	ObjectInputStream inputStream;
	boolean isListening = true;
	private Client client;

	public ObListener(Socket clientSocket, Client client) throws IOException {
		this.client = client;
		inputStream = new ObjectInputStream(clientSocket.getInputStream());
	}

	public void close() {
		try {
			isListening = false;
			inputStream.close(); // input 스트림 종료.
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void dataEvent(DataHeader data) {
		// 입력된 데이터의 처리는 여기에 추가할것.
		// if( data.getSender().equals(playerNickname)) { // 내가 보냈던 데이터가
		// server 에 의해 재전송되어오는 경우 이를 막는다.
		// System.out.println("재전송된 데이터 막힘.");
		// return;
		// }
		System.out.println("[ Client : dataEvent ]");
		int flag = data.getFlag();
		switch (flag) {
		case DataHeader.CHAT: // 데이터 헤더가 대화 이벤트일 경우.
			client.getMyRoomWnd().AddChatString((String) data.getData());
			break;
		case DataHeader.GAME:
			// if(gameProcess.gameControl == null ||
			// !gameProcess.gameControl.equals((Game)data.getData()))
			client.gameProcess.setGameEnv((Game) data.getData());
			break;
		case DataHeader.GAMEDATA:
			client.gameProcess.setGameEnv((GameData) data.getData());
			break;
		case DataHeader.PASS: // 턴 넘김 메시지를 받고서, client 자신이 플레이할 차례가 되면 턴을
								// 시행한다.
			if (client.gameProcess.getPlayOrder() == ((Integer) data.getData())
					.intValue())
				client.gameProcess.turn();
			break;
		case DataHeader.MYORDER: // 해당 client 의 순서를 전달받으면 그 순서로 세팅.
			client.gameProcess.setPlayOrder(((Integer) data.getData()).intValue());
			break;
		case DataHeader.TOTALCOUNT: // 총 인원수를 전달받으면 그 숫자대로 게임 GUI 를 세팅.
			client.gameProcess.getGameWndGUI().Setting(((Integer) data.getData())
					.intValue());
			break;
		}
	}

	public void run() {
		while (isListening) {
			// 입력 데이터 조건 필요.
			try {
				dataEvent((DataHeader) inputStream.readObject());
			} catch (EOFException e) { // 서버로부터의 스트림이 갑자기 끊기는 경우
				JOptionPane.showMessageDialog(null,
						"서버로부터 연결이 종료되었습니다.\n연결을 위해서는 프로그램을 재시작해주시기 바랍니다.",
						"서버 연결 종료", JOptionPane.OK_OPTION);
				isListening = false;
			} catch (SocketException e) {
				System.out.println("Server 가 연결되어있지 않습니다.");
				isListening = false;
			} catch (IOException e) {
				isListening = false;
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// close();
				e.printStackTrace();
			}
		}
	}
}