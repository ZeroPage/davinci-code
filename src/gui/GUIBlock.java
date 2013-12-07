package gui;

import java.awt.Insets;

import resource.ResourceManager;
import core.Block;
import core.GameProcess;

public class GUIBlock extends JStyleButton {
	private static final long serialVersionUID = -5740660089413594516L;
	protected GameProcess gameProcess;
	private BlockColor color = null;
	protected int index;
	protected int playerNum;

	public GUIBlock(GameProcess gameProcess, int playerNum, int index) {
		this.gameProcess = gameProcess;
		this.setMargin(new Insets(0, 0, 0, 0));
		this.playerNum = playerNum;
		this.index = index;
	}

	public void update(Block block) {
		ResourceManager resourceManager = ResourceManager.getInstance();
		color = null;

		if (block.getColor() == 0) {
			color = BlockColor.Black;
		} else if (block.getColor() == 1) {
			color = BlockColor.White;
		}

		if (block.isOpen() || block.isOwned()) {
			setIcon(resourceManager.getCardImage(color, block.getNum(),
					block.isOpen()));
		} else {
			setIcon(resourceManager.getCardImage(color, false));
			setRolloverIcon(resourceManager.getCardImage(color, true));
		}
		setRolloverEnabled(false);
	}

	@Override
	public void setEnabled(boolean b) {
		setRolloverEnabled(b);
		super.setEnabled(b);
	}

}
