/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
import play.game.areagame.actor.AreaEntity;
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

public class Grass extends AreaEntity {

	final static double PROBABILITY_TO_DROP_ITEM = 0.5;
	final static double PROBABILITY_TO_DROP_HEART = 0.5;

	private boolean isCut;
	private Sprite sprite;
	private Sprite[] cutGrassSprites;
	private int cutGrassSpritesSize;
	private Animation cuttingAnimation;

	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 8;

    /**
     * Default Grass constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the grass in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the grass in the Area. Not null
     */
	public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sprite = new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
		isCut = false;

		cutGrassSpritesSize = 4;
		cutGrassSprites = new RPGSprite[cutGrassSpritesSize];
		for (int i = 0; i < cutGrassSprites.length; ++i) {
			cutGrassSprites[i] = new RPGSprite("zelda/grass.sliced", 2, 2,
					this,new RegionOfInterest(i*32, 0, 32, 32),
					new Vector(-0.25f, -0.25f));
		}

		cuttingAnimation = new Animation(ANIMATION_DURATION/4, cutGrassSprites, false);

	}

	@Override
	public void update(float deltaTime) {

		super.update(deltaTime);

		if (isCut) {
			cuttingAnimation.update(deltaTime);
			if (cuttingAnimation.isCompleted()) {
				getOwnerArea().unregisterActor(this);
				if (RandomGenerator.getInstance().nextDouble() <= PROBABILITY_TO_DROP_ITEM) {

					if (RandomGenerator.getInstance().nextDouble() <= PROBABILITY_TO_DROP_HEART) {
						getOwnerArea().registerActor(new Heart(getOwnerArea(), Orientation.DOWN, this.getCurrentMainCellCoordinates()));
					}
					else {
						getOwnerArea().registerActor(new Coin(getOwnerArea(), Orientation.DOWN, this.getCurrentMainCellCoordinates()));
					}
				}	
			}
		}

	}
	
	/*
	 * Cuts grass
	 */
	public void cut() {
		isCut = true;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return !isCut;
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
		if (!isCut) {
			sprite.draw(canvas);
		}
		else if (!cuttingAnimation.isCompleted()) {
			cuttingAnimation.draw(canvas);	
		}

	}

}