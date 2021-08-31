/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.AreaEntity;
import play.game.areagame.actor.Interactable;
import play.game.areagame.actor.Interactor;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.actor.Monster.MonsterVulnerabilities;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.math.Vector;
import play.window.Canvas;

public class Bomb extends AreaEntity implements Interactor {

	private int countDown;
	// we have 2 different booleans: one for the interactions and one for the animation
	// will be used with animations
	private boolean isExploding;
	// used with interactions
	private boolean hasExploded;

	private int spriteSize;
	private Sprite[] sprites;
	private Animation spritesAnimation;

	private int explosionSpritesSize;
	private Sprite[] explosionSprites;
	private Animation explodingAnimation;
	private final float EXPLOSION_DAMAGE = 1.5f;

	// Interaction handler
	private final BombHandler HANDLER;

	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 8;

    /**
     * Default Bomb constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		countDown = 100;
		isExploding = false;
		hasExploded = false;
		HANDLER = new BombHandler();

		spriteSize = 2;
		sprites = new RPGSprite[spriteSize];
		for (int i = 0; i < sprites.length; ++i) {
			sprites[i] = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}

		spritesAnimation = new Animation(ANIMATION_DURATION/2, sprites);

		explosionSpritesSize = 7;
		explosionSprites = new RPGSprite[explosionSpritesSize];

		for (int i = 0; i < explosionSprites.length; ++i) {
			explosionSprites[i] = new RPGSprite("zelda/explosion", 2, 2, this, new
					RegionOfInterest(i*32, 0, 32, 32),
					new Vector(-0.5f, -0.5f));
		}

		explodingAnimation = new Animation(ANIMATION_DURATION/7, explosionSprites, false);

	}

	@Override
	public void update(float deltaTime) {

		super.update(deltaTime);

		if (isExploding) {
			finishExplosion();
		}
		if (countDown > 0) {
			--countDown;
		} else if (countDown == 0) {
			explode();
		}

		if (isExploding) {
			explodingAnimation.update(deltaTime);
		} else spritesAnimation.update(deltaTime);

		if (explodingAnimation.isCompleted()) {
			getOwnerArea().unregisterActor(this);
		}

	}

	/*
	 * Bomb explodes
	 */
	public void explode() {
		isExploding = true;
	}

	/*
	 * Indicates that bomb has exploded
	 */
	private void finishExplosion() {
		hasExploded = true;
	}



	@Override
	public boolean takeCellSpace() {
		return true;
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
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (!isExploding) {
			spritesAnimation.draw(canvas);
		}
		else {
			if (!explodingAnimation.isCompleted())
				explodingAnimation.draw(canvas);
		}

	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<DiscreteCoordinates>();

		for (int i = -1; i < 2; ++i) {
			for (int j = -1; j < 2; ++j) {
				if (i != 0 || j != 0) {
					fieldOfViewCells.add(getCurrentMainCellCoordinates().jump(new Vector(i, j)));
				}

			}
		}
		return fieldOfViewCells;
	}

	@Override
	public boolean wantsCellInteraction() {
		return isExploding && !hasExploded;
	}

	@Override
	public boolean wantsViewInteraction() {
		return isExploding && !hasExploded;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(HANDLER);

	}

	private class BombHandler implements ARPGInteractionVisitor {

		@Override
		public void interactWith(Grass grass){
			grass.cut();
		}

		@Override
		public void interactWith(ARPGPlayer player){
			player.loseHp(EXPLOSION_DAMAGE);
		}

		@Override
		public void interactWith(Monster monster) {
			monster.loseHP(EXPLOSION_DAMAGE, MonsterVulnerabilities.FIRE);
		}
	}

}
