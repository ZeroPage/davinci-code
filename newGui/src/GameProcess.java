
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
		playOrder = 0;
	}
	public void Start()
	{
		GC = new Game(this, ((Server)m_NetTaget).clientNum+1);
		((Server)m_NetTaget).SendOrder();
		m_NetTaget.SendOb(new DataHeader("���ο���", GC.getPlayers().size()));
		m_NetTaget.SendOb(new DataHeader("game", GC));
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
		m_GUITaget.CenterUpdate();
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
		m_NetTaget.SendOb(new DataHeader("game", GC));
		m_GUITaget.setCenterEnable(false);
		m_GUITaget.update(playOrder);
		m_GUITaget.CenterUpdate();
		if(GC.getPlayers().get(playOrder).getHand().size()<=4)
		{
			Next();
		}
		else
			AskBlock();
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
	public int getPlayerNum()
	{
		return GC.getPlayers().size();
	}
	public void AskBlock()
	{
		//����濡�� ���� ���� ���� �Ѵ�.
		//�׷��� ���ؼ��� �ٸ� ����� �и� �ϳ� ��� �����ؾ� �Ѵ�. ���� �ٸ� �÷��̾��� ���� enable ���ش�
		for(int i=0;i<4;i++)
		{
			if(i!=playOrder)
				m_GUITaget.setEnable(i, true);
		}
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

		m_NetTaget.SendOb(new DataHeader("pass", ((Integer.valueOf((playOrder+1))%(GC.getPlayers().size())))));
	}
	public void End()
	{
		//������ ���������� ȣ�� �����ư�� Ȱ��ȭ ���ְ� ��� �̹����� �Ⱥ��̰� �����ѵ� 
		//���ڸ� ǥ�����ش�.
		//
	}
	public void setGC(Game gc)
	{
		if(GC != null)
			for(int i=0; i<gc.getPlayers().get(playOrder).getHand().size(); i++)
				gc.getPlayers().get(playOrder).getHand().get(i).setOwn(GC.getPlayers().get(playOrder).getHand().get(i).getOwn());
		GC = gc;
		m_GUITaget.update();
		GC.setModule(this);
	}
	public void setPlayOrder(int n)
	{
		playOrder = n;
	}
	public int getPlayOrder()
	{
		return playOrder;
	}
}
