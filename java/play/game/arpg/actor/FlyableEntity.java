package play.game.arpg.actor;

public interface FlyableEntity {
	
	/**
	 * indicates if the entity can fly
	 * @return true by default (boolean)
	 */
	default boolean CanFly() {
		// returns true by default
		return true;
	}

}
