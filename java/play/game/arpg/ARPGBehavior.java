/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg;

import play.game.areagame.AreaBehavior;
import play.game.areagame.actor.Interactable;
import play.game.areagame.handler.AreaInteractionVisitor;
import play.game.arpg.actor.FlyableEntity;
// import ch.epfl.cs107.play.math.DiscreteCoordinates;
import play.window.Window;


/**
 * ARPGBehavior is a basically a map made of ARPGCells. Those cells are used for the game behavior
 */
public class ARPGBehavior extends AreaBehavior {
	 
	public enum ARPGCellType{
		NULL(0, false, false),
		WALL(-16777216, false, false),
		IMPASSABLE(-8750470, false, true),
		INTERACT(-256, true, true),
		DOOR(-195580, true, true),
		WALKABLE(-1, true, true);

		final int type;
		final boolean isWalkable;
		final boolean IS_FLYABLE;

		/**
		 * Constructor for ARPGCellType
		 * @param type (int) Cell type.
		 * @param isWalkable (boolean) true if walkable.
		 * @param isFlyable (boolean) true if flyable.
		 */
		ARPGCellType(int type, boolean isWalkable, boolean isFlyable){
			this.type = type;
			this.isWalkable = isWalkable;
			this.IS_FLYABLE = isFlyable;
		}

		/**
		 * Returns the cell type from the int value
		 * @param type (int) Cell type
		 * @return cellType (ARPGCellType) Cell type from enum
		 */
		public static ARPGCellType toType(int type){
			for(ARPGCellType ict : ARPGCellType.values()){
				if(ict.type == type)
					return ict;
			}
			return NULL;
		}
	}

	/**
	 * Default ARPGBehavior Constructor
	 * @param window (Window), not null
	 * @param name (String): Name of the Behavior, not null
	 */
	public ARPGBehavior(Window window, String name){
		super(window, name);
		int height = getHeight();
		int width = getWidth();
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width ; x++) {
				ARPGCellType cellType = ARPGCellType.toType(getRGB(height-1-y, x));
				setCell(x,y, new ARPGCell(x,y,cellType));
			}
		}
	}
	
	/**
	 * Cell adapted to the ARPG game
	 */
	public class ARPGCell extends AreaBehavior.Cell {
		/// Type of the cell following the enum
		private final ARPGCellType type;
		
		/**
		 * Default ARPGCell Constructor
		 * @param x (int): x coordinate of the cell
		 * @param y (int): y coordinate of the cell
		 * @param type (ARPGCellType), not null
		 */
		private ARPGCell(int x, int y, ARPGCellType type){
			super(x, y);
			this.type = type;
		}
		
		
		@Override
		protected boolean canLeave(Interactable entity) {
			return true;
		}
 
		
		@Override
		protected boolean canEnter(Interactable entity) {
//			if (type.isWalkable) {
//				if (hasNonTraversableContent() && entity.takeCellSpace()) {
//					return false;
//				}
//				return true;
//			}
//			if (type.IS_FLYABLE) {
//				if(entity instanceof FlyableEntity) {
//					if (hasNonTraversableContent() && entity.takeCellSpace()) {
//						return false;						
//					}
//					return ((FlyableEntity)entity).CanFly();
//				}
//			}
//			return false;
			if (entity instanceof FlyableEntity) {
				return type.IS_FLYABLE;
			}
			return (type.isWalkable && (!hasNonTraversableContent()  || !entity.takeCellSpace()));
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
		}

	}
}
