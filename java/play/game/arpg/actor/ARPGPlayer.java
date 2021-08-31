/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.actor.Monster.MonsterVulnerabilities;
import play.game.arpg.actor.Seller.ARPGSellerState;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.CollectableAreaEntity;
import play.game.rpg.actor.Door;
import play.game.rpg.actor.InventoryItem;
import play.game.rpg.actor.Player;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.Vector;
import play.window.Button;
import play.window.Canvas;
import play.window.Keyboard;

/**
 * ARPGPlayer is a Specific ARPG actor
 */
public class ARPGPlayer extends Player implements ARPGInventory.Holder {

	private float hp;
	private final static float MAX_HP = 5.f;
	// StatusGUI
	private ARPGPlayerStatusGUI status;

	private final Sprite[][] IDLE_SPRITES;
	private final Sprite[][] BOW_SPRITES;
	private final Sprite[][] STAFF_SPRITES;
	private final Sprite[][] SWORD_SPRITES;
	private final Sprite[][] DARK_SPRITES;

	private final Animation[] IDLE_ANIMATION;
	private final Animation[] BOW_ANIMATION;
	private final Animation[] STAFF_ANIMATION;
	private final Animation[] SWORD_ANIMATION;
	private final Animation[] DARK_STATE_ANIMATIONS;
	private int currentAnimationIndex;

	// Inventory
	private ARPGInventory inventory;
	private ARPGInventoryGUI inventoryGUI;
	// current Item
	private ARPGItem equippedItem;
	private int currentItemIndex;
	private int currentBagIndex;

	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 4;

	// Interaction handler
	private final ARPGPlayerHandler HANDLER;

	private final float INVIS_EFFECT = 2f;
	private final float STATE_TIMER = 0.01f;
	private float remainingEffectTimer = INVIS_EFFECT;
	private float remainingStateTimer = STATE_TIMER;

	private boolean tookDamage;
	private boolean isVisible;
	private ARPGPlayerStates currentState;

	protected enum ARPGPlayerStates {
		IDLE, USING_SWORD, USING_BOW, USING_STAFF, DARK, OPENING_INVENTORY,
		SHOPPING;
	}

	/**
	 * Constructor for ARPGPlayer
	 * 
	 * @param area        (Area) Owner Area, not null
	 * @param orientation (Orientation) Initial player orientation, not null
	 * @param coordinates (DiscretesCoordinates) Initial position, not null
	 */
	public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		hp = MAX_HP;

		IDLE_SPRITES = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32,
				new Orientation[] { Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT });
		BOW_SPRITES = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f),
				new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT });
		STAFF_SPRITES = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32,
				new Vector(-0.5f, 0f),
				new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT });
		SWORD_SPRITES = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f),
				new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT });
		DARK_SPRITES = RPGSprite.extractSprites("skull_mode", 3, 1, 1, this, 32, 32,
				new Orientation[] { Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT, Orientation.UP });

		IDLE_ANIMATION = RPGSprite.createAnimations(ANIMATION_DURATION / 2, IDLE_SPRITES);
		BOW_ANIMATION = RPGSprite.createAnimations(ANIMATION_DURATION / 4, BOW_SPRITES, false);
		STAFF_ANIMATION = RPGSprite.createAnimations(ANIMATION_DURATION / 4, STAFF_SPRITES, false);
		SWORD_ANIMATION = RPGSprite.createAnimations(ANIMATION_DURATION / 2, SWORD_SPRITES, true);
		DARK_STATE_ANIMATIONS = RPGSprite.createAnimations(ANIMATION_DURATION / 2, DARK_SPRITES);

		currentAnimationIndex = Orientation.DOWN.ordinal();

		HANDLER = new ARPGPlayerHandler();

		resetMotion();

		// Hard coded items in inventory as suggested
		inventory = new ARPGInventory(50.f, 300);
		inventory.addItem(ARPGItem.BOMB, 10);
		inventory.addItem(ARPGItem.POTION, 5);
		inventory.addItem(ARPGItem.SWORD, 1);
		inventory.addItem(ARPGItem.MONSTER_PART, 10);
