
abstract public class Network
{
	protected mWindow m_Taget;//ä�� �޼����� ������ ������ ��� 
	protected Network m_net;
	protected String m_Name;
	protected int portNum = 10000, portNum2 = 10001;
	public void setPortNum(int portNum)
	{
		this.portNum = portNum;
	}
	public void setPortNum2(int portNum2)
	{
		this.portNum2 = portNum2;
	}
	public void setM_Taget(mWindow taget)
	{
		m_Taget = taget;
	}
	public void setM_Name(String name)
	{
		m_Name = name;
	}
	abstract public void Connect(String ip);//������ ip�� �����ϴ� �Լ�
	abstract public void SendChatMsg(String msg);//���ӵ� ��Ʈ��ũ�� ä�� msg�� ������ �Լ�
	abstract public void Close();//��������� ���� �Լ�. ������ �����ʼ�
	abstract public void SendOb(int sel, Object ob);//���ӵ� ��Ʈ��ũ�� ä�� ������Ʈ�� ������ �Լ�
}