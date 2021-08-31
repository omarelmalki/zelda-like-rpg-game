/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.arpg.actor;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.CollectableAreaEntity;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.window.Canvas;

public class Heart extends CollectableAreaEntity {
	
	private final float RECOVERY = 1f;
	private Sprite[] heartSprites;
	private int heartSpritesSize;
	private Animation heartAnimation;;
	
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;

    /**
     * Default Heart constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
		heartSpritesSize = 4;
		heartSprites = new RPGSprite[heartSpritesSize];
		for (int i = 0; i < heartSprites.length; ++i) {
			heartSprites[i] = new RPGSprite("zelda/heart", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}
		heartAnimation = new Animation(ANIMATION_DURATION/4, heartSprites, true);
	}
	

	@Override
	public void draw(Canvas canvas) {
		heartAnimation.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		heartAnimation.update(deltaTime);
	}

	/**
	 * getter for RECOVERY
	 * @return RECOVERY (float)
	 */
	public float getRecovery() {
		return RECOVERY;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
}
