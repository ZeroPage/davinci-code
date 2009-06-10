import java.util.ArrayList;


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
	int playOrder = 0; // �ڽ��� �÷��� ������ ����̴�.?
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
		//System.out.println(playOrder);
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
		if(GC.getBlocks().get(indexNum).getNum()==-1)
		{
			if(GC.getBlocks().get(indexNum).getColor()==0)
				GC.getPlayers().get(playOrder).setIsjoker(0);
			else
				GC.getPlayers().get(playOrder).setIsjoker(1);
			joKer();
		}
		else
		{
		GC.getPlayers().get(playOrder).getBlock(indexNum);
		GC.getPlayers().get(playOrder).sortBlock(0, GC.getPlayers().get(playOrder).getHand().size()-1);
		}
		/*for(int i = 0; i<GC.getPlayers().get(playOrder).getHand().size();i++)
		{
			System.out.print(GC.getPlayers().get(playOrder).getHand().get(i).getNum()+ " ");
		}
		System.out.println();*/
		m_GUITaget.setCenterEnable(false);
		m_GUITaget.update(); //�׽�Ʈ�� �ּ�ó��
		//m_NetTaget.SendOb(new DataHeader("game2", new GameData(GC)));
		int onlyDraw=4;
		if(GC.getPlayers().size()==4)
			onlyDraw=3;
		if(GC.getPlayers().get(playOrder).getHand().size()<=onlyDraw)
		{
			Next();
		}
		else
			AskBlock();
	}
	public void joKer()
	{
		m_GUITaget.setEnable(playOrder, true);
	}
	public void joKer(int indexNum)
	{
		if(GC.getPlayers().get(playOrder).getIsjoker()==0)
			GC.getPlayers().get(playOrder).getHand().add(indexNum,GC.bJoker);
		else if(GC.getPlayers().get(playOrder).getIsjoker()==1)
			GC.getPlayers().get(playOrder).getHand().add(indexNum,GC.wJoker);
		else
			return;
	}
	public Block[] GetJokerInput(int playerNum)
	{
		Block[] JokerInput = new Block[GC.getPlayers().get(playerNum).getHand().size()+2];
		return JokerInput;
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
		for(int i=0;i<GC.getPlayers().size();i++)
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
	
	public void Next()
	{
		//���� �÷��̾�� ���� �Ѱ��ش�. ���� �������� ��� �Է��� ��� ó�� �Ǿ� �����Ƿ� �ڵ����� �����°� �ȴ�. 
		for(int i=0;i<GC.getPlayers().size();i++)
			m_GUITaget.setEnable(i, false);
		m_GUITaget.setCenterEnable(false);
		//m_NetTaget.SendOb(new DataHeader("game2", new GameData(GC)));
		m_NetTaget.SendOb(new DataHeader("pass", ((Integer.valueOf((playOrder+1))%(GC.getPlayers().size())))));
	}
	public void End()
	{
		//������ ���������� ȣ�� �����ư�� Ȱ��ȭ ���ְ� ��� �̹����� �Ⱥ��̰� �����ѵ� 
		//���ڸ� ǥ�����ش�.
		//
		m_NetTaget.SendChatMsg("�̰���ϴ�.");
		m_NetTaget.SendOb(new DataHeader("game2", new GameData(GC)));
		m_GUITaget.RemoveAll();
		m_GUITaget.update();
		GC=null;
	}
	public void setGC(Game gc)
	{
		GC = gc;
		GC.setModule(this);
		m_GUITaget.update();
	}
	public void setGC(GameData gc)
	{
		ArrayList<Block> temp = new ArrayList<Block>();
		for(int i=0; i< gc.floor.length; i++)
			temp.add(gc.floor[i]);
		GC.blocks = temp;

		for(int i=0; i<gc.p.length; i++)
		{
			temp = new ArrayList<Block>();
			for(int j=0; gc.p[i][j] != null; j++)
			{
				if(i == playOrder)
					gc.p[i][j].setOwn(true);
				else
					gc.p[i][j].setOwn(false);
				for(int k=0; k<GC.players.get(i).hand.size(); k++)
				{
					if(GC.players.get(i).hand.get(k).getColor() == gc.p[i][j].getColor() && GC.players.get(i).hand.get(k).getNum() == gc.p[i][j].getNum())
						gc.p[i][j].setOpen(GC.players.get(i).hand.get(k).getOpen());
				}
				temp.add(gc.p[i][j]);
			}
			GC.players.get(i).hand = temp;
		}
		GC.setModule(this);
		for(int i = 0;i<GC.getPlayers().size();i++)
			GC.getPlayers().get(i).sortBlock(0, GC.getPlayers().get(i).getHand().size()-1);
		m_GUITaget.update();
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
