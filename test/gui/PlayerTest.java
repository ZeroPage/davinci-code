package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;

import network.Network;

import org.junit.Test;

import core.Block;
import core.Computer;
import core.GameProcess;
import core.Player;

public class PlayerTest {
	@Test
	public void testAskBlock() {
		// Given
		Network network = new MockNetwork();
		GameProcess gameProcess = new GameProcess(network);
		MockPlayerStrategy mockStrategy = new MockPlayerStrategy();
		Player player1 = new Player(mockStrategy);
		Player player2 = new Player(mockStrategy);
		// When
		player1.askBlock(player2, gameProcess, 0, 0);
		
		// Then
		assertEquals(true, mockStrategy.isCalledAskBlock);
	}

	@Test
	public void testHandleJoker() {
		// Given
		MockPlayerStrategy mockStrategy = new MockPlayerStrategy();
		Player player1 = new Player(mockStrategy);
		Block block = new Block(0, 0);
		// When
		int blockResult = player1.handleJoker(block, 0);
		// Then
		assertEquals(true, mockStrategy.isCallHandleJoker);
		
		assertEquals(mockStrategy.handleJoker(block, 0), blockResult);
	}
	
	@Test
	public void testSelectCard() {
		// Given
		Network network = new MockNetwork();
		GameProcess gameProcess = new GameProcess(network);
		MockPlayerStrategy mockStrategy = new MockPlayerStrategy();
		Player player1 = new Player(mockStrategy);
		// When
		player1.selectCard(gameProcess);
		// Then
		assertEquals(true, mockStrategy.isCallSelectCard);
	}
	
	@Test
	public void testDoAction(){
		// Given
		Network network = new MockNetwork();
		GameProcess gameProcess = new GameProcess(network);
		MockPlayerStrategy mockStrategy = new MockPlayerStrategy();
		Player player1 = new Player(mockStrategy);
		ArrayList<Block> arrayBlock = new ArrayList<Block>();
		// When
		player1.doAction(gameProcess, arrayBlock);
		// Then
		assertEquals(true, mockStrategy.isCallDoAction);
	}
	
	@Test
	public void testGetBlcok(){
		// Given
		Network network = new MockNetwork();
		GameProcess gameProcess = new GameProcess(network);
		MockPlayerStrategy mockStrategy = new MockPlayerStrategy();
		Player player1 = new Player(mockStrategy);
		ArrayList<Block> arrayBlock = new ArrayList<Block>();
		// When
		player1.getBlock(arrayBlock, 0);
		
		// Then
		assertEquals(true,mockStrategy.isCallGetBlock);
	}
}
