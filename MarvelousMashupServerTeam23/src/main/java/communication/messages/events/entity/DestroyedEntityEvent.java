package communication.messages.events.entity;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent when an entity is removed from the map. This does not mean that the entity doesn't exist
 * anymore, but the entity might be placed in the inventory of a character if it is an Infinity Stone
 *
 * @author Sarah Engele
 *
 */

public class DestroyedEntityEvent extends Message {

    public int[] targetField;
    public IDs targetEntity;

    /**
     * the constructor of the DestroyEntityEvent-Class. Creates a DestroyEntityEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetField The field on the map where the entity was placed before being destroyed
     * @param targetEntity The entity that is about to be destroyed
     */
    public DestroyedEntityEvent(int[] targetField, IDs targetEntity){
        super(EventType.DestroyedEntityEvent);
        this.targetEntity = targetEntity;
        this.targetField = targetField;

    }


}
