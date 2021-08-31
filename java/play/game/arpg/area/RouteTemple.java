/*
 *	Author:      Omar El Malki
 *	Date:        12 Dec 2019
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
public class RouteTemple extends ARPGArea {
	
	private final Door[] BASE_DOORS = { new Door("zelda/Route", new DiscreteCoordinates(18,9), Logic.TRUE, this, Orientation.LEFT, new DiscreteCoordinates(0,4), new DiscreteCoordinates(0,5)),
			new Door("zelda/Temple", new DiscreteCoordinates(4,1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(5,6))
	};
	
	@Override
	public String getTitle() {
		return "zelda/RouteTemple";
	}
	
	@Override
	protected void createArea() {
        // Base
	
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        for (Door door : BASE_DOORS) {
        	registerActor(door);
        }
	}
}