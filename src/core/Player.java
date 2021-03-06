package core;

import java.util.ArrayList;

public class Player {
	ArrayList<Block> hand = null; // 자신이 가진 블록을 저장하는 배열
	private Block last = null; // 마지막에 가져온 block. 상대방의 block 을 추리한 게 틀릴 경우 이
								// block 을 공개해야 한다.
	private boolean play = false; // 플레이 여부를 결정
	private PlayerStrategy strategy;

	Player() {
		hand = new ArrayList<Block>();
		play = true;
	}

	public Player(PlayerStrategy strategy) {
		hand = new ArrayList<Block>();
		play = true;
		this.strategy = strategy;
	}

	public Block getLast() {
		return last;
	} // 마지막으로 받은 block 을 반환하는 메소드.

	public ArrayList<Block> getHand() {
		return hand;
	} // player 가 가지고 있는 block 들을 반환.

	public boolean isPlaying() {
		return play;
	} // 현재 play 중인지를 반환.

	// center 에 있던 block 을 가져오는 함수. player 의 소유가 되도록 설정된다.
	public void getBlock(ArrayList<Block> floor, int blockIndex) {
		System.out.println("[ Player : getBlock ]");
		this.strategy.getBlock(this, floor, blockIndex);
	}

	public void askBlock(Player targetPlayer, GameProcess process,
			int selectedBlockIndex, int selectedNum) {
		System.out.println("[ Player : askBlock ]");
		this.strategy.askBlock(this, targetPlayer, process, selectedBlockIndex,
				selectedNum);
	}

	public boolean checkBlock(GameProcess process, int selectedBlockIndex,
			int num) {
		// 다른 player 가 추측한 num 과 내 block의 숫자가 같은지 체크하는 함수.
		System.out.println("[ Player : cheeckBlock ]");
		Block block = hand.get(selectedBlockIndex);
		if (block.getNum() == num) { // 선택된 block 의 숫자가 num 과 같으면
			block.setOpen(true); // 그 block 을 open 상태로 설정.
			process.updateBlock(); // 게임 프로세스의 GUI 상태를 업데이트 한다.
			checkMyTurn(process);
			return true;
		}
		return false;
	}

	public void checkMyTurn(GameProcess process) {
		// player가 계속 play 할수 있는 상태인지를 확인하고 그에
		// 따른 동작을 수행한다.
		for (int i = 0; i < hand.size(); i++)
			// 아직 open 되지 않은 block 이 있을 경우
			if (hand.get(i).isOpen() == false)
				return; // 함수 종료.

		play = false; // 모두 open 되었으므로 play 상태를 false로 설정.
		process.sendChatMsg("패를 모두 알아냈습니다.");
		// 게임 프로세스의 network 타겟으로 메시지 전달.
		process.sendGameData();
		if (process.isEnd())
			process.End();
	}

	public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
		Block tb1 = blocks.get(n1);
		Block tb2 = blocks.get(n2);
		blocks.set(n1, tb2);
		blocks.set(n2, tb1);
	}

	public void sortBlock() {
		// player 의 block 을 정렬.
		Block pre, pst;
		int preCol, pstCol;
		int preNum, pstNum;
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - 1; j++) {
				pre = hand.get(j);
				pst = hand.get(j + 1);
				preCol = pre.getColor(); // block 의 색깔
				pstCol = pst.getColor();
				preNum = pre.getSortingNum(); // block 의 정렬숫자.
				pstNum = pst.getSortingNum();

				if (preNum > pstNum)
					swapBlock(hand, j, j + 1); // 앞의 것이 더 숫자가 크면 순서교환.
				else if ((preNum == pstNum) && (preCol == Block.WHITE)
						&& (pstCol == Block.BLACK))
					swapBlock(hand, j, j + 1);
				// 두 block 의 숫자가 같을 경우에는 검은색 블럭을 왼쪽에 놓는다.
			}
		}
	}

	public void setLast(Block block) {
		this.last = block;
	}

	public void doAction(GameProcess gameProcess, ArrayList<Block> floorBlocks) {
		strategy.doAction(hand.size(), gameProcess, floorBlocks);
	}

	public int handleJoker(Block target, int color) {
		return strategy.handleJoker(target, color);
	}

	public void selectCard(GameProcess gameProcess) {
		strategy.selectCard(gameProcess);
	}

	public boolean isComputer() {
		return strategy instanceof Computer;
	}
}
