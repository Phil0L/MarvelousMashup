package logic.gameObjects;

import communication.messages.events.character.UseInfinityStoneEvent;
import logic.model.Model;

import java.awt.*;

/**
 * This class represents the SoulStone
 * @author Luka Stoehr
 */
public class SoulStone extends InfinityStone{
    /**
     * Constructor for the SoulStone.
     * @author Luka Stoehr
     * @param coolDown CoolDown for this stone in rounds.
     * @param model Model for this match
     */
    public SoulStone(int coolDown, Model model){
        super(model, 5); //ID as specified in network standard document
        this.coolDown = coolDown;
        this.color = Color.ORANGE;
        this.everUsed = false;
    }

    /**
     * Infinteraction for the SoulStone. Heals a knocked out character on a nearby field.
     * @author Luka Stoehr
     * @param hero     The hero that performs the Infinteraction
     * @param position Position of the character to heal.
     * @return Whether the infinteraction was carried out successfully
     */
    @Override
    boolean infinteraction(Hero hero, Position position) {
        if(this.everUsed == true && (this.lastUsedInRound + this.coolDown) >= model.round){
            return false;
        }
        Placeable target = model.field[position.getX()][position.getY()];
        if(hero.isNeighbour(position) && target instanceof Hero && ((Hero) target).getHealthPoints() == 0){
            model.controller.eventList.add(
                    new UseInfinityStoneEvent(hero.getIDs(), target.getIDs(), hero.getPosAsArray(), target.getPosAsArray(), this.getIDs())
            );
            ((Hero) target).heal();
            this.everUsed = true;
            this.lastUsedInRound = model.round;
            return true;
        }else{
            return false;
        }
    }
}
