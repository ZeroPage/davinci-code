package gui.listeners;

import gui.DaVinciGUI;
import gui.HelpDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutButtonListener implements ActionListener {
	private DaVinciGUI gui;

	public AboutButtonListener(DaVinciGUI gui) {
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new HelpDialog(gui.getWindow());
	}
}
