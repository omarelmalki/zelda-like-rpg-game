/*
 *	Author:      Omar El Malki
 *	Date:        24 Nov 2019
 */

package play.game.arpg.handler;

import play.game.arpg.ARPGBehavior.ARPGCell;
import play.game.arpg.actor.ARPGPlayer;
import play.game.arpg.actor.Bomb;
import play.game.arpg.actor.Bridge;
import play.game.arpg.actor.CastleDoor;
import play.game.arpg.actor.CastleKey;
import play.game.arpg.actor.Chest;
import play.game.arpg.actor.Coin;
import play.game.arpg.actor.FireSpell;
import play.game.arpg.actor.FlameSkull;
import play.game.arpg.actor.Grass;
import play.game.arpg.actor.Heart;
import play.game.arpg.actor.Monster;
import play.game.arpg.actor.Portal;
import play.game.arpg.actor.Seller;
import play.game.arpg.actor.Staff;
import play.game.rpg.actor.CollectableAreaEntity;
import play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
	
    /// Add Interaction method with all non Abstract Interactable

    /**
     * Simulate and interaction between ARPG Interactor and an ARPGCell
     * @param cell (ARPGCell), not null
     */
    default void interactWith(ARPGCell cell){
        // by default the interaction is empty
    }
    
    /**
     * Simulate and interaction between ARPG Interactor and an FlameSkull
     * @param skull (FlameSkull), not null
     */
    default void interactWith (FlameSkull skull) {
	}
    
    /**
     * Simulate and interaction between ARPG Interactor and an ARPGPlayer
     * @param player (ARPGPlayer), not null
     */
    default void interactWith(ARPGPlayer player){
        // by default the interaction is empty
    }
    
    /**
     * Simulate and interaction between ARPG Interactor and an Grass
     * @param grass (Grass), not null
     */
    default void interactWith(Grass grass){
        // by default the interaction is empty
    }
    
    /**
     * Simulate and interaction between ARPG Interactor and a Collectable
     * @param collectable (CollectableAreaEntity), not null
     */
    default void interactWith(CollectableAreaEntity collectable){
    }
    
	/**
     * Simulate and interaction between ARPG Interactor and a Coin
     * @param coin (Coin), not null
     */
    default void interactWith(Coin coin){

    }
    
	/**
     * Simulate and interaction between ARPG Interactor and a Heart
     * @param heart (Heart), not null
     */
    default void interactWith(Heart heart){

    }
    
    /**
     * Simulate and interaction between ARPG Interactor and a CastleKey
     * @param key (CastleKey), not null
     */
     default void interactWith(CastleKey key){
 
    }
    
	/**
	* Simulate and interaction between ARPG Interactor and a CastleDoor
	* @param door (CastleDoor), not null
	*/
	default void interactWith(CastleDoor door){
		// by default the interaction is empty
	}
	
	/**
	* Simulate and interaction between ARPG Interactor and a Bomb
	* @param bomb (Bomb), not null
	*/
	default void interactWith(Bomb bomb){
		// by default the interaction is empty
	}
	
	/**
	* Simulate and interaction between ARPG Interactor and a Monster
	* @param monster (Monster), not null
	*/
	default void interactWith(Monster monster) {
    	// by default the interaction is empty
    }
	
	/**
	* Simulate and interaction between ARPG Interactor and a Fire Spell
	* @param fire (FireSpell), not null
	*/
	default void interactWith(FireSpell fire) {
    	// by default the interaction is empty
    }
	
    /**
     * Simulate and interaction between ARPG Interactor and a seller
     * @param cell (ARPGCell), not null
     */
    default void interactWith(Seller seller){
        // by default the interaction is empty
    }
    
    /**
     * Simulate and interaction between ARPG Interactor and a portal
     * @param portal (Portal), not null
     */
    default void interactWith(Portal portal){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between ARPG Interactor and a chest
     * @param chest (Chest), not null
     */
    default void interactWith(Chest chest){
        // by default the interaction is empty
    }
    
    /**
     * Simulate and interaction between ARPG Interactor and a staff
     * @param staff (Staff), not null
     */
    default void interactWith(Staff staff){
    	
    }
    
    /**
     * Simulate and interaction between ARPG Interactor and an orb
     * @param orb (Bridge.Orb), not null
     */
    default void interactWith(Bridge.Orb orb){
        // by default the interaction is empty
    }
    
}
