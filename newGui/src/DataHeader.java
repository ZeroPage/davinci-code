import java.io.Serializable;

public class DataHeader implements Serializable
{
	String headData = "";
	Object data = null;

	public DataHeader()
	{
	}
	public DataHeader(String header, Object data)
	{
		headData = header;
		this.data = data;
	}
	public void setHeadData(String headData)
	{
		this.headData = headData;
	}
	public void setData(Object data)
	{
		this.data = data;
	}
	public String getHeadData()
	{
		return headData;
	}
	public Object getData()
	{
		return data;
	}
}
