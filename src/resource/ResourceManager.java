package resource;

import gui.BlockColor;

import javax.swing.ImageIcon;

public class ResourceManager {
	private static volatile ResourceManager manager = null;

	public static ResourceManager getInstance() {
		if (manager == null) {
			synchronized (ResourceManager.class) {
				if (manager == null) {
					manager = new ResourceManager();
				}
			}
		}
		return manager;
	}
	
	private ImageIcon[] cardBlack;
	private ImageIcon[] cardBlackOpen;
	private ImageIcon[] cardWhite;
	private ImageIcon[] cardWhiteOpen;
	private ImageIcon cardBlackUnknown;
	private ImageIcon cardWhiteUnknown;
	private ImageIcon cardBlackUnknownRollerover;
	private ImageIcon cardWhiteUnknownRollerover;
	private ImageIcon gameBackground;
	private ImageIcon lobbyBackground;
	private ImageIcon about;

	private ResourceManager() {
		
		cardBlack     = new ImageIcon[13];
		cardBlackOpen = new ImageIcon[13];
		cardWhite     = new ImageIcon[13];
		cardWhiteOpen = new ImageIcon[13];
		// 이미지 로딩
		Class<ResourceManager> clazz = ResourceManager.class;
		
		gameBackground = new ImageIcon(clazz.getResource("img/board.jpg"));
		lobbyBackground = new ImageIcon(clazz.getResource("img/cover.gif"));
		about = new ImageIcon(clazz.getResource("img/About.jpg"));
		
		for (int i = 0; i < 13; i++) {
			cardBlack[i] = new ImageIcon(clazz.getResource("img/card/b" + i + ".gif"));
			cardBlackOpen[i] = new ImageIcon(clazz.getResource("img/card/b" + i + "r.gif"));
			cardWhite[i] = new ImageIcon(clazz.getResource("img/card/w" + i + ".gif"));
			cardWhiteOpen[i] = new ImageIcon(clazz.getResource("img/card/w" + i + "r.gif"));
		}
		cardBlackUnknown = new ImageIcon(clazz.getResource("img/card/bu.gif"));
		cardWhiteUnknown = new ImageIcon(clazz.getResource("img/card/wu.gif"));
		cardBlackUnknownRollerover = new ImageIcon(clazz.getResource("img/card/bur.gif"));
		cardWhiteUnknownRollerover = new ImageIcon(clazz.getResource("img/card/wur.gif"));
	}

	public ImageIcon getCardImage(BlockColor color, int num, boolean isOpened) {
		switch (color){
		case Black :
			return isOpened ? cardBlackOpen[num] : cardBlack[num];
		case White :
			return isOpened ? cardWhiteOpen[num] : cardWhite[num];
		}
		throw new NullPointerException();
	}
	public ImageIcon getCardImage(BlockColor color, boolean rollover){
		switch (color) {
		case Black:
			return rollover ? cardBlackUnknownRollerover : cardBlackUnknown;
		case White:
			return rollover ? cardWhiteUnknownRollerover : cardWhiteUnknown;
		}
		throw new NullPointerException();
	}

	public ImageIcon getGameBackground() {
		return gameBackground;
	}

	public ImageIcon getLobbyBackground() {
		return lobbyBackground;
	}
	public ImageIcon getHelp(){
		return about;
	}
}
