import java.io.Serializable;
/****************************************************************
 * 
 * @author Teolex received from khk1027
 * @version 1.0
 * 2010.08.19( Teolex ) : Comments added. Nothing to touch.
 */
public class Block implements Serializable {
	private int 		color;		// 블럭의 색깔. 검정 == 0, 하양 == 1
	private int			num; 		//블럭의 숫자. 조커는 12.
	private boolean		joker;
	private boolean		open;
	private int			sortingNum;	// 조커와 일반 블럭을 구분하지 않고, block 을 정렬하기 위해 선언된 변수. 조커는 기본적으로 12이지만 사용자의 선택에 따라 그 외의 숫자가 되기 때문에 이 변수가 필요함.
	private boolean		own;		// 소유된 블럭인지 아닌지를 저장할 변수. transient는 직렬화할 때 제외할 변수임을 명시하는 기능을 함.
	
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	
	/**
	 * 기본 생성자는 만약을 대비해서 유지하는 것이 좋겠으나,
	 * block 중에 번호가 없는 block 이 있을 수 없으므로
	 * 주석으로만 유지하기로 한다.
	 */
//	Block()					// 기본 생성자
//	{
//		color	= -1;
//		num		= -2;
//		own 	= false;
//		open 	= false;
//		joker 	= false;
//	}
	Block(int color, int num)
	{
		setColor(color);
		setNum(num);
		own  		= false; //누군가 가졌는지 여부. 바닥에 깔린 패와 player 가 가진 패 두 가지로 나뉘므로.
		open 		= false; //공개여부. player 가 가진 패 중에 공개된 것과 안된 것이 있으므로.
		joker 		= false;
		sortingNum 	= num;
		if( num == 12)	joker = true;
	}
	public void setSortingNum(int sortingNum) {	// block 이 조커인 경우 
		this.sortingNum = sortingNum;
	}
	public void setColor(int n) {
		color = n;
	}
	public void setNum(int n) {
		num = n;
	}
	public void setOwn(boolean n) {
		own = n;
	}
	public void setOpen(boolean n) {
		open = n;
	}
	public int getSortingNum() {
		return sortingNum;
	}
	public boolean isJoker() {
		return joker;
	} 
	public int getColor() {
		return color;
	}
	public int getNum() {
		return num;
	}
	public boolean isOwned() {
		return own;
	}
	public boolean isOpen() {
		return open;
	}
}