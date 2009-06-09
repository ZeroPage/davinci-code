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
		this.setBorderPainted(false);//테두리 그리기
		this.setContentAreaFilled(false);//네모난 영역 채우기
		this.setDefaultCapable(false);//네모난 선택영역 취소
		this.setFocusPainted(false);//선택됬을때 주변의 점선표시
		//this.setMargin(new Insets(3,3,3,3));//주변에 여유분 주기
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
