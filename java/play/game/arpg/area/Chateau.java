/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.arpg.area;

import play.game.areagame.actor.Background;
import play.game.areagame.actor.Foreground;
import play.game.areagame.actor.Orientation;
import play.game.arpg.actor.ARPGItem;
import play.game.arpg.actor.Chest;
import play.game.arpg.actor.Portal;
import play.game.rpg.actor.Door;
import play.math.DiscreteCoordinates;
import play.signal.logic.Logic;

/**
 * Specific area
 */
public class Chateau extends ARPGArea {
	
	private final Door[] BASE_DOORS = { 
			new Door("zelda/RouteChateau", new DiscreteCoordinates(9,11), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(7,0), new DiscreteCoordinates(8,0)),
			new Portal("zelda/Ferme", new DiscreteCoordinates(6,10), Logic.FALSE, this, Orientation.DOWN, new DiscreteCoordinates(2,11)),
	};
	
	@Override
	public String getTitle() {
		return "zelda/Chateau";
	}
	
	@Override
	protected void createArea() {
        // Base
	
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        for (Door door : BASE_DOORS) {
        	registerActor(door);
        }
        registerActor(new Chest(this, Orientation.DOWN, new DiscreteCoordinates(13,12), ARPGItem.MAX_POTION));
	}
}