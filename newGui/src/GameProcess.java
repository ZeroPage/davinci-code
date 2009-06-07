
public class GameProcess
{
	int playerNum;
	Network connection;
	GameWindow m_Taget;
	Network m_NetTaget;
	int playOrder;
	
	Game DC;
	
	public GameProcess(GameWindow Taget, Network NetTaget)
	{
		m_Taget = Taget;
		m_NetTaget = NetTaget;
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
	public void setDC(Game dc)
	{
		DC = dc;
	}
	public Game getDC()
	{
		return DC;
	}
	public void setPlayOrder(int playOrder)
	{
		this.playOrder = playOrder;
	}
	public int getPlayOrder()
	{
		return playOrder;
	}
	public void Start() {		
		connection.SendOb(Integer.valueOf(getPlayerNum()));
		DC = new Game(getConnection(), getM_Taget(), getPlayerNum());
		connection.SendOb(new DataHeader("firstBlocks-arrayList",DC.getBlocks()));
	}

	public void report() {
		//자신 차례가 끝나고 어떤것을 했는지 보고
	}
}
