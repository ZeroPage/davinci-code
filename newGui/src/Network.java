
abstract public class Network
{
	protected mWindow m_Taget;//ä�� �޼����� ������ ������ ��� 
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
	abstract public boolean isServer();//�������� �ƴ��� ����.
	abstract public void Connect(String ip);//������ ip�� �����ϴ� �Լ�
	abstract public void SendChatMsg(String msg);//���ӵ� ��Ʈ��ũ�� ä�� msg�� ������ �Լ�
	abstract public void SendOb(Object ob);//���ӵ� ��Ʈ��ũ�� ä�� ������Ʈ�� ������ �Լ�
	abstract public void Close();//��������� ���� �Լ�. ������ �����ʼ�
}