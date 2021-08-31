/*
 *	Author:      Omar El Malki
 *	Date:        25 Nov 2019
 */

package play.game.rpg.actor;

/**
 * Represent InventoryItem object (i.e. can be contained in an Inventory)
 */
public interface InventoryItem {

    /**
     * Get this InventoryItem's name
     * @return name (String) Item's name.
     */
    String getName();

    /**
     * Get this InventoryItem's weight
     * @return weight (float) Item's weight
     */
    float getWeight();
    
    /**
     * Get this InventoryItem's price
     * @return price (int) Item's price/cost.
     */
    int getPrice();

    

}
