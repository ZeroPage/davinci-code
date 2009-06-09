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
			for(int j=0; j<gc.players.get(i).hand.size();j++)
				p[i][j] = gc.players.get(i).hand.get(j);
	}
}
