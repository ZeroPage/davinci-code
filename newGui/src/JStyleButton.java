import javax.swing.Icon;
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
		this.setSize(image.getIconWidth(), image.getIconHeight());
		initalButton();
	}
	private void initalButton()
	{
		this.setBorderPainted(false);//�׵θ� �׸���
		this.setContentAreaFilled(false);//�׸� ���� ä���
		this.setDefaultCapable(false);//�׸� ���ÿ��� ���
		this.setFocusPainted(false);//���É����� �ֺ��� ����ǥ��
		//this.setMargin(new Insets(3,3,3,3));//�ֺ��� ������ �ֱ�
		this.setOpaque(false);
		
		this.setEnabled(false);
		this.setRolloverEnabled(false);
	}
	public void setIcon(Icon defaultIcon)
	{
		setDisabledIcon(defaultIcon);
		super.setIcon(defaultIcon);
	}
}
