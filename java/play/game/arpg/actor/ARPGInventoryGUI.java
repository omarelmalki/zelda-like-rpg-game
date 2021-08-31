package play.game.arpg.actor;

import java.awt.Color;
import java.util.List;

/*
 *	Author:      Omar El Malki
 *	Date:        13 Dec 2019
 */

import play.game.actor.Graphics;
import play.game.actor.ImageGraphics;
import play.game.actor.TextGraphics;
import play.game.areagame.io.ResourcePath;
import play.math.RegionOfInterest;
import play.math.TextAlign;
import play.math.Vector;
import play.window.Canvas;


class ARPGInventoryGUI implements Graphics {

	private List<ARPGItem> availableItems;
	private ARPGItem selectedItem;
	private String name;
	
	// depth of the ARPG Player Status Gui (because it is supposed to be over it)
	private static final float ARPGPLAYER_GUI_DEPTH = 1002f;
	
	/**
	 * Constructor for ARPGInventoryGUI
	 * @param items (List<ARPGItem>).
	 * @param selectedItem (ARPGItem).
	 * @param name (String).
	 */
	protected ARPGInventoryGUI(List<ARPGItem> items, ARPGItem selectedItem, String name) {
		if (items.indexOf(selectedItem) > 7) {
			items.remove(items.indexOf(selectedItem));
			items.add(0, selectedItem);
		}
		if (items.size() > 8) {
			items = items.subList(0, 8);
		}
		this.availableItems = items;
		this.selectedItem = selectedItem;
		this.name = name;
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new
		Vector(width/2, height/2));
		
		ImageGraphics background = createBackgroundDisplay(width, height, anchor);
		background.draw(canvas);
		TextGraphics nameDisplay = createNameDisplay(width, height, anchor);
		nameDisplay.draw(canvas);
		
		
		for (ImageGraphics s : createItemSlots(width, height, anchor)) {
			s.draw(canvas);
		}
		for (ImageGraphics i : createItemsDisplay(width, height, anchor)) {
			i.draw(canvas);
		}
		
		if (!availableItems.isEmpty()) {
			createSelector(width, height, anchor).draw(canvas);
			
		}

	}
	
	/**
	 * creates background according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return background (ImageGraphics)
	 */
	private ImageGraphics createBackgroundDisplay(float width, float height, Vector anchor) {
		
		return new ImageGraphics(ResourcePath.getSprite("zelda/inventory.background"),
				13f, 10f, new RegionOfInterest(0, 0, 240, 240),
				anchor.add(new Vector(0f, height - 10.2f)), 1, ARPGPLAYER_GUI_DEPTH + 1);
	}
	
	/**
	 * creates name of inventory according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return name (TextGraphics)
	 */
	private TextGraphics createNameDisplay(float width, float height, Vector anchor) {
		
		return new TextGraphics(name, 0.5f, Color.BLACK, Color.BLACK, 0.0f, false, false, anchor.add(new Vector(4.5f, height - 1.6f)), TextAlign.Horizontal.LEFT, TextAlign.Vertical.BOTTOM, 1.0f, ARPGPLAYER_GUI_DEPTH + 2);
	}
	
	
	/**
	 * creates item slots according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return slots (ImageGraphics[])
	 */
	private ImageGraphics[] createItemSlots(float width, float height, Vector anchor) {
		ImageGraphics[] slots = new ImageGraphics[8];
		
		
		for (int i = 0; i < 4; ++i) {
			slots[i] = new ImageGraphics(ResourcePath.getSprite("zelda/inventory.slot"),
					2.5f, 2.5f, new RegionOfInterest(0, 0, 64, 64),
					anchor.add(new Vector(0.8f + i*3f, height - 5f)), 1, ARPGPLAYER_GUI_DEPTH + 2);
		}
		
		for (int i = 4; i < slots.length; ++i) {
				slots[i] = new ImageGraphics(ResourcePath.getSprite("zelda/inventory.slot"),
						2.5f, 2.5f, new RegionOfInterest(0, 0, 64, 64),
						anchor.add(new Vector(0.8f + (i-4)*3f, height - 8f)), 1, ARPGPLAYER_GUI_DEPTH + 2);
		}
		return slots;
	}
	
	/**
	 * creates a itemDisplay according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return itemDisplay (ImageGraphics)
	 */
	private ImageGraphics[] createItemsDisplay(float width, float height, Vector anchor) {
		ImageGraphics[] items = new ImageGraphics[availableItems.size()];
		for (ARPGItem i : availableItems) {
			RegionOfInterest roi = null;
			switch (i) {
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
			if (availableItems.indexOf(i) < 4) {
				items[availableItems.indexOf(i)] = new
						ImageGraphics(ResourcePath.getSprite(i.getSpriteName()),
								1.5f, 1.5f, roi,
								anchor.add(new Vector(1.2f + availableItems.indexOf(i)*3f, height - 4.5f)), 1f, ARPGPLAYER_GUI_DEPTH + 3);
			} else {
				items[availableItems.indexOf(i)] = new
						ImageGraphics(ResourcePath.getSprite(i.getSpriteName()),
								1.5f, 1.5f, roi,
								anchor.add(new Vector(1.2f + (availableItems.indexOf(i)-4)*3f, height - 7.5f)), 1f, ARPGPLAYER_GUI_DEPTH + 3);
			}
		}
		return items;

	}
	
	/**
	 * creates a selector on the selected item according to canvas information
	 * @param width (float)
	 * @param height (float)
	 * @param anchor (Vector)
	 * @return selector (ImageGraphics)
	 */
	private ImageGraphics createSelector(float width, float height, Vector anchor) {
		
		if (availableItems.indexOf(selectedItem) < 4) {
			return new ImageGraphics(ResourcePath.getSprite("zelda/inventory.selector"),
					2.5f, 2.5f, new RegionOfInterest(0, 0, 64, 64),
					anchor.add(new Vector(0.8f + availableItems.indexOf(selectedItem)*3f, height - 5f)), 1, ARPGPLAYER_GUI_DEPTH + 4);
		} else {
			return new ImageGraphics(ResourcePath.getSprite("zelda/inventory.selector"),
					2.5f, 2.5f, new RegionOfInterest(0, 0, 64, 64),
					anchor.add(new Vector(0.8f + (availableItems.indexOf(selectedItem)-4)*3f, height - 8f)), 1, ARPGPLAYER_GUI_DEPTH + 4);
		}
		
	}

}
