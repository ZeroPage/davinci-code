
abstract public class Network
{
	protected mWindow m_Taget;//ä�� �޼����� ������ ������ ��� 
	protected Network m_net;
	public void setM_Taget(mWindow taget)
	{
		m_Taget = taget;
	}
	abstract public void Connect(String ip);//������ ip�� �����ϴ� �Լ�
	abstract public void SendChatMsg(String msg);//���ӵ� ��Ʈ��ũ�� ä�� msg�� ������ �Լ�
	abstract public void Close();//��������� ���� �Լ�. ������ �����ʼ�
}
