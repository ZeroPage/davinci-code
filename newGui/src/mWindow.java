abstract class mWindow
{
	protected Network NC;
	
	public void setNetwork(Network n)
	{
		NC = n;
	}
	
	public mWindow()
	{
	}
	abstract public void AddChatString(String msg);
}