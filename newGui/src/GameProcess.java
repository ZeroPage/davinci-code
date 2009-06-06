
public class GameProcess
{
	public static void main(String[] args)
	{

	}
	public int checkPlayers() {
		int count;
		//게임을 플레이할 플레이어를 체크한다.
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
