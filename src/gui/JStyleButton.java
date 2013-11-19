package gui;
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
		this.setBorderPainted(false); 
		this.setContentAreaFilled(false); 
		this.setDefaultCapable(false); 
		this.setFocusPainted(false); 
		this.setOpaque(false);

		this.setEnabled(false);
		this.setRolloverEnabled(false);
	}

	public void setIcon(Icon defaultIcon) {
		setDisabledIcon(defaultIcon);
		super.setIcon(defaultIcon);
	}
}
