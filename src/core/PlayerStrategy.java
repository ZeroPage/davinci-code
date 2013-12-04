package core;

import java.io.Serializable;
import java.util.ArrayList;

public interface PlayerStrategy extends Serializable {
	public void getBlock(Player player, ArrayList<Block> floor, int blockIndex);
	public void askBlock(Player player, Player targetPlayer, GameProcess process, int selectedBlockIndex, int selectedNum);
	public void doAction(int handSize, GameProcess gameProcess, ArrayList<Block> floorBlocks);
	public int handleJoker(Block target, int color);
	public void selectCard(GameProcess gameProcess);
}
