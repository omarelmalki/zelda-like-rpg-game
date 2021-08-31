/*
 *	Author:      Omar El Malki
 *	Date:        28 Nov 2019
 */

package play.game.arpg.actor;

import play.game.areagame.Area;
import play.game.areagame.actor.Orientation;
import play.game.areagame.actor.Sprite;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.handler.ARPGInteractionVisitor;
import play.game.rpg.actor.Door;
import play.game.rpg.actor.RPGSprite;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.signal.logic.Logic;
import play.window.Canvas;

public class CastleDoor extends Door{
	
	private Sprite closed;
	private Sprite open;

    /**
     * Default CastleDoor constructor, signal always false
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area,
                      Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
		super(destination, otherSideCoordinates, Logic.FALSE, area, orientation, position, otherCells);
		closed = new RPGSprite("zelda/castleDoor.close", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
		open = new RPGSprite("zelda/castleDoor.open", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
	}
	
	/**
	 * Closes CastleDoor
	 */
	protected void close() {
		setSignal(Logic.FALSE);
	}
	
	/**
	 * Opens CastleDoor
	 */
	protected void open() {
		setSignal(Logic.TRUE);
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isOpen()) {
			open.draw(canvas);
		}
		else {
			closed.draw(canvas);
		}
	}
	
    @Override
    public boolean takeCellSpace() {
        return !isOpen();
    }
    
    @Override
    public boolean isViewInteractable(){
        return !isOpen();
    }
	
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);
    }

}
