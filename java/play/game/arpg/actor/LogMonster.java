package play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RandomGenerator;
import play.math.RegionOfInterest;
import play.math.Vector;
import play.window.Canvas;

public class LogMonster extends Monster {
	private final Sprite[][] LOG_MONSTER_SPRITES;
	private final Sprite[] WAKE_SPRITES, SLEEP_SPRITES;

	private final Animation[] LOG_MONSTER_ANIMATIONS;
	private final Animation WAKING_UP, SLEEPING;
	private final static int ANIMATION_DURATION = 15;
	
	private final static float DAMAGE = 2f;

	private final Orientation[] ORIENTATION;
	private final MonsterVulnerabilities[] LOG_MONSTER_VULNERABILITIES;

	/// Animation duration in frame number
	private final static int MAX = 100, MAX_ORIENTATION_INDEX = 4;
	private final LogMonsterHandler HANDLER;
	private LogMonsterStates state;

	private static final float MIN_SLEEPING_DURATION = 2f;
	private static final float MAX_SLEEPING_DURATION = 5f;
	private float sleepingDuration;

	private enum LogMonsterStates {
		ASLEEP, AWAKE, IDLE, ATTACK;
	}

    /**
     * Default LogMonster constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
	public LogMonster(Area area, Orientation orientation, DiscreteCoordinates position, int maximumHP) {
		super(area, orientation, position, maximumHP);

		ORIENTATION = new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT };

		LOG_MONSTER_SPRITES = RPGSprite.extractSprites("zelda/logMonster", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0f),
				ORIENTATION);

		LOG_MONSTER_ANIMATIONS = RPGSprite.createAnimations(ANIMATION_DURATION / 2, LOG_MONSTER_SPRITES);

		LOG_MONSTER_VULNERABILITIES = new MonsterVulnerabilities[] { MonsterVulnerabilities.PHYSICAL,
				MonsterVulnerabilities.FIRE };

		HANDLER = new LogMonsterHandler();

		SLEEP_SPRITES = new Sprite[4];

		WAKE_SPRITES = new Sprite[3];

		for (int i = 0; i < SLEEP_SPRITES.length; ++i) {
			SLEEP_SPRITES[i] = new RPGSprite("zelda/logMonster.sleeping", 2, 2, this,
					new RegionOfInterest(0, i * 32, 32, 32), new Vector(-0.5f, 0f));
		}

		for (int j = 0; j < WAKE_SPRITES.length; ++j) {
			WAKE_SPRITES[j] = new RPGSprite("zelda/logMonster.wakingUp", 2, 2, this,
					new RegionOfInterest(0, j * 32, 32, 32), new Vector(-0.5f, 0f));
		}

		WAKING_UP = new Animation(ANIMATION_DURATION, WAKE_SPRITES, false);

		SLEEPING = new Animation(ANIMATION_DURATION / 2, SLEEP_SPRITES, true);

		state = LogMonsterStates.IDLE;

		sleepingDuration = MIN_SLEEPING_DURATION
				+ new Random().nextFloat() * (MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION);
	}

	/**
	 * randomizes log monster's movement
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

		switch (state) {
		case IDLE:
			if (!isInactive()) {
				LOG_MONSTER_ANIMATIONS[getOrientation().ordinal()].update(deltaTime);
				randomizeMovement();
			}
			int random = RandomGenerator.getInstance().nextInt(MAX);
			if (isTargetReached() && random <= 30) {
				LOG_MONSTER_ANIMATIONS[getOrientation().ordinal()].reset();
			}

			break;

		case ASLEEP:
			SLEEPING.update(deltaTime);

			sleepingDuration -= deltaTime;
			if (sleepingDuration <= 0) {
				state = LogMonsterStates.AWAKE;
				sleepingDuration = MIN_SLEEPING_DURATION
						+ new Random().nextFloat() * (MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION);
			}

			break;

		case AWAKE:
			WAKING_UP.update(deltaTime);
			break;

		case ATTACK:
			LOG_MONSTER_ANIMATIONS[getOrientation().ordinal()].update(deltaTime);
			move(ANIMATION_DURATION / 4);
			if (!isDisplacementOccurs() && !getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells())) {
				state = LogMonsterStates.ASLEEP;
			}
			break;

		}

		super.blinkingEffect(deltaTime);

		super.update(deltaTime);

		if (monsterIsDead()) {
			abortCurrentMove();
			if (isDeathAnimationCompleted()) {
				drop(new Coin(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates()));
			}
		}

	}

	@Override
	public void draw(Canvas canvas) {
		if (isVisible()) {
			if (!monsterIsDead()) {
				switch (state) {
				case ASLEEP:
					SLEEPING.draw(canvas);
					break;
				case AWAKE:
					WAKING_UP.draw(canvas);
					if (WAKING_UP.isCompleted())
						state = LogMonsterStates.IDLE;
					break;
				case IDLE:
					LOG_MONSTER_ANIMATIONS[getOrientation().ordinal()].draw(canvas);
					break;
				case ATTACK:
					LOG_MONSTER_ANIMATIONS[getOrientation().ordinal()].draw(canvas);
					break;
				}
			}

		}
		super.draw(canvas);
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return state == LogMonsterStates.ATTACK || state == LogMonsterStates.IDLE;
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {

		if (state == LogMonsterStates.ATTACK)
			return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));

		List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<DiscreteCoordinates>();

		for (int i = 1; i <= 8; ++i) {
			fieldOfViewCells.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i)));
		}

		return fieldOfViewCells;
	}

	@Override
	protected MonsterVulnerabilities[] getMonsterVulnerabilities() {
		return LOG_MONSTER_VULNERABILITIES;
	}

	public class LogMonsterHandler extends Monster.MonsterHandler {

		@Override
		public void interactWith(ARPGPlayer player) {
			switch (state) {
			case IDLE:
				state = LogMonsterStates.ATTACK;
				break;
			case ATTACK:
				player.loseHp(DAMAGE);
				break;
			default:
				break;
			}
		}

	}

}
