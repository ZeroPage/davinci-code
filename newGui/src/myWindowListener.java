import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

class myWindowListener extends WindowAdapter		// Main frame ���Ḧ ���� Ŭ����. 
	{
		public void windowClosing(WindowEvent e) {
			if(JOptionPane.showConfirmDialog(null, "���������Ͻðڽ��ϱ�?","����Ȯ��",JOptionPane.YES_NO_OPTION) == 0) {
				try {
//					lobbyWnd.myNetworkType.Close();
					//TODO 
				} catch(NullPointerException event) {
					System.out.println("Lobby Window doesn't exist.");
				} finally {
					e.getWindow().setVisible(false);	// Frame�� ȭ�鿡�� ������ �ʵ��� �Ѵ�.
				    e.getWindow().dispose(); 			// �޸𸮿��� �����Ѵ�.
				    System.exit(0); 					// ���α׷��� �����Ѵ�.
				}
			}
		}
	}