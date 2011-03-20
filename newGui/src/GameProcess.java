import java.util.ArrayList;

import javax.swing.JOptionPane;


public class GameProcess {
	GameWindow	gameWndGUI;
	Network		netObject;
	int 		myPlayOrder = 0;		 // �ڽ��� �÷��� ����.

	Game gameControl;

	public GameProcess(GameWindow GUITarget, Network NetTarget) {	// ���� ���μ��� ��ü�� ���� GUI �� ��Ʈ��ũ�� ����.
		gameWndGUI	= GUITarget;		// ���μ����� ������ game window�� ����Ű������ ����.
		netObject	= NetTarget;
	}
	public void Start() {		// server �� ������ ������ ��, �ڽ��� ���� ��Ʈ�Ѹ� �����ϰ�, client �鿡�� �� play �ο��� ������ ������ ���� ��Ʈ���� �����ϰ� �ڽź��� ������ �����ϴ� �޼ҵ�.
		gameControl = new Game( ((Server)netObject).clientNum+1);
		((Server)netObject).SendOrder();
		netObject.SendOb( new DataHeader( DataHeader.TOTALCOUNT, gameControl.getPlayers().size()) );
		netObject.SendOb( new DataHeader( DataHeader.GAME, gameControl) );		// ��� client �鿡�� server �� ������ ���� ��Ʈ���� �����Ѵ�.
	}
	public void turn() {
		gameWndGUI.update();
		gameWndGUI.setEnable(GameWindow.CENTER, true);				// player �ڽ��� ���� �Ǳ� �������� center block �� ������ �� ����.
		netObject.SendChatMsg("�� �Դϴ�.");
	}
	public void Next()	//���� �÷��̾�� ���� �Ѱ��ش�. ���� �������� ��� �Է��� ��� ó�� �Ǿ� �����Ƿ� �ڵ����� �����°� �ȴ�.
	{
		gameWndGUI.setEnable(GameWindow.OTHERS, false);			 	// ���� player �� �ٸ� block ���� Ŭ������ ���ϵ��� ����. 
		gameWndGUI.setEnable(GameWindow.CENTER, false);				// center �� block �鵵 �������� ���ϵ��� ����.
		netObject.SendOb( new DataHeader( DataHeader.PASS, ( ( Integer.valueOf( ( myPlayOrder+1 ) ) % ( gameControl.getPlayers().size() ) ) )) );
		// �� �� ���� player ���� ���� �ѱ�.
	}
	public void End() {	//������ ���������� ȣ�� �����ư�� Ȱ��ȭ ���ְ� ��� �̹����� �Ⱥ��̰� �����ѵ� ���ڸ� ǥ�����ش�.
		netObject.SendChatMsg("�̰���ϴ�.");
//		netObject.SendOb(new DataHeader("game2", new GameData(gameControl), netObject.playerNickname));
		gameWndGUI.RemoveAll();
		gameWndGUI.update();
		gameControl=null;
	}
	public void moveBlock(int blockIndex) {		// center �� �ִ� block ���� player ���� �ű�� �Լ�.
		Player me = gameControl.getPlayers().get(myPlayOrder);
		
		if(gameControl.getBlocks().get(blockIndex).getNum()==12) {	// ������ block �� Joker �� ��� diag ��ȭ������ ��ư ���� �����ϰ�, joker �� ���� ��Ҹ� player ���� �����.
			String color;
			if( gameControl.getBlocks().get(blockIndex).getColor() == 0 )
				color = "black";
			else
				color = "white";
			AskDlg diag = new AskDlg("��Ŀ�� ����� ���ڸ� �����ϼ���", color);
			
			int num = diag.getNum();
			gameControl.getBlocks().get(blockIndex).setSortingNum(num);
		}
		me.getBlock(gameControl.getBlocks(), blockIndex);	// �� block �� player ���� ����.
		gameWndGUI.update();
		netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(gameControl)));

		int onlyDraw=4;							// player ���� block �� 4 ���� ���� ������ �����Ѵ�.
		if(gameControl.getPlayers().size()==4)	// player ���� 4 ���� ��쿡��
			onlyDraw=3;							// block�� 3 ���� ������ ������ �����Ѵ�.
		if(me.getHand().size()<=onlyDraw) {		// ���簡 ������  player �� block �� �� ���� ������ block �� �������� ���� ��� ������.
			Next(); 
		}
		else {													// player �� block �� �� ���������� ������ block �� �����ϱ� �����Ѵ�.
			gameWndGUI.setEnable(GameWindow.OTHERS, true);	// �����ϱ� ���� �ٸ� player ���� block �� ���ð����ϰ� �����Ѵ�.
		}
	}
	
	public ArrayList<Block> GetPlayerBlocksState(int playerNum)	// player�� block ���� ��ȯ�Ѵ�.(���¸� ��ȯ�ϴ� ����.)
	{
		return gameControl.getPlayers().get(playerNum).getHand();
	}
	public ArrayList<Block> GetCenterBlocksState() {			// ��� ������ �����Ѵ�.(���¸� ��ȯ�Ѵٰ� ���� Ÿ����)
		return gameControl.getBlocks();
	}
	public int getNumOfPlayer()	{								// play ���� player ���� ���� ��ȯ�Ѵ�.
		return gameControl.getPlayers().size();
	}
	public void AskBlock(int playerOrder, int index, int num) {	// �ش� player ���� block �� ���ڸ� ����� �޼ҵ�.
		Player me = gameControl.getPlayers().get(myPlayOrder);
		Player toTargetPlayer = gameControl.getPlayers().get(playerOrder);
		
		me.askBlock(toTargetPlayer, this, index, num);			// ���� player �� ����� �Ǵ� Player ���� block �� ���.
		gameWndGUI.update();
	}
