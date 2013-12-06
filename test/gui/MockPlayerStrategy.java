package gui;

import java.util.ArrayList;

import core.Block;
import core.GameProcess;
import core.Player;
import core.PlayerStrategy;

public class MockPlayerStrategy implements PlayerStrategy{
	public boolean isCallGetBlock = false;
	public boolean isCalledAskBlock = false;
	public boolean isCallDoAction = false;
	public boolean isCallHandleJoker = false;
	public boolean isCallSelectCard = false;
	
	@Override
	public void getBlock(Player player, ArrayList<Block> floor, int blockIndex) {
		
		isCallGetBlock = true;
	}

	@Override
	public void askBlock(Player player, Player targetPlayer,
			GameProcess process, int selectedBlockIndex, int selectedNum) {
		
		isCalledAskBlock = true;
	}

	@Override
	public void doAction(int handSize, GameProcess gameProcess,
			ArrayList<Block> floorBlocks) {
		
		isCallDoAction = true;
	}

	@Override
	public int handleJoker(Block target, int color) {
		isCallHandleJoker = true;
		return 3;
	}

	@Override
	public void selectCard(GameProcess gameProcess) {
		isCallSelectCard = true;
	}

}
