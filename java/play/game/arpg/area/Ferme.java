/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.area;

import play.game.areagame.actor.Background;
import play.game.areagame.actor.Foreground;
import play.game.areagame.actor.Orientation;
import play.game.rpg.actor.Door;
import play.math.DiscreteCoordinates;
import play.signal.logic.Logic;

/**
 * Specific area
 */
public class Ferme extends ARPGArea {
	
	private final Door[] BASE_DOORS = { 
			new Door("zelda/Route", new DiscreteCoordinates(1,15), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19,15), new DiscreteCoordinates(19,16)),
			new Door("zelda/Village", new DiscreteCoordinates(4,18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(4,0), new DiscreteCoordinates(5,0)),
			new Door("zelda/Village", new DiscreteCoordinates(14,18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(13,0), new DiscreteCoordinates(14,0))
			};
	
	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}
	
	@Override
	protected void createArea() {
        // Base
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        for (Door door : BASE_DOORS) {
        	registerActor(door);
        }
	}
	
}
