
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
	int playOrder; // 자신의 플레이 순서인 모양이다.?
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
		GC = new Game(this, ((Server)m_NetTaget).clientNum);
		//서버측에서만 인원수를 알수잇다. 우선은 접속한인원수를 게임 참가인원으로 한다.
		//게임을 시작하는 것은 방장만이 할수 있는것 이것에 대한 조치를 취해야 한다.
		//Network에 isSever함수를 두고 클라에서는 false를 리턴하게 오버로딩을
		//서버에서는 true를 리턴도록 오버로딩을 하면 된다.(isServer 구현완료.)
		
		//게임 시작시 필요한 준비 과정을 여기에서 끝내고
		//이 함수를 끝내기 전에 네트워크로 데이터를 보내서 모두의 블럭들을 동기화 하자.
	}
	public void turn()
	{
		//내턴이 왔을때는 일단 하나 골라온다음에 상대방것을 추리하고 맞추면 그걸 까고 아니면
		//가져온 내것을 깐다.
		SeleteBlock();
	}
	public void SeleteBlock()
	{
		//패를 고르기 위해서 GUI의 가운데 패를 enable 시킨다.
		m_GUITaget.CenterEnable(true);
	}
	public void moveBlock(int indexNum)
	{
		//가운데에 있는 블럭을 가져오는 것이다.
		//게임 클래스의 moveCenterBlock으로 동작을 넘겨준다.
		//몇번째 넘인지 알면 그 블럭을 플레이어로 이동시킨다.
		//블럭을 적당한 위치에 넣은후 그사실을 Gui에 넘겨준다(업데이트 함수를 부른다.)
		//네트워크에도 그내용을 전송해준다.(가운데에 몇번째 패가 선택되었는지..그게 뭔지..)
		//Gui에서는 블럭배열을 받은뒤 블럭을 바꾸어 준다.
		//그다음 상대방에게 블럭을 물어본다. 
	}
	public Block[] GetBlocksState(int playerNum)
	{
		//enable할때 블럭의 상태를 알아야 이미지를 바꿔줄수 있다.
		//몇번 플레이어의 블록 배열을 리턴하면 된다.
		return null;
	}
	public void AskBlock()
	{
		//생대방에게 블럭을 물어 봐야 한다.
		//그러기 위해서는 다른 사람들 패를 하나 골라 선택해야 한다. 따라서 다른 플레이어의 패을 enable 해준다
	}
	public void AskBlock(int playerNum, int index, int num)
	{
		//파라미터가 있으므로 위에 함수와 구분이 된다.몇번 플레이어에 몇번 인덱스라는 것만 알려주면 된다.
		//어떤 숫자인지 알아야한다.
		//네트워크에 물어보는 내용을 전송한다.
	}
	public void CheekBlock(int index, int num)
	{
		//네크워크에서 물음을 받으면 자신의 패를 체크해준다.그리고 리턴값을 돌려주는것이 아니라 네트워크에 맞는지 틀린지 전송해준다.
		//게임의 체크 블록을 부른다. 단 자신의 패가 아니면 건들지 않는다.
	}
	public void correct()
	{
		//맞았을때의 내용이다. 계속할껀지 턴을 넘길지 컨핌 다이얼 로그로 물어본후
		//계속하려면 askblock()을 
		//턴을 넘기려면 next()를 호출하면 된다.
	}
	public void incorrect()
	{
		//틀렸을때의 내용이다. 자신의 패를 하나 깐다.
	}
	public void Next()
	{
		//다음 플레이어에게 턴을 넘겨준다. 게임 윈도우의 모든 입력은 블록 처리 되어 있으므로 자동으로 대기상태가 된다. 
	}
}
