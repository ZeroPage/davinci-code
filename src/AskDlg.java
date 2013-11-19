import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

class AskDlg extends JDialog implements ActionListener // 추측할 숫자를 생성하는 대화상자
{
	JButton[] JB_Num = new JButton[13];
	int Num;

	public AskDlg() // 조커 맞추기용 대화상자
	{
		super((JFrame) getWindows()[0], "숫자를 선택하세요", true);
		this.setSize(240, 300);
		this.setLayout(new GridLayout(5, 3));
		this.setLocation(getRootPane().getSize().width / 2, getRootPane()
				.getSize().height / 2);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			}
		});

		for (int i = 0; i < 13; i++) {
			JB_Num[i] = new JButton("" + i);
			JB_Num[i].addActionListener(this);
			this.getContentPane().add(JB_Num[i]);
		}
		JB_Num[12].setText("Joker");

		this.setVisible(true);
	}

	public AskDlg(int color) { // 조커 설정용 대화상자.
		super((JFrame) getWindows()[0], "조커가 대신할 숫자를 선택하세요", true);
		this.setSize(240, 300);
		this.setLayout(new GridLayout(5, 3));
		this.setLocation(getRootPane().getSize().width / 2, getRootPane()
				.getSize().height / 2);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			}
		});

		Color bgColor = null;
		Color frColor = null;

		switch (color) {
		case Block.BLACK:
			bgColor = new Color(0, 0, 0);
			frColor = new Color(255, 255, 255);
			break;
		case Block.WHITE:
			frColor = new Color(0, 0, 0);
			bgColor = new Color(255, 255, 255);
			break;
		}
		for (int i = 0; i < 13; i++) {
			JB_Num[i] = new JButton("" + i);
			JB_Num[i].addActionListener(this);
			JB_Num[i].setForeground(frColor);
			JB_Num[i].setBackground(bgColor);
			this.getContentPane().add(JB_Num[i]);
		}
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) // 상태 block을 맞추는 대화상자에서 눌려진 버튼이
												// 무엇인지 판별한 후 Num 에 저장.
	{
		for (int i = 0; i < 13; i++) {
			if (e.getSource() == JB_Num[i]) {
				Num = i;
				setVisible(false);
				break;
			}
		}
	}

	public int getNum() { // 대화상자에서 클릭된 숫자 num 을 반환
		return Num;
	}
}