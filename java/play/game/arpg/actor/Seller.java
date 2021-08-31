/*
 *	Author:      Omar El Malki
 *	Date:        7 Dec 2019
 */

package play.game.arpg.actor;

import java.util.ArrayList;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.AreaEntity;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.InventoryItem;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.math.Transform;
import play.window.Button;
import play.window.Canvas;
import play.window.Keyboard;

public class Seller extends AreaEntity implements ARPGInventory.Holder{

	// Inventory
	private ARPGInventory inventory;
	private ARPGItem selectedItem;
	private ARPGInventoryGUI inventoryGUI;
	private ARPGSellerState state;
	
	private Sprite sprite;
	
	protected enum ARPGSellerState {
		IDLE,
		OPENING_INVENTORY;
	}
	
    /**
     * Default Seller constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Seller(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		inventory = new ARPGInventory(50.f, 100);
		inventory.addItem(ARPGItem.BOMB, 200);
		inventory.addItem(ARPGItem.ARROW, 100);
		inventory.addItem(ARPGItem.BOW, 3);
		if (!getAvailableItems().isEmpty()) {			
			selectedItem = getAvailableItems().get(0);
		}
		state = ARPGSellerState.IDLE;
		sprite = new RPGSprite("shopAssistant", 2, 64*2/52, this, new RegionOfInterest(0, 0, 52, 64));
	}
	
	@Override
	public int getCurrentBagIndex() {
		return 0;
	}


	@Override
	public boolean possess(InventoryItem item) {
		return inventory.contains(item);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Keyboard keyboard = getOwnerArea().getKeyboard();
		Button N = keyboard.get(Keyboard.N);
		Button ENTER = keyboard.get(Keyboard.ENTER);
		if (N.isPressed()) {
			
			List<ARPGItem> items = getAvailableItems();
			
			if (!items.isEmpty()) {
				
				if (items.contains(selectedItem)) {
					
					int index = (items.indexOf(selectedItem) + 1) % items.size();
					selectedItem = items.get(index);
					
				} else {
					selectedItem = items.get(0);
				}
				
			}
		}
		if (ENTER.isPressed() && state == ARPGSellerState.OPENING_INVENTORY) {
			state = ARPGSellerState.IDLE;
		}
		
		inventoryGUI = new ARPGInventoryGUI(getAvailableItems(), selectedItem, "SHOP");
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (state == ARPGSellerState.IDLE) {
			sprite.draw(canvas);
		} else if (state == ARPGSellerState.OPENING_INVENTORY) {
			inventoryGUI.draw(canvas);
		}

	}
	
	/**
	 * Getter for state
	 * @return state (ARPGSellerState).
	 */
	protected ARPGSellerState getState() {
		return state;
	}
	
	/**
	 * Setter for state
	 * @param s (ARPGSellerState) state.
	 */
	protected void setState(ARPGSellerState s) {
		state = s;
	}
	
	/**
	 * Getter for selected item
	 * @return selectedItem (ARPGItem) selected item.
	 */
	protected ARPGItem getSelectedItem() {
		return selectedItem;
	}
	
	/**
	 * Sells an Item
	 * @param item (ARPGItem) Item to sell.
	 * @param otherInventory (ARPGInventory) destination inventory.
	 * @return success (boolean)
	 */
	public boolean sell(ARPGItem item, ARPGInventory otherInventory) {
		if (possess(item) && otherInventory.getMoney() >= item.getPrice()) {
			inventory.transferItem(item, otherInventory);
			return true;
		}
		return false;
	}
	
	@Override
	public Transform getTransform() {
		Transform transform = null;
        // Compute the transform only when needed
        if (transform == null) {
            float x = getCurrentMainCellCoordinates().x;
            float y = getCurrentMainCellCoordinates().y;
            transform = new Transform(
                    1, 0, x - 0.5f,
                    0, 1, y+1
            );
        }
        return transform;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> currentCells = new ArrayList<DiscreteCoordinates>();
		DiscreteCoordinates main = this.getCurrentMainCellCoordinates();
		currentCells.add(main);
		currentCells.add(new DiscreteCoordinates(main.x, main.y + 1));
		return currentCells;
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}
}
