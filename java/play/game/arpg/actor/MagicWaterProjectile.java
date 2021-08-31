package play.game.arpg.actor;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.window.Canvas;

public class MagicWaterProjectile extends Projectile {
	private final Sprite[] MAGIC_WATER_SPRITES;
	private final Animation MAGIC_WATER_ANIMATION;
	private final static float DAMAGE = 0.5f;
	private final MagicWaterProjectileHandler HANDLER;
	
	private final static int ANIMATION_DURATION = 4;

	/**
	 * Default MagicWaterProjectile constructor
	 * @param area (Area): Owner area. Not null
	 * @param position (Coordinate): Initial position of the entity. Not null
	 * @param orientation (Orientation): Initial orientation of the entity. Not null
	 * @param speed (int) :Speed.
	 * @param maxDistance (int): Maximum distance.
	 */
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed,
                                int maxDistance) {
		super(area, orientation, position, speed, maxDistance);
		MAGIC_WATER_SPRITES = new Sprite[4];

		for (int i = 0; i< MAGIC_WATER_SPRITES.length; ++i) {
			MAGIC_WATER_SPRITES[i] = new Sprite("zelda/magicWaterProjectile", 1, 1, this, 
					new RegionOfInterest(32*i, 0, 32, 32));
		}
		
		MAGIC_WATER_ANIMATION = new Animation(ANIMATION_DURATION, MAGIC_WATER_SPRITES);

		HANDLER = new MagicWaterProjectileHandler();
	}

	@Override
	public void update(float deltaTime) {
		MAGIC_WATER_ANIMATION.update(deltaTime);
		super.update(deltaTime);
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);	
	}

	@Override
	public void draw(Canvas canvas) {
		MAGIC_WATER_ANIMATION.draw(canvas);;
	}

	private class MagicWaterProjectileHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith (Monster monster){
			monster.loseHP(DAMAGE, Monster.MonsterVulnerabilities.MAGIC);
			MagicWaterProjectile.this.getOwnerArea().unregisterActor(MagicWaterProjectile.this);
		}

		@Override
		public void interactWith(FireSpell flame) {
			flame.extinguishFlame();
		}
	}

}
