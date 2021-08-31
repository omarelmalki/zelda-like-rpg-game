/*
 *	Author:      Omar El Malki
 *	Date:        12 Dec 2019
 */

package play.game.arpg.area;

import play.game.areagame.actor.Background;
import play.game.areagame.actor.Foreground;
import play.game.areagame.actor.Orientation;
import play.game.arpg.actor.ARPGItem;
import play.game.arpg.actor.Chest;
import play.game.arpg.actor.Portal;
import play.game.arpg.actor.Staff;
import play.game.rpg.actor.Door;
import play.math.DiscreteCoordinates;
import play.signal.logic.Logic;

/**
 * Specific area
 */
public class Temple extends ARPGArea {
	
	private final Door[] BASE_DOORS = { 
			new Door("zelda/RouteTemple", new DiscreteCoordinates(5,5), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(4,0)),
			new Portal("zelda/RouteChateau", new DiscreteCoordinates(9,4), Logic.FALSE, this, Orientation.UP, new DiscreteCoordinates(4,3))
	};
	
	@Override
	public String getTitle() {
		return "zelda/Temple";
	}
	
	@Override
	protected void createArea() {
        // Base
	
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        for (Door door : BASE_DOORS) {
        	registerActor(door);
        }
        
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(1,3), ARPGItem.APE_FLOWER));
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(5,3)));
        
	}
}
