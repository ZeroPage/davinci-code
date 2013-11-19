package gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import resource.ResourceManager;
import core.Block;

public class PlayerBlock extends JStyleButton implements ActionListener {

	private Process process;
	private BlockColor color = null;
	private int num;
	private int index;

	public PlayerBlock(Process process, int index) {
		this.process = process;
		this.addActionListener(this);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.index = index;
	}

	public void update(Block block) {
		ResourceManager resourceManager = ResourceManager.getInstance();
		num = block.getNum();
		color = null;
		
		if (block.getColor() == 0) {
			color = BlockColor.Black;
		} else if(block.getColor() == 1) {
			color = BlockColor.White;
		}
		
		if (block.isOpen() || block.isOwned()) {
			setIcon(resourceManager.getCardImage(color,block. getNum(), block.isOpen()));
		} else {
			setIcon(resourceManager.getCardImage(color, false));
			setRolloverIcon(resourceManager.getCardImage(color, true));
		}
		setRolloverEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedNum = (new AskDlg()).getNum();
	}

}
