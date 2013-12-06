package gui;

import network.Network;
import core.GameProcess;
import core.Player;

public class MockGameProcess extends GameProcess{

	private boolean isCalledCenterBlockSelect = false;
	private boolean isCalledNext = false;
	private boolean isCalledSelectPlayer = false;
	private boolean isCalledAskBlock = false;
	private boolean isCalledGetNumOfPlayer = false;
	private MockPlayer mockPlayer;
	public MockGameProcess(Network network) {
		super(network);
		// TODO Auto-generated constructor stub
	}
	
	public void centerBlockSelect(int blockIndex) {
		isCalledCenterBlockSelect = true;
	}
	
	public Player selectPlayer(int i){
		isCalledSelectPlayer = true;
		mockPlayer = new MockPlayer();
		return mockPlayer;
	}
	
	public int getNumOfPlayer() {
		isCalledGetNumOfPlayer = true;
		return 2;
	}
	
	public void AskBlock(int playerOrder, int index, int num){
		isCalledAskBlock = true;
	}
	public void next() {
		isCalledNext = true;
	}
	
	public boolean getIsCalledCenterBlockSelect(){
		return isCalledCenterBlockSelect;
	}
	
	public boolean getIsCalledNext(){
		return isCalledNext;
	}
	
	public boolean getIsCalledSelectPlayer(){
		return isCalledSelectPlayer;
	}
	
	public boolean getIsCalledAskBlock(){
		return isCalledAskBlock;
	}
	
	public boolean getIsCalledGetNumOfPlayer(){
		return isCalledGetNumOfPlayer;
	}
	public MockPlayer getMockPlayer(){
		return mockPlayer;
	}
}
