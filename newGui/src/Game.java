import java.io.Serializable;
import java.util.ArrayList;

/**
 * �ϳ��� ȣ��Ʈ���� �������� �� player ��� block ���� ���� ���� ���� �غ��ϴ� Ŭ����. 
 * 
 * @author Teolex
 *
 */
public class Game implements Serializable {		// ������ ������ ���� �ʱ� �۾��� �����Ѵ�.
	
	private ArrayList<Player>	players;		// ���� ���� player ���� ������ ����Ʈ.
	private ArrayList<Block>	floor;			// ������ �ٴڿ� ������� block ���� ������ ����Ʈ.
	
	Game( int numOfPlayer )
	{
		players	= new ArrayList<Player>();	// player ���� ������ ����Ʈ.
		floor	= new ArrayList<Block>();	// ��� block ���� ������ ����Ʈ.
		setPlayers(numOfPlayer);			// playerNum ���� player �� ������.
		makeBlocks();						// block ���� ������.
		mixBlocks(floor);					// ������ block ���� ����.
	}

	public void 				setFloor( ArrayList<Block> target )	{	floor = target; }
	
	public void 				setPlayers(int numOfPlayer) 		{	for(int i = 0; i < numOfPlayer; i++)	players.add( new Player() ); }// �־��� ���� ��ŭ�� player �� �����Ѵ�.
	public ArrayList<Player> 	getPlayers() 						{	return players; }
	
	//Block( int color, int num ), Black 0 : white 1
	public void 				makeBlocks() 						{	for(int i = 0 ; i < 26; i++) floor.add( new Block( i/13, i%13 ) ); }// block ���� ���ڿ� ���� ������ �� blocks �� �߰��Ѵ�.
	public ArrayList<Block>		getBlocks()				 			{	return floor;	}
	
	public void swapBlock(ArrayList<Block> blocks, int n1, int n2)
	{
		Block tb = blocks.get(n1);

		blocks.set(n1,blocks.get(n2));
		blocks.set(n2,tb);
	}
	public void mixBlocks(ArrayList<Block> blocks)	// ���� ��Ʈ���� ������ block ���� ������ ���´�.
	{
		int n1, n2;
		for(int i = 0; i < 50; i++)
		{
			n1 = (int)(Math.random()*26);
			n2 = (int)(Math.random()*26);
			swapBlock(blocks, n1, n2);
		}
	}
	public boolean isEnd()	// player ���� ���°� ��� Ȯ���ϰ�  ������ �������� Ȯ���ϴ� �޼ҵ�.
	{
		int alive = 0;
		for( int i = 0; i < players.size(); i++ )
			if(players.get(i).getPlay() == true)	alive++;
		return alive==1;	// �����ִ� ���� ȥ���̸� ������ ���� ��.
	}
}
