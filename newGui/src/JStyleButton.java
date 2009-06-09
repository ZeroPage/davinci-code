import javax.swing.ImageIcon;
import javax.swing.JButton;


public class JStyleButton extends JButton
{
	public JStyleButton()
	{
		super();
		initalButton();
	}
	public JStyleButton(ImageIcon image)
	{
		super(image);
		setDisabledIcon(image);
		initalButton();
		this.setSize(image.getIconWidth(), image.getIconHeight());
		this.setEnabled(false);
	}
	private void initalButton()
	{
		this.setBorderPainted(false);//�׵θ� �׸���
		this.setContentAreaFilled(false);//�׸� ���� ä���
		this.setDefaultCapable(false);//�׸� ���ÿ��� ���
		this.setFocusPainted(false);//���É����� �ֺ��� ����ǥ��
		//this.setMargin(new Insets(3,3,3,3));//�ֺ��� ������ �ֱ�
		this.setOpaque(false);
	}
}
