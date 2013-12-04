package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.Block;
import core.GameProcess;

public class UserBoard extends PlayBoard {

	public UserBoard(int playerNum, JPanel mainPanel, GameProcess gameProcess) {
		super();
		this.gameProcess = gameProcess;

		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, -1, -1);
		playerOrder = playerNum;
		playerBlock = new UserBlock[13];

		String location = "";
		switch (playerNum) {
		// PlayerNum 에 따라 다른 사람들과 자신의 block 이 놓일 위치를 설정한다.
		case 0:
			location = BorderLayout.SOUTH;
			boardPanel.setPreferredSize(new Dimension(0, windowSize));
			break;
		case 1:
			location = BorderLayout.WEST;
			boardPanel.setPreferredSize(new Dimension(windowSize, 0));
			layout.setAlignment(FlowLayout.LEADING);
			break;
		case 2:
			location = BorderLayout.NORTH;
			boardPanel.setPreferredSize(new Dimension(0, windowSize));
			break;
		case 3:
			location = BorderLayout.EAST;
			boardPanel.setPreferredSize(new Dimension(windowSize, 0));
			layout.setAlignment(FlowLayout.TRAILING);
			break;
		default:
			break;
		}
		boardPanel.setLayout(layout);
		mainPanel.add(location, boardPanel);
		boardPanel.setOpaque(false); // 불투명하게 설정하는 메소드에 false를 주어 투명하게 만듬.
	}

	@Override
	public void update(ArrayList<Block> blocks) {
		super.update(blocks);
	}

	@Override
	public void setEnable(ArrayList<Block> blockState, boolean state) {
		// player가 가진 block 들의 상태를 state로 설정함.
		System.out.println("[ PlayerWindow : setEnable ]");
		// NPC에 같은 함수 같이 변경해야 함
		for (int i = 0; playerBlock[i] != null; i++) {
			playerBlock[i].setEnabled(state);
			if (blockState.get(i).isOpen()) {
				// 이미 open 된 카드일 경우, 카드 선택이 불가능하도록 설정.
				playerBlock[i].setEnabled(false);
			}
		}
	}

	@Override
	public GUIBlock makeBlock(GameProcess gameProcess, int playerNum, int index) {
		return new UserBlock(gameProcess, playerNum, index);
	}
}