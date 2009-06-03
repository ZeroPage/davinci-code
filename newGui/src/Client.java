import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends Network
{
	Socket m_Sorcket;
	public void Connect(String ip)
	{
		try
		{
			m_Sorcket = new Socket(ip, 10000);
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SendChatMsg(String msg)
	{
		// TODO Auto-generated method stub
	}
	public void Close()
	{
		// TODO Auto-generated method stub
	}
}
