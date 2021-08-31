package play.game.actor;

import play.window.Canvas;


/**
 * Represents a drawable element.
 */
public interface Graphics {

    /**
     * Renders itself on specified canvas.
     * @param canvas target, not null
     */
    void draw(Canvas canvas);
}
