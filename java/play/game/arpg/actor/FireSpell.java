package play.game.arpg.actor;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.AreaEntity;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Interactor;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.window.Canvas;

public class FireSpell extends AreaEntity implements Interactor {
	private final Monster.MonsterVulnerabilities DAMAGE_TYPE;
	private final Sprite[] FIRE_SPRITES;
	private final Animation FIRE_ANIMATION;
	private final FireSpellHandler HANDLER;

	private final static float DAMAGE = 0.5f;
	private int power;

	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 3;
	private final static float PROPAGATION_TIME = 2;
	private final static float MIN_LIFE_TIME = 12.0f;
	private final static float MAX_LIFE_TIME = 24.0f;

	private float lifeTimer;
	private float propagationTimer;
	private boolean isExtinguished;

	/**
	 * Default FireSpell constructor
	 * 
	 * @param area        (Area): Owner area. Not null
	 * @param orientation (Orientation): Initial orientation of the entity in the
	 *                    Area. Not null
	 * @param position    (DiscreteCoordinate): Initial position of the entity in
	 *                    the Area. Not null
	 * @param power       (int) fire spell power.
	 */
	public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int power) {
		super(area, orientation, position);
		DAMAGE_TYPE = Monster.MonsterVulnerabilities.FIRE;
		HANDLER = new FireSpellHandler();

		FIRE_SPRITES = new Sprite[7];
		for (int i = 0; i <= 6; ++i) {
			FIRE_SPRITES[i] = new RPGSprite("zelda/fire", 1, 1, this, new RegionOfInterest(16 * i, 0, 16, 16));
		}

		FIRE_ANIMATION = new Animation(ANIMATION_DURATION, FIRE_SPRITES, true);

		lifeTimer = MIN_LIFE_TIME + new Random().nextFloat() * (MAX_LIFE_TIME - MIN_LIFE_TIME);
		propagationTimer = PROPAGATION_TIME;
		isExtinguished = false;
		this.power = power;
	}

	/**
	 * sets isExtinguished to true
	 */
	protected void extinguishFlame() {
		isExtinguished = true;
	}

	@Override
	public void update(float deltaTime) {
		lifeTimer -= deltaTime;
		propagationTimer -= deltaTime;

		FIRE_ANIMATION.update(deltaTime);

		if (lifeTimer <= 0)
			isExtinguished = true;

		if (propagationTimer <= 0 && getOwnerArea().canEnterAreaCells(this,
				Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) {
			propagationTimer = PROPAGATION_TIME;
			--power;
			if (power > 0)
				getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(),
						getCurrentMainCellCoordinates().jump(getOrientation().toVector()), power));
		}

		if (isExtinguished) {
			getOwnerArea().unregisterActor(this);
		}

		super.update(deltaTime);
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (!isExtinguished)
			FIRE_ANIMATION.draw(canvas);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);
	}

	private class FireSpellHandler implements ARPGInteractionVisitor {

		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}

		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
		}

		@Override
		public void interactWith(ARPGPlayer player) {
			player.loseHp(DAMAGE);
		}

		@Override
		public void interactWith(Monster monster) {
			monster.loseHP(DAMAGE, DAMAGE_TYPE);
		}
	}
}
