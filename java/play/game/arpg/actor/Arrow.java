package play.game.arpg.actor;

import play.game.areagame.Area;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.window.Canvas;

public class Arrow extends Projectile {
	private final Sprite[] ARROW_SPRITES;
	private final static float DAMAGE = 0.5f;
	private final ArrowHandler HANDLER;
	private int orientationIndex;

	/**
	 * Default Arrow constructor
	 * 
	 * @param area        (Area): Owner area. Not null
	 * @param position    (Coordinate): Initial position of the entity. Not null
	 * @param orientation (Orientation): Initial orientation of the entity. Not null
	 * @param speed       (int) Speed.
	 * @param maxDistance (int) Maximum distance.
	 */
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int maxDistance) {
		super(area, orientation, position, speed, maxDistance);

		ARROW_SPRITES = new Sprite[4];

		for (int i = 0; i < ARROW_SPRITES.length; ++i) {
			ARROW_SPRITES[i] = new Sprite("zelda/arrow", 1, 1, this, new RegionOfInterest(32 * i, 0, 32, 32));
		}

		HANDLER = new ArrowHandler();
		orientationIndex = orientation.ordinal();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
 	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	@Override
	public void draw(Canvas canvas) {
		ARROW_SPRITES[orientationIndex].draw(canvas);
	}

	private class ArrowHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
			Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
		}

		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
			Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
		}

		@Override
		public void interactWith(Monster monster) {
			monster.loseHP(DAMAGE, Monster.MonsterVulnerabilities.PHYSICAL);
			Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
		}

		@Override
		public void interactWith(FireSpell flame) {
			flame.extinguishFlame();
		}

		@Override
		public void interactWith(Bridge.Orb orb) {
			orb.changeBridgeSignal();
			Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
		}
	}
}
