import java.io.Serializable;
/****************************************************************
 * 
 * @author Teolex received from khk1027
 * @version 1.0
 * 2010.08.19( Teolex ) : Comments added. Nothing to touch.
 */
public class Block implements Serializable {
	private int 		color;		// ���� ����. ���� == 0, �Ͼ� == 1
	private int			num; 		//���� ����. ��Ŀ�� 12.
	private boolean		joker;
	private boolean		open;
	private int			sortingNum;	// ��Ŀ�� �Ϲ� ���� �������� �ʰ�, block �� �����ϱ� ���� ����� ����. ��Ŀ�� �⺻������ 12������ ������� ���ÿ� ���� �� ���� ���ڰ� �Ǳ� ������ �� ������ �ʿ���.
	private boolean		own;		// ������ ������ �ƴ����� ������ ����. transient�� ����ȭ�� �� ������ �������� ����ϴ� ����� ��.
	
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	
	/**
	 * �⺻ �����ڴ� ������ ����ؼ� �����ϴ� ���� ��������,
	 * block �߿� ��ȣ�� ���� block �� ���� �� �����Ƿ�
	 * �ּ����θ� �����ϱ�� �Ѵ�.
	 */
//	Block()					// �⺻ ������
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
		own  		= false; //������ �������� ����. �ٴڿ� �� �п� player �� ���� �� �� ������ �����Ƿ�.
		open 		= false; //��������. player �� ���� �� �߿� ������ �Ͱ� �ȵ� ���� �����Ƿ�.
		joker 		= false;
		sortingNum 	= num;
		if( num == 12)	joker = true;
	}
	public void setSortingNum(int sortingNum) {	// block �� ��Ŀ�� ��� 
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