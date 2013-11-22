package gui.lobby;

import gui.DaVinciGUI;
import gui.LobbyWindow;
import gui.RoomWindow;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import network.Network;

public class ConnectPanel {

	private JPanel connectPanel;
	private NickNamePanel nickNamePanel;
	private AddressPanel addressPanel;
	private ServerCheckPanel serverCheckPanel;
	private ButtonPanel buttonPanel;
	private DaVinciGUI gui;
	private LobbyWindow lobbyWindow;

	public ConnectPanel(DaVinciGUI gui, LobbyWindow lobbyWindow) {
		this.gui = gui;
		this.lobbyWindow = lobbyWindow;
		connectPanel = new JPanel();
		connectPanel.setOpaque(false);
		connectPanel.setPreferredSize(new Dimension(380, 300));

		connectPanel.setLayout(null);
		connectPanel.setBounds(600, 300, 380, 300);
		connectPanel.setOpaque(false);

		nickNamePanel = new NickNamePanel(this);
		serverCheckPanel = new ServerCheckPanel(this);
		addressPanel = new AddressPanel(this);
		buttonPanel = new ButtonPanel(this, gui);
		
		//update gui mode
		serverCheckPanel.itemStateChanged(null);
		
		lobbyWindow.append(connectPanel);
	}

	public void append(JComponent component) {
		connectPanel.add(component);
	}

	public String getNickName() {
		return nickNamePanel.getNickName();
	}

	public int getPortNum() {
		return addressPanel.getPortNum();
	}

	public String getAddress() {
		return addressPanel.getAddress();
	}

	public void enableServerMode() {
		ActionListener action = new LinstenAction(this);
		addressPanel.disableAddressField();
		addressPanel.setAction(action);
		buttonPanel.setAction(action);
	}

	public void enableClinetMode() {
		ActionListener action = new ConnectAction(this);
		addressPanel.enableAddressField();
		addressPanel.setAction(action);
		buttonPanel.setAction(action);
	}

	public void onConnected(Network network) {
		new RoomWindow(gui, network);
		lobbyWindow.disable();
		network.sendChatMessage("접속하였습니다.");
	}

}