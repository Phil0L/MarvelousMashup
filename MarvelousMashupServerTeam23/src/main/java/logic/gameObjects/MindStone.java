package logic.gameObjects;

import communication.messages.events.character.UseInfinityStoneEvent;
import logic.model.Model;

import java.awt.*;

/**
 * This class represents the MindStone.
 * @author Luka Stoehr
 */
public class MindStone extends InfinityStone{
    /**
     * Damage this InfinityStone causes.
     */
    private int mindStoneDamage;

    /**
     * Constructor for the MindStone.
     * @author Luka Stoehr
     * @param coolDown CoolDown for this stone in rounds.
     * @param damage Damage of the MindStone
     * @param model Model for this match
     */
    public MindStone(int coolDown, int damage, Model model){
        super(model, 1); //ID as specified in network standard document
        this.coolDown = coolDown;
        this.color = Color.YELLOW;
        this.everUsed = false;
        this.mindStoneDamage = damage;
    }

    /**
     * Infinteraction of the MindStone, allows farAttack within the line of sight, but
     * without being limited by farAttackRange.
     * @param hero     The hero that performs the Infinteraction
     * @param position Position of the Hero that will be attacked.
     * @return Whether the infinteraction was carried out successfully
     * @author Luka Stoehr
     */
    @Override
    boolean infinteraction(Hero hero, Position position) {
        if(this.everUsed == true && (this.lastUsedInRound + this.coolDown) >= model.round){
            return false;
        }

        if(hero.isNeighbour(position) ||
                !model.checkLineOfSight(position, hero.getPosition())){
            return false;
        }
        Placeable target = model.field[position.getX()][position.getY()];
        if(target instanceof Attackable){
            model.controller.eventList.add(
                    new UseInfinityStoneEvent(hero.getIDs(), target.getIDs(), hero.getPosAsArray(), target.getPosAsArray(), this.getIDs())
            );
            int doneDamage = ((Attackable) target).attacked(this.mindStoneDamage);
            if( doneDamage > 0){
                hero.checkKnockedOpponentHeroAndDamage((Attackable) target, doneDamage);
            }
            this.everUsed = true;
            this.lastUsedInRound = model.round;
            return true;
        }
        return false;
    }
}
