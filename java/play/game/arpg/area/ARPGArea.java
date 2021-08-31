/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.area;

import play.game.areagame.Area;
import play.game.arpg.ARPG;
import play.game.arpg.ARPGBehavior;
import play.io.FileSystem;
// import ch.epfl.cs107.play.math.DiscreteCoordinates;
import play.window.Window;

/**
 * ARPGArea is a "Part" of the AreaGame. An ARPGArea is made of an ARPGBehavior, and a List of Actors
 */
public abstract class ARPGArea extends Area {
	
	private ARPGBehavior behavior;

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();

    /// ARPGArea extends Area

    @Override
    public final float getCameraScaleFactor() {
        return ARPG.CAMERA_SCALE_FACTOR;
    }

    /// ARPGArea implements Playable

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
        	behavior = new ARPGBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
    
}
