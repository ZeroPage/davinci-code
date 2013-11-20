package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.GameProcess;

public class UserBlock extends GUIBlock implements ActionListener {

	public UserBlock(GameProcess gameProcess, int playerNum, int index) {
		super(gameProcess, playerNum, index);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedNum = (new AskDlg()).getNum();
		gameProcess.AskBlock(playerNum, index, selectedNum);
	}

}
