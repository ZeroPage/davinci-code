abstract class mWindow
{
	protected Network NC;
	
	public void setNetwork(Network n)
	{
		NC = n;
	}
	protected Network getNetwork()
	{
		return NC;
	}
	
	public mWindow()
	{
	}
	abstract public void AddChatString(String msg);
}