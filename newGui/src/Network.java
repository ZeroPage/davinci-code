
abstract public class Network
{
	protected mWindow m_Taget;//ä�� �޼����� ������ ������ ��� 
	protected Network m_net;
	abstract public void Connet(String ip);//������ ip�� �����ϴ� �Լ�
	abstract public void SendChatMsg(String msg);//���ӵ� ��Ʈ��ũ�� msg�� ������ �Լ�
	abstract public void Close();//��������� ���� �Լ�. ������ �����ʼ�
}
