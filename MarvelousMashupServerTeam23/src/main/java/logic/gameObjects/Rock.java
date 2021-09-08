package logic.gameObjects;

import communication.messages.enums.EntityID;
import communication.messages.events.entity.DestroyedEntityEvent;
import communication.messages.events.entity.TakenDamageEvent;
import communication.messages.objects.Entities;
import logic.model.Model;

/**
 * This class represents a rock.
 * @author Luka Stoehr
 */
public class Rock extends Placeable implements Attackable{
    /**
     * Current health points of this Rock
     */
    public int healthPoints;
    /**
     * Counts how many rocks exist in total. This is used to set the IDs of the Rocks uniquely when they are created.
     */
    public static int rockCount;

    /**
     * Constructor for class Rock. Rocks always have 100 HP at the beginning.
     * @author Luka Stoehr
     * @param model Model for this match
     */
    public Rock(Model model){
        super(EntityID.Rocks, rockCount);
        this.rockCount++;   // Increment rockCount for next rock
        this.healthPoints = 100;
        this.model = model;
    }

    /**
     * Attacks this rock and decreases its healthPoints
     * @author Luka Stoehr
     * @param damage How much damage the attack theoretically causes
     * @return How much damage actually was caused
     */
    @Override
    public int attacked(int damage) {
        if(this.healthPoints > damage){
            model.controller.eventList.add(
                    new TakenDamageEvent(this.getIDs(), this.getPosAsArray(), damage)
            );
           this.healthPoints -= damage;
           return damage;
        }else{
            int doneDamage = this.healthPoints;
            this.healthPoints = 0;
            model.controller.eventList.add(
                    new DestroyedEntityEvent(this.getPosAsArray(), this.getIDs())
            );
            Position position = this.getPosition();
            if(position!=null) model.field[position.getX()][position.getY()] = null;       // Rock is destroyed
            return doneDamage;
        }
    }

    /**
     * This method always returns false, as rocks cannot be moved.
     * @author Luka Stoehr
     * @param newPosition where the object is moved to
     * @return false
     */
    @Override
    public boolean move(Position newPosition) {
        return false;
    }

    /**
     * Translates this Placeable object to an Entities object that is needed for some
     * network messages.
     *
     * @return Entity object
     * @author Luka Stoehr
     */
    @Override
    public Entities toEntity() {
        Rock r = this;
        return new communication.messages.objects.Rock(r.ID, r.healthPoints, r.getPosAsArray());
    }
}
