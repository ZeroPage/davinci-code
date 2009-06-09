import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Game implements Serializable {
	ArrayList<Player> players;
	ArrayList<Block> blocks;
	transient private GameProcess module;
	
	Game(GameProcess pro,  int n)
	{
		module = pro;
		players = new ArrayList<Player>();
		blocks = new ArrayList<Block>();
		setPlayers(n);
		setBlocks();
		mixBlocks(blocks);
	}
	public void setModule(GameProcess module)
	{
		this.module = module;
	}
	public void setPlayers(int n) {
		Player[] p = new Player[n];
		for(int i=0; i<n; i++)
		{
			p[i] = new Player();
			players.add(p[i]);
		}
	}
	public void setBlocks() {
		Block[] b = new Block[26];

		for(int i=0; i<26; i++)
		{
			b[i] = new Block(((i<13) ? 0 : 1), i%13);
			if(i==12||i==25)
				b[i].setNum(-1);
			blocks.add(b[i]);
		}
	}
	public void setBlocks(ArrayList<Block> tb)
	{
		for(int i=0;i<tb.size();i++)
			blocks.add(tb.get(i));
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
		Block tb = blocks.get(n1);

		blocks.set(n1,blocks.get(n2));
		blocks.set(n2,tb);
	}
	public void mixBlocks(ArrayList<Block> blocks) {
		int n1 = (int)(Math.random()*26);
		int n2 = (int)(Math.random()*26);

		for(int i=0; i<50; i++) {
			swapBlock(blocks, n1, n2);
			n1 = (int)(Math.random()*26);
			n2 = (int)(Math.random()*26);
		}
	}

	/*for(int i=0; i<playerNum; i++) {
			for(int j=0; j<4; j++) { // 시작하면 j 개 만큼 패를 가진다.
				System.out.println("\n\n");
				printBlocks(blocks);
				//players[i].getBlock(blocks);
			}
		}
		int i=0;
		while(End()) {
			if(players[i].getPlay() == false) {
				i = ++i % 2;
				continue;
			}

			System.out.println("\n\n");
			printBlocks(blocks);

			for(int k=0; k<playerNum; k++) {
				System.out.println("\n\n");
				System.out.println("Player : " + (k+1));
				printBlocks(players[k].getHand());
				System.out.println();
			}

			players[i].getBlock(blocks);
			sortBlock(players[i].getHand(), players[i].getBlockNum());

			players[i].askBlock(players);				

			i = ++i % playerNum;
		}*/

	public boolean End() {
		int alive = 0;
		for(int i=0; i<players.size(); i++) {
			Player tp = players.get(i);
			if(true == tp.getPlay())
				alive++;
		}
		return alive<1;
	}

	public class Player implements Serializable {
		ArrayList<Block> hand;//자신이 가진 블록을 저장하는 배열
		private Block last;
		private boolean play;//플레이여부를 결정
		private int loh = -5;//핸드의 가장 왼쪽 숫자.기본값은 범위 밖
		private int roh = 15;//핸드의 가장 오른쪽 숫자.기본값은 범위 밖
		private boolean isBjoker = false;//블랙조커 소유 여부
		private boolean isWjoker = false;//화이트조커 소유 여부
		private boolean isJoker = isBjoker&isWjoker;//조커 소유여부
		Player() {
			hand = new ArrayList<Block>();
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
			module.m_GUITaget.setCenterEnable(true);
		}
		public void getBlock(int blockNum) {
			last = blocks.get(blockNum);
			hand.add(last);
			hand.get(hand.size()-1).setOwn(true);
			blocks.remove(blockNum);
			module.m_NetTaget.SendOb(new DataHeader("game2", new GameData(module.GC)));
		}
			/*if(DC.getBlocks().get(blockNum).getNum()==-1)
			{
				if(DC.getBlocks().get(blockNum).getColor()==0)
					setIsbjoker(true);
				else
					setIswjoker(true);
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
			//sortBlock(getHand(), 0, hand.size()-1);

		public void askBlock(int selectedPlayer, int selectedBlock, int selectedNum) {		
				
			if(players.get(selectedPlayer).checkBlock(selectedBlock, selectedNum))
			{
				module.m_NetTaget.SendChatMsg("정답입니다.");
				if(JOptionPane.showConfirmDialog(null, "빙고! 계속하시겠습니까?", "확인",JOptionPane.YES_NO_OPTION)==0)
					module.AskBlock();
				else
					module.Next();
			}
			else
			{
				module.m_NetTaget.SendChatMsg("오답입니다.");
				last.setOpen(true);
				module.Next();
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
			module.m_NetTaget.SendOb(new DataHeader("game2", new GameData(module.GC)));
		}
		public void swapBlock(ArrayList<Block> blocks, int n1, int n2) {
			Block tb1 = blocks.get(n1);
			Block tb2 = blocks.get(n2);
			blocks.set(n1,tb2);
			blocks.set(n2,tb1);
		}
		public void sortBlock(int s, int e) {
			for(int i = s; i<e;i++)
			{
				for(int j=s;j<e;j++){
					if(hand.get(j).getNum()>hand.get(j+1).getNum())
						swapBlock(hand, j, j+1);
					if(hand.get(j).getNum()==hand.get(j+1).getNum()&&hand.get(j).getColor()==1&&hand.get(j+1).getColor()==0)
						swapBlock(hand, j, j+1);
				}
			}
		}
		public void next(int n) {
			module.m_NetTaget.SendOb(Integer.valueOf(n+1));
		}
	}
}
