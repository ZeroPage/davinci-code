import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class GameData implements Serializable
{
	// ��ü�� �����Ǹ� �ٴڿ� �� block ��� �� player ���� ������ �ִ� block ���� ������ �����Ѵ�.
	private ArrayList<Block>				floor;	// �ٴڿ� ����ִ� block ��.
	private ArrayList< ArrayList<Block> >	blocks;	// �� �������� ������ �ִ� block ��.

	public GameData(Game gc)
	{
		System.out.println("[ GameData : Constructor ]");
		ArrayList<Block>	tmp;
		ArrayList<Boolean>	boolTmp;
		floor = new ArrayList<Block>();
		for(int i = 0 ; i < gc.getFloorBlocks().size() ; i++)
			floor.add(gc.getFloorBlocks().get(i));
		
		blocks = new ArrayList<ArrayList<Block>>();
		
		for(int i = 0 ; i < gc.getPlayers().size() ; i++)
		{
			tmp		= new ArrayList<Block> ();
			boolTmp	= new ArrayList<Boolean>();
			
			for(int j = 0, size = gc.getPlayers().get(i).hand.size() ; j < size ; j++)
			{
				tmp.add(gc.getPlayers().get(i).hand.get(j));
				boolTmp.add(gc.getPlayers().get(i).hand.get(j).isOpen());
			}
			blocks.add(tmp);		// player 0,1,2,3 ���� ���� block( hand ) ����  p �� �����Ѵ�.
			System.out.println("�÷��̾� "+i+"�� ������ ����"+boolTmp);
		}
		System.out.println("**************************************************");
	}
	public ArrayList<Block> 			getFloor()			{	return floor; }
	public ArrayList<ArrayList<Block>>	getPlayers()		{	return blocks; }
	public ArrayList<Block>				getBlocksOfPlayer(int index)	{	return blocks.get(index); }
}
