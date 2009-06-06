import java.util.ArrayList;

import javax.swing.JOptionPane;


public class GameProcess
{
	public int checkPlayers() {
		int count;
		//게임을 플레이할 플레이어를 체크한다.
		return count;
	}
	
	public void Start() {		
		Game DC = new Game(checkPlayers());
		turn(DC, DC.getPlayers(), DC.getPlayers().get(0));
	}
	public void turn(Game DC, ArrayList<Player> players, Player P)
	{
		int input;//인풋
		P.getBlock(DC.getBlocks().get(input));
		DC.askBlock(players.indexOf(P));
		if(DC.End()==true)
			return;
		else
			turn(DC, players, players.get(players.indexOf(P)+1));
	}
}
