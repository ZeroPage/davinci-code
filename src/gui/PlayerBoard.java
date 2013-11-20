package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.Block;
import core.GameProcess;

public class PlayerBoard {
	
	GUIBlock [] playerBlock;
	
	JPanel m_Panel;
	int m_WindowNum;
	int playerOrder;
	final int windowSize = 100;

	protected GameProcess gameProcess;

	protected PlayerBoard() { // 하위클래스에서 필요한것 바깥에서는 위험하므로 쓰지말것.
	} // 내용 없음.

	public PlayerBoard(int PlayerNum, JPanel mainPanel, GameProcess gameProcess) {
		this.gameProcess = gameProcess;
		m_Panel = new JPanel(); // 게임 내에서 player 의 block 이 놓이는 위치
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, -1, -1);
		playerOrder = PlayerNum;

		playerBlock = new PlayerBlock[13];
		
		String lo = "";
		switch (PlayerNum) {
		// PlayerNum 에 따라 다른 사람들과 자신의 block 이 놓일 위치를 설정한다.
		case 0:
			lo = BorderLayout.SOUTH;
			m_Panel.setPreferredSize(new Dimension(0, windowSize));
			break;
		case 1:
			lo = BorderLayout.WEST;
			m_Panel.setPreferredSize(new Dimension(windowSize, 0));
			layout.setAlignment(FlowLayout.LEADING);
			break;
		case 2:
			lo = BorderLayout.NORTH;
			m_Panel.setPreferredSize(new Dimension(0, windowSize));
			break;
		case 3:
			lo = BorderLayout.EAST;
			m_Panel.setPreferredSize(new Dimension(windowSize, 0));
			layout.setAlignment(FlowLayout.TRAILING);
			break;
		default:
			break;
		}
		m_Panel.setLayout(layout);
		mainPanel.add(lo, m_Panel);
		m_Panel.setOpaque(false); // 불투명하게 설정하는 메소드에 false를 주어 투명하게 만듬.
	}

	public void setEnable(ArrayList<Block> blockState, boolean state) {
		// player가 가진 block 들의 상태를 state로 설정함.
		System.out.println("[ PlayerWindow : setEnable ]");
		// NPC에 같은 함수 같이 변경해야 함
		for (int i = 0; playerBlock[i] != null; i++) {
			playerBlock[i].setEnabled(state);
			
			if (blockState.get(i).isOpen()) { // 이미 open 된 카드일 경우,
				playerBlock[i].setEnabled(false); // 카드 선택이 불가능하도록 설정.
			}
		}
	}

	public void update(ArrayList<Block> blocks) {
		
		for(int i = 0; i < blocks.size(); i++){
			if(playerBlock[i] == null){
				playerBlock[i] = new PlayerBlock(gameProcess, playerOrder, i);
				m_Panel.add(playerBlock[i]);
			}
			playerBlock[i].update(blocks.get(i));
		}
	}
}