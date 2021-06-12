package communication.messages.events.entity;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent when a character consumes MP (e.g. character moves)
 *
 * @author Sarah Engele
 *
 */

public class ConsumedMPEvent extends Message {

    public IDs targetEntity;
    public int[] targetField;
    public int amount;


    /**
     * the constructor of the ConsumedAPEvent-Class. Creates a ConsumedAPEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetEntity The Entity that is about to consume its MP
     * @param targetField The field where the targetEntity is placed on the map
     * @param amount The amount of movement points the targetEntity consumes
     *
     */

    public ConsumedMPEvent(IDs targetEntity, int[] targetField, int amount){
        super(EventType.ConsumedMPEvent);
        this.targetEntity = targetEntity;
        this.targetField = targetField;
        this.amount = amount;
    }

}
