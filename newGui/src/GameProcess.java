import java.util.ArrayList;

import javax.swing.JOptionPane;


public class GameProcess
{
	int playerNum;
	Network connection;
	GameWindow m_Taget;
	
	public GameProcess(GameWindow Taget)
	{
		m_Taget = Taget;
	}
	
	public void setPlayerNum(int playerNum)
	{
		this.playerNum = playerNum;
	}
	public int getPlayerNum()
	{
		return playerNum;
	}
	public void setConnection(Network connection)
	{
		this.connection = connection;
	}
	public Network getConnection()
	{
		return connection;
	}
	public void Start() {		
		connection.setM_Game(this);
		connection.SendOb(Integer.valueOf(getPlayerNum()));
		
		Game DC = new Game(getPlayerNum());
		
	}

	public void report() {
		//�ڽ� ���ʰ� ������ ����� �ߴ��� ����
	}
}
