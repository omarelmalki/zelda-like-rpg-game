package play.game.areagame.actor;

import play.game.actor.Entity;
import play.game.actor.ImageGraphics;
import play.game.areagame.Area;
import play.game.areagame.io.ResourcePath;
import play.math.DiscreteCoordinates;
import play.math.RegionOfInterest;
import play.math.Vector;
import play.window.Canvas;


public class Background extends Entity {

    /// Sprite of the actor
    private final ImageGraphics sprite;

    /**
     * Default Background Constructor
     * by default the Background image is using the area title as file name
     * @param area (Area): ownerArea. Not null
     */
    public Background(Area area) {
        super(DiscreteCoordinates.ORIGIN.toVector());
        sprite = new ImageGraphics(ResourcePath.getBackgrounds(area.getTitle()), area.getWidth(), area.getHeight(), null, Vector.ZERO, 1.0f, -Float.MAX_VALUE);
        sprite.setParent(this);
    }

    /**
     * Extended Background Constructor
     * by default the Background image is using the area title as file name
     * @param area (Area): ownerArea. Not null
     * @param region (RegionOfInterest): region of interest in the image for the background, may be null
     */
    public Background(Area area, RegionOfInterest region) {
        super(DiscreteCoordinates.ORIGIN.toVector());
        sprite = new ImageGraphics(ResourcePath.getBackgrounds(area.getTitle()), area.getWidth(), area.getHeight(), region, Vector.ZERO, 1.0f, -Float.MAX_VALUE);
        sprite.setParent(this);
    }

    /**
     * Extended Background Constructor
     * @param area (Area): ownerArea. Not null
     * @param region (RegionOfInterest): region of interest in the image for the background, may be null
     * @param name (String): Background file name (i.e only the name, with neither path, nor file extension). Not null
     */
    public Background(Area area, RegionOfInterest region, String name) {
        super(DiscreteCoordinates.ORIGIN.toVector());
        sprite = new ImageGraphics(ResourcePath.getBackgrounds(name), area.getWidth(), area.getHeight(), region, Vector.ZERO, 1.0f, -Float.MAX_VALUE);
        sprite.setParent(this);
    }

    /**
     * Alternative Background Constructor
     * @param name (String): Background file name (i.e only the name, with neither path, nor file extension). Not null
     * @param width (int): of the desired background
     * @param height (int): of the desired background
     * @param region (RegionOfInterest): region of interest in the image for the background. May be null
     */
    public Background(String name, int width, int height, RegionOfInterest region) {
        super(DiscreteCoordinates.ORIGIN.toVector());
        sprite = new ImageGraphics(ResourcePath.getBackgrounds(name), width, height, region, Vector.ZERO, 1.0f, -Float.MAX_VALUE);
        sprite.setParent(this);
    }

    /// Background implements Graphics

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

}
