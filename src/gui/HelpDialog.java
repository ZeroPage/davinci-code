package gui;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import resource.ResourceManager;

public class HelpDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private ImageIcon BG = ResourceManager.getInstance().getHelp();

	public HelpDialog(JFrame owner) {
		super(owner, "게임설명", true);
		this.setBounds(0, 0, 100, 100);
		this.setVisible(true);
	}

	public void paint(Graphics g) {
		this.setSize(BG.getIconWidth(), BG.getIconHeight());
		g.drawImage(BG.getImage(), 0, 20, this.getWidth(), this.getHeight(),
				null);
		// this.getContentPane().
		// super.paint(g);
	}
}
