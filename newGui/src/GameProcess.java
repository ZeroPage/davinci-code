
public class GameProcess
{
	//������ �Է��̳� ��Ʈ��ũ�l �Էµ� ������ ������������ΰ��� ����Ѵ�. 
	//GUI�� ������ �����ϱ� ���ؼ��� �׳� ����÷��̾ ������Ʈ �ش޶��ϸ� �ڵ����� ������·� ����ȭ �����ش�.
	//�� Ŭ�������� �˰����� ����ְ� Game�� player�׸��� block�� ���� �����͸� ����Ѵ�.
	//���� Game�� player, block�� ��Ʈ��ũ�� gui�� ���ʿ���� ���� �̰�ü�� �ּҰ��� �˰� ������ �ȴ�.
	//���������� �׿� �ٸ� Ŭ������ ���ӿ� �����ϰ� ������ ��Ŭ������ ���ؼ� �����ؾ� �Ѵ�.
	//gui�� game�� ����ȭ�̳� network���� ����� ���� �ʾҴ�. network�� ������ ������ �����ֱ�
	GameWindow m_GUITaget;
	Network m_NetTaget;
	int playOrder; // �ڽ��� �÷��� ������ ����̴�.?
	//next�Լ��� ���� ��Ʈ��ũ���� ��ε�ĳ�������� ���� ����� ã���� int�� ���� �����Ѵ�.
	//��Ʈ��ũ���� int�� �޾� �� ���� ���� �ڽ��� ���ʸ� turn�� ȣ���ϵ��� �ϰڴ�.
	
	Game GC;
	
	public GameProcess(GameWindow GUITaget, Network NetTaget)
	{
		m_GUITaget = GUITaget;
		m_NetTaget = NetTaget;
	}
	public void Start()
	{
		GC = new Game(this, ((Server)m_NetTaget).clientNum);
		m_GUITaget.CenterUpdate();
		//������������ �ο����� �˼��մ�. �켱�� �������ο����� ���� �����ο����� �Ѵ�.
		//������ �����ϴ� ���� ���常�� �Ҽ� �ִ°� �̰Ϳ� ���� ��ġ�� ���ؾ� �Ѵ�.
		//Network�� isSever�Լ��� �ΰ� Ŭ�󿡼��� false�� �����ϰ� �����ε���
		//���������� true�� ���ϵ��� �����ε��� �ϸ� �ȴ�.(isServer �����Ϸ�.)
		//�����ư�� �����Ҽ� ������ �ϴ� �ڵ嵵 �־�� �Ѵ�.
		
		//���� ���۽� �ʿ��� �غ� ������ ���⿡�� ������
		//�� �Լ��� ������ ���� ��Ʈ��ũ�� �����͸� ������ ����� ������ ����ȭ ����.
	}
	public void turn()
	{
		//������ �������� �ϴ� �ϳ� ���´����� ������� �߸��ϰ� ���߸� �װ� ��� �ƴϸ�
		//������ ������ ���.
		selectBlock();
	}
	public void selectBlock()
	{
		//�и� ���� ���ؼ� GUI�� ��� �и� enable ��Ų��.
		m_GUITaget.setCenterEnable(true);
	}
	public void moveBlock(int indexNum)
	{
		//����� �ִ� ���� �������� ���̴�.
		//���� Ŭ������ moveCenterBlock���� ������ �Ѱ��ش�.
		//���° ������ �˸� �� ���� �÷��̾�� �̵���Ų��. indexNum
		//���� ������ ��ġ�� ������ �׻���� Gui�� �Ѱ��ش�(������Ʈ �Լ��� �θ���.)
		//��Ʈ��ũ���� �׳����� �������ش�.(����� ���° �а� ���õǾ�����..�װ� ����..)
		//Gui������ ���迭�� ������ ���� �ٲپ� �ش�.
		//�״��� ���濡�� ���� �����.
		GC.getPlayers().get(playOrder).getBlock(indexNum);
		m_GUITaget.update(playOrder);
	}
	public Block[] GetBlocksState(int playerNum)
	{
		//enable�Ҷ� ���� ���¸� �˾ƾ� �̹����� �ٲ��ټ� �ִ�.
		//��� �÷��̾��� ��� �迭�� �����ϸ� �ȴ�.
		Block[] temp = new Block[GC.getPlayers().get(playerNum).getHand().size()];
		GC.getPlayers().get(playerNum).getHand().toArray(temp);//�ظ��ϸ� �̷� ������ ���ĺ��ٴ� GameŬ���� ���ٰ� getPlayerBlock�Լ��� �ΰ� �ű⼭ Block�� �迭�� �����ϵ��� ������ �׷�? �̷��� ¥�� �бⰡ �����.
		return temp;
	}
	public Block[] GetCenterBlocksState()
	{
		//��� ���� ���¸� �����Ѵ�.
		Block[] temp = new Block[GC.getBlocks().size()];
		GC.getBlocks().toArray(temp);
		return temp;
	}
	public void AskBlock()
	{
		//����濡�� ���� ���� ���� �Ѵ�.
		//�׷��� ���ؼ��� �ٸ� ����� �и� �ϳ� ��� �����ؾ� �Ѵ�. ���� �ٸ� �÷��̾��� ���� enable ���ش�
	}
	public void AskBlock(int playerNum, int index, int num)
	{
		//�Ķ���Ͱ� �����Ƿ� ���� �Լ��� ������ �ȴ�.��� �÷��̾ ��� �ε������ �͸� �˷��ָ� �ȴ�.
		//� �������� �˾ƾ��Ѵ�.
		//��Ʈ��ũ�� ����� ������ �����Ѵ�.
		GC.getPlayers().get(playOrder).askBlock(playerNum, index, num);
	}
	public void CheckBlock(int index, int num)
	{
		//��ũ��ũ���� ������ ������ �ڽ��� �и� üũ���ش�.�׸��� ���ϰ��� �����ִ°��� �ƴ϶� ��Ʈ��ũ�� �´��� Ʋ���� �������ش�.
		//������ üũ ����� �θ���. �� �ڽ��� �а� �ƴϸ� �ǵ��� �ʴ´�.
		GC.getPlayers().get(playOrder).checkBlock(index, num);
	}
	public void correct()
	{
		//�¾������� �����̴�. ����Ҳ��� ���� �ѱ��� ���� ���̾� �α׷� �����
		//����Ϸ��� askblock()�� 
		//���� �ѱ���� next()�� ȣ���ϸ� �ȴ�.
	}
	public void incorrect()
	{
		//Ʋ�������� �����̴�. �ڽ��� �и� �ϳ� ���.
	}
	public void Next()
	{
		//���� �÷��̾�� ���� �Ѱ��ش�. ���� �������� ��� �Է��� ��� ó�� �Ǿ� �����Ƿ� �ڵ����� �����°� �ȴ�. 
		GC.getBlocks();
	}
	public void End()
	{
		//������ ���������� ȣ�� �����ư�� Ȱ��ȭ ���ְ� ��� �̹����� �Ⱥ��̰� �����ѵ� 
		//���ڸ� ǥ�����ش�.
		//
	}
}
