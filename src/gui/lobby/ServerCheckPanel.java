package gui.lobby;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class ServerCheckPanel implements ItemListener {

	private JCheckBox serverCheckBox;
	private ConnectPanel connectPanel;

	public ServerCheckPanel(ConnectPanel connectPanel) {
		this.connectPanel = connectPanel;
		JLabel label = new JLabel("Server");
		label.setForeground(Color.red);
		label.setBounds(200, 10, 80, 30);
		label.setHorizontalAlignment(JLabel.CENTER);
		connectPanel.append(label);

		serverCheckBox = new JCheckBox();
		serverCheckBox.setBounds(280, 19, 13, 13);
		serverCheckBox.addItemListener(this);
		serverCheckBox.setMargin(new Insets(-2, -2, -2, -2));
		connectPanel.append(serverCheckBox);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// Server 항목에 체크할 경우 IP 입력필드가 비활성화됨.
		if (serverCheckBox.isSelected()) {
			connectPanel.enableServerMode();
		} else {
			connectPanel.enableClinetMode();
		}
	}

}
