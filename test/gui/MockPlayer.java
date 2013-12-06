package gui;

import java.util.ArrayList;

import core.Block;
import core.Player;
import core.PlayerStrategy;

public class MockPlayer extends Player{
	
	private boolean isCalledSetLast = false;
	private boolean isCalledGetLast = false;
	private boolean isCalledSortBlock = false;
	private boolean isCalledGetHand = false;
	
	public MockPlayer() {
		super(new MockPlayerStrategy());
	}
	
	public void setLast(Block block) {
		super.setLast(block);
		isCalledSetLast = true;
	}
	
	public ArrayList<Block> getHand(){
		ArrayList<Block> blockList = new ArrayList<Block>();
		Block block1 = new Block(0,0);
		Block block2 = new Block(1,1);
		
		blockList.add(block1);
		blockList.add(block2);
		isCalledGetHand = true;
		
		return blockList;
	}
	public Block getLast() {
		isCalledGetLast = true;
		return super.getLast();
	}
	
	public void sortBlock(){
		super.sortBlock();
		isCalledSortBlock = true;
	}
	
	public boolean getIsCalledSetLast(){
		return isCalledSetLast;
	}
	public boolean getIsCalledGetLast(){
		return isCalledGetLast;
	}
	public boolean getIsCalledSortBlock(){
		return isCalledSortBlock;
	}
	public boolean getIsCalledGetHand(){
		return isCalledGetHand;
	}
}
