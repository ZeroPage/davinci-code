import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Color;
class AskDlg extends JDialog implements ActionListener		// ������ ���ڸ� �����ϴ� ��ȭ����
	{
		JButton [] JB_Num = new JButton[13];
		int Num;
		public AskDlg() {
			super((JFrame) getWindows()[0], "���ڸ� �����ϼ���",true);
			this.setSize(240,300);
			this.setLayout(new GridLayout(5,3));
			this.setLocation(getRootPane().getSize().width/2, getRootPane().getSize().height/2);
			this.setResizable(false);
			this.addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
				}	
			});
			
			for(int i = 0; i < 13; i++) {
				JB_Num[i] = new JButton(""+ i);
				JB_Num[i].addActionListener(this);
				this.getContentPane().add(JB_Num[i]);
			}
			JB_Num[12].setText("Joker");
			
			this.setVisible(true);
		}
		public AskDlg(String title, String color) {		// ��Ŀ�� ��ȭ����.
			super((JFrame) getWindows()[0], title,true);
			this.setSize(240,300);
			this.setLayout(new GridLayout(5,3));
			this.setLocation(getRootPane().getSize().width/2, getRootPane().getSize().height/2);
			this.setResizable(false);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
				}	
			});
			
			Color bgColor, frColor;
			if(color.equals("black")) {
				bgColor = new Color(0,0,0);
				frColor = new Color(255,255,255);
			} else {
				frColor = new Color(0,0,0);
				bgColor = new Color(255,255,255);
			}
			for(int i = 0; i < 13; i++) {
				JB_Num[i] = new JButton(""+ i);
				JB_Num[i].addActionListener(this);
				JB_Num[i].setForeground(frColor);
				JB_Num[i].setBackground(bgColor);
				this.getContentPane().add(JB_Num[i]);
			}
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent e)	// ���� block�� ���ߴ� ��ȭ���ڿ��� ������ ��ư�� �������� �Ǻ��� �� Num �� ����.
		{
			for(int i = 0; i <13; i++) {
				if(e.getSource() == JB_Num[i]) {
					Num = i;
					setVisible(false);
					break;
				}
			}
		}
		public int getNum() {		// ��ȭ���ڿ��� Ŭ���� ���� num �� ��ȯ
			return Num;
		}
	}