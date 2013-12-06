package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;

import network.Network;

import org.junit.Test;

import core.Block;
import core.Computer;

public class ComputerTest {

	@Test
	public void testGetBlock() {
		// Given
		int blockIndex = 0;
		Computer computer = new Computer();
		MockPlayer player = new MockPlayer();
		Block block1 = new Block(0,0);
		Block block2 = new Block(1,1);
		ArrayList<Block> blockArray = new ArrayList<Block>();
		// When
		blockArray.add(block1);
		blockArray.add(block2);
		assertEquals(2,blockArray.size());
		
		computer.getBlock(player, blockArray, blockIndex);
		// Then
		assertEquals(true,player.getIsCalledSetLast());
		assertEquals(true,player.getIsCalledGetLast());
		assertEquals(true,player.getIsCalledSortBlock());
		
		assertEquals(1,blockArray.size());
	}
	
	@Test
	public void testDoAction(){
		// Given
		int handSize = 0;
		Computer computer = new Computer();
		Network network = new MockNetwork();
		MockGameProcess gameProcess = new MockGameProcess(network);
		ArrayList<Block> emptyBlockList = new ArrayList<Block>();
		ArrayList<Block> filledBlockList = new ArrayList<Block>();
		Block block1 = new Block(0,0);
		Block block2 = new Block(1,1);
		// When
		filledBlockList.add(block1);
		filledBlockList.add(block2);
		assertEquals(2, filledBlockList.size());
		
		computer.doAction(handSize, gameProcess, emptyBlockList);
		computer.doAction(handSize, gameProcess, filledBlockList);
		// Then
		assertEquals(true, gameProcess.getIsCalledNext());
		assertEquals(true, gameProcess.getIsCalledCenterBlockSelect());
	}

	@Test
	public void testHandleJoker(){
		// Given
		int handSize = 0;
		Computer computer = new Computer();
		Block targetBlock = new Block(0,0);
		
		// When
		int result[] = new int[100];
		for(int i =0;i<100;i++){
			result[i] = computer.handleJoker(targetBlock, 0);
		}
		// Then
		for(int i =0;i<100;i++){
			assertEquals(true,result[i] >= 0 || result[i] <= 13);
		}
	}
	
	@Test
	public void testSelectCard(){
		// Given
		Computer computer = new Computer();
		Network network = new MockNetwork();
		MockGameProcess gameProcess = new MockGameProcess(network);
		// When
		computer.selectCard(gameProcess);
		// Then
		assertEquals(true,gameProcess.getIsCalledGetNumOfPlayer());
		assertEquals(true,gameProcess.getIsCalledSelectPlayer());
		assertEquals(true,gameProcess.getIsCalledAskBlock());
	
	}
}
