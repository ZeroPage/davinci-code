package gui.listeners;

import gui.GameWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.GameProcess;
import network.Network;

public class StartButtonListener implements ActionListener {
	private GameProcess gameProcess;

	public StartButtonListener(Network network, GameProcess gameProcess) {
		this.gameProcess = gameProcess;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gameProcess.start();
	}

}
