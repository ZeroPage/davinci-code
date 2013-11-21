package gui.listeners;

import gui.DaVinciGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonListener implements ActionListener {
	private DaVinciGUI gui;

	public ExitButtonListener(DaVinciGUI gui) {
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gui.fireClosingEvent();
	}

}
