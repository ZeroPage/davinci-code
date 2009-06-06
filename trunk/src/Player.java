import java.util.ArrayList;

public class Player {
	private ArrayList<Block> hand;//자신이 가진 블록을 저장하는 배열
	private boolean play;//플레이여부를 결정
	private int loh = -5;//핸드의 가장 왼쪽 숫자.기본값은 범위 밖
	private int roh = 15;//핸드의 가장 오른쪽 숫자.기본값은 범위 밖
	private boolean isBjoker = false;//블랙조커 소유 여부
	private boolean isWjoker = false;//화이트조커 소유 여부
	private boolean isJoker = isBjoker&isWjoker;//조커 소유여부
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
	public void getBlock(ArrayList<Block> blocks, int selectedBlock) {
		hand.add(blocks.get(selectedBlock));
		blocks.remove(selectedBlock);
		/*if(blocks.get(selectedBlock).getNum()==-1)
		{
			if(blocks.get(selectedBlock)==blackJoker)
			int input;//원하는 위치에 클릭.input받음
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
				int choice;//어느쪽에 들어갈지 선택.-1은 왼쪽, 1은 오른쪽(가정)
				if(choice == -1)
				{
					hand.add(, blocks.get(selectedBlock));
				}
			}
		}
			*/
			sortBlock(getHand(), 0, hand.size()-1);
	}
	public boolean askBlock(Player tp, Block tb, int selectedNum) {		
		return tp.checkBlock(tb, selectedNum);
	}
	public boolean checkBlock(Block tb, int num) {
		if(tb.getNum() == num) {
			openBlock(tb);
			isPlay();
			return true;
		}
		return false;
	}
	public void openBlock(Block selectedBlock) {
		selectedBlock.setOpen(true);
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
