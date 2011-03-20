import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class DavichiGUI extends JFrame 
{
	RoomWindow	roomWnd		= null;
	LobbyWindow	lobbyWnd	= null;

	public DavichiGUI() {
		super("�ٺ�ġ �ڵ� - õ��� �Ǹ� Ver 1.9");
	
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(1000, 600);		// ũ�� ����.
		this.setResizable(false);		// ũ�⺯�� �Ұ���.
		
		lobbyWnd = new LobbyWindow((JPanel)this.getContentPane(), this.getRootPane());		// ������ ���� window ����
		this.addWindowListener(new myWindowListener());										// �̺�Ʈ ������ ���.
	}
	public static void main(String [] args) {
		DavichiGUI obj = new DavichiGUI();
		obj.setVisible(true);
	}
}