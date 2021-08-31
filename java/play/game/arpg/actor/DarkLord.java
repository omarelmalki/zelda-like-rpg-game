package play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class DarkLord extends Monster {
	private final Sprite[][] DARK_LORD_SPRITES;
	private final Sprite[][] SPELL_SPRITES;

	private final Animation[] DARK_LORD_ANIMATION;
	private final Animation[] SPELL_ANIMATION;
	private DarkLordStates state;

	private final MonsterVulnerabilities[] VULNERABILITIES;

	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 10;
	private final static int MAX = 100;
	private final static int MAX_ORIENTATION_INDEX = 3;
	private final static int TELEPORTATION_RADIUS = 5;
	private final static int MIN_WAIT_DURATION = 150;
	private final static int MAX_WAIT_DURATION = 250;
	private final static int FIELD_OF_VIEW_RADIUS = 3;

	private final DarkLordHandler HANDLER;

	private int cycleDuration;
	private float cycle = 0;

	private enum DarkLordStates {
		IDLE, ATTACK, SPAWNING, INVOKING_TELEPORT, TELEPORTING;
	}

	/**
	 * Default DarkLord constructor
	 * @param area (Area): Owner area. Not null
	 * @param position (Coordinate): Initial position of the entity. Not null
	 * @param orientation (Orientation): Initial orientation of the entity. Not null
	 * @param maximumHP (int) Max hp.
	 */
	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position, int maximumHP) {
		super(area, orientation, position, maximumHP);

		DARK_LORD_SPRITES = RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0f),
				new Orientation[] { Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT });

		SPELL_SPRITES = RPGSprite.extractSprites("zelda/darkLord.spell", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0f),
				new Orientation[] { Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT });
		state = DarkLordStates.IDLE;
		DARK_LORD_ANIMATION = RPGSprite.createAnimations(ANIMATION_DURATION / 2, DARK_LORD_SPRITES);
		SPELL_ANIMATION = RPGSprite.createAnimations(ANIMATION_DURATION / 2, SPELL_SPRITES, false);

		VULNERABILITIES = new MonsterVulnerabilities[] { MonsterVulnerabilities.MAGIC };
		HANDLER = new DarkLordHandler();

		cycleDuration = MIN_WAIT_DURATION
				+ RandomGenerator.getInstance().nextInt(MAX_WAIT_DURATION - MIN_WAIT_DURATION);
	}

	/**
	 * orientates the dark lord towards a valable coordinate for spawning and
	 * returns the corresponding coordinates (the ones of the cell right in front of
	 * him)
	 * 
	 * @return (DiscreteCoordinates)
	 */
	private DiscreteCoordinates randomizedSpawnerCoordinates() {
		int randomValue = RandomGenerator.getInstance().nextInt(3);
		int randomOrientationIndex = (randomValue + 1) % 4;
		do {
			randomOrientationIndex = (randomOrientationIndex + 1) % 4;
			orientate(Orientation.values()[randomOrientationIndex]);
		} while (!(getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) && 
				randomOrientationIndex != randomValue) ;

		return getCurrentMainCellCoordinates().jump(getOrientation().toVector());

	}

	/**
	 * Teleports dark lord to a possible location
	 */
	private void teleport() {
		DiscreteCoordinates enteringCells;
		int tries = 0;
		int maxTries = 10;
		do {
			int x = -TELEPORTATION_RADIUS + RandomGenerator.getInstance().nextInt(2 * TELEPORTATION_RADIUS);
			int y = -TELEPORTATION_RADIUS + RandomGenerator.getInstance().nextInt(2 * TELEPORTATION_RADIUS);
			enteringCells = getCurrentMainCellCoordinates().jump(new Vector(x, y));
			++tries;
		} while (!getOwnerArea().canEnterAreaCells(this, Collections.singletonList(enteringCells)) && tries <= maxTries);

		if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(enteringCells))) {
			getOwnerArea().leaveAreaCells(this, getCurrentCells());
			setCurrentPosition(enteringCells.toVector());
			getOwnerArea().enterAreaCells(this, Collections.singletonList(enteringCells));
		}

	}

	/**
	 * randomizes dark lord's movement
	 */
	private void randomizeMovement() {
		int random = RandomGenerator.getInstance().nextInt(MAX);

		if (random > 60 && !isDisplacementOccurs()) {
			int randomOrientationIndex = RandomGenerator.getInstance().nextInt(MAX_ORIENTATION_INDEX);

			orientate(Orientation.values()[randomOrientationIndex]);
			move(ANIMATION_DURATION);
		}
	}
	
	/**
	 * for each cycle, the dark lord either spawns a flame skull
	 * or a fire spell
	 */
	private void cycleGenerator() {
		if (cycle % cycleDuration == 0) {
			int random = RandomGenerator.getInstance().nextInt(MAX);
			if (random <= 50)
				state = DarkLordStates.SPAWNING;
			else
				state = DarkLordStates.ATTACK;
		}
	}

	@Override
	public void update(float deltaTime) {

		++cycle;

		switch (state) {

		case IDLE:
			cycleGenerator();
			
			DARK_LORD_ANIMATION[getOrientation().ordinal()].update(deltaTime);

			if (!isInactive()) {
				randomizeMovement();
			}

			if (isTargetReached()) {
				DARK_LORD_ANIMATION[getOrientation().ordinal()].reset();
			}
			break;

		case ATTACK:
			SPELL_ANIMATION[getOrientation().ordinal()].update(deltaTime);

			if (SPELL_ANIMATION[getOrientation().ordinal()].isCompleted()) {
				DiscreteCoordinates spawnCoordinates = randomizedSpawnerCoordinates();
				getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(), spawnCoordinates, 5));
				SPELL_ANIMATION[getOrientation().ordinal()].reset();
				state = DarkLordStates.IDLE;
			}
			break;

		case SPAWNING:
			SPELL_ANIMATION[getOrientation().ordinal()].update(deltaTime);

			if (SPELL_ANIMATION[getOrientation().ordinal()].isCompleted()) {
				DiscreteCoordinates spawnCoordinates = randomizedSpawnerCoordinates();
				getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), getOrientation(), spawnCoordinates, 3));
				SPELL_ANIMATION[getOrientation().ordinal()].reset();
				state = DarkLordStates.IDLE;
			}
			break;

		case INVOKING_TELEPORT:
			if (!isDisplacementOccurs()) {
				SPELL_ANIMATION[getOrientation().ordinal()].update(deltaTime);
				if (SPELL_ANIMATION[getOrientation().ordinal()].isCompleted()) {
					state = DarkLordStates.TELEPORTING;
				}
			}
			break;

		case TELEPORTING:
			if (SPELL_ANIMATION[getOrientation().ordinal()].isCompleted()) {
				state = DarkLordStates.IDLE;
				teleport();
				SPELL_ANIMATION[getOrientation().ordinal()].reset();

			}
			break;
		}

		// makes dark lord blink upon receiving damage

		super.blinkingEffect(deltaTime);

		super.update(deltaTime);

		if (monsterIsDead() && isDeathAnimationCompleted())
			drop(new CastleKey(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates()));

	}

	@Override
	public void draw(Canvas canvas) {
		if (!monsterIsDead() && isVisible()) {
			switch (state) {
			case IDLE:
				DARK_LORD_ANIMATION[getOrientation().ordinal()].draw(canvas);
				break;
			case ATTACK:
				SPELL_ANIMATION[getOrientation().ordinal()].draw(canvas);
				break;
			case SPAWNING:
				SPELL_ANIMATION[getOrientation().ordinal()].draw(canvas);
				break;
			case INVOKING_TELEPORT:
				SPELL_ANIMATION[getOrientation().ordinal()].draw(canvas);
				break;
			default:
				break;
			}
		}

		super.draw(canvas);
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return !monsterIsDead();
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {

		List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<DiscreteCoordinates>();
		DiscreteCoordinates mainCellCoordinates = this.getCurrentMainCellCoordinates();
		for (int i = -FIELD_OF_VIEW_RADIUS; i <= FIELD_OF_VIEW_RADIUS; ++i) {
			for (int j = -FIELD_OF_VIEW_RADIUS; j <= FIELD_OF_VIEW_RADIUS; ++j) {
				if (i != 0 || j != 0) {
					fieldOfViewCells.add(new DiscreteCoordinates(mainCellCoordinates.x + i, mainCellCoordinates.y + j));
				}

			}
		}
		return fieldOfViewCells;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	@Override
	protected MonsterVulnerabilities[] getMonsterVulnerabilities() {
		return VULNERABILITIES;
	}

	private class DarkLordHandler extends Monster.MonsterHandler {

		@Override
		public void interactWith(ARPGPlayer player) {
			switch (state) {
			case IDLE:
				state = DarkLordStates.INVOKING_TELEPORT;
				break;
			default:
				break;
			}
		}
	}
}
