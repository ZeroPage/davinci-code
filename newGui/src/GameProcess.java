import java.util.ArrayList;


public class GameProcess
{
	//개인의 입력이나 네트워크릐 입력등 게임의 입출력전반적인것을 담당한다. 
	//GUI에 정보를 전송하기 위해서는 그냥 몇번플레이어만 업데이트 해달라하면 자동으로 현재상태로 동기화 시켜준다.
	//이 클래스에는 알고리즘이 담겨있고 Game과 player그리고 block은 내부 데이터만 담당한다.
	//따라서 Game과 player, block은 네트워크나 gui를 알필요없이 현재 이객체의 주소값만 알고 있으면 된다.
	//마찬가지로 그외 다른 클래스도 게임에 접근하고 싶으면 이클래스를 통해서 접근해야 한다.
	//gui와 game은 동기화이나 network와의 관계는 적지 않았다. network는 혁수가 적당히 적어주길
	GameWindow m_GUITaget;
	Network m_NetTaget;
	int playOrder = 0; // 자신의 플레이 순서인 모양이다.?
	//next함수를 쓰면 네트워크에서 브로드캐스팅으로 다음 사람을 찾도록 int형 값을 전송한다.
	//네트워크에서 int를 받아 이 값과 같아 자신의 차례면 turn을 호출하도록 하겠다.

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
		m_NetTaget.SendOb(new DataHeader("총인원수", GC.getPlayers().size()));
		m_NetTaget.SendOb(new DataHeader("game", GC));
		//서버측에서만 인원수를 알수잇다. 우선은 접속한인원수를 게임 참가인원으로 한다.
		//게임을 시작하는 것은 방장만이 할수 있는것 이것에 대한 조치를 취해야 한다.
		//Network에 isSever함수를 두고 클라에서는 false를 리턴하게 오버로딩을
		//서버에서는 true를 리턴도록 오버로딩을 하면 된다.(isServer 구현완료.)
		//레디버튼을 선택할수 없도록 하는 코드도 있어야 한다.

		//게임 시작시 필요한 준비 과정을 여기에서 끝내고
		//이 함수를 끝내기 전에 네트워크로 데이터를 보내서 모두의 블럭들을 동기화 하자.
	}
	public void turn()
	{
		//내턴이 왔을때는 일단 하나 골라온다음에 상대방것을 추리하고 맞추면 그걸 까고 아니면
		//가져온 내것을 깐다.
		//System.out.println(playOrder);
		m_GUITaget.CenterUpdate();
		selectBlock();
	}
	public void selectBlock()
	{
		//패를 고르기 위해서 GUI의 가운데 패를 enable 시킨다.
		m_GUITaget.setCenterEnable(true);
	}
	public void moveBlock(int indexNum)
	{
		//가운데에 있는 블럭을 가져오는 것이다.
		//게임 클래스의 moveCenterBlock으로 동작을 넘겨준다.
		//몇번째 넘인지 알면 그 블럭을 플레이어로 이동시킨다. indexNum
		//블럭을 적당한 위치에 넣은후 그사실을 Gui에 넘겨준다(업데이트 함수를 부른다.)
		//네트워크에도 그내용을 전송해준다.(가운데에 몇번째 패가 선택되었는지..그게 뭔지..)
		//Gui에서는 블럭배열을 받은뒤 블럭을 바꾸어 준다.
		//그다음 상대방에게 블럭을 물어본다.
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
		m_GUITaget.update(); //테스트차 주석처리
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
		//enable할때 블럭의 상태를 알아야 이미지를 바꿔줄수 있다.
		//몇번 플레이어의 블록 배열을 리턴하면 된다.
		Block[] temp = new Block[GC.getPlayers().get(playerNum).getHand().size()];
		GC.getPlayers().get(playerNum).getHand().toArray(temp);//왠만하면 이런 복잡한 형식보다는 Game클래스 에다가 getPlayerBlock함수를 두고 거기서 Block의 배열을 리턴하도록 만들지 그래? 이렇게 짜면 읽기가 힘들어.
		return temp;
	}
	public Block[] GetCenterBlocksState()
	{
		//가운데 블럭의 상태를 리턴한다.
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
		//생대방에게 블럭을 물어 봐야 한다.
		//그러기 위해서는 다른 사람들 패를 하나 골라 선택해야 한다. 따라서 다른 플레이어의 패을 enable 해준다
		for(int i=0;i<GC.getPlayers().size();i++)
		{
			if(i!=playOrder)
				m_GUITaget.setEnable(i, true);
		}
	}
	public void AskBlock(int playerNum, int index, int num)
	{
		//파라미터가 있으므로 위에 함수와 구분이 된다.몇번 플레이어에 몇번 인덱스라는 것만 알려주면 된다.
		//어떤 숫자인지 알아야한다.
		//네트워크에 물어보는 내용을 전송한다.
		GC.getPlayers().get(playOrder).askBlock(playerNum, index, num);
	}
	
	public void Next()
	{
		//다음 플레이어에게 턴을 넘겨준다. 게임 윈도우의 모든 입력은 블록 처리 되어 있으므로 자동으로 대기상태가 된다. 
		for(int i=0;i<GC.getPlayers().size();i++)
			m_GUITaget.setEnable(i, false);
		m_GUITaget.setCenterEnable(false);
		//m_NetTaget.SendOb(new DataHeader("game2", new GameData(GC)));
		m_NetTaget.SendOb(new DataHeader("pass", ((Integer.valueOf((playOrder+1))%(GC.getPlayers().size())))));
	}
	public void End()
	{
		//게임이 끝났을때의 호출 레디버튼을 활성화 해주고 모든 이미지를 안보이게 지정한뒤 
		//승자를 표시해준다.
		//
		m_NetTaget.SendChatMsg("이겼습니다.");
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
