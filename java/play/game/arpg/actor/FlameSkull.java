package play.game.arpg.actor;

import java.util.Random;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RandomGenerator;
import play.math.Vector;
import play.window.Canvas;

public class FlameSkull extends Monster implements FlyableEntity {
	private final static float MIN_LIFE_TIME = 1f;
	private final static float MAX_LIFE_TIME = 50f;
	private float lifeTime;
	private final Sprite[][] FLAME_SKULL_SPRITES;
	private final Animation[] FLAME_SKULL_ANIMATIONS;
	
	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 15;

	private final static int MAX = 100;
	private final static int MAX_ORIENTATION_INDEX = 3;
	private final Orientation[] ORIENTATION;
	private final FlameSkullHandler HANDLER;
	private final MonsterVulnerabilities[] FLAME_SKULL_VULNERABILITIES;

    /**
     * Default FlameSkull constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position, int maximumHP) {
		super(area, orientation, position, maximumHP);
		lifeTime = 0f;
		ORIENTATION = new Orientation[] 
				{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT};
		FLAME_SKULL_SPRITES = RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32,
				new Vector(-0.5f, 0f), ORIENTATION);
		FLAME_SKULL_ANIMATIONS = RPGSprite.createAnimations(ANIMATION_DURATION/3, FLAME_SKULL_SPRITES, true);
		
		HANDLER = new FlameSkullHandler();
		FLAME_SKULL_VULNERABILITIES = new MonsterVulnerabilities [] 
				{MonsterVulnerabilities.MAGIC, MonsterVulnerabilities.PHYSICAL};

		lifeTime = MIN_LIFE_TIME + new Random().nextFloat()*(MAX_LIFE_TIME - MIN_LIFE_TIME);
	}

	/**
	 * indicates if the flame skull has been alive for 
	 * longer than the allowed the allowed time
	 * @return if(lifeTime>= MAX_LIFE_TIME) (boolean)
	 */
	protected boolean livedEnough() {
		return lifeTime <=0;
	}

	/**
	 * randomizes flame skull's movement
	 */
	private void randomizeMovement() {
		int random = RandomGenerator.getInstance().nextInt(MAX);

		if (random > 60 && !isDisplacementOccurs()) {
			int randomOrientationIndex = RandomGenerator.getInstance().nextInt(MAX_ORIENTATION_INDEX);
			orientate(Orientation.values()[randomOrientationIndex]);
			move(ANIMATION_DURATION);	
		}
	}

	@Override
	public void update(float deltaTime) {
		lifeTime -= deltaTime;

		if(livedEnough()) {
			this.monsterDead();
			abortCurrentMove();
		}

		super.blinkingEffect(deltaTime);

		if(!isInactive()) {
			FLAME_SKULL_ANIMATIONS[getOrientation().ordinal()].update(deltaTime);
			randomizeMovement();
		}

		if(isTargetReached())
			FLAME_SKULL_ANIMATIONS[getOrientation().ordinal()].reset();
		super.update(deltaTime);

	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}
	@Override
	public void draw(Canvas canvas) {
		if(!monsterIsDead() && isVisible()) {
			FLAME_SKULL_ANIMATIONS[getOrientation().ordinal()].draw(canvas);
		}
		super.draw(canvas);
	}

	@Override
	protected MonsterVulnerabilities[] getMonsterVulnerabilities() {
		return FLAME_SKULL_VULNERABILITIES;
	}

	private class FlameSkullHandler extends Monster.MonsterHandler{
		
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}

		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
		}

		@Override
		public void interactWith(Monster monster) {
			monster.loseHP(ATTACK_POWER, MonsterVulnerabilities.FIRE);
		}
		
	}

}
