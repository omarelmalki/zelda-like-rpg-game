package play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.AreaEntity;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Interactor;
import play.game.areagame.actor.MovableAreaEntity;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RandomGenerator;
import play.math.RegionOfInterest;
import play.math.Vector;
import play.window.Canvas;

public abstract class Monster extends MovableAreaEntity implements Interactor {

	private int hp;
	private final int MAX_HP;
	private Sprite[] deathSprites;
	private Animation deathAnimation;
	private boolean isDead;
	protected final static int ATTACK_POWER = 1;
	private final MonsterHandler HANDLER;
	private boolean tookDamage;
	private boolean isInactive;

	private final static float MAX_INACTIVITY_TIME = 1f;
	private float inactivityTimer;

	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 1;
	private boolean isVisible;

	private final static float INVIS_EFFECT = 0.25f;
	private final static float STATE_TIMER = 0.01f;

	private float remainingEffectTimer = INVIS_EFFECT;
	private float remainingStateTimer = STATE_TIMER;

	protected enum MonsterVulnerabilities {
		PHYSICAL,
		FIRE,
		MAGIC;
	}

	/**
	 * Default Monster constructor
	 * @param area (Area): Owner area. Not null
	 * @param position (Coordinate): Initial position of the entity. Not null
	 * @param orientation (Orientation): Initial orientation of the entity. Not null
	 */
	public Monster(Area area, Orientation orientation, DiscreteCoordinates position, int maximumHP) {
		super(area, orientation, position);
		this.MAX_HP = maximumHP;
		hp = MAX_HP;
		deathSprites = new Sprite[7];
		for (int i = 0; i < deathSprites.length; ++i) {
			deathSprites[i] = new RPGSprite("zelda/vanish", 2, 2, this,
					new RegionOfInterest(i*32, 0, 32, 32),
					new Vector(-0.5f, 0f));
		}
		deathAnimation = new Animation(ANIMATION_DURATION, deathSprites, false);
		isDead = false;
		HANDLER = new MonsterHandler();
		tookDamage = false;

		isInactive = false;
		inactivityTimer = 0;

		isVisible = true;
	}

	@Override
	public void update(float deltaTime) {

		if (hp<=0) {
			monsterDead();
		}
		if (isDead) {
			abortCurrentMove();
			deathAnimation.update(deltaTime);
			if (deathAnimation.isCompleted()) {
				getOwnerArea().unregisterActor(this);
			}

		}

		// randomizes a certain duration for which the monster
		// is inactive, is done with delta time (instead of a 
		// certain number of frames)
		int random = RandomGenerator.getInstance().nextInt(100);
		if(isTargetReached() && random <= 30) {
			inactivityTimer -= deltaTime;
			isInactive = true;
			if (inactivityTimer <= 0) {
				isInactive = false;
				inactivityTimer = RandomGenerator.getInstance().nextFloat()*(MAX_INACTIVITY_TIME);
			}
		}
		super.update(deltaTime);
	}

	/**
	 * getter for isVisible
	 * @return isVisible (boolean)
	 */
	protected boolean isVisible() {
		return isVisible;
	}

	/**
	 * Drops an item
	 * @param object (AreaEntity) object to drop.
	 * @return success (boolean).
	 */
	protected boolean drop(AreaEntity object) {
		if (getOwnerArea().registerActor(object)) {
			return true;
		}
		return false;
	}

	/**
	 * makes said monster blink after taking damage for a set duration of time
	 */
	protected void blinkingEffect(float deltaTime) {
		if(tookDamage){
			remainingEffectTimer -= deltaTime;
			remainingStateTimer -= deltaTime;
			if (remainingEffectTimer>0) {
				if (remainingStateTimer<0) {
					isVisible = !isVisible;
					remainingStateTimer = STATE_TIMER;
				}
			} else {
				remainingEffectTimer = INVIS_EFFECT;
				remainingStateTimer = STATE_TIMER;
				isVisible = true;
				resetDamage();
			}
		}
	}

	/**
	 * sets isDead to true, indicating that said monster is dead
	 */
	protected void monsterDead() {
		isDead = true;
	}

	/**
	 * getter for isInactive 
	 * @return isInactive (boolean)
	 */
	protected boolean isInactive() {
		return isInactive;
	}
	/**
	 * getter for isDead
	 * @return isDead (boolean)
	 */
	protected boolean monsterIsDead() {
		return isDead;
	}

	/**
	 * Checks if deathAnimation is completed
	 * @return isDeathAnimationCompleted (boolean) true if death Animation is completed
	 */
	protected boolean isDeathAnimationCompleted() {
		return deathAnimation.isCompleted();
	}

	/**
	 * getter for said monster's vulnerabilities
	 * @return	Monster vulnerabilities (MonsterVulnerabilities[])
	 */
	abstract protected MonsterVulnerabilities[] getMonsterVulnerabilities();

	/**
	 * if no damage has been done to said monster recently (depending on the update), damage is delt
	 * @param damage (float): damage to be delt
	 */
	protected void loseHP(float damage, MonsterVulnerabilities attackType) {
		if(!tookDamage && damage >= 0) {
			for (MonsterVulnerabilities vulnerability : getMonsterVulnerabilities()) {
				if (vulnerability == attackType) {
					this.hp -= damage;
					tookDamage = true;					
				}
			}
		}
	}

	/**
	 * resets said monster's damage state
	 */
	private void resetDamage() {
		tookDamage = false;
	}

	@Override
	public boolean takeCellSpace() {
		return !isDead;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if(isDead && !deathAnimation.isCompleted()) {
			deathAnimation.draw(canvas);
		}
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	public class MonsterHandler implements ARPGInteractionVisitor{

		@Override
		public void interactWith(ARPGPlayer player) {
				player.loseHp(ATTACK_POWER);
		}
	}
}
