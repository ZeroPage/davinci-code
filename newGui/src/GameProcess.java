import java.util.ArrayList;

import javax.swing.JOptionPane;


public class GameProcess {
	GameWindow	gameWndGUI;
	Network		netObject;
	int 		myPlayOrder = 0;		 // 자신의 플레이 순서.

	Game gameControl;

	public GameProcess(GameWindow GUITarget, Network NetTarget) {	// 게임 프로세스 객체에 게임 GUI 와 네트워크를 설정.
		gameWndGUI	= GUITarget;		// 프로세스를 생성한 game window를 가리키기위한 변수.
		netObject	= NetTarget;
	}
	public void Start() {		// server 가 게임을 시작할 때, 자신의 게임 컨트롤를 생성하고, client 들에게 총 play 인원과 서버가 생성한 게임 컨트롤을 전달하고 자신부터 게임을 시작하는 메소드.
		gameControl = new Game( ((Server)netObject).clientNum+1);
		((Server)netObject).SendOrder();
		netObject.SendOb( new DataHeader( DataHeader.TOTALCOUNT, gameControl.getPlayers().size()) );
		netObject.SendOb( new DataHeader( DataHeader.GAME, gameControl) );		// 모든 client 들에게 server 가 생성한 게임 컨트롤을 전달한다.
	}
	public void turn() {
		gameWndGUI.update();
		gameWndGUI.setEnable(GameWindow.CENTER, true);				// player 자신의 턴이 되기 전까지는 center block 을 가져올 수 없다.
		netObject.SendChatMsg("턴 입니다.");
	}
	public void Next()	//다음 플레이어에게 턴을 넘겨준다. 게임 윈도우의 모든 입력은 블록 처리 되어 있으므로 자동으로 대기상태가 된다.
	{
		gameWndGUI.setEnable(GameWindow.OTHERS, false);			 	// 현재 player 가 다른 block 들을 클릭하지 못하도록 설정. 
		gameWndGUI.setEnable(GameWindow.CENTER, false);				// center 의 block 들도 선택하지 못하도록 설정.
		netObject.SendOb( new DataHeader( DataHeader.PASS, ( ( Integer.valueOf( ( myPlayOrder+1 ) ) % ( gameControl.getPlayers().size() ) ) )) );
		// 그 후 다음 player 에게 턴을 넘김.
	}
	public void End() {	//게임이 끝났을때의 호출 레디버튼을 활성화 해주고 모든 이미지를 안보이게 지정한뒤 승자를 표시해준다.
		netObject.SendChatMsg("이겼습니다.");
//		netObject.SendOb(new DataHeader("game2", new GameData(gameControl), netObject.playerNickname));
		gameWndGUI.RemoveAll();
		gameWndGUI.update();
		gameControl=null;
	}
	public void moveBlock(int blockIndex) {		// center 에 있는 block 들을 player 에게 옮기는 함수.
		Player me = gameControl.getPlayers().get(myPlayOrder);
		
		if(gameControl.getBlocks().get(blockIndex).getNum()==12) {	// 선택한 block 이 Joker 일 경우 diag 대화상자의 버튼 색을 변경하고, joker 를 놓을 장소를 player 에게 물어본다.
			String color;
			if( gameControl.getBlocks().get(blockIndex).getColor() == 0 )
				color = "black";
			else
				color = "white";
			AskDlg diag = new AskDlg("조커가 대신할 숫자를 선택하세요", color);
			
			int num = diag.getNum();
			gameControl.getBlocks().get(blockIndex).setSortingNum(num);
		}
		me.getBlock(gameControl.getBlocks(), blockIndex);	// 이 block 을 player 에게 전달.
		gameWndGUI.update();
		netObject.SendOb(new DataHeader(DataHeader.GAMEDATA, new GameData(gameControl)));

		int onlyDraw=4;							// player 들은 block 을 4 개를 먼저 가지고 시작한다.
		if(gameControl.getPlayers().size()==4)	// player 수가 4 명일 경우에는
			onlyDraw=3;							// block을 3 개만 가지고 게임을 시작한다.
		if(me.getHand().size()<=onlyDraw) {		// 현재가 차례인  player 가 block 을 다 받을 때까지 block 만 가져가고 턴을 계속 돌린다.
			Next(); 
		}
		else {													// player 가 block 을 다 가져가고나면 상대방의 block 을 추측하기 시작한다.
			gameWndGUI.setEnable(GameWindow.OTHERS, true);	// 추측하기 위해 다른 player 들의 block 을 선택가능하게 설정한다.
		}
	}
	
