/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.arpg.actor;

import play.game.actor.Graphics;
import play.game.actor.ImageGraphics;
import play.game.areagame.io.ResourcePath;
import play.math.RegionOfInterest;
import play.math.Vector;
import play.window.Canvas;

class ARPGPlayerStatusGUI implements Graphics {

	private float playerHp;
	private float playerMaxHp;
	private ARPGItem equippedItem;
	private int playerMoney;
	private int currentBagIndex;
	
	// the foreground's depth is already "hard-coded" at 1000f 
	private static final float FOREGROUND_DEPTH = 1000f;
	
	/**
	 * Constructor for ARPGPlayerStatusGUI
	 * @param hp (float) initial player currentHp.
	 * @param maxHp (float) player MaxHp .
	 * @param equippedItem (ARPGItem) current equipped Item.
	 * @param money (money) initial current player money.
	 * @param currentBagIndex (int) currentBagindex (according to ARPGInventory bags
	 */
	protected ARPGPlayerStatusGUI(float hp, float maxHp, ARPGItem equippedItem, int money, int bag) {
		playerHp = hp;
		playerMaxHp = maxHp;
		this.equippedItem = equippedItem;
		playerMoney = money;
		currentBagIndex = bag;
	}
	
	@Override
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector(width/2, height/2));
		
		createGearDisplay(width, height, anchor).draw(canvas);
		for (ImageGraphics h : createHeartDisplay(width, height, anchor)) {
			h.draw(canvas);
		}
		
		if (equippedItem != null) {
			createItemDisplay(width, height, anchor).draw(canvas);
		}
		
		createCoinsDisplay(width,height,anchor).draw(canvas);
		
		for (ImageGraphics m : createMoneyDisplay(width, height, anchor)) {
			m.draw(canvas);
		}

	}
	
	/**
	 * creates a gearDisplay according to canvas information
	 * @param width (float) canvas width
	 * @param height (float) canvas height
	 * @param anchor (Vector) canvas anchor
	 * @return gearDisplay (ImageGraphics) Graphics for gearDisplay
	 */
	private ImageGraphics createGearDisplay(float width, float height, Vector anchor) {
		ImageGraphics gearDisplay = null;
		
		switch (currentBagIndex) {
		case 0: 
			gearDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"),
					1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32),
					anchor.add(new Vector(0, height - 1.75f)), 1, FOREGROUND_DEPTH + 1);
			break;
		case 1: 
			gearDisplay = new ImageGraphics(ResourcePath.getSprite("orangeDisplay"),
					1.5f, 1.5f, new RegionOfInterest(0, 0, 256, 256),
					anchor.add(new Vector(0, height - 1.75f)), 1, FOREGROUND_DEPTH + 1);
			break;
		case 2: 
			gearDisplay = new ImageGraphics(ResourcePath.getSprite("greenDisplay"),
					1.5f, 1.5f, new RegionOfInterest(0, 0, 278, 278),
					anchor.add(new Vector(0, height - 1.75f)), 1, FOREGROUND_DEPTH + 1);
			break;
		case 3: 
			gearDisplay = new ImageGraphics(ResourcePath.getSprite("light-blue-circle"),
					1.5f, 1.5f, new RegionOfInterest(0, 0, 120, 120),
					anchor.add(new Vector(0, height - 1.75f)), 1, FOREGROUND_DEPTH + 1);
			break;
		case 4: 
			gearDisplay = new ImageGraphics(ResourcePath.getSprite("violetDisplay"),
					1.5f, 1.5f, new RegionOfInterest(0, 0, 300, 300),
					anchor.add(new Vector(0, height - 1.75f)), 1, FOREGROUND_DEPTH + 1);
			break;
		default: 
			gearDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"),
					1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32),
					anchor.add(new Vector(0, height - 1.75f)), 1, FOREGROUND_DEPTH + 1);
			break;
			
		}
		
		return gearDisplay;
	}
	
	
	/**
	 * creates a heartDisplay according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return heartDisplay (ImageGraphics[])
	 */
	private ImageGraphics[] createHeartDisplay(float width, float height, Vector anchor) {
		ImageGraphics[] heartDisplay = new ImageGraphics[(int) Math.ceil(playerMaxHp)];
		
		// fills with filled hearts
		for (int i = 0; i < ((int)Math.floor(playerHp)); ++i) {
			heartDisplay[i] = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"),
					1.5f, 1.5f, new RegionOfInterest(32, 0, 16, 16),
					anchor.add(new Vector(1.5f + i*1.5f, height - 1.75f)), 1, FOREGROUND_DEPTH + 2);
		}
		
		for (int i = ((int)Math.floor(playerHp)); i < heartDisplay.length; ++i) {
			// fills with half-filled hearts
			if (i < playerHp) {
				heartDisplay[i] = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"),
						1.5f, 1.5f, new RegionOfInterest(16, 0, 16, 16),
						anchor.add(new Vector(1.5f + i*1.5f, height - 1.75f)), 1, FOREGROUND_DEPTH + 2);
			}
			// fills with empty hearts
			else {
				heartDisplay[i] = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"),
						1.5f, 1.5f, new RegionOfInterest(0, 0, 16, 16),
						anchor.add(new Vector(1.5f + i*1.5f, height - 1.75f)), 1, FOREGROUND_DEPTH + 2);
			}
		}
		return heartDisplay;
	}
	
	/**
	 * creates a itemDisplay according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return itemDisplay (ImageGraphics)
	 */
	private ImageGraphics createItemDisplay(float width, float height, Vector anchor) {
		RegionOfInterest roi;
		switch (equippedItem) {
		case FOOD:
			roi = new RegionOfInterest(0, 0, 1600, 1600);
			break;
		case MONSTER_PART:
			roi = new RegionOfInterest(0, 0, 100, 100);
			break;
		case POTION:
			roi = new RegionOfInterest(0, 0, 256, 256);
			break;
		case WOOD:
			roi = new RegionOfInterest(0, 0, 256, 256);
			break;
		case APE_FLOWER:
			roi = new RegionOfInterest(0, 0, 1024, 1024);
			break;
		case MAX_POTION:
			roi = new RegionOfInterest(0, 0, 256, 256);
			break;
		default:
			roi = new RegionOfInterest(0, 0, 16, 16);
			break;
		}
		return new
				ImageGraphics(ResourcePath.getSprite(equippedItem.getSpriteName()),
						1f, 1f, roi,
						anchor.add(new Vector(0.25f, height - 1.5f)), 1f, FOREGROUND_DEPTH + 2);
	}
	
	/**
	 * creates a itemDisplay according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return itemDisplay (ImageGraphics)
	 */
	private ImageGraphics createCoinsDisplay(float width, float height, Vector anchor) {
		return new
				ImageGraphics(ResourcePath.getSprite("zelda/coinsDisplay"),
						3.5f, 1.75f, new RegionOfInterest(0, 0, 64, 32),
						anchor.add(new Vector(0, 0.5f)), 1, FOREGROUND_DEPTH + 1);
	}
	
	/**
	 * creates a itemDisplay according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return itemDisplay (ImageGraphics)
	 */
	private ImageGraphics[] createMoneyDisplay(float width, float height, Vector anchor) {
		ImageGraphics[] moneyDisplay = new ImageGraphics[(playerMoney + "").length()];
		
		int moneyToPrint = playerMoney;
		for (int i = moneyDisplay.length - 1; i >= 0; --i) {
			RegionOfInterest roi = null;
			switch (moneyToPrint % 10) {
			case 1:
				roi = new RegionOfInterest(0, 0, 16, 16);
				break;
			case 2:
				roi = new RegionOfInterest(16, 0, 16, 16);
				break;
			case 3:
				roi = new RegionOfInterest(32, 0, 16, 16);
				break;
			case 4:
				roi = new RegionOfInterest(48, 0, 16, 16);
				break;
			case 5:
				roi = new RegionOfInterest(0, 16, 16, 16);
				break;
			case 6:
				roi = new RegionOfInterest(16, 16, 16, 16);
				break;
			case 7:
				roi = new RegionOfInterest(32, 16, 16, 16);
				break;
			case 8:
				roi = new RegionOfInterest(48, 16, 16, 16);
				break;
			case 9:
				roi = new RegionOfInterest(0, 32, 16, 16);
				break;
			case 0:
				roi = new RegionOfInterest(16, 32, 16, 16);
				break;
			}
			moneyDisplay[i] = new ImageGraphics(ResourcePath.getSprite("zelda/digits"),
							.65f, .65f, roi,
							anchor.add(new Vector(1.25f + i * 0.5f, 1.1f)), 1, FOREGROUND_DEPTH + 2);
			moneyToPrint /= 10;
		}
		
		return moneyDisplay;
	}
	

}
