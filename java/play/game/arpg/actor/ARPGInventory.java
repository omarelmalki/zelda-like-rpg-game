/*
 *	Author:      Omar El Malki
 *	Date:        25 Nov 2019
 */

package play.game.arpg.actor;

import java.util.LinkedList;
import java.util.List;

import play.game.rpg.actor.Inventory;
import play.game.rpg.actor.InventoryItem;

public class ARPGInventory extends Inventory {
	
	private int money;
	private final int MAX_MONEY = 9999;
	
	// Different bags made available to all ARPGActors and sub classes
	private final static String[] BAGS = {"all", "weapons", "utilities", "resources", "rare items"};

	
    /**
     * ARPGInventory constructor
     * @param maxWeight (float) Maximum weight it can contain.
     * @param initialMoney (int) Initial money it contains. 
     */
	protected ARPGInventory(float maxWeight, int initialMoney) {
		super(50.f);
		money = initialMoney;
	}
	
	/**
	 * Returns the number of bags available
	 * @return number (int). Number of bags in BAGS.
	 */
	protected int getNumberOfBags() {
		return BAGS.length;
	}
    /**
     * Calculates the fortune of the owner of the inventory
     * @return fortune (int) Total value of items + money.
     */
	public int getFortune() {
		return money + getValue();
	}
	
    /**
     * Returns money of the Inventory
     * @return money (int) ARPGInventory's money.
     */
	public int getMoney() {
		return money;
	}
	
	
    /**
     * Adds money to the Inventory
     * @param money (int) Money to add.
     * @return success (boolean).
     */
	protected boolean addMoney(int money) {
		if (this.money < MAX_MONEY) {
			this.money += money;
			if (this.money > MAX_MONEY) {
				this.money = MAX_MONEY;
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean addItem(InventoryItem item, int qty) {
		return super.addItem(item, qty);
	}
	
	@Override
	protected boolean removeItem(InventoryItem item) {
		return super.removeItem(item);
	}
	
	/**
	 * Transfers qty items from this inventory to the another
	 * @param item (InventoryItem) Item to transfer.
	 * @param qty (int) Quantity to transfer.
	 * @param other Inventory (ARPGInventory) Destination inventory.
	 * @return success of removing (boolean).
	 */
	protected boolean transferItem(ARPGItem item, int qty, ARPGInventory other) {
		return this.removeItem(item, qty) && other.addItem(item, qty);
	}
	
	/**
	 * Transfers an item from this inventory to the another
	 * @param item (InventoryItem) Item to transfer.
	 * @param other Inventory (ARPGInventory) Destination inventory.
	 * @return success of removing (boolean).
	 */
	protected boolean transferItem(ARPGItem item, ARPGInventory other) {
		return transferItem(item, 1, other);
	}
	
	public interface Holder extends Inventory.Holder{
		
		/**
		 * @return currentBagIndex (int).
		 */
		int getCurrentBagIndex();

		/**
		 * Gets all available items in the inventory according to the currentBag 
		 * (by default the first bag contains all items)
		 * @return availableItems (List<ARPGItem>) All available items according to the currentBagIndex.
		 */
		default List<ARPGItem> getAvailableItems(){
			List<ARPGItem> availableItems = new LinkedList<ARPGItem>();
			for (ARPGItem item : ARPGItem.values()) {
				if (getCurrentBagIndex() == 0 || item.getBag().equals(ARPGInventory.BAGS[getCurrentBagIndex()])) {
					if (this.possess(item)) {
						availableItems.add(item);
					}
				}
			}
			return availableItems;
		}
		

	}
}
