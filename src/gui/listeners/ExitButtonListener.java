package gui.listeners;

import gui.RoomWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ExitButtonListener implements ActionListener {
	private RoomWindow roomWindow;

	public ExitButtonListener(RoomWindow roomWindow) {
		this.roomWindow = roomWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		roomWindow.getWindowListeners()[0].windowClosing(new WindowEvent(
				RoomWindow.getWindows()[0], 0));
	}

}
