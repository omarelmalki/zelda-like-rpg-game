package play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.Interactor;
import play.game.areagame.actor.MovableAreaEntity;
import play.game.areagame.actor.Orientation;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.math.DiscreteCoordinates;

public abstract class Projectile extends MovableAreaEntity implements FlyableEntity, Interactor {
	private int speed;
	private float maxDistance;
	private final DiscreteCoordinates INITIAL_POSITION;

	/**
	 * Default Projectile constructor
	 * 
	 * @param area        (Area): Owner area. Not null
	 * @param position    (Coordinate): Initial position of the entity. Not null
	 * @param orientation (Orientation): Initial orientation of the entity. Not null
	 * @param speed       (int) Speed.
	 * @param maxDistance (int) Maximum distance.
	 */
	public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int maxDistance) {
		super(area, orientation, position);
		this.speed = speed;
		this.maxDistance = maxDistance;
		INITIAL_POSITION = position;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		move(speed);
		float traveledDistance = DiscreteCoordinates.distanceBetween(INITIAL_POSITION, getCurrentMainCellCoordinates());
		if (traveledDistance >= maxDistance || !isDisplacementOccurs())
			getOwnerArea().unregisterActor(this);
		
		move(speed);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
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

}
