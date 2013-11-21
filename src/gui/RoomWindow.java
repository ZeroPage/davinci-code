package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import core.GameProcess;
import network.Network;

public class RoomWindow {

	public RoomWindow(DaVinciGUI gui, Network network) {

		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new BorderLayout());

		GameProcess gameProcess = new GameProcess(network);
		new GameWindow(roomPanel, network, gameProcess);
		new ChatWindow(roomPanel, network, gameProcess, gui); 
		
		// gui.add(BorderLayout.CENTER, roomPanel);
		gui.append(roomPanel);
		
		roomPanel.setVisible(true);
	}

}
