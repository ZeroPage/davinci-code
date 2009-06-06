import java.util.ArrayList;

import DavichiGUI.RoomWindow.GameWindow;

public class Player {
	private ArrayList<Block> hand;//�ڽ��� ���� ����� �����ϴ� �迭
	private boolean play;//�÷��̿��θ� ����
	private int loh = -5;//�ڵ��� ���� ���� ����.�⺻���� ���� ��
	private int roh = 15;//�ڵ��� ���� ������ ����.�⺻���� ���� ��
	private boolean isBjoker = false;//����Ŀ ���� ����
	private boolean isWjoker = false;//ȭ��Ʈ��Ŀ ���� ����
	private boolean isJoker = isBjoker&isWjoker;//��Ŀ ��������
	Player() {
		play = true;
	}
	public void setHand(Block n) {
		
	}
	public void setPlay(boolean n) {
		play = n;
	}
	public void setLoh(int n) {
		loh = n;
	}
	public void setRoh(int n) {
		roh = n;
	}
	public void setIsbjoker(boolean n) {
		isBjoker = n;
	}
	public void setIswjoker(boolean n) {
		isWjoker = n;
	}
	public ArrayList<Block> getHand() {
		return hand;
	}
	public boolean getPlay() {
		return play;
	}
	public int getLoh() {
		return loh;
	}
	public int getRoh() {
		return roh;
	}
	public boolean getIsbjoker() {
		return isBjoker;
	}
	public boolean getIswjoker() {
		return isWjoker;
	}
	public boolean getIsjoker() {
		return isJoker;
	}
	public void selectBlock()
	{
		GameWindow.
	}
	public void getBlock(Game DC,int blockNum) {
		hand.add(DC.getBlocks().get(blockNum));
		
		/*if(DC.getBlocks().get(blockNum).getNum()==-1)
		{
			if(DC.getBlocks().get(blockNum).getColor()==0)
				setIsbjoker(true);
			else
				setIswjoker(true);
			int input;//���ϴ� ��ġ�� Ŭ��.input����
			hand.add(input,blocks.get(selectedBlock));
			blocks.remove(selectedBlock);
			if(input==0)
			{
				setRoh(hand.get(1).getNum());
			}
			else if(input>=hand.get(hand.size()-1).getNum())
			{
				setLoh(hand.get(hand.size()-2).getNum());
			}
			else
			{
				setRoh(hand.get(input-1).getNum());
				setLoh(hand.get(input+1).getNum());
			}
		}
		else if(blocks.get(selectedBlock).getNum()!=-1&&getIsjoker()==true)
		{
			if(blocks.get(selectedBlock).getNum()>loh && blocks.get(selectedBlock).getNum()<roh)
			{
				int choice;//����ʿ� ���� ����.-1�� ����, 1�� ������(����)
				if(choice == -1)
				{
					hand.add(, blocks.get(selectedBlock));
				}
			}
		}
			*/
			//sortBlock(getHand(), 0, hand.size()-1);
	}
	public void askBlock(int turnPlayernum) {		
		int selectedPlayer;//�� �߰� ��������
		int selectedBlock;
		int selectedNum;
		if(players.get(selectedPlayer).checkBlock(selectedBlock, selectedNum)==true)
		{
			boolean again;//�ٽ� �߸���������
			if(again==true)
				askBlock(turnPlayernum);
		}
		else
		{
			int num;//������ ���� �ε���.�÷��̾ ����
			players.get(turnPlayernum).getHand().get(num).setOpen(true);
		}

	}
	public boolean checkBlock(int selectedBlock, int num) {
		if(hand.get(selectedBlock).getNum() == num) {
			hand.get(selectedBlock).setOpen(true);
			isPlay();
			return true;
		}
		return false;
	}
	public void isPlay() {
		for(int i=0; i<hand.size(); i++) {
			Block tb = hand.get(i);
			if(tb.getOpen() == false)
				return;
		}
		setPlay(false);
	}
	public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
		Block tb1 = blocks.get(n1);
		Block tb2 = blocks.get(n2);
		blocks.set(n1,tb2);
		blocks.set(n2,tb1);
	}
	public void sortBlock(ArrayList<Block> blocks, int s, int e) {
		for(int i = s; i<e;i++)
		{
			for(int j=s;j<e;j++){
				if(blocks.get(j).getNum()>blocks.get(j+1).getNum())
					swapBlock(blocks, j, j+1);
			}
		}
	}
}
