package gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import core.GameProcess;
import network.Network;

public class RoomWindow {

	private JPanel roomPanel;

	public RoomWindow(DaVinciGUI gui, Network network) {

		roomPanel = new JPanel();
		roomPanel.setLayout(new BorderLayout());

		GameProcess gameProcess = new GameProcess(network);
		new GameWindow(this, network, gameProcess);
		new ChatWindow(this, network, gameProcess, gui);

		// gui.add(BorderLayout.CENTER, roomPanel);
		gui.append(roomPanel);

		roomPanel.setVisible(true);
	}

	public void add(String name, JComponent component) {
		roomPanel.add(name, component);
	}

	public void add(JComponent component) {
		roomPanel.add(component);
	}

}
