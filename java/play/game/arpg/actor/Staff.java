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

public class Staff extends CollectableAreaEntity {
	
	private Sprite[] staffSprites;
	private int staffSpritesSize;
	private Animation staffAnimation;
	
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 4;

    /**
     * Default Staff constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Staff(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
		staffSpritesSize = 8;
		staffSprites = new RPGSprite[staffSpritesSize];
		for (int i = 0; i < staffSprites.length; ++i) {
			staffSprites[i] = new RPGSprite("zelda/staff", 2, 2, this, new RegionOfInterest(i*32, 0, 32, 32));
		}
		staffAnimation = new Animation(ANIMATION_DURATION, staffSprites, true);
	}
	

	@Override
	public void draw(Canvas canvas) {
		staffAnimation.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		staffAnimation.update(deltaTime);
	}
	
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

}
