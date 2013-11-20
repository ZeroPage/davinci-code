package gui;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JBackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1989959541462832478L;
	
	private ImageIcon image;

	public JBackgroundPanel(ImageIcon background) {
		this.image = background;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(image.getImage(), 0, 0, image.getIconWidth(),
				image.getIconHeight(), null);
		this.setOpaque(false);
		super.paint(g);
	}
}