//	public void AskBlock(int playerOrder, int index, int num) {	// �ش� player ���� block �� ���ڸ� ����� �޼ҵ�.
//		Player me = gameControl.getPlayers().get(myPlayOrder);
//		Player toTargetPlayer = gameControl.getPlayers().get(playerOrder);
//		
//		if( toTargetPlayer.getHand().get(index).getNum() == num ) {			// ��� player ���� ��� ����� ������ ���
//			toTargetPlayer.getHand().get(index).setOpen(true);
//			gameWndGUI.update();
//			netObject.SendOb(new DataHeader("game2", new GameData(gameControl), ""));
//			toTargetPlayer.isPlay(this);
//			
//			if(JOptionPane.showConfirmDialog(null, "����! ����Ͻðڽ��ϱ�?", "Ȯ��",JOptionPane.YES_NO_OPTION)==0)
//				gameWndGUI.setEnable(GameWindow.OTHERS, true);		// ����� ��� �ٸ� �÷��̾���� block �� ���ð����ϰ� ����.
//			else
//				Next();							// �׸��� ��� ���� player ���� �� �ѱ�.
//		}
//		else {			// Ʋ���� ���
//			netObject.SendChatMsg("�����Դϴ�.");		// ���� �޽����� ä��â�� ������
//			me.getLast().setOpen(true);								// �������� ������ block �� �����ϵ��� �����ϰ�
//			gameWndGUI.update();
//			netObject.SendOb(new DataHeader("game2", new GameData(gameControl), ""));
//			Next();
//		}
//	}
	
	public void setGameControl(Game gc) {
		gameControl = gc;
		gameWndGUI.update();
	}

	public void setGameControl(GameData newBlockState) {			// ���޹��� ���ڿ� ���ο� block ���� �й� ������ ����־�, �� ������ ������ block ���� ��Ʈ���� block ���¸� �����ϴ� �޼ҵ�.
//		gameControl.floor = newBlockState.floor;					// center block ���� ���� ������.
		gameControl.setFloor( newBlockState.getFloor() );
		for(int i=0; i<newBlockState.getPlayers().size(); i++) {			// ���ڷ� ���� ���� ��Ʈ�ѷ��� player ����ŭ ���� 
			gameControl.getPlayers().get(i).hand = newBlockState.getPlayers().get(i);
			if( i == myPlayOrder ) {
				for(int j = 0 ; j < newBlockState.getPlayers().get(i).size() ; j++) {
					newBlockState.getPlayers().get(i).get(j).setOwn(true);
				}
			}
			else {
				for(int j = 0 ; j < newBlockState.getPlayers().get(i).size() ; j++) {
					newBlockState.getPlayers().get(i).get(j).setOwn(false);
				}
			}
		}
		for(int i = 0;i<gameControl.getPlayers().size();i++)
			gameControl.getPlayers().get(i).sortBlock();
		gameWndGUI.update();
	}

	public void setPlayOrder(int order) {
		myPlayOrder = order;
	}
	public int getPlayOrder() {
		return myPlayOrder;
	}
}
