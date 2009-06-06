import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class Player {
	private ArrayList<Block> hand;//자신이 가진 블록을 저장하는 배열
	private boolean play;//플레이여부를 결정
	
	Player() {
		play = true;
	}
	public void setHand(Block n) {
		
	}
	public void setPlay(boolean n) {
		play = n;
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
		if(blocks.get(selectedBlock).getNum()==-1)
		{
			//원하는 위치에 클릭 삽입
			//if(삽입위치가 맨 앞)
		}
		else if(blocks.get(selectedBlock).getNum()!=-1)
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
	public void sortBlock(ArrayList<Block> blocks, int s, int e) {
		for(int i = s; i<e;i++)
		{
			for(int j=s;j<e;j++){
				Block tb1 = blocks.get(j);
				Block tb2 = blocks.get(j+1);
				if(tb1.getNum()>tb2.getNum())
				{
					blocks.set(j, tb2);
					blocks.set(j+1, tb1);
				}
			}
		}
	}
}
