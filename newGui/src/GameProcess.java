
public class GameProcess
{
	int playerNum;
	Network connection;
	GameWindow m_Taget;
	
	Game DC;
	
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
	public void setM_Taget(GameWindow taget)
	{
		m_Taget = taget;
	}
	public GameWindow getM_Taget()
	{
		return m_Taget;
	}
	public void Start() {		
		connection.SendOb(Integer.valueOf(getPlayerNum()));
		DC = new Game(getConnection(), getM_Taget(), getPlayerNum());
	}

	public void report() {
		//�ڽ� ���ʰ� ������ ����� �ߴ��� ����
	}
}
