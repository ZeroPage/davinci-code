package gui;

import java.util.ArrayList;

import javax.swing.JPanel;

import core.Block;
import core.GameProcess;

public abstract class PlayBoard {

	GUIBlock [] playerBlock;
	JPanel m_Panel;
	int playerOrder;
	final int windowSize = 100;
	protected GameProcess gameProcess;
	
	public PlayBoard(){
		m_Panel = new JPanel(); // 게임 내에서 player 의 block 이 놓이는 위치
	}
	
	public abstract GUIBlock makeBoard(GameProcess gameProcess, int playerNum, int index);

	public void update(ArrayList<Block> blocks){
		for(int i = 0; i < blocks.size(); i++){
			if(playerBlock[i] == null){
				playerBlock[i] = makeBoard(gameProcess, playerOrder, i);
				m_Panel.add(playerBlock[i]);
			}
			playerBlock[i].update(blocks.get(i));
		}
	}
	 
	public abstract void setEnable(ArrayList<Block> blockState, boolean state);
		
	
}
