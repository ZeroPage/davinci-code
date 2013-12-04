package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.Block;
import core.GameProcess;

public class CenterBoard extends PlayBoard {

	public CenterBoard(JPanel mainPanel, GameProcess gameProcess) {
		super();
		this.gameProcess = gameProcess;

		playerBlock = new CenterBlock[27]; // 모두 27개의 block 들.
		mainPanel.add(BorderLayout.CENTER, boardPanel);
		boardPanel.setOpaque(false);
	}

	@Override
	public void update(ArrayList<Block> blocks) {
		System.out.println("[ NPC : update ]");
		super.update(blocks);
		// center 가 가진 block 들은 아무것도 소유되어진 것이 없기 때문에 모두 뒷면으로 이미지가 설정된다.
		for (int i = blocks.size(); playerBlock[i] != null; i++) {
			boardPanel.remove(playerBlock[i]);
			boardPanel.repaint();
		}
	}

	@Override
	public void setEnable(ArrayList<Block> blockState, boolean state) {
		// 위의 플레이어 윈도우와 같이 변경해야함.
		System.out.println("[ NPC : setEnable ]");
		for (int i = 0; playerBlock[i] != null; i++) {
			playerBlock[i].setEnabled(state);
			playerBlock[i].setRolloverEnabled(state);
		}
	}

	@Override
	public GUIBlock makeBlock(GameProcess gameProcess, int playerNum, int index) {
		return new CenterBlock(gameProcess, playerNum, index);
	}
}