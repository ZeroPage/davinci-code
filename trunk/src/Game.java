

public class Game {
	private int playerNum;
	private Player[] players;
	private Block[] blocks;
	Game() {
		playerNum = 2;
	}
	public void setPlayerNum(int n) {
		playerNum = n;
	}
	public void setPlayers(int n) {
		players = new Player[n];
		
		for(int i=0; i<n; i++)
			players[i] = new Player();
	}
	public void setBlocks() {
		blocks = new Block[26];
		
		for(int i=0; i<26; i++)
			blocks[i] = new Block(((i<13) ? 0 : 1), i%13);
		blocks[12].setNum(-1);
		blocks[25].setNum(-1);
	}
	public int getPlayerNum() {
		return playerNum;
	}
	public Player[] getPlayers() {
		return players;
	}
	public Block[] getBlocks() {
		return blocks;
	}
	
	public void swapBlock(Block[] array, int n1, int n2) {
		Block temp = array[n1];
		array[n1] = array[n2];
		array[n2] = temp;
	}
	public void mixBlocks(Block[] array) {
		int n1 = (int)(Math.random()*26);
		int n2 = (int)(Math.random()*26);
		
		for(int i=0; i<50; i++) {
			swapBlock(array, n1, n2);
			n1 = (int)(Math.random()*26);
			n2 = (int)(Math.random()*26);
		}
	}
	public void printBlocks(Block[] array) {		
		for(int i=0; i<array.length; i++) {
			System.out.print(array[i].getColor() + " ");
			//if(array[i].getOpen() == true)
				System.out.print(array[i].getNum() + " ");
			/*else
				System.out.print("?");*/
			System.out.print(array[i].getOwn() + " ");
			System.out.print(array[i].getOpen());
			System.out.print("\t");
			if(i%5 == 0)
				System.out.println();
		}
	}
	public void Start() {		
		setBlocks(); //게임이 시작되면 제일 처음 패를 세팅한다.
		setPlayers(playerNum);  //플레이어 수만큼 객체를 만든다.
		mixBlocks(blocks); //패를 섞는다.
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
		for(int i=0; i<playerNum; i++) {
			if(true == players[i].getPlay())
				alive++;
		}
		return alive>1;
	}
}
