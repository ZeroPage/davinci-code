
//������ Ŭ���̾�Ʈ�� ���� �������ϰ� ������ ����Ͽ� ���� ����� ��,
//������ ���� output ��Ʈ���� input ��Ʈ���� �����س�����,
//���ΰ� �����͸� �ְ���� �� ����� �޼ҵ尡 �ſ� �����ϱ� ������
//�ϴ� ������ ������ �� input/output ��Ʈ���� ����Ǿ��ִٸ�
//������ Ŭ���̾�Ʈ�� ���� ����ϵ��� �ϴµ� �ſ� ����.
abstract public class Network
{
	protected RoomWindow	myRoomWnd;				// ä�� �޼����� ������ ������ RoomWindow.
	protected Network		m_net;				
	protected GameProcess	gameProcess;			// �ش� ��Ʈ��ũ���� �������� ���� ���μ���.
	protected String		playerNickname;			// �ش� ��Ʈ��ũ��  �� player �� �̸�
	protected int 			portNum = 10000;		// �ش� ��Ʈ��ũ�� ���ӵǾ��ִ� ������ ��Ʈ.
	public void setM_Taget(RoomWindow target) {
		myRoomWnd = target;
	}
	public void setM_net(Network m_net) {
		this.m_net = m_net;
	}
	public void setM_Game(GameProcess game) {
		gameProcess = game;
	}
	public void setM_Name(String name) {
		playerNickname = name;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
	abstract public boolean	isServer();				//�������� �ƴ��� ����.
	abstract public void	Connect(String ip);		//������ ip�� �����ϴ� �Լ�
	abstract public void	SendChatMsg(String msg);//���ӵ� ��Ʈ��ũ�� ä�� msg�� ������ �Լ�
	abstract public void	SendOb(Object ob);		//���ӵ� ��Ʈ��ũ�� ä�� ������Ʈ�� ������ �Լ�
	abstract public void	Close();				//��������� ���� �Լ�. ������ �����ʼ�
}