package gui;

import network.Network;

public class MockNetwork extends Network{
	public boolean isCallIsServer = false;
	public boolean isCallSendObject = false;
	public boolean isCallClose = false;
	@Override
	public boolean isServer() {
		isCallIsServer = true;
		
		return false;
	}

	@Override
	public void sendObject(Object ob) {
		isCallSendObject = true;
		
		
	}

	@Override
	public void Close() {
		isCallClose = true;
		
		
	}

}
