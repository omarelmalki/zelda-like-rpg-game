/*
 *	Author:      Omar El Malki
 *	Date:        12 Dec 2019
 */

package play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.AreaEntity;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.math.Transform;
import play.signal.logic.Logic;
import play.window.Canvas;

public class Bridge extends AreaEntity {
	
	private final Sprite BRIDGE_SPRITE;
	private Logic isVisible;
	
    /**
     * Default Bridge constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Bridge(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
		super(area, orientation, position);
		BRIDGE_SPRITE = new RPGSprite("zelda/bridge", 4, 3, this, new RegionOfInterest(0, 0, 64, 48));
		isVisible = signal;
	}
	
	@Override
	public Transform getTransform() {
		Transform transform = null;
        // Compute the transform only when needed
        if (transform == null) {
            float x = getCurrentMainCellCoordinates().x;
            float y = getCurrentMainCellCoordinates().y;
            transform = new Transform(
                    1, 0, x-1,
                    0, 1, y-1
            );
        }
        return transform;
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isActivated()) {
		BRIDGE_SPRITE.draw(canvas);
		}
	}
	
	private boolean isActivated() {
		return (isVisible == Logic.TRUE);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		List<DiscreteCoordinates> currentCells = new ArrayList<DiscreteCoordinates>();
		DiscreteCoordinates main = this.getCurrentMainCellCoordinates();
		currentCells.add(main);
		currentCells.add(new DiscreteCoordinates(main.x + 1, main.y));
		return currentCells;
	}

	@Override
	public boolean takeCellSpace() {
		return !isActivated();
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
	}
	
	public class Orb extends AreaEntity{
		
		private final Sprite[] RED_SPRITES;
		private final Animation RED_ANIMATION;
		private final Sprite[] GREEN_SPRITES;
		private final Animation GREEN_ANIMATION;
		
		/// Animation duration in frame number
	    private final static int ANIMATION_DURATION = 6;
		
	    /**
	     * Default Orb constructor
	     * @param area (Area): Owner area. Not null
	     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
	     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
	     */
		public Orb(Area area, Orientation orientation, DiscreteCoordinates position) {
			super(area, orientation, position);
			RED_SPRITES = new Sprite[6];
			GREEN_SPRITES = new Sprite[6];
			for (int i = 0; i<6; ++i) {
				RED_SPRITES[i] = new RPGSprite("zelda/orb", 1, 1, this, new RegionOfInterest(i*32, 64, 32, 32));
				GREEN_SPRITES[i] = new RPGSprite("zelda/orb", 1, 1, this, new RegionOfInterest(i*32, 32, 32, 32));
			}
			RED_ANIMATION = new Animation(ANIMATION_DURATION, RED_SPRITES, true);
			GREEN_ANIMATION = new Animation(ANIMATION_DURATION, GREEN_SPRITES, true);
		}
		
		@Override
		public void update(float deltaTime) {
			RED_ANIMATION.update(deltaTime);
			GREEN_ANIMATION.update(deltaTime);
		}
		@Override
		public void draw(Canvas canvas) {
			if (!isActivated()) {
				RED_ANIMATION.draw(canvas);
			} else {
				GREEN_ANIMATION.draw(canvas);
			}
		}
		/**
		 * Activates/Desactivates Bridge (makes it visible or not)
		 */
		public void changeBridgeSignal() {
			Bridge.this.isVisible = (Bridge.this.isActivated()) ? Logic.FALSE : Logic.TRUE;
		}

		@Override
		public List<DiscreteCoordinates> getCurrentCells() {
			return Collections.singletonList(getCurrentMainCellCoordinates());
		}

		@Override
		public boolean takeCellSpace() {
			return true;
		}

		@Override
		public boolean isCellInteractable() {
			return true;
		}

		@Override
		public boolean isViewInteractable() {
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
			((ARPGInteractionVisitor)v).interactWith(this);
		}
	}
	
}
