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

	public void actionPerformed(ActionEvent e) {
		// center에서 선택된 block 의 index 를 넘겨 block 을 player 에게 전달하도록 한다.
		// 가운데는 선택되면 이것은 처음에 가운데 블럭을 선택하기 위한것이다.
		// moveblock을 호출해서 가운데것을 가져가게 하면 된다.
		// 그리고 선택된것은 빼어버린다.(업데이트가 나으려나.)
		gameProcess.getGameWndGUI().setEnable(GameWindow.CENTER, false);
		// block 을 가져가고난 후에는 다시 center의 block 을 선택하지 못하게 막는다.
		for (int i = 0; i < 26; i++) {
			if (playerBlock[i] != null) {
				if (e.getSource() == playerBlock[i]) {
					gameProcess.moveBlock(i);
				}
			}
		}
	}
}