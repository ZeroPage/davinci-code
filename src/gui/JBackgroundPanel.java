package gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JBackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1989959541462832478L;

	public static final int MODE_NONE = 0;
	public static final int MODE_STRECH = 1;

	private ImageIcon image;
	private int mode = MODE_NONE;

	public JBackgroundPanel(ImageIcon background) {
		this.image = background;
	}

	public JBackgroundPanel(ImageIcon background, int mode) {
		this(background);
		this.mode = mode;
	}

	@Override
	public void paint(Graphics g) {
		switch (mode) {
		case MODE_STRECH:
			g.drawImage(image.getImage(), 0, 0, this.getWidth(),
					this.getHeight(), null);
			this.setPreferredSize(new Dimension(image.getIconWidth(), image
					.getIconHeight()));
			break;
		default:
			g.drawImage(image.getImage(), 0, 0, image.getIconWidth(),
					image.getIconHeight(), null);
		}
		this.setOpaque(false);
		super.paint(g);
	}
}
