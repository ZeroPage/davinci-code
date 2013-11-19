
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Test {
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);

		//Game temp = new Game();
		//temp.Start();
		String input = "", name;
		Server host;
		Client client;

		System.out.print("Enter the name : ");
		name = scan.nextLine();
		System.out.print("1 : server.\n2 : client.\nEnter the case : ");
		switch(Integer.parseInt(scan.nextLine())) {
		case 1: //서버 부분
			host = new Server(10002);
			host.start();
			
			while(!input.equals("exit")) {
				input = scan.nextLine();
				host.sendData(input);
			}
			break;
		case 2: //클라이언트 부분
			client = new Client();
			System.out.print("Enter the server ip : ");
			input = scan.nextLine();
			client.connectServer(new Socket(input, 10001));
			
			while(client.server.isConnected() || !input.equals("exit")) {
				input = scan.nextLine();
				client.sendData(input);
			}
			break;
		}
	}
}