	public ArrayList<Block> GetPlayerBlocksState(int playerNum)	// player의 block 들을 반환한다.(상태를 반환하는 것임.)
	{
		return gameControl.getPlayers().get(playerNum).getHand();
	}
	public ArrayList<Block> GetCenterBlocksState() {			// 가운데 블럭들을 리턴한다.(상태를 반환한다고 봐도 타당함)
		return gameControl.getBlocks();
	}
	public int getNumOfPlayer()	{								// play 중인 player 들의 수를 반환한다.
		return gameControl.getPlayers().size();
	}
	public void AskBlock(int playerOrder, int index, int num) {	// 해당 player 에게 block 의 숫자를 물어보는 메소드.
		Player me = gameControl.getPlayers().get(myPlayOrder);
		Player toTargetPlayer = gameControl.getPlayers().get(playerOrder);
		
		me.askBlock(toTargetPlayer, this, index, num);			// 현재 player 가 대상이 되는 Player 에게 block 을 물어봄.
		gameWndGUI.update();
	}
//	public void AskBlock(int playerOrder, int index, int num) {	// 해당 player 에게 block 의 숫자를 물어보는 메소드.
//		Player me = gameControl.getPlayers().get(myPlayOrder);
//		Player toTargetPlayer = gameControl.getPlayers().get(playerOrder);
//		
//		if( toTargetPlayer.getHand().get(index).getNum() == num ) {			// 대상 player 에게 물어본 결과가 정답일 경우
//			toTargetPlayer.getHand().get(index).setOpen(true);
//			gameWndGUI.update();
//			netObject.SendOb(new DataHeader("game2", new GameData(gameControl), ""));
//			toTargetPlayer.isPlay(this);
//			
//			if(JOptionPane.showConfirmDialog(null, "빙고! 계속하시겠습니까?", "확인",JOptionPane.YES_NO_OPTION)==0)
//				gameWndGUI.setEnable(GameWindow.OTHERS, true);		// 계속할 경우 다른 플레이어들의 block 을 선택가능하게 설정.
//			else
//				Next();							// 그만둘 경우 다음 player 에게 턴 넘김.
//		}
//		else {			// 틀렸을 경우
//			netObject.SendChatMsg("오답입니다.");		// 오답 메시지를 채팅창에 보내고
//			me.getLast().setOpen(true);								// 마지막에 가져온 block 을 공개하도록 설정하고
//			gameWndGUI.update();
//			netObject.SendOb(new DataHeader("game2", new GameData(gameControl), ""));
//			Next();
//		}
//	}
	
	public void setGameControl(Game gc) {
		gameControl = gc;
		gameWndGUI.update();
	}

	public void setGameControl(GameData newBlockState) {			// 전달받은 인자에 새로운 block 들의 분배 정보가 들어있어, 그 정보로 현재의 block 게임 컨트롤의 block 상태를 갱신하는 메소드.
//		gameControl.floor = newBlockState.floor;					// center block 들을 새로 설정함.
		gameControl.setFloor( newBlockState.getFloor() );
		for(int i=0; i<newBlockState.getPlayers().size(); i++) {			// 인자로 받은 게임 컨트롤러의 player 수만큼 돌며 
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
