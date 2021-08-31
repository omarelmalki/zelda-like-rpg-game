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

public class Coin extends CollectableAreaEntity {
	
	private final static int VALUE = 50;
	private Sprite[] coinSprites;
	private int coinSpritesSize;
	private Animation coinAnimation;
	
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;

    /**
     * Default Coin constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
		coinSpritesSize = 4;
		coinSprites = new RPGSprite[coinSpritesSize];
		for (int i = 0; i < coinSprites.length; ++i) {
			coinSprites[i] = new RPGSprite("zelda/coin", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}
		coinAnimation = new Animation(ANIMATION_DURATION/4, coinSprites, true);
	}
	

	@Override
	public void draw(Canvas canvas) {
		coinAnimation.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		coinAnimation.update(deltaTime);
	}
	
	/**
	 * getter for VALUE
	 * @return VALUE (int).
	 */
	public int getValue() {
		return VALUE;
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

}
