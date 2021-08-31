/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.arpg.actor;

import play.game.areagame.Area;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.CollectableAreaEntity;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.window.Canvas;

public class CastleKey extends CollectableAreaEntity {
	
	private Sprite keySprite;
	
    /**
     * Default CastleKey constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public CastleKey(Area area, Orientation orientation,
                     DiscreteCoordinates position) {
		super(area, orientation, position);
		keySprite = new RPGSprite(ARPGItem.CASTLE_KEY.getSpriteName(), 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	}

	
	@Override
	public void draw(Canvas canvas) {
		keySprite.draw(canvas);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
}
