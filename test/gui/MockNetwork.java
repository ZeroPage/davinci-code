package gui;

import network.Network;

public class MockNetwork extends Network{
	public boolean isCallIsServer = false;
	public boolean isCallSendObject = false;
	public boolean isCallClose = false;
	@Override
	public boolean isServer() {
		isCallIsServer = true;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendObject(Object ob) {
		isCallSendObject = true;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Close() {
		isCallClose = true;
		// TODO Auto-generated method stub
		
	}

}
