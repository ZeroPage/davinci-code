import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class DavichiGUI extends JFrame 
{
	RoomWindow	roomWnd		= null;
	LobbyWindow	lobbyWnd	= null;

	public DavichiGUI() {
		super("다빈치 코드 - 천사와 악마 Ver 1.9");
	
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(1000, 600);		// 크기 설정.
		this.setResizable(false);		// 크기변경 불가능.
		
		lobbyWnd = new LobbyWindow((JPanel)this.getContentPane(), this.getRootPane());		// 접속을 위한 window 생성
		this.addWindowListener(new myWindowListener());										// 이벤트 리스너 등록.
	}
	public static void main(String [] args) {
		DavichiGUI obj = new DavichiGUI();
		obj.setVisible(true);
	}
}