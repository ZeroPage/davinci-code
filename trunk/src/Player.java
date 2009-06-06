import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class Player {
	private int blockNum;//자신이 가진 블록수를 저장
	private ArrayList<Block> hand;//자신이 가진 블록을 저장하는 배열
	private boolean play;//플레이여부를 결정
	
	Player() {
		play = true;
	}
	public void setBlockNum(int n) {
		blockNum = n;
	}
	public void setHand(Block n) {
		
	}
	public void setPlay(boolean n) {
		play = n;
	}
	public int getBlockNum() {
		return blockNum;
	}
	public ArrayList<Block> getHand() {
		return hand;
	}
	public boolean getPlay() {
		return play;
	}
	
	public void getBlock(ArrayList<Block> blocks, int selectedBlock) {
		hand.add(blocks.get(selectedBlock));
		blocks.remove(selectedBlock);
		sortBlock(getHand(), getBlockNum());
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
		Block tb = blocks.get(n1);
		
		blocks.set(n1,blocks.get(n2));
		blocks.set(n2,tb);
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
