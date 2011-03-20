import java.util.ArrayList;


public class GameProcess
{
	GameWindow	gameWndGUI;
	Network		netObject;
	int 		myPlayOrder	= 0;	// �ڽ��� �÷��� ����.
	int 		onlyDraw	= 4;			// player ���� block �� 4 ���� ���� ������ �����Ѵ�.

	Game		gameEnv;

	public GameProcess(GameWindow GUITarget, Network NetTarget) {	// ���� ���μ��� ��ü�� ���� GUI �� ��Ʈ��ũ�� ����.
		gameWndGUI	= GUITarget;		// ���μ����� ������ game window�� ����Ű������ ����.
		netObject	= NetTarget;
	}
	public void Start() {		// server �� ������ ������ ��, �ڽ��� ���� ��Ʈ�Ѹ� �����ϰ�, client �鿡�� �� play �ο��� ������ ������ ���� ��Ʈ���� �����ϰ� �ڽź��� ������ �����ϴ� �޼ҵ�.
		System.out.println("[ GameProcess : Start ]");
		gameEnv = new Game( ((Server)netObject).clientNum+1 );
		((Server)netObject).SendOrder();
		netObject.SendOb( new DataHeader( DataHeader.TOTALCOUNT, gameEnv.getPlayers().size()) );
		netObject.SendOb( new DataHeader( DataHeader.GAME, gameEnv) );		// ��� client �鿡�� server �� ������ ���� ��Ʈ���� �����Ѵ�.
		
		if(gameEnv.getPlayers().size()==4)	onlyDraw = 3;		// player ���� 4 ���� ���, 3 ���� ������ ������ ����.	
	}
	public void turn() {
		System.out.println("[ GameProcess : turn ]");
		gameWndGUI.update();
		gameWndGUI.setEnable(GameWindow.CENTER, true);				// player �ڽ��� ���� �Ǳ� �������� center block �� ������ �� ����.
		netObject.SendChatMsg("�� �Դϴ�.");
	}
	public void Next()	//���� �÷��̾�� ���� �Ѱ��ش�. ���� �������� ��� �Է��� ��� ó�� �Ǿ� �����Ƿ� �ڵ����� �����°� �ȴ�.
	{
		System.out.println("[ GameProcess : Next ]");
		gameWndGUI.setEnable(GameWindow.OTHERS, false);			 	// ���� player �� �ٸ� block ���� Ŭ������ ���ϵ��� ����. 
		gameWndGUI.setEnable(GameWindow.CENTER, false);				// center �� block �鵵 �������� ���ϵ��� ����.
		netObject.SendOb( new DataHeader( DataHeader.PASS, ( ( Integer.valueOf( ( myPlayOrder+1 ) ) % ( gameEnv.getPlayers().size() ) ) )) );
		// �� �� ���� player ���� ���� �ѱ�.
	}
	public void End() {	//������ ���������� ȣ�� �����ư�� Ȱ��ȭ ���ְ� ��� �̹����� �Ⱥ��̰� �����ѵ� ���ڸ� ǥ�����ش�.
		netObject.SendChatMsg("�̰���ϴ�.");
		gameWndGUI.RemoveAll();
		gameWndGUI.update();
		gameEnv=null;
	}
	public void moveBlock(int blockIndex) 		// center �� �ִ� block ���� player ���� �ű�� �Լ�.
	{
		System.out.println("[ GameProcess : moveBlock ]");
		Player me = gameEnv.getPlayer(myPlayOrder);
		
		if(gameEnv.getFloorBlocks().get(blockIndex).getNum()==12)	// ������ block �� Joker �� ��� diag ��ȭ������ ��ư ���� �����ϰ�, joker �� ���� ��Ҹ� player ���� �����.
		{
			Block	target	= gameEnv.getFloorBlocks().get(blockIndex);
			AskDlg	diag	= new AskDlg( target.getColor() );
			int		num		= diag.getNum();
			target.setSortingNum(num);
		}
		
		me.getBlock( gameEnv.getFloorBlocks(), blockIndex );	// �� block �� player ���� ����.
		gameWndGUI.update();
		netObject.SendOb( new DataHeader(DataHeader.GAMEDATA, new GameData(gameEnv)) );

		if(me.getHand().size()<=onlyDraw)	Next();			// ���簡 ������  player �� block �� �� ���� ������ block �� �������� ���� ��� ������. 
		else {												// player �� block �� �� ���������� ������ block �� �����ϱ� �����Ѵ�.
			gameWndGUI.setEnable(GameWindow.OTHERS, true);	// �����ϱ� ���� �ٸ� player ���� block �� ���ð����ϰ� �����Ѵ�.
		}
	}
	
	public ArrayList<Block> GetPlayerBlocksState(int playerNum)	{	return gameEnv.getPlayer(playerNum).getHand();	}// player�� block ���� ��ȯ�Ѵ�.(���¸� ��ȯ�ϴ� ����.)
	public ArrayList<Block> GetCenterBlocksState()				{	return gameEnv.getFloorBlocks();	}			// ��� ������ �����Ѵ�.(���¸� ��ȯ�Ѵٰ� ���� Ÿ����)
	public int 				getNumOfPlayer()					{	return gameEnv.getPlayers().size();	}	// play ���� player ���� ���� ��ȯ�Ѵ�.
	public void AskBlock(int playerOrder, int index, int num) {	// �ش� player ���� block �� ���ڸ� ����� �޼ҵ�.
		System.out.println("[ GameProcess : AskBlock ]");
		Player me		= gameEnv.getPlayer(myPlayOrder);
		Player target	= gameEnv.getPlayer(playerOrder);
		
		me.askBlock(target, this, index, num);			// ���� player �� ����� �Ǵ� Player ���� block �� ���.
		gameWndGUI.update();
	}
	
	public void setGameEnv(Game gc) {
		System.out.println("[ GameProcess : setGameEnv(Game gc) ]");
		gameEnv = gc;
		gameWndGUI.update();
	}
	public void setPlayOrder(int order) {	myPlayOrder = order; }
	public int	getPlayOrder()		 	{	return myPlayOrder;	}
	public Game getGameEnv() 			{	return gameEnv; }

	public void setGameEnv(GameData newBlockState)			// ���޹��� ���ڿ� ���ο� block ���� �й� ������ ����־�, �� ������ ������ block ���� ��Ʈ���� block ���¸� �����ϴ� �޼ҵ�.
	{
		System.out.println("[ GameProcess : setGameEnv(GameData newBlockState ]");
		gameEnv.setFloor( newBlockState.getFloor() );				// center block ���� ���� ������.
		
		for(int i = 0; i < newBlockState.getPlayers().size(); i++)	// ���ڷ� ���� ���� ��Ʈ�ѷ��� player ����ŭ ����
		{
			if( i == myPlayOrder ) {
				for(int j = 0 ; j < newBlockState.getBlocksOfPlayer(i).size() ; j++) {
					newBlockState.getBlocksOfPlayer(i).get(j).setOwn(true);
				}
			}
			else {
				for(int j = 0 ; j < newBlockState.getBlocksOfPlayer(i).size() ; j++) {
					newBlockState.getBlocksOfPlayer(i).get(j).setOwn(false);
				}
			}
			gameEnv.getPlayer(i).hand = newBlockState.getBlocksOfPlayer(i);
		}
		for(int i = 0;i<gameEnv.getPlayers().size();i++)
			gameEnv.getPlayer(i).sortBlock();
		gameWndGUI.update();
	}
}
