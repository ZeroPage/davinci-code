
public class GameProcess
{
	public static void main(String[] args)
	{

	}
	public int checkPlayers() {
		int count;
		//������ �÷����� �÷��̾ üũ�Ѵ�.
		return count;
	}
	
	public void Start() {		
		Game G = new Game(checkPlayers());
		turn();
	}
	public void turn(Player P)
	{
		P.getBlock(tb);
		P.askBlock(tp, tb, selectedNum);
		
	}
}
