package core;

import gui.AskDlg;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Human implements PlayerStrategy {

	@Override
	public void getBlock(Player player, ArrayList<Block> floor, int blockIndex) {
		player.setLast(floor.get(blockIndex));
		// 바닥에 깔린 block 들 중 blockIndex 에 해당하는 block 을 선택.
		player.getLast().setOwn(true); // 해당 block 을 소유된 block 으로 설정.
		player.hand.add(player.getLast()); // player 의 block 에 추가한다.
		floor.remove(blockIndex); // 바닥에서 그 block 을 제거한다.
		player.sortBlock(); // 현재 가지고 있는 block 을 정렬한다.
	}

	@Override
	public void askBlock(Player player, Player targetPlayer,
			GameProcess process, int selectedBlockIndex, int selectedNum) {
		if (targetPlayer.checkBlock(process, selectedBlockIndex, selectedNum)) {
			// 추측이 맞으면
			if (JOptionPane.showConfirmDialog(null, "빙고! 계속하시겠습니까?", "확인",
					JOptionPane.YES_NO_OPTION) == 0) {
				process.enableOthers();
			} else {// 계속할 경우 다른 플레이어들의 block 을 선택가능하게 설정.
				process.next(); // 그만둘 경우 다음 player 에게 턴 넘김.
			}
		} else { // 틀렸을 경우
			System.out.println("last = " + player.getLast().getNum());
			process.sendChatMsg("오답입니다."); // 오답 메시지를 채팅창에 보내고
			player.getLast().setOpen(true); // 마지막에 가져온 block 을 공개하도록 설정하고
			process.updateBlock();
			process.next();
		}
	}

	@Override
	public void doAction(int handSize, GameProcess gameProcess,
			ArrayList<Block> floorBlocks) {
		// do nothing
	}

	@Override
	public int handleJoker(Block target, int color) {
		AskDlg diag = new AskDlg(target.getColor());
		return diag.getNum();
	}

	@Override
	public void selectCard(GameProcess gameProcess) {
		// do nothing
	}
}
