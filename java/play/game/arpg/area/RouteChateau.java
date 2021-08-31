/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.arpg.area;

import play.game.areagame.actor.Background;
import play.game.areagame.actor.Foreground;
import play.game.areagame.actor.Orientation;
import play.game.arpg.actor.Bomb;
import play.game.arpg.actor.CastleDoor;
import play.game.arpg.actor.DarkLord;
import play.game.arpg.actor.FlameSkull;
import play.game.arpg.actor.Grass;
import play.game.arpg.actor.LogMonster;
import play.game.rpg.actor.Door;
import play.math.DiscreteCoordinates;
import play.signal.logic.Logic;
import play.window.Button;
import play.window.Keyboard;

/**
 * Specific area
 */
public class RouteChateau extends ARPGArea {

	private final Door[] BASE_DOORS = { 
			new Door("zelda/Route", new DiscreteCoordinates(9,18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0)),
			new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7,1), this, Orientation.UP, new DiscreteCoordinates(9,13), new DiscreteCoordinates(10,13))
	};

	@Override
	public String getTitle() {
		return "zelda/RouteChateau";
	}

	@Override
	protected void createArea() {
		// Base

		registerActor(new Background(this)) ;
		registerActor(new Foreground(this)) ;
		for (Door door : BASE_DOORS) {
			registerActor(door);
		}
		registerActor(new DarkLord(this, Orientation.RIGHT, new DiscreteCoordinates(9, 12), 1));
        for (int i = 6; i < 11; ++i) {
        		registerActor(new Grass(this, Orientation.DOWN, new DiscreteCoordinates(6, i)));
        		registerActor(new Grass(this, Orientation.DOWN, new DiscreteCoordinates(13, i)));
        }
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Keyboard keyboard = this.getKeyboard();
		
		Button B = keyboard.get(Keyboard.B);
		Button S = keyboard.get(Keyboard.S);
		Button L = keyboard.get(Keyboard.L);

		if (B.isPressed()) {
			registerActor(new Bomb(this, Orientation.DOWN, new DiscreteCoordinates(7, 7)));
		}
		if (S.isPressed()) {
			registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(8, 10), 5));
		}
		if (L.isPressed()) {
			registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9, 9), 500));
		}
		
		
		
	}
}