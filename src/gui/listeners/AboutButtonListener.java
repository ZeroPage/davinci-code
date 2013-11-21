package gui.listeners;

import gui.RoomWindow;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import resource.ResourceManager;

public class AboutButtonListener implements ActionListener {
	private RoomWindow roomWindow;

	public AboutButtonListener(RoomWindow roomWindow) {
		this.roomWindow = roomWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog helpDialog = new JDialog(roomWindow.getFrames()[0], "게임설명",
				true) {
			private static final long serialVersionUID = 1L;
			ImageIcon BG = ResourceManager.getInstance().getHelp();

			public void paint(Graphics g) {
				this.setSize(BG.getIconWidth(), BG.getIconHeight());
				g.drawImage(BG.getImage(), 0, 20, this.getWidth(),
						this.getHeight(), null);
				// this.getContentPane().
				// super.paint(g);
			}
		};
		helpDialog.setBounds(0, 0, 100, 100);
		helpDialog.setVisible(true);
	}
}
