import java.io.IOException;
import java.net.Socket;


public class SLisener extends Lisener {
	private Server server;
	public SLisener(Socket com, Server server) throws IOException {
		setConnection(com);
		setInMessage();
		this.server = server;
	}
	public void printMessage() throws IOException {
		String temp = "";
		while(true) {
			if(!getConnection().isConnected() || temp.equals("exit")) {
				getInMessage().close();
				getConnection().close();
				System.out.println(temp);
				return;
			}
			temp = readInMessage();
			server.sendData(temp);
			//System.out.println(temp);
			getGUI().ChatLisener(temp);
			
		}
	}
}
