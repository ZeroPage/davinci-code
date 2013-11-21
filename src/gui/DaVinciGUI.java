package gui;

import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class DaVinciGUI{
	private JFrame window;
	private CloseListener listener;

	public DaVinciGUI() {
		window  = new JFrame("다빈치 코드 - 천사와 악마 Ver 2.0");
		
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.setSize(1000, 600); // 크기 설정.
		window.setResizable(false); // 크기변경 불가능.
		
		new LobbyWindow(this);
		this.listener = new CloseListener();
		window.addWindowListener(this.listener); // 이벤트 리스너 등록.
		window.setVisible(true);
	}
	public void append(JComponent component){
		window.getContentPane().add(component);
	}
	public JFrame getWindow(){
		return window;
	}
	
	public static void main(String[] args) {
		new DaVinciGUI();
	}
	public void fireClosingEvent() {
		this.listener.windowClosing(new WindowEvent(window, 0));
	}
	
}
