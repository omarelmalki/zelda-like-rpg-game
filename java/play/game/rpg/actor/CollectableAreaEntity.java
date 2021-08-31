/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.rpg.actor;

import java.util.Collections;
import java.util.List;

import play.game.areagame.Area;
import play.game.areagame.actor.AreaEntity;
import play.game.areagame.actor.Orientation;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.math.DiscreteCoordinates;

public abstract class CollectableAreaEntity extends AreaEntity {

    /**
     * Default CollectableAreaEntity constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
	public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
	}

	/**
	 * Gets collected and disappears from Area
	 */
	public void collect() {
		getOwnerArea().unregisterActor(this);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
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
		((ARPGInteractionVisitor)v).interactWith(this);
	}

}
