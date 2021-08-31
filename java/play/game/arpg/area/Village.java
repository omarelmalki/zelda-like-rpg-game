/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.area;

import play.game.areagame.actor.Background;
import play.game.areagame.actor.Foreground;
import play.game.areagame.actor.Orientation;
import play.game.arpg.actor.Seller;
import play.game.rpg.actor.Door;
import play.math.DiscreteCoordinates;
import play.signal.logic.Logic;

/**
 * Specific area
 */
public class Village extends ARPGArea {

	private final Door[] BASE_DOORS = { 
			new Door("zelda/Ferme", new DiscreteCoordinates(4,1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(4,19), new DiscreteCoordinates(5,19)),
			new Door("zelda/Ferme", new DiscreteCoordinates(14,1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(13,19), new DiscreteCoordinates(14,19), new DiscreteCoordinates(15,19)),
			new Door("zelda/Route", new DiscreteCoordinates(9,1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(29,19), new DiscreteCoordinates(30,19)) 
	};

	@Override
	public String getTitle() {
		return "zelda/Village";
	}

	@Override
	protected void createArea() {
		// Base

		registerActor(new Background(this)) ;
		registerActor(new Foreground(this)) ;
		for (Door door : BASE_DOORS) {
			registerActor(door);
		}

		registerActor(new Seller(this, Orientation.DOWN, new DiscreteCoordinates(17, 10)));
	}
}
