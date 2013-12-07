package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.GameProcess;

public class SingleButtonListener implements ActionListener {

	private GameProcess gameProcess;

	public SingleButtonListener(GameProcess gameProcess) {
		this.gameProcess = gameProcess;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gameProcess.startSingle();
	}

}
