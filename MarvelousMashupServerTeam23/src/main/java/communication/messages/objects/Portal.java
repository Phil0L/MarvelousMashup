package communication.messages.objects;

import communication.messages.enums.EntityType;

/**
 * Class that represents a Portal in the Ingame messages.
 * @author Luka
 */
public class Portal extends Entities{


    /**
     * Constructor of Portal class, calls constructor of the Entities-class
     * to create a new Portal object
     *
     * @param ID         the specific ID of the entity
     * @param position   the position of the entity on the map
     * @author Sarah Engele
     * @author Luka Stoehr
     */
    public Portal(int ID, int[] position) {
        super(EntityType.Portal, ID, position);
    }
}
