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
		public Block			getLast() {	return last; }	// ���������� ���� block �� ��ȯ�ϴ� �޼ҵ�.
		public ArrayList<Block> getHand() {	return hand; }	// player �� ������ �ִ� block ���� ��ȯ.
		public boolean 			getPlay() {	return play; }	// ���� play �������� ��ȯ.
		public void 			getBlock(ArrayList<Block> floor, int blockIndex)	// center �� �ִ� block �� �������� �Լ�. player �� ������ �ǵ��� �����ȴ�.
		{
			last = floor.get(blockIndex);	// �ٴڿ� �� block �� �� blockIndex �� �ش��ϴ� block �� ����.
			last.setOwn(true);				// �ش� block �� ������ block ���� ����.
			hand.add(last);					// player �� block �� �߰��Ѵ�.
			floor.remove(blockIndex);		// �ٴڿ��� �� block �� �����Ѵ�.
			sortBlock();					// ���� ������ �ִ� block �� �����Ѵ�.
			
		}
		public void askBlock(Player targetPlayer, GameProcess proc, int selectedBlockIndex, int selectedNum)
		{
			System.out.println("[ Player : askBlock ]");
			if(targetPlayer.checkBlock(proc, selectedBlockIndex, selectedNum))	// ������ ������
			{
				if(JOptionPane.showConfirmDialog(null, "����! ����Ͻðڽ��ϱ�?", "Ȯ��",JOptionPane.YES_NO_OPTION)==0)
					proc.gameWndGUI.setEnable(GameWindow.OTHERS, true);		// ����� ��� �ٸ� �÷��̾���� block �� ���ð����ϰ� ����.
				else
					proc.Next();							// �׸��� ��� ���� player ���� �� �ѱ�.
			}
			else {			// Ʋ���� ���
				System.out.println("last = "+last.getNum());
				proc.netObject.SendChatMsg("�����Դϴ�.");		// ���� �޽����� ä��â�� ������
				last.setOpen(true);								// �������� ������ block �� �����ϵ��� �����ϰ�
				proc.gameWndGUI.update();
				proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.getGameEnv())));
				proc.Next();
			}
		}

		public boolean checkBlock(GameProcess proc, int selectedBlockIndex, int num)	// �ٸ� player �� ������ num ��  �� block�� ���ڰ� ������ üũ�ϴ� �Լ�.
		{
			System.out.println("[ Player : cheeckBlock ]");
			Block b_tmp = hand.get(selectedBlockIndex); 
			if(b_tmp.getNum() == num) {	// ���õ� block �� ���ڰ� num �� ������
				b_tmp.setOpen(true);		// �� block �� open ���·� ����.
				proc.gameWndGUI.update();						// ���� ���μ����� GUI ���¸� ������Ʈ �Ѵ�.
				proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.getGameEnv())));
				// ���� ���μ����� ��Ʈ��ũ�� ���� ���ӻ���(GameData(proc.gameControl))��ü�� �����Ѵ�.
				isPlay(proc);
				return true;
			}
			return false;
		}
		public void isPlay(GameProcess proc)					// player�� ��� play �Ҽ� �ִ� ���������� Ȯ���ϰ� �׿� ���� ������ �����Ѵ�.
		{
			for(int i = 0; i < hand.size(); i++)				// ���� open ���� ���� block �� ���� ���
				if(hand.get(i).isOpen() == false)	return;		// �Լ� ����.

			play = false;										// ��� open �Ǿ����Ƿ� play ���¸� false�� ����.
			proc.netObject.SendChatMsg("�и� ��� �˾Ƴ½��ϴ�.");	// ���� ���μ����� network Ÿ������ �޽��� ����.
			proc.netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(proc.getGameEnv())));
			if(proc.getGameEnv().isEnd())
				proc.End();
		}
		public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
			Block tb1 = blocks.get(n1);
			Block tb2 = blocks.get(n2);
			blocks.set(n1,tb2);
			blocks.set(n2,tb1);
		}
		public void sortBlock()			// player �� block �� ����.
		{
			Block	pre, pst;
			int		preCol, pstCol;
			int		preNum,	pstNum;
			for(int i = 0; i < hand.size()-1 ; i++)
			{
				for(int j = 0 ; j < hand.size()-1 ; j++)
				{
					pre	= hand.get(j);
					pst	= hand.get(j+1);
					preCol	= pre.getColor();		// block �� ����
					pstCol	= pst.getColor();
					preNum	= pre.getSortingNum();	// block �� ���ļ���.
					pstNum	= pst.getSortingNum();
					
					if( preNum > pstNum )
						swapBlock(hand, j, j+1);	// ���� ���� �� ���ڰ� ũ�� ������ȯ.
					else if( (preNum == pstNum) && ( preCol == Block.WHITE) && (pstCol == Block.BLACK ) )
						swapBlock(hand, j, j+1);	// �� block �� ���ڰ� ���� ��쿡�� ������ ���� ���ʿ� ���´�.
				}
			}
		}
	}