import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

class LobbyWindow implements ActionListener, ItemListener
{
	JTextField	JTF_IPAddr,	JTF_Nick,	JTF_Port;
	JCheckBox	JCB_Server;
	JButton		JB_Connect, JB_Cancel;
	JPanel		JPanel_Lobby;
	JRootPane	motherPane;
	
	Network		myNetworkType;			// ��Ʈ��ũ�� ������ ���� player�� ���¸� ������ ����.
										// server ������ �ϴ� player�� ��� myNetworkType = Server();
										// client �� player �� ��� myNetworkType = Client();
	
	ImageIcon BG = new ImageIcon(DavichiGUI.class.getResource("cover.gif"));
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == JB_Connect || event.getSource() == JTF_IPAddr) {
			if(JTF_Nick.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "�г����� �Է��ϼ���.", "�˸�", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(JCB_Server.isSelected())	myNetworkType = new Server();	//����
			else						myNetworkType = new Client();	//Ŭ��				
			myNetworkType.setM_Name		( JTF_Nick.getText() );							// ��� ��Ʈ��ũ ��ü�� nick name ����.
			myNetworkType.setPortNum	( Integer.parseInt( JTF_Port.getText() ) );		// ��Ʈ ����.
			myNetworkType.Connect		( JTF_IPAddr.getText() );						// server �� ����.
			
			myNetworkType.myRoomWnd = new RoomWindow((JPanel) motherPane.getContentPane(), myNetworkType);	//�������� �����ϰ� ��Ʈ��ũ�� ����.
			
			JPanel_Lobby.setVisible(false);
			myNetworkType.myRoomWnd.JPanel_Room.setVisible(true);
			
			if(myNetworkType.isServer())	myNetworkType.SendChatMsg("������ �����Ͽ����ϴ�,");
			else							myNetworkType.SendChatMsg("�����Ͽ����ϴ�.");
		}
		if(event.getSource() == JB_Cancel)	System.exit(0);
	}
	
	public LobbyWindow(JPanel main, JRootPane mother)	// Lobby window �� ������ ����.
	{
		motherPane		= mother;
		JPanel_Lobby	= new JPanel()
		{
			public void paint(Graphics g)
			{
				g.drawImage(BG.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
				this.setOpaque(false);
				this.setPreferredSize(new Dimension(BG.getIconWidth(), BG.getIconHeight()));
				super.paint(g);
			}
		};
		JPanel_Lobby.setLayout(null);
		
		JPanel JPanel_Connect = new JPanel();
		JPanel_Connect.setOpaque(false);
		JPanel_Connect.setPreferredSize(new Dimension(380,300));
		
		JPanel_Connect.setLayout(null);
		JPanel_Connect.setBounds(600, 300, 380, 300);
		JPanel_Connect.setOpaque(false);
		
		JLabel temp = new JLabel("Nick Name");
		temp.setForeground(Color.red);
		temp.setBounds(0, 10, 100, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		JPanel_Connect.add(temp);
		
		JTF_Nick = new JTextField();
		JTF_Nick.setBounds(100, 10, 100, 30);
		JPanel_Connect.add(JTF_Nick);
		
		temp = new JLabel("Server");
		temp.setForeground(Color.red);
		temp.setBounds(200, 10, 80, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		JPanel_Connect.add(temp);
		
		JCB_Server = new JCheckBox();
		JCB_Server.setBounds(280, 19, 13, 13);
		JCB_Server.addItemListener(this);
		JCB_Server.setMargin(new Insets(-2,-2,-2,-2));
		JPanel_Connect.add(JCB_Server);
		
		temp = new JLabel("IP");
		temp.setForeground(Color.red);
		temp.setBounds(0, 50, 100, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		JPanel_Connect.add(temp);
		
		JTF_IPAddr = new JTextField();
		JTF_IPAddr.setBounds(100, 50, 200, 30);
		JTF_IPAddr.setText("127.0.0.1");
		JTF_IPAddr.addActionListener(this);
		JPanel_Connect.add(JTF_IPAddr);
		
		temp = new JLabel("Port");
		temp.setForeground(Color.red);
		temp.setBounds(0, 90, 100, 30);
		temp.setHorizontalAlignment(JLabel.CENTER);
		JPanel_Connect.add(temp);
		
		JTF_Port = new JTextField();
		JTF_Port.setBounds(100, 90, 50, 30);
		JTF_Port.setText("10000");
		JPanel_Connect.add(JTF_Port);
		
		JB_Connect = new JButton("����");
		JB_Connect.setBounds(50 , 130, 100, 30);
		JB_Connect.addActionListener(this);
		JPanel_Connect.add(JB_Connect);
		
		
		JB_Cancel = new JButton("���");
		JB_Cancel.setBounds(200, 130, 100, 30);
		JB_Cancel.addActionListener(this);
		JPanel_Connect.add(JB_Cancel);
		
		JPanel_Lobby.add(JPanel_Connect);
		JTF_Nick.requestFocus();
		
		JPanel_Lobby.add(JPanel_Connect);
		main.add(JPanel_Lobby);
	}
	public void itemStateChanged(ItemEvent event) {		// Server �׸� üũ�� ��� IP �Է��ʵ尡 ��Ȱ��ȭ��.
		if(event.getSource() == JCB_Server) {
			if(JCB_Server.isSelected())	JTF_IPAddr.setEnabled(false);
			else						JTF_IPAddr.setEnabled(true);
		}
	}

	public void AddChatString(String msg) {	// DavichiGUI ���� ����޽����� ������ ���� �ۼ���.
		myNetworkType.SendChatMsg(msg);		// ��� player �鿡�� �޽����� �����Ѵ�.
	}
}