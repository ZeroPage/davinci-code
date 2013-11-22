package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import network.Network;
import core.GameProcess;

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
