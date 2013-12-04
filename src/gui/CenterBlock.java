package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.GameProcess;

public class CenterBlock extends GUIBlock implements ActionListener{

	public CenterBlock(GameProcess gameProcess, int playerNum, int index) {
		super(gameProcess, playerNum, index);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gameProcess.centerBlockSelect(index);
	}
}
