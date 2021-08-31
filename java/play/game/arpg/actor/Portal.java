package play.game.arpg.actor;

/*
 *	Author:      Omar El Malki
 *	Date:        7 Dec 2019
 */

import play.game.areagame.Area;
import play.game.areagame.actor.Animation;
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

public class Portal extends Door{
	
	private Sprite[] sprites;
	private Animation animation;
	
	/// Animation duration in frame number
    private final static int ANIMATION_DURATION = 4;

    /**
     * Default Portal constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal (Logic): LogicGate signal opening the door, may be null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
	public Portal(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
                  Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
		super(destination, otherSideCoordinates, Logic.FALSE, area, orientation, position, otherCells);
		
		sprites = new Sprite[4];
		for (int i = 0; i<sprites.length; ++i) {
			sprites[i] = new RPGSprite("portal", 1f, 4, this, new RegionOfInterest(i*50, 0, 50, 200));
		}
		animation = new Animation(ANIMATION_DURATION, sprites, true);
	}
	
    @Override
    public void update(float deltaTime) {
    	super.update(deltaTime);
        animation.update(deltaTime);
    }
	
	
	/**
	 * Activate portal
	 */
	protected void activate() {
		setSignal(Logic.TRUE);
	}
	
	/**
	 * Desactivate portal
	 */
	protected void desactivate() {
		setSignal(Logic.FALSE);
	}
	
	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
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
