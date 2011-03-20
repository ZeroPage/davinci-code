import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Player implements Serializable
{
		ArrayList<Block>	hand	= null;		//�ڽ��� ���� ����� �����ϴ� �迭
		private Block		last	= null;		//�������� ������ block. ������ block �� �߸��� �� Ʋ�� ��� �� block �� �����ؾ� �Ѵ�.
		private boolean		play	= false;	//�÷��� ���θ� ����
		
		Player() {
			hand = new ArrayList<Block>();
			play = true;
		}
		public Block			getLast() {	return last; }
		public ArrayList<Block> getHand() {	return hand; }	// player �� ������ �ִ� block ���� ��ȯ.
		public boolean 			getPlay() {	return play; }	// ���� play �������� ��ȯ.
		public void 			getBlock(ArrayList<Block> floor, int blockNum)	// center �� �ִ� block �� �������� �Լ�. player �� ������ �ǵ��� �����ȴ�.
		{
			last = floor.get(blockNum);		// �ٴڿ� �� block �� �� blockNum �� �ش��ϴ� block �� ����.
			hand.add(last);					// player �� block �� �߰��Ѵ�.
			last.setOwn(true);				// �ش� block �� ������ block ���� ����.
			floor.remove(blockNum);			// �ٴڿ��� �� block �� �����Ѵ�.
			sortBlock();					// ���� ������ �ִ� block �� �����Ѵ�.
		}
		public void askBlock(Player targetPlayer, GameProcess proc, int selectedBlockIndex, int selectedNum) {
			if(targetPlayer.checkBlock(proc, selectedBlockIndex, selectedNum))	// ������ ������
			{
				if(JOptionPane.showConfirmDialog(null, "����! ����Ͻðڽ��ϱ�?", "Ȯ��",JOptionPane.YES_NO_OPTION)==0)
					proc.gameWndGUI.setEnable(GameWindow.OTHERS, true);		// ����� ��� �ٸ� �÷��̾���� block �� ���ð����ϰ� ����.
				else
					proc.Next();							// �׸��� ��� ���� player ���� �� �ѱ�.
			}
			else {			// Ʋ���� ���
				proc.netObject.SendChatMsg("�����Դϴ�.");		// ���� �޽����� ä��â�� ������
				last.setOpen(true);								// �������� ������ block �� �����ϵ��� �����ϰ�
				proc.gameWndGUI.update();
				proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.gameControl)));
				proc.Next();
			}
//			return targetPlayer.getHand().get(selectedBlockIndex).getNum(); 
		}
		public int askBlock(Player targetPlayer, int selectedBlockIndex) {
			return targetPlayer.getHand().get(selectedBlockIndex).getNum(); 
		}

		public boolean checkBlock(GameProcess proc, int selectedBlockIndex, int num) {	// �ٸ� player �� ������ num �� block�� ���ڰ� ������ üũ�ϴ� �޼ҵ�.  
			if(hand.get(selectedBlockIndex).getNum() == num) {	// ���õ� block �� ���ڰ� num �� ������
				hand.get(selectedBlockIndex).setOpen(true);		// �� block �� open ���·� ����.
				proc.gameWndGUI.update();						// ���� ���μ����� GUI ���¸� ������Ʈ �Ѵ�.
				proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.gameControl)));
				// ���� ���μ����� ��Ʈ��ũ�� ���� ���ӻ���(GameData(proc.gameControl))��ü�� �����Ѵ�.
				isPlay(proc);
				return true;
			}
			return false;
		}
		public void isPlay(GameProcess proc) {					// player�� ��� play �Ҽ� �ִ� ���������� Ȯ���ϰ� �׿� ���� ������ �����Ѵ�.
			for(int i=0; i<hand.size(); i++) {					// ���� open ���� ���� block �� ���� ���
				if(hand.get(i).isOpen() == false)
					return;										// �Լ� ����.
			}
			play = false;										// ��� open �Ǿ����Ƿ� play ���¸� false�� ����.
			proc.netObject.SendChatMsg("�и� ��� �˾Ƴ½��ϴ�.");	// ���� ���μ����� network Ÿ������ �޽��� ����.
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
		public void sortBlock() {			// player �� block �� ���´�.
			for(int i = 0; i<hand.size()-1;i++)					// player �� block ���� ����.
			{
				for(int j=0;j<hand.size()-1;j++) {
					if(hand.get(j).getSortingNum()>hand.get(j+1).getSortingNum())
						swapBlock(hand, j, j+1);
					if((hand.get(j).getSortingNum()==hand.get(j+1).getSortingNum()) && (hand.get(j).getColor()==1) && (hand.get(j+1).getColor()==0))
						swapBlock(hand, j, j+1);				// �� block �� ���ڰ� ���� ��쿡�� ������ ���� ���ʿ� ���´�.
				}
			}
		}
	}