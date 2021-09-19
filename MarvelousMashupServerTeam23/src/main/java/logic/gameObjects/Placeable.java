package logic.gameObjects;

import communication.messages.IDs;
import communication.messages.enums.EntityID;
import communication.messages.enums.EntityType;
import communication.messages.events.entity.SpawnEntityEvent;
import communication.messages.objects.InGameCharacter;
import communication.messages.objects.Entities;
import communication.messages.objects.InGameCharacter;
import communication.messages.objects.NPC;
import logic.model.Model;

/**
 * Interface for all objects that can be placed on a field.
 * @author Luka Stoehr
 */
public abstract class Placeable {
    /**
     * Model of this match.
     */
    public Model model;
    /**
     * EntityID of this Placeable. Placeables are Entities in the Network Standard document.
     */
    public EntityID entityID;
    /**
     * ID of this Placeable.
     */
    public int ID;

    /**
     * Constructor for Placeable
     * @author Luka Stoehr
     * @param entityID EntityID as specified in Network Standard
     * @param ID ID as specified in Network Standard
     */
    public Placeable(EntityID entityID, int ID){
        this.entityID = entityID;
        this.ID = ID;
    }

    /**
     * Called when an object is to be placed on the field. Placeable can only be placed on empty field.
     * @author Luka Stoehr
     * @param position where object should be placed
     * @return Whether placing was successful
     */
    public boolean place(Position position){
        if(model.isFree(position)){
            // Create the right entity for the SpawnEntityEvent
            model.field[position.getX()][position.getY()] = this;
            Entities entity = this.toEntity();
            model.controller.eventList.add(new SpawnEntityEvent(entity));
            return true;
        }else{
            return false;
        }
    }

    /**
     * Called when an object should be moved on the field. As Stones and characters
     * behave differently, this is abstract. Currently a placeable can only be moved
     * to a neighbouring field with this method. For several steps, this method needs to be called
     * several times.
     * @author Luka Stoehr
     * @param newPosition where the object is moved to, must be a neighbouring field
     * @return Whether moving the object was successful
     */
    public abstract boolean move(Position newPosition);

    /**
     * Getter for the position of this Placeable
     * @author Luka Stoehr
     * @return current Position of this object or null if not found
     */
    public Position getPosition(){
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                if(model.field[x][y] != null && model.field[x][y].equals(this)){
                    return new Position(x,y);
                }
            }
        }
        return null;
    }

    /**
     * Getter for the position of this Placeable, but returns the Position as int[] array,
     * as needed in the Ingame Messages.
     * @author Luka Stoehr
     * @return current Position of this object or empty array (new int[0]) if Placeable is currently not on map
     */
    public int[] getPosAsArray(){
        int[] pos = new int[2];
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                if(model.field[x][y] != null && model.field[x][y].equals(this)){
                    pos[0] = x;
                    pos[1] = y;
                    return pos;
                }
            }
        }
        // If Placeable is not found return an empty array
        return new int[0];
    }

    /**
     * Checks if a Position is on a neighbouring field of this Placeable.
     * @author Luka Stoehr
     * @param otherPos The other field, to which the position is compared.
     * @return true if position is a neighbour of this placeable
     */
    public boolean isNeighbour(Position otherPos){
        Position a = otherPos;
        Position b = this.getPosition();
        if(b == null) return false;     //This Placeable was not placed yet
        int ax = a.getX();
        int ay = a.getY();
        int bx = b.getX();
        int by = b.getY();

        if(ax==bx && ay==by-1 || ax==bx && ay==by+1 || ax==bx+1 && ay==by || ax==bx-1 && ay==by ||
            ax==bx+1 && ay==by+1 || ax==bx-1 && ay==by+1 || ax==bx+1 && ay==by-1 || ax==bx-1 && ay==by-1){
            return true;
        }else{
            return false;
        }

    }

    /**
     * Checks if this Placeable matches to the given IDs object used to identify entities
     * during the Ingame Phase.
     * The IDs object consists of a entityID and an ID.
     * @author Luka Stoehr
     * @param entityIDs IDs object to compare
     * @return True, if same entity
     */
    public boolean compareIDs(IDs entityIDs){
        if(entityIDs.ID == this.ID && entityIDs.entityID == this.entityID){
            return true;
        }
        return false;
    }

    /**
     * Returns the IDs object of this Placeable as specified in the network standard document.
     * @author Luka Stoehr
     * @return IDs
     */
    public IDs getIDs(){
        return new IDs(this.entityID, this.ID);
    }

    /**
     * Translates this Placeable object to an Entities object that is needed for some
     * network messages.
     * @author Luka Stoehr
     * @return Entity
     */
    public abstract Entities toEntity();
}
