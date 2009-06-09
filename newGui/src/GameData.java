import java.io.Serializable;


public class GameData implements Serializable
{
	Block[] floor;
	Block[][] p;
	
	public GameData(Game gc)
	{
		floor = new Block[gc.blocks.size()];
		gc.getBlocks().toArray(floor);
		
		p = new Block[gc.players.size()][13];
		for(int i=0; i<gc.players.size(); i++)
			gc.players.get(i).hand.toArray(p[i]);
	}
}
