package core;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 하나의 호스트에서 보여져야 할 player 들과 block 들을 게임 시작 전에 준비하는 클래스.
 * @author Teolex
 * 
 */
public class Game implements Serializable {
	// 게임의 시작을 위한 초기 작업을 수행한다.
	private ArrayList<Player> players; // 게임 내의 player 들을 저장할 리스트.
	private ArrayList<Block> floor; // 게임중 바닥에 깔려있을 block 들을 저장할 리스트.

	public Game(int numOfPlayer) {
		players = new ArrayList<Player>(); // player 들을 저장할 리스트.
		floor = new ArrayList<Block>(); // 모든 block 들을 저장할 리스트.
		setPlayers(numOfPlayer); // playerNum 명의 player 를 생성함.
		makeFloorBlocks(); // block 들을 생성함.
		mixBlocks(floor); // 생성한 block 들을 섞음.
	}

	public void setFloor(ArrayList<Block> target) {
		floor = target;
	}

	public void setPlayers(int numOfPlayer) {
		for (int i = 0; i < numOfPlayer; i++)
			players.add(new Player(new Human()));
			/*
			if ( i == 0 ) {
				players.add(new Player(new Human()));
			} else {
				players.add(new Player(new Computer()));
			}
			*/
	}// 주어진 인자 만큼의 player 를 생성한다.

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Player getPlayer(int index) {
		return players.get(index);
	}

	// Block( int color, int num ), Black 0 : white 1
	public void makeFloorBlocks() {
		for (int i = 0; i < 26; i++)
			floor.add(new Block(i / 13, i % 13));
	}// block 들의 숫자와 색을 설정한 뒤 blocks 에 추가한다.

	public ArrayList<Block> getFloorBlocks() {
		return floor;
	}

	public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
		Block tb = blocks.get(n1);

		blocks.set(n1, blocks.get(n2));
		blocks.set(n2, tb);
	}

	public void mixBlocks(ArrayList<Block> blocks) {
		// 게임 컨트롤이 생성한 block 들의 순서를 섞는다.
		int n1, n2;
		for (int i = 0; i < 50; i++) {
			n1 = (int) (Math.random() * 26);
			n2 = (int) (Math.random() * 26);
			swapBlock(blocks, n1, n2);
		}
	}

	public boolean isEnd() {
		// player 들의 상태가 어떤지 확인하고 게임이 끝났는지 확인하는 메소드.
		int alive = 0;
		for (int i = 0; i < players.size(); i++)
			if (players.get(i).isPlaying() == true)
				alive++;
		return alive == 1; // 남아있는 것이 혼자이면 게임이 끝난 것.
	}
}
