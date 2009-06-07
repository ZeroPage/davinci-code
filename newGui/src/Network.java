
abstract public class Network
{
	protected mWindow m_Taget;//채팅 메세지를 받으면 전달할 대상 
	protected Network m_net;
	protected GameProcess m_Game;
	protected String m_Name;
	protected int portNum = 10000;
	public void setM_Taget(mWindow taget)
	{
		m_Taget = taget;
	}
	public void setM_net(Network m_net)
	{
		this.m_net = m_net;
	}
	public void setM_Game(GameProcess game)
	{
		m_Game = game;
	}
	public void setM_Name(String name)
	{
		m_Name = name;
	}
	public void setPortNum(int portNum)
	{
		this.portNum = portNum;
	}
	abstract public boolean isServer();//서버인지 아닌지 리턴.
	abstract public void Connect(String ip);//지정된 ip로 접속하는 함수
	abstract public void SendChatMsg(String msg);//접속된 네트워크에 채팅 msg를 날리는 함수
	abstract public void SendOb(Object ob);//접속된 네트워크에 채팅 오브젝트를 날리는 함수
	abstract public void Close();//모든접속을 끊는 함수. 쓰레드 종료필수
}