//		inventory.addItem(ARPGItem.BOW, 1);
//		inventory.addItem(ARPGItem.ARROW, 50);
		currentItemIndex = 0;
		currentBagIndex = 0;

		if (!getAvailableItems().isEmpty()) {
			equippedItem = getAvailableItems().get(currentItemIndex);
		} else {
			System.out.println("inventory is empty");
			equippedItem = null;
		}

		status = new ARPGPlayerStatusGUI(hp, MAX_HP, equippedItem, inventory.getMoney(), currentBagIndex);
		inventoryGUI = new ARPGInventoryGUI(getAvailableItems(),equippedItem, "ALL ITEMS");
		
		tookDamage = false;
		isVisible = true;

		currentState = ARPGPlayerStates.IDLE;
	}
	
	/**
	 * Getter for current state 
	 * @return state (ARPGPlayerStates) current state.
	 */
	protected ARPGPlayerStates getCurrentState() {
		return currentState;
	}

	@Override
	public int getCurrentBagIndex() {
		return currentBagIndex;
	}

	/**
	 * Switches the current bag
	 */
	public void switchBag() {
		do {
			currentBagIndex = (currentBagIndex + 1) % inventory.getNumberOfBags();
		} while (getAvailableItems().isEmpty());
	}
	
	

	/**
	 * Switches the current equipped item
	 */
	private void switchEquippedItem() {
		List<ARPGItem> items = getAvailableItems();
		if (!getAvailableItems().isEmpty()) {
			currentItemIndex = (currentItemIndex + 1) % items.size();
			equippedItem = items.get(currentItemIndex);
		}
	}

	/**
	 * Uses the current equipped item
	 */
	private void useEquippedItem() {
		switch (equippedItem) {
		case BOMB:
			if (useBomb()) {
				inventory.removeItem(ARPGItem.BOMB);
			}
			break;
		case POTION:
			if (usePotion()) {
				inventory.removeItem(ARPGItem.POTION);
			}
			break;
		case MAX_POTION:
			if (useMaxPotion()) {
				inventory.removeItem(ARPGItem.MAX_POTION);
			}
			break;
		case BOW:
			if (currentState == ARPGPlayerStates.IDLE && !isDisplacementOccurs())
				currentState = ARPGPlayerStates.USING_BOW;
			break;
		case STAFF:
			if (currentState == ARPGPlayerStates.IDLE && !isDisplacementOccurs())
				currentState = ARPGPlayerStates.USING_STAFF;
			break;
		case SWORD:
			if (currentState == ARPGPlayerStates.IDLE && getOwnerArea().getKeyboard().get(Keyboard.SPACE).isDown()
					&& !isDisplacementOccurs())
				currentState = ARPGPlayerStates.USING_SWORD;
			break;
		case APE_FLOWER:
			if (currentState == ARPGPlayerStates.IDLE) {
				currentState = ARPGPlayerStates.DARK;
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * resets the player's damage state
	 */
	private void resetDamage() {
		tookDamage = false;
	}

	/**
	 * Use a bomb : drop it in the cell in front of the player
	 * 
	 * @return success of Using a Bomb (boolean).
	 */
	private boolean useBomb() {
		if (getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells())) {

			// Because getFieldOfViewCells() is a singleton List, we can use get(0) without
			// "0" being "hard-coded"
			getOwnerArea().registerActor(
					new Bomb(this.getOwnerArea(), this.getOrientation(), this.getFieldOfViewCells().get(0)));
			return true;
		}
		return false;

	}

	/**
	 * Use a potion : recover some hp
	 * 
	 * @return success of Using a potion (boolean).
	 */
	private boolean usePotion() {
		if (hp < MAX_HP) {
			recover(1);
			return true;
		}
		return false;

	}

	/**
	 * Use a max potion : recover all his hp
	 * 
	 * @return success of Using a max_potion (boolean).
	 */
	private boolean useMaxPotion() {
		if (hp < MAX_HP) {
			hp = MAX_HP;
			return true;
		}
		return false;

	}

	/**
	 * Decreases player's health points by damage if the damage is positive
	 * 
	 * @param damage (float): hp to lose.
	 */
	public void loseHp(float damage) {
		if (!tookDamage && damage >= 0 && currentState != ARPGPlayerStates.DARK) {
			this.hp -= damage;
			if (this.hp < 0) {
				this.hp = 0.f;
			}
			tookDamage = true;
		}
	}

	/**
	 * Increases player's hp by health
	 * 
	 * @param health (float): hp to recover.
	 */
	private boolean recover(float health) {
		if (this.hp < MAX_HP && health >= 0) {
			this.hp += health;
			if (this.hp > MAX_HP) {
				this.hp = MAX_HP;
			}
			return true;
		}
		return false;
	}

	/**
	 * makes the player blink after taking damage for a set duration of time
	 */
	private void blinkingEffect(float deltaTime) {
		if (tookDamage) {
			remainingEffectTimer -= deltaTime;
			remainingStateTimer -= deltaTime;
			if (remainingEffectTimer > 0) {
				if (remainingStateTimer < 0) {
					isVisible = !isVisible;
					remainingStateTimer = STATE_TIMER;
				}
			} else {
				remainingEffectTimer = INVIS_EFFECT;
				remainingStateTimer = STATE_TIMER;
				isVisible = true;
				resetDamage();
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		Keyboard keyboard = getOwnerArea().getKeyboard();
		if (hp > 0) {

			switch (currentState) {
			case IDLE:
				if (isDisplacementOccurs()) {
					IDLE_ANIMATION[currentAnimationIndex].update(deltaTime);
				} else {
					IDLE_ANIMATION[currentAnimationIndex].reset();
				}
				break;
			case USING_BOW:
				BOW_ANIMATION[currentAnimationIndex].update(deltaTime);
				if (BOW_ANIMATION[currentAnimationIndex].isCompleted()) {
					if (possess(ARPGItem.ARROW)) {
						getOwnerArea().registerActor(new Arrow(getOwnerArea(), getOrientation(),
								getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 2, 5));
						inventory.removeItem(ARPGItem.ARROW);
					}
					currentState = ARPGPlayerStates.IDLE;
					BOW_ANIMATION[currentAnimationIndex].reset();
				}
				break;
			case USING_STAFF:
				STAFF_ANIMATION[currentAnimationIndex].update(deltaTime);
				if (STAFF_ANIMATION[currentAnimationIndex].isCompleted()) {
					getOwnerArea().registerActor(new MagicWaterProjectile(getOwnerArea(), getOrientation(),
							getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 4, 10));
					currentState = ARPGPlayerStates.IDLE;
					STAFF_ANIMATION[currentAnimationIndex].reset();
				}
				break;
			case USING_SWORD:
				SWORD_ANIMATION[currentAnimationIndex].update(deltaTime);
				if (getOwnerArea().getKeyboard().get(Keyboard.SPACE).isUp()) {
					currentState = ARPGPlayerStates.IDLE;
					SWORD_ANIMATION[currentAnimationIndex].reset();
				}
				break;
			case SHOPPING:
				Button ENTER = keyboard.get(Keyboard.ENTER);
				if (ENTER.isPressed()) {
					currentState = ARPGPlayerStates.IDLE;
				}
				break;
			case DARK:
				if (isDisplacementOccurs())
					DARK_STATE_ANIMATIONS[currentAnimationIndex].update(deltaTime);
				else
					DARK_STATE_ANIMATIONS[currentAnimationIndex].reset();
				if(getOwnerArea().getKeyboard().get(Keyboard.D).isPressed())
					currentState = ARPGPlayerStates.IDLE;
				break;
			default:
				break;
			}
			blinkingEffect(deltaTime);
			
			Button tab = keyboard.get(Keyboard.TAB);
			Button space = keyboard.get(Keyboard.SPACE);
			Button U = keyboard.get(Keyboard.U);
			Button I = keyboard.get(Keyboard.I);
			
			if (!(currentState == ARPGPlayerStates.SHOPPING || currentState == ARPGPlayerStates.OPENING_INVENTORY)) {
				moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
				moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
				moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
				moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
				
				if (space.isPressed()) {
					if (!getAvailableItems().isEmpty()) {
						useEquippedItem();
					}
				}
			} 
			
			if (!(currentState == ARPGPlayerStates.OPENING_INVENTORY)) {
				if (I.isPressed() && !(currentState == ARPGPlayerStates.SHOPPING)) {
					currentState = ARPGPlayerStates.OPENING_INVENTORY;
				}
			} else {
				if (I.isPressed()) {
					currentState = ARPGPlayerStates.IDLE;
				}
				String bagName;
				switch (currentBagIndex) {

				case 1:
					bagName = "WEAPONS";
					break;
				case 2:
					bagName = "UTILITIES";
					break;
				case 3:
					bagName = "RESOURCES";
					break;
				case 4:
					bagName = "RARE ITEMS";
					break;
				case 0 :
					bagName = "ALL ITEMS";
					break;
				default:
					bagName = "ALL ITEMS";
					break;
				}
				inventoryGUI = new ARPGInventoryGUI(getAvailableItems(), equippedItem, bagName);
			}

			if (tab.isPressed()) {
				switchEquippedItem();
			}

			if (U.isPressed()) {
				switchBag();
			}


			if (!(currentItemIndex < getAvailableItems().size())) {
				currentItemIndex = 0;
			}
			if (!getAvailableItems().isEmpty()) {
				equippedItem = getAvailableItems().get(currentItemIndex);
				;
			} else {
				equippedItem = null;
			}

			super.update(deltaTime);
		} else {
			Button R = keyboard.get(Keyboard.R);
			if (R.isPressed()) {
				recover(MAX_HP);
			}
		}

		Button M = keyboard.get(Keyboard.M);
		int moneyToPrint = inventory.getMoney();
		if (M.isDown()) {
			moneyToPrint = inventory.getFortune();
		}

		status = new ARPGPlayerStatusGUI(hp, MAX_HP, equippedItem, moneyToPrint, currentBagIndex);
		// System.out.println(getCurrentMainCellCoordinates());

	}

	/**
	 * Orientate or Move this player in the given orientation if the given button is
	 * down
	 * 
	 * @param orientation (Orientation): given orientation, not null
	 * @param b           (Button): button corresponding to the given orientation,
	 *                    not null
	 */
	private void moveOrientate(Orientation orientation, Button b) {

		if (b.isDown() && (currentState == ARPGPlayerStates.IDLE || currentState == ARPGPlayerStates.DARK)) {
			if (getOrientation() == orientation)
				move(ANIMATION_DURATION);
			else
				orientate(orientation);
		}
		currentAnimationIndex = getOrientation().ordinal();
	}

	@Override
	public void draw(Canvas canvas) {
		if (isVisible) {
			switch (currentState) {
			case IDLE:
				IDLE_ANIMATION[currentAnimationIndex].draw(canvas);
				break;
			case USING_BOW:
				BOW_ANIMATION[currentAnimationIndex].draw(canvas);
				break;
			case USING_STAFF:
				STAFF_ANIMATION[currentAnimationIndex].draw(canvas);
				break;
			case USING_SWORD:
				SWORD_ANIMATION[currentAnimationIndex].draw(canvas);
				break;
			case DARK:
				DARK_STATE_ANIMATIONS[currentAnimationIndex].draw(canvas);
				break;
			case OPENING_INVENTORY:
				inventoryGUI.draw(canvas);
				break;
			default:
				break;
			}
		}
		status.draw(canvas);
	}

	/// ARPGPlayer implements Interactor

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	/// ARPG implements Interactable

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		Keyboard keyboard = getOwnerArea().getKeyboard();
		Button E = keyboard.get(Keyboard.E);
		return E.isPressed() || currentState == ARPGPlayerStates.USING_SWORD;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public boolean possess(InventoryItem item) {
		return inventory.contains(item);
	}

	/**
	 * Adds qty of item to player's inventory
	 * 
	 * @param item (ARPGItem) Item to add.
	 * @param qty  (int) qty to add.
	 * @return success (boolean).
	 */
	private boolean addItem(ARPGItem item, int qty) {
		return inventory.addItem(item, qty);
	}

	/**
	 * Adds an item to player's inventory
	 * 
	 * @param item (ARPGItem) Item to add.
	 * @return success (boolean).
	 */
	private boolean addItem(ARPGItem item) {
		return addItem(item, 1);
	}

	private class ARPGPlayerHandler implements ARPGInteractionVisitor {

		@Override
		public void interactWith(Door door) {
			ARPGPlayer.this.setIsPassingADoor(door);
		}

		@Override
		public void interactWith(CastleDoor door) {
			if (door.isOpen()) {
				interactWith((Door) door);
				door.close();
			} else if (ARPGPlayer.this.possess(ARPGItem.CASTLE_KEY)) {
				door.open();
			}
		}

		@Override
		public void interactWith(Monster monster) {
			if (currentState == ARPGPlayerStates.USING_SWORD) {
				monster.loseHP(2, MonsterVulnerabilities.PHYSICAL);

			}
		}

		@Override
		public void interactWith(Grass grass) {
			if (currentState == ARPGPlayerStates.USING_SWORD) {
				grass.cut();
			}
		}

		@Override
		public void interactWith(CollectableAreaEntity collectable) {
			collectable.collect();
		}

		@Override
		public void interactWith(Coin coin) {
			interactWith((CollectableAreaEntity) coin);
			ARPGPlayer.this.inventory.addMoney(coin.getValue());
		}

		@Override
		public void interactWith(Heart heart) {
			interactWith((CollectableAreaEntity) heart);
			ARPGPlayer.this.recover(heart.getRecovery());
		}

		@Override
		public void interactWith(CastleKey key) {
			if (ARPGPlayer.this.addItem(ARPGItem.CASTLE_KEY)) {
				interactWith((CollectableAreaEntity) key);
			}
		}

		@Override
		public void interactWith(Seller seller) {
			if (seller.getState() == ARPGSellerState.IDLE && currentState != ARPGPlayerStates.SHOPPING) {
				seller.setState(ARPGSellerState.OPENING_INVENTORY);
				currentState = ARPGPlayerStates.SHOPPING;
			} else if (seller.getState() == ARPGSellerState.OPENING_INVENTORY) {
				if (seller.sell(seller.getSelectedItem(), inventory)) {
					inventory.addMoney(- seller.getSelectedItem().getPrice());
				};
				seller.setState(ARPGSellerState.IDLE);
				currentState = ARPGPlayerStates.IDLE;
			} else {
				currentState = ARPGPlayerStates.IDLE;
			}
		}
		
		@Override
		public void interactWith(Bomb bomb) {
			if (currentState == ARPGPlayerStates.USING_SWORD) {
				bomb.explode();
			}
		}

		@Override
		public void interactWith(Portal portal) {
			if (portal.isOpen() ) {
				interactWith((Door) portal);
				portal.desactivate();
			} else if (currentState == ARPGPlayerStates.DARK) { // add if in dark state
				portal.activate();
			}
		}

		@Override
		public void interactWith(Chest chest) {
			if (!chest.isOpen()) {
				chest.open();
				addItem(chest.getContent());
			}
		}

		@Override
		public void interactWith(Staff staff) {
			if (ARPGPlayer.this.inventory.addItem(ARPGItem.STAFF, 1)) {
				interactWith((CollectableAreaEntity) staff);
			}
		}

	}

}
