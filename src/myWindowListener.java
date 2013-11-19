import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

class myWindowListener extends WindowAdapter // Main frame 종료를 위한 클래스.
{
	public void windowClosing(WindowEvent e) {
		if (JOptionPane.showConfirmDialog(null, "정말종료하시겠습니까?", "종료확인",
				JOptionPane.YES_NO_OPTION) == 0) {
			try {
				// lobbyWnd.myNetworkType.Close();
				// TODO
			} catch (NullPointerException event) {
				System.out.println("Lobby Window doesn't exist.");
			} finally {
				e.getWindow().setVisible(false); // Frame을 화면에서 보이지 않도록 한다.
				e.getWindow().dispose(); // 메모리에서 제거한다.
				System.exit(0); // 프로그램을 종료한다.
			}
		}
	}
}