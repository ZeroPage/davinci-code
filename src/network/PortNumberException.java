package network;

public class PortNumberException extends Exception {
	private static final long serialVersionUID = -7932626798953836466L;
	
	private int portNum;

	public PortNumberException(int portNum, IllegalArgumentException e) {
		super(e);
		this.portNum = portNum;
	}
	public int getPortNum() {
		return portNum;
	}
}
