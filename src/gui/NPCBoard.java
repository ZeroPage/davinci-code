package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.Block;
import core.GameProcess;



public class NPCBoard extends PlayerBoard {
	public NPCBoard(JPanel mainPanel, GameProcess gameProcess) {
		super();
		this.gameProcess = gameProcess;
		m_Panel = new JPanel();
		m_WindowNum = 5;
		playerBlock = new NPCBlock[27]; // 모두 27개의 block 들.
		mainPanel.add(BorderLayout.CENTER, m_Panel);
		m_Panel.setOpaque(false);
	}

	public void update(ArrayList<Block> blocks) {
		System.out.println("[ NPC : update ]");
		for(int i = 0; i < blocks.size(); i++){
			if(playerBlock[i] == null){
				//TODO maybe apply abstract factory
				playerBlock[i] = new NPCBlock(gameProcess, playerOrder, i);
				m_Panel.add(playerBlock[i]);
			}
			playerBlock[i].update(blocks.get(i));
		}
		// center 가 가진 block 들은 아무것도 소유되어진 것이 없기 때문에 모두 뒷면으로 이미지가 설정된다.
		for (int i = blocks.size(); playerBlock[i] != null; i++) {
			// playerBlock[i].removeAll();
			m_Panel.remove(playerBlock[i]);
			m_Panel.repaint();
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
}