package logic.gameObjects;

import communication.messages.events.character.UseInfinityStoneEvent;
import logic.model.Model;

import java.awt.*;

/**
 * This class represents the PowerStone
 * @author Luka Stoehr
 */
public class PowerStone extends InfinityStone{
    /**
     * Constructor for the PowerStone.
     * @author Luka Stoehr
     * @param coolDown CoolDown for this stone in rounds.
     * @param model Model for this match
     */
    public PowerStone(int coolDown, Model model){
        super(model, 3);    //ID as specified in network standard document
        this.coolDown = coolDown;
        this.color = new Color(148,0,211);
        this.everUsed = false;
    }

    /**
     * Infinteraction of the PowerStone. Doubles the nearAttackDamage for one attack,
     * but reduces the heroes healthPoints by 10%, but not under 1.
     * @author Luka Stoehr
     * @param hero The hero that performs the Infinteraction
     * @param position Target of the attack.
     * @return
     */
    @Override
    boolean infinteraction(Hero hero, Position position) {
        if(this.everUsed == true && (this.lastUsedInRound + this.coolDown) >= model.round){
            return false;
        }
        Placeable target = model.field[position.getX()][position.getY()];
        if(hero.isNeighbour(position) && target instanceof Hero){
            model.controller.eventList.add(
                new UseInfinityStoneEvent(hero.getIDs(), target.getIDs(), hero.getPosAsArray(), target.getPosAsArray(), this.getIDs())
            );
            int doneDamage = ((Hero) target).attacked(2*hero.nearAttackDamage);
            if(doneDamage > 0){
                hero.checkKnockedOpponentHeroAndDamage((Attackable) target, doneDamage);
            }

            //Damage that happens to the one using the PowerStone
            double currentHP = hero.getHealthPoints();
            double ownDamage = hero.maxHealthPoints * 0.1;

            if(currentHP < ownDamage + 1){
                ownDamage = currentHP - 1;
            }
            hero.attacked((int) ownDamage);   //Round ownDamage down to next int using typecast

            this.everUsed = true;
            this.lastUsedInRound = model.round;
            return true;
        }else{
            return false;
        }
    }
}
