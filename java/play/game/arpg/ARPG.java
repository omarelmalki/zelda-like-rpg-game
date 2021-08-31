/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg;

import play.game.areagame.Area;
import play.game.areagame.actor.Orientation;
import play.game.arpg.actor.ARPGPlayer;
import play.game.arpg.area.Chateau;
import play.game.arpg.area.Ferme;
import play.game.arpg.area.Route;
import play.game.arpg.area.RouteChateau;
import play.game.arpg.area.RouteTemple;
import play.game.arpg.area.Temple;
import play.game.arpg.area.Village;
import play.game.rpg.RPG;
import play.game.rpg.actor.Player;
import play.io.FileSystem;
import play.math.DiscreteCoordinates;
import play.window.Window;

/**
 * Class ARPG. It extends from RPG and develops its features
 * When initializing the ARPGPlayer is added to the current area
 */
public class ARPG extends RPG {

	public final static float CAMERA_SCALE_FACTOR = 18.f;
	public final static float STEP = 0.05f;

	private final String[] areas = {"zelda/Ferme", "zelda/Village", "zelda/Route", "zelda/RouteChateau", "zelda/Chateau", 
			"zelda/RouteTemple", "zelda/Temple"};
	private final DiscreteCoordinates[] startingPositions = {new DiscreteCoordinates(6,10),
			new DiscreteCoordinates(6,10), new DiscreteCoordinates(10,9), 
			new DiscreteCoordinates(6,10), new DiscreteCoordinates(6,10), 
			new DiscreteCoordinates(5,5), new DiscreteCoordinates(5,5)};
	private int areaIndex;

	/**
	 * Add all the areas
	 */
	 private void createAreas(){
		 addArea(new Ferme());
		 addArea(new Village());
		 addArea(new Route());
		 addArea(new RouteChateau());
		 addArea(new Chateau());
		 addArea(new RouteTemple());
		 addArea(new Temple());
		 
	 }

	 @Override
	 protected void initPlayer(Player player){
		 super.initPlayer(player);
	 }

	 @Override
	 public boolean begin(Window window, FileSystem fileSystem) {

		 if (super.begin(window, fileSystem)) {

			 createAreas();
			 areaIndex = 2;
			 Area area = setCurrentArea(areas[areaIndex], true);
			 initPlayer(new ARPGPlayer(area, Orientation.DOWN, startingPositions[areaIndex]));

			 return true;

		 }
		 return false;
	 }

	 @Override
	 public void end() {
	 }

	 @Override
	 public String getTitle() {
		 return "The Legend of Homziz";
	 }


}
