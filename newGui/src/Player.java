import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Player implements Serializable
{
		ArrayList<Block>	hand	= null;		//자신이 가진 블록을 저장하는 배열
		private Block		last	= null;		//마지막에 가져온 block. 상대방의 block 을 추리한 게 틀릴 경우 이 block 을 공개해야 한다.
		private boolean		play	= false;	//플레이 여부를 결정
		
		Player() {
			hand = new ArrayList<Block>();
			play = true;
		}
		public Block			getLast() {	return last; }
		public ArrayList<Block> getHand() {	return hand; }	// player 가 가지고 있는 block 들을 반환.
		public boolean 			getPlay() {	return play; }	// 현재 play 중인지를 반환.
		public void 			getBlock(ArrayList<Block> floor, int blockNum)	// center 에 있던 block 을 가져오는 함수. player 의 소유가 되도록 설정된다.
		{
			last = floor.get(blockNum);		// 바닥에 깔린 block 들 중 blockNum 에 해당하는 block 을 선택.
			hand.add(last);					// player 의 block 에 추가한다.
			last.setOwn(true);				// 해당 block 을 소유된 block 으로 설정.
			floor.remove(blockNum);			// 바닥에서 그 block 을 제거한다.
			sortBlock();					// 현재 가지고 있는 block 을 정렬한다.
		}
		public void askBlock(Player targetPlayer, GameProcess proc, int selectedBlockIndex, int selectedNum) {
			if(targetPlayer.checkBlock(proc, selectedBlockIndex, selectedNum))	// 추측이 맞으면
			{
				if(JOptionPane.showConfirmDialog(null, "빙고! 계속하시겠습니까?", "확인",JOptionPane.YES_NO_OPTION)==0)
					proc.gameWndGUI.setEnable(GameWindow.OTHERS, true);		// 계속할 경우 다른 플레이어들의 block 을 선택가능하게 설정.
				else
					proc.Next();							// 그만둘 경우 다음 player 에게 턴 넘김.
			}
			else {			// 틀렸을 경우
				proc.netObject.SendChatMsg("오답입니다.");		// 오답 메시지를 채팅창에 보내고
				last.setOpen(true);								// 마지막에 가져온 block 을 공개하도록 설정하고
				proc.gameWndGUI.update();
				proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.gameControl)));
				proc.Next();
			}
//			return targetPlayer.getHand().get(selectedBlockIndex).getNum(); 
		}
		public int askBlock(Player targetPlayer, int selectedBlockIndex) {
			return targetPlayer.getHand().get(selectedBlockIndex).getNum(); 
		}

		public boolean checkBlock(GameProcess proc, int selectedBlockIndex, int num) {	// 다른 player 가 추측한 num 과 block의 숫자가 같은지 체크하는 메소드.  
			if(hand.get(selectedBlockIndex).getNum() == num) {	// 선택된 block 의 숫자가 num 과 같으면
				hand.get(selectedBlockIndex).setOpen(true);		// 그 block 을 open 상태로 설정.
				proc.gameWndGUI.update();						// 게임 프로세스의 GUI 상태를 업데이트 한다.
				proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.gameControl)));
				// 게임 프로세스의 네트워크에 현재 게임상태(GameData(proc.gameControl))객체를 전달한다.
				isPlay(proc);
				return true;
			}
			return false;
		}
		public void isPlay(GameProcess proc) {					// player가 계속 play 할수 있는 상태인지를 확인하고 그에 따른 동작을 수행한다.
			for(int i=0; i<hand.size(); i++) {					// 아직 open 되지 않은 block 이 있을 경우
				if(hand.get(i).isOpen() == false)
					return;										// 함수 종료.
			}
			play = false;										// 모두 open 되었으므로 play 상태를 false로 설정.
			proc.netObject.SendChatMsg("패를 모두 알아냈습니다.");	// 게임 프로세스의 network 타겟으로 메시지 전달.
			proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.gameControl)));
			if(proc.gameControl.isEnd())
				proc.End();
		}
		public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
			Block tb1 = blocks.get(n1);
			Block tb2 = blocks.get(n2);
			blocks.set(n1,tb2);
			blocks.set(n2,tb1);
		}
		public void sortBlock() {			// player 의 block 을 섞는다.
			for(int i = 0; i<hand.size()-1;i++)					// player 의 block 들을 정렬.
			{
				for(int j=0;j<hand.size()-1;j++) {
					if(hand.get(j).getSortingNum()>hand.get(j+1).getSortingNum())
						swapBlock(hand, j, j+1);
					if((hand.get(j).getSortingNum()==hand.get(j+1).getSortingNum()) && (hand.get(j).getColor()==1) && (hand.get(j+1).getColor()==0))
						swapBlock(hand, j, j+1);				// 두 block 의 숫자가 같을 경우에는 검은색 블럭을 왼쪽에 놓는다.
				}
			}
		}
	}