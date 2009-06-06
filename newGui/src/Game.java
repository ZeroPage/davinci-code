import java.util.ArrayList;

public class Game {
	private ArrayList<Player> players;
	private ArrayList<Block> blocks;

	Game() {
		setPlayers(1);
		setBlocks();
	}
	Game(int n) {
		setPlayers(n);
		setBlocks();
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
			if(i==12&&i==25)
				b[i].setNum(-1);
			blocks.add(b[i]);
		}
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
	public void printBlocks(ArrayList<Block> blocks) {		
		for(int i=0; i<blocks.size(); i++) {
			Block tb = blocks.get(i);
			System.out.print(tb.getColor() + " ");
			//if(blocks[i].getOpen() == true)
			System.out.print(tb.getNum() + " ");
			/*else
				System.out.print("?");*/
			System.out.print(tb.getOwn() + " ");
			System.out.print(tb.getOpen());
			System.out.print("\t");
			if(i%5 == 0)
				System.out.println();
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
	
}
