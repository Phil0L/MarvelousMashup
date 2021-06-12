package logic.gameObjects;

import communication.messages.events.character.UseInfinityStoneEvent;
import logic.model.Model;

import java.awt.*;

/**
 * Represents the SpaceStone
 * @author Luka Stoehr
 */
public class SpaceStone extends InfinityStone{
   /**
     * Constructor for the SpaceStone.
     * @author Luka Stoehr
     * @param coolDown CoolDown for this stone in rounds.
     * @param model Model for this match
     */
    public SpaceStone(int coolDown, Model model){
        super(model, 0); //ID as specified in network standard document
        this.coolDown = coolDown;
        this.color = Color.BLUE;
        this.everUsed = false;
    }

    /**
     * A Hero can move to every free position on the whole field with this infinteraction.
     * @author Luka Stoehr
     * @param hero The hero that performs the Infinteraction
     * @param position Field where the hero wants to move to.
     * @return Whether this was successful.
     */
    @Override
    boolean infinteraction(Hero hero, Position position) {
        if(this.everUsed == true && (this.lastUsedInRound + this.coolDown) >= model.round){
            return false;
        }
        if(model.field[position.getX()][position.getY()] == null){
            Position oldPos = hero.getPosition();
            int[] oldPosArr = {oldPos.getX(), oldPos.getY()};
            int[] newPosArr = {position.getX(), position.getY()};
            model.controller.eventList.add(
                    // Network standard document specifies that originEntity=targetEntity for the infinteraction of the Space Stone
                    new UseInfinityStoneEvent(hero.getIDs(), hero.getIDs(), oldPosArr, newPosArr, this.getIDs())
            );
            model.field[oldPos.getX()][oldPos.getY()] = null;
            model.field[position.getX()][position.getY()] = hero;
            this.everUsed = true;
            this.lastUsedInRound = model.round;
            return true;
        }else{
            return false;
        }
    }
}
