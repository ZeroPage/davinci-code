package network;
import java.io.Serializable;

// 네트워크로 오갈 데이터의 헤더. 필요한 정보들을 입력한다.
public class DataHeader implements Serializable{
	private int		flag;
	private Object	data = null;
	
	public static final int	CHAT        = 0x1;
	public static final int	GAME        = 0x2;
	public static final int	GAMEDATA    = 0x3;
	public static final int	PASS        = 0x4;
	public static final int	MYORDER     = 0x5;
	public static final int	TOTALCOUNT  = 0x6;

	public DataHeader(int flag, Object data) {
		this.flag = flag;		// 데이터의 종류
		this.data = data;		// 데이터의 내용
	}
	public int		getFlag() { return flag; }
	public Object	getData() { return data; }
}
