import java.io.IOException;
import java.net.Socket;


public class SLisener extends Lisener {
	private Server server;
	public SLisener(Socket com, Server server) throws IOException {
		setConnection(com);
		setInMessage();
		this.server = server;
	}
	public void close() throws IOException {
		getInMessage().close();
		getConnection().close();
	}
	public void printMessage() throws IOException {
		String temp = "";
		while(true) {
			if(!getConnection().isConnected() || temp.equals("exit")) {
				close();
				getGUI().ChatLisener("접속이 종료되었습니다.");
				//System.out.println(temp);
				return;
			}
			temp = readInMessage();
			server.sendData(temp);
			//System.out.println(temp);
			getGUI().ChatLisener(temp);
			
		}
	}
}
