import java.io.Serializable;

public class Block implements Serializable {
	private int color; //���� 0, �Ͼ� 1
	private int num; //������(��Ŀ)�� -1
	private boolean own;
	private boolean open;
	
	Block() {
		color = -1;
		num = -2;
		own = false;
		open = false;
	}
	Block(int color, int num){
		setColor(color);
		setNum(num);
		own  = false; //������ �������� ����
		open = false; //��������
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
	public int getColor() {
		return color;
	}
	public int getNum() {
		return num;
	}
	public boolean getOwn() {
		return own;
	}
	public boolean getOpen() {
		return open;
	}
}