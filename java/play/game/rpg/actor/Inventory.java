/*
 *	Author:      Omar El Malki
 *	Date:        25 Nov 2019
 */

package play.game.rpg.actor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Inventory {
	
	private float maxWeight;
	private Map<InventoryItem, Integer> items;
	private float currentWeight;
	
	
	
	/**
	 * Inventory constructor
	 * @param maxWeight Maximum weight that it can contain
	 */
    protected Inventory(float maxWeight){
    	this.maxWeight = maxWeight;
    	currentWeight = 0;
    	items = new HashMap<InventoryItem, Integer>();
    }
	
	/**
	 * Adds qty items of InventoryItem to the Inventory
	 * @param item (InventoryItem) Item to add.
	 * @param qty (int) How many items to add.
	 * @return success of adding (boolean).
	 */
	protected boolean addItem(InventoryItem item, int qty) {
		if (!(currentWeight + item.getWeight()*qty > maxWeight)) {
			if (!contains(item)) {
				items.put(item, qty);
				return true;
			} else {
				items.replace(item, items.get(item) + qty);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds one item of InventoryItem to the Inventory
	 * @param item (InventoryItem) Item to add.
	 * @return success of adding (boolean).
	 */
	protected boolean addItem(InventoryItem item) {
		return addItem(item, 1);
	}
	
	/**
	 * returns the value of the items in Inventory (i.e. the sum of each item's cost times its quantity
	 * @return value (int) Inventory's total value.
	 */
	protected int getValue() {
		int value = 0;
		for (Entry<InventoryItem,Integer> pair : items.entrySet()) { 
			value += pair.getKey().getPrice() * pair.getValue();
		}
		return value;
	}
	
	/**
	 * Removes qty items of InventoryItem to the Inventory
	 * @param item (InventoryItem) Item to remove.
	 * @param qty (int) Quantity to remove.
	 * @return success of removing (boolean).
	 */
	protected boolean removeItem(InventoryItem item, int qty) {
		if (contains(item) && ((int)items.get(item)) >= qty) {
			if (((int)items.get(item)) == qty){
				items.remove(item, qty);
				return true;
			} else {
				items.replace(item, items.get(item) - qty);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes one item of InventoryItem to the Inventory
	 * @param item (InventoryItem) Item to remove.
	 * @return success of removing (boolean).
	 */
	protected boolean removeItem(InventoryItem item) {
		return removeItem(item, 1);
	}
	
	
	/**
	 * Checks if an item is contained in Inventory
	 * @param item (InventoryItem) Item to check.
	 * @return (boolean) true if the item is contained
	 */
	public boolean contains(InventoryItem item) {
		return items.containsKey(item);
	}
	
	
	public interface Holder {
		
		/**
		 * Checks if Holder possesses an InventoryItem
		 * @param item (InventoryItem) Item to check.
		 * @return possess (boolean).
		 */
		boolean possess(InventoryItem item);
		

	}
	

}





















