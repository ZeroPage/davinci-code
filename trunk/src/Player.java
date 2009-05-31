

public class Player {
	private int blockNum;//자신이 가진 블록수를 저장
	private Block[] hand;//자신이 가진 블록을 저장하는 배열
	private boolean play;//플레이여부를 결정
	
	Player() {
		hand = new Block[13];
		for(int i=0; i<13; i++)
			hand[i] = new Block();
		blockNum = 0;
		play = true;
	}
	public void setBlockNum(int n) {
		blockNum = n;
	}
	public void setHand(Block n) {
		hand[blockNum].setColor(n.getColor());
		hand[blockNum].setNum(n.getNum());
		hand[blockNum].setOwn(n.getOwn());
		blockNum++;
	}
	public void setPlay(boolean n) {
		play = n;
	}
	public int getBlockNum() {
		return blockNum;
	}
	public Block[] getHand() {
		return hand;
	}
	public boolean getPlay() {
		return play;
	}
	
	public void getBlock(Block[] blocks, int selectedBlock) {
		blocks[selectedBlock].setOwn(true);
		setHand(blocks[selectedBlock]);
		sortBlock(getHand(), getBlockNum());
	}
	public boolean askBlock(Player[] players, int selctedPlayer, int selectedBlock, int selectedNum) {		
		return players[selctedPlayer].checkBlock(selectedBlock, selectedNum);
	}
	public boolean checkBlock(int selection, int num) {
		if(hand[selection].getNum() == num) {
			hand[selection].setOpen(true);
			isPlay();
			return true;
		}
		return false;
	}
	public void openBlock(int selectedBlock) {
		hand[selectedBlock].setOpen(true);
	}
	public void isPlay() {
		for(int i=0; i<blockNum; i++) {
			if(hand[i].getOpen() == false)
				return;
		}
		setPlay(false);
	}
	public void swapBlock(Block[] array, int n1, int n2) {
		Block temp = array[n1];
		array[n1] = array[n2];
		array[n2] = temp;
	}
	public void sortBlock(Block[] array, int length) {
		for(int i=1; i<length; i++) {
			int j;
			for(j=i-1; (j>=0) && ((array[i].getNum() > array[j].getNum()) || ((array[i].getNum() == array[j].getNum()) && (array[i].getColor() > array[j].getColor()))); j--)
				if(array[j].getNum() == -1) {
					j--;
					continue;
				}
			Block temp = array[i];
			for(int k=i; (k>=0) && (k>j+1); k--)
				array[k] = array[k-1];
			array[j+1] = temp;
		}
	}
}
