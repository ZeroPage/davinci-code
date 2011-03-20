import java.io.Serializable;

// ��Ʈ��ũ�� ���� �������� ���. �ʿ��� �������� �Է��Ѵ�.
public class DataHeader implements Serializable
{
	private int		flag;
	private Object	data = null;
	
	public static final int	CHAT		= 0x1;
	public static final int	GAME		= 0x2;
	public static final int	GAMEDATA	= 0x3;
	public static final int	PASS		= 0x4;
	public static final int	MYORDER		= 0x5;
	public static final int	TOTALCOUNT	= 0x6;

	public DataHeader() {	}
	public DataHeader(int flag, Object data) {
		this.flag = flag;		// �������� ����
		this.data = data;		// �������� ����
	}
	public void 	setFlag(int target) 	{ flag = target; }
	public void 	setData(Object target)	{ data = target; }
	public int		getFlag() { return flag; }
	public Object	getData() { return data; }
}
