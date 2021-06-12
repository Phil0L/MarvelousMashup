package logic.gameObjects;

import communication.messages.events.character.UseInfinityStoneEvent;
import logic.model.Model;

import java.awt.*;

/**
 * This class represents the InfinityStone
 * @author Luka Stoehr
 */
public class TimeStone extends InfinityStone{
    /**
     * Constructor for the TimeStone.
     * @author Luka Stoehr
     * @param coolDown CoolDown for this stone in rounds.
     * @param model Model for this match
     */
    public TimeStone(int coolDown, Model model){
        super(model, 4); //ID as specified in network standard document
        this.coolDown = coolDown;
        this.color = Color.GREEN;
        this.everUsed = false;
    }

    /**
     * Infinteraction of the TimeStone. Sets MPs and APs to initial value
     * @param hero     The hero that performs the Infinteraction
     * @param position Not necessary here, set to null.
     * @return Whether the infinteraction was carried out successfully
     * @author Luka Stoehr
     */
    @Override
    boolean infinteraction(Hero hero, Position position) {
        if(this.everUsed == true && (this.lastUsedInRound + this.coolDown) >= model.round){
            return false;
        }
        model.controller.eventList.add(
                // The network standard specifies that target=origin for infinteraction of timeStone
                new UseInfinityStoneEvent(hero.getIDs(), hero.getIDs(), hero.getPosAsArray(), hero.getPosAsArray(), this.getIDs())
        );
        hero.movementPoints = hero.maxMovementPoints;
        hero.actionPoints = hero.maxActionPoints;

        this.everUsed = true;
        this.lastUsedInRound = model.round;
        return true;
    }
}
