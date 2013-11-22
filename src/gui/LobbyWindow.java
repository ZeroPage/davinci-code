package gui;

import gui.lobby.ConnectPanel;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import resource.ResourceManager;

public class LobbyWindow {
	private JPanel lobbyPanel;

	private DaVinciGUI gui;

	public LobbyWindow(DaVinciGUI daVinciGUI) {
		this.gui = daVinciGUI;
		// Lobby window 의 외형을 구현.
		ImageIcon BG = ResourceManager.getInstance().getLobbyBackground();
		lobbyPanel = new JBackgroundPanel(BG, JBackgroundPanel.MODE_STRECH);

		lobbyPanel.setLayout(null);

		new ConnectPanel(gui, this);

		daVinciGUI.append(lobbyPanel);
	}

	public void append(JComponent component) {
		lobbyPanel.add(component);
	}

	public void disable() {
		lobbyPanel.setVisible(false);
	}
}
