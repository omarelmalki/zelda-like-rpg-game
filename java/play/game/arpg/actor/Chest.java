/*
 *	Author:      Omar El Malki
 *	Date:        7 Dec 2019
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
import play.math.RegionOfInterest;
import play.signal.logic.Logic;
import play.window.Canvas;

public class Chest extends AreaEntity {
	

	// private final Logic signal; // set non final in 2019 project
    private Logic signal;
    private boolean isOpening;
    
    private Sprite open;
    private Sprite closed;
    private Sprite[] openingSprites;
    private Animation openingAnimation;
    
    private ARPGItem content;
    
	/// Animation duration in frame number
	private final static int ANIMATION_DURATION = 4;


    /**
     * Default Chest constructor
     * @param area (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param item (ARPGItem) item contained in the chest
     */
    public Chest(Area area, Orientation orientation, DiscreteCoordinates position, ARPGItem item) {
		super(area, orientation, position);
		content = item;
		signal = Logic.FALSE;
		isOpening = false;
		open = new RPGSprite("chest", 1.5f, 1.5f, this, new RegionOfInterest(0, 96, 32, 32));
		closed = new RPGSprite("chest", 1.5f, 1.5f, this, new RegionOfInterest(0, 0, 32, 32));
		openingSprites = new Sprite[4];
		for (int i = 0; i < openingSprites.length; ++i) {
			openingSprites[i] = new RPGSprite("chest", 1.5f, 1.5f, this, new RegionOfInterest(i*32, 0, 32, 32));
		}
		openingAnimation = new Animation(ANIMATION_DURATION, openingSprites, false);

	}
    
    @Override
    public void update(float deltaTime) {
    	super.update(deltaTime);
    	openingAnimation.update(deltaTime);
    	
    	if (openingAnimation.isCompleted()) {
    		isOpening = false;
    	}
    		

    }
    /// Chest extends AreaEntity

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
    	return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /// Chest implements Graphics

    @Override
    public void draw(Canvas canvas) {
    	if (!isOpen()) {
    		closed.draw(canvas);
    	} else if (isOpening) {
    		openingAnimation.draw(canvas);
    	} else {
    		open.draw(canvas);
    	}
    }
    /**
     * @return is Open (boolean) true if open, false if closed.
     */
    public boolean isOpen() {
    	return signal.isOn();
    }
    
    /**
     * Opens the chest
     */
    protected void open() {
    	if (!isOpen()) {
        	signal = Logic.TRUE;
        	isOpening = true;
    	}
    }
    
    /**
     * Getter for content of the chest
     * @return content (ARPGItem)
     */
    protected ARPGItem getContent() {
    	return content;
    }
 
    
  
    /// Door Implements Interactable

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable(){
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);
    }
}
