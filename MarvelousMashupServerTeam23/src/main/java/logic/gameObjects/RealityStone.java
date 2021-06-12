package logic.gameObjects;

import communication.messages.events.character.UseInfinityStoneEvent;
import logic.model.Model;

import java.awt.*;

/**
 * Class for a RealityStone
 * @author Luka Stoehr
 */
public class RealityStone extends InfinityStone{
    /**
     * Constructor for the space stone
     * @author Luka Stoehr
     * @param coolDown in rounds
     * @param model Model for this match
     */
    public RealityStone(int coolDown, Model model){
        super(model, 2); //ID as specified in network standard document
        this.coolDown = coolDown;
        this.color = Color.RED;
        this.everUsed = false;
    }

    /**
     * Infinteraction for the RealityStone. A Hero with this stone can create or destroy
     * rocks on neighbouring fields, depending if there is already a rock or if the field
     * is empty.
     * @param hero The hero that performs the Infinteraction
     * @param position Target field where rock will be created or destroyed.
     * @return Whether infinteraction was successful
     */
    @Override
    boolean infinteraction(Hero hero, Position position) {
        if (this.everUsed == true && (this.lastUsedInRound + this.coolDown) >= model.round) {
            return false;
        }
        Placeable target = model.field[position.getX()][position.getY()];
        if (hero.isNeighbour(position) &&  target == null) {
            int[] newPos = {position.getX(), position.getY()};
            model.controller.eventList.add(
                    //Network standard specifies that targetEntity=originEntity when RealityStone is used to create rock
                    new UseInfinityStoneEvent(hero.getIDs(), hero.getIDs(), hero.getPosAsArray(), newPos, this.getIDs())
            );
            model.field[position.getX()][position.getY()] = new Rock(model);
            this.everUsed = true;
            this.lastUsedInRound = model.round;
            return true;
        }else if(hero.isNeighbour(position) && target instanceof Rock){
            model.controller.eventList.add(
                    new UseInfinityStoneEvent(hero.getIDs(), target.getIDs(), hero.getPosAsArray(), target.getPosAsArray(), this.getIDs())
            );
            ((Rock) target).attacked(Integer.MAX_VALUE);
            this.everUsed = true;
            this.lastUsedInRound = model.round;
            return true;
        }else{
            return false;
        }
    }
}
