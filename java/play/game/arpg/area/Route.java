/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.area;

import play.game.actor.Actor;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.Background;
import play.game.areagame.actor.Foreground;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.arpg.actor.Bridge;
import play.game.arpg.actor.Grass;
import play.game.rpg.actor.Door;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.math.Transform;
import play.math.Vector;
import play.signal.logic.Logic;
import play.window.Canvas;

/**
 * Specific area
 */
public class Route extends ARPGArea {
	
	private final Door[] BASE_DOORS = { 
			new Door("zelda/Ferme", new DiscreteCoordinates(18,15), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(0,15), new DiscreteCoordinates(0,16)),
			new Door("zelda/Village", new DiscreteCoordinates(29,18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0)),
			new Door("zelda/RouteChateau", new DiscreteCoordinates(9,2), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(9,19), new DiscreteCoordinates(10,19)),
			new Door("zelda/RouteTemple", new DiscreteCoordinates(1,4), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19,9), new DiscreteCoordinates(19,10))
	};
	
	@Override
	public String getTitle() {
		return "zelda/Route";
	}
	
	@Override
	protected void createArea() {
        // Base
	
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        for (Door door : BASE_DOORS) {
        	registerActor(door);
        }
        for (int i = 5; i < 8; ++i) {
        	for (int j = 6; j < 12; ++j) {
        		registerActor(new Grass(this, Orientation.DOWN, new DiscreteCoordinates(i, j)));
        	}
        }
        registerActor(new Waterfall());
        Bridge bridge = new Bridge(this, Orientation.DOWN, new DiscreteCoordinates(16, 10), Logic.FALSE);
        registerActor(bridge);
        registerActor(bridge.new Orb(this, Orientation.DOWN, new DiscreteCoordinates(19, 8)));
	}
	
	
	private class Waterfall implements Actor {
		private final Sprite[] WATERFALL_SPRITES;
		private final Animation WATERFALL_ANIMATION;
		
		/// Animation duration in frame number
	    private final static int ANIMATION_DURATION = 3;
		
		Waterfall() {
			WATERFALL_SPRITES = new Sprite[3];
			for (int i = 0; i<3; ++i) {
				WATERFALL_SPRITES[i] = new RPGSprite("zelda/waterfall", 4, 4, this, new RegionOfInterest(i*64, 0, 64, 64));
			}
			WATERFALL_ANIMATION = new Animation(ANIMATION_DURATION, WATERFALL_SPRITES, true);
		}
		
		@Override
		public void update(float deltaTime) {
			WATERFALL_ANIMATION.update(deltaTime);
		}
		@Override
		public void draw(Canvas canvas) {
			WATERFALL_ANIMATION.draw(canvas);
		}
		@Override
		public Transform getTransform() {
			return new Transform(1, 0, 15, 0, 1, 3);
		}
		@Override
		public Vector getVelocity() {
			return null;
		}
	}
}
