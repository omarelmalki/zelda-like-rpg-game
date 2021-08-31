/*
 *	Author:      Omar El Malki
 *	Date:        25 Nov 2019
 */

package play.game.arpg.actor;

import play.game.rpg.actor.InventoryItem;

public enum ARPGItem implements InventoryItem {
	
	/// Weapons
	ARROW("Arrow", 5, "zelda/arrow.icon", "weapons"),
	SWORD("Sword", 10, "zelda/sword.icon", "weapons"),
	STAFF("Staff", 10, "zelda/staff_water.icon", "weapons"),
	BOW("Bow", 10, "zelda/bow.icon", "weapons"),
	BOMB("Bomb", 15, "zelda/bomb", "weapons"),
	
	//Utilities
	POTION("Potion", 10, "potion.icon", "utilities"),
	MAX_POTION("Max Potion", 20, "max_potion.icon", "utilities"),
	FOOD("Food", 10, "razzberry.icon", "utilities"),
	APE_FLOWER("Ape Flower", 10, "flower.icon", "utilities"),
	
	
	// Resources
	MONSTER_PART("Monster Part", 5, "monster_part.icon", "resources"),
	WOOD("Wood", 10, "wood.icon", "resources"),
	
	//Rare Items
	CASTLE_KEY("Castle key", 100, "zelda/key", "rare items");

	
	private final String NAME;
	private final float WEIGHT;
	private float defaultWeight = 0.f;
	private final int PRICE;
	private final String SPRITE_NAME;
	private final String BAG;
	
    /**
     * Default ARPGItem constructor
     * @param direction (Vector). Not null
     */
    ARPGItem(String name, int price, String spriteName, String bagName){
        this.NAME = name;
        this.WEIGHT = defaultWeight;
        this.PRICE = price;
        this.SPRITE_NAME = spriteName;
        this.BAG = bagName;
    } 

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public float getWeight() {
		return WEIGHT;
	}

	@Override
	public int getPrice() {
		return PRICE;
	}
	
	/**
	 * Gets the sprite name associated with the ARPGItem
	 * @return spriteName (String) Sprite's name.
	 */
	public String getSpriteName() {
		return SPRITE_NAME;
	}
	
	/**
	 * Gets the bag's name in which is classified the ARPGitem
	 * @return bag (String) Item's bag/type.
	 */
	public String getBag() {
		return BAG;
	}

}
