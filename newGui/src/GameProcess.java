import java.util.ArrayList;

import javax.swing.JOptionPane;


public class GameProcess
{
	public int checkPlayers() {
		int count;
		//������ �÷����� �÷��̾ üũ�Ѵ�.
		return count;
	}
	
	public void Start() {		
		Game DC = new Game(checkPlayers());
		turn(DC, DC.getPlayers(), DC.getPlayers().get(0));
	}
	public void turn(Game DC, ArrayList<Player> players, Player P)
	{
		int input;//��ǲ
		P.getBlock(DC.getBlocks().get(input));
		int playerNum;
		int selectedBlock;
		int selectedNum = Integer.parseInt(JOptionPane.showInputDialog("���ϱ�?"));
		DC.askBlock(playerNum, selectedBlock, selectedNum);
		if(DC.End()==true)
			return;
		else
			turn(DC, players, players.get(players.indexOf(P)+1));
	}
}
