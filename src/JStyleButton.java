import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JStyleButton extends JButton {
	public JStyleButton() {
		super();
		initialButton();
	}

	public JStyleButton(ImageIcon image) {
		super(image);
		setDisabledIcon(image);
		this.setSize(image.getIconWidth(), image.getIconHeight());
		initialButton();
	}

	private void initialButton() {
		this.setBorderPainted(false); // Å×µÎ¸® ±×¸®±â
		this.setContentAreaFilled(false); // ³×¸ð³­ ¿µ¿ª Ã¤¿ì±â
		this.setDefaultCapable(false); // ³×¸ð³­ ¼±ÅÃ¿µ¿ª Ãë¼Ò
		this.setFocusPainted(false); // ¼±ÅÃ‰çÀ»¶§ ÁÖº¯ÀÇ Á¡¼±Ç¥½Ã
		this.setOpaque(false);

		this.setEnabled(false);
		this.setRolloverEnabled(false);
	}

	public void setIcon(Icon defaultIcon) {
		setDisabledIcon(defaultIcon);
		super.setIcon(defaultIcon);
	}
}
