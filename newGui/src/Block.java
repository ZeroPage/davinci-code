import java.io.Serializable;

public class Block implements Serializable {
	private int color; //검정 0, 하양 1
	private int num; //하이픈(조커)는 -1
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
		own  = false; //누군가 가졌는지 여부
		open = false; //공개여부
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