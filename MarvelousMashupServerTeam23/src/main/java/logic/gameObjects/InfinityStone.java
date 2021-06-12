package logic.gameObjects;

import communication.messages.enums.EntityID;
import communication.messages.events.character.MoveEvent;
import logic.model.Model;

import java.awt.*;

/**
 * Abstract class for the InfinityStones
 * @author Luka Stoehr
 */
public abstract class InfinityStone extends Placeable{
    /**
     * How many rounds this InfinityStone needs to cool down, as specified in config file.
     */
    public int coolDown;
    /**
     * Round in which this InfinityStone was last used in.
     */
    public int lastUsedInRound;
    /**
     * True if this InfinityStone was ever used.
     */
    public boolean everUsed;
    /**
     * Color of this InfinityStone.
     */
    public Color color;

    /**
     * Constructor for InfinityStone
     * @author Luka Stoehr
     * @param model Model for this match
     * @param ID The unique ID for this stone
     */
    public InfinityStone(Model model, int ID){
        super(EntityID.InfinityStones, ID);
        this.model = model;
    }

    /**
     * Abstract method for infinteractions. Has to be implemented by each InfinityStone.
     * @author Luka Stoehr
     * @param hero The hero that performs the Infinteraction
     * @param position Necessary for some infinteractions, depending on the used InfinityStone.
     * @return Whether the infinteraction was carried out successfully
     */
    abstract boolean infinteraction(Hero hero, Position position);

    /**
     * This method is used to move an InfinityStone on the field.
     * @author Luka Stoehr
     * @param newPosition where the object is moved to, must be a neighbouring field
     * @return Whether moving the Stone was successful
     */
    @Override
    public boolean move(Position newPosition) {
        Position oldPos = this.getPosition();
        if(oldPos == null || !this.isNeighbour(newPosition)){
            return false;       // Placeable was not found and can therefore not be moved, use place() instead
        }

        Placeable target = model.field[newPosition.getX()][newPosition.getY()];
        if(target == null) {
            // target field is free -> Placeable can be moved
            model.field[oldPos.getX()][oldPos.getY()] = null;
            model.field[newPosition.getX()][newPosition.getY()] = this;
            model.controller.eventList.add(
                new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
            );
            return true;
        }else{
            return false;
        }

    }
}

