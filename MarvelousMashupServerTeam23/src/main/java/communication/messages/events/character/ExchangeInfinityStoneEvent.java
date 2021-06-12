package communication.messages.events.character;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EventType;
import communication.messages.enums.RequestType;

/**
 *  Message-Class, which is responsible to send ExchangeInfinityStoneEvents to the clients
 *  if the user wants one of his characters to exchange an Infinity Stone with another character
 *   and this was approved by the server to do so
 *
 *  @author Sarah Engele
 *
 */

public class ExchangeInfinityStoneEvent extends Message {

    /**
     * The entity that owns an Infinity Stone and wants to hand it over to the targetEntity
     */
    public IDs originEntity;
    /**
     * The entity that will receive the Infinity Stone
     */
    public IDs targetEntity;
    /**
     * The position of the originEntity on the game field
     */
    public int[] originField;
    /**
     * The position of the targetEntity on the game field
     */
    public int[] targetField;
    /**
     * The Infinity Stone that is about to be exchanged
     */
    public IDs stoneType;

    /**
     *
     * the constructor of the ExchangeInfinityStoneEvent-Class.
     * Creates a ExchangeInfinityStoneEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that owns an Infinity Stone and wants to hand
     *                     it over to the targetEntity
     * @param targetEntity The entity that will receive the Infinity Stone
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param stoneType The Infinity Stone that is about to be exchanged
     *
     */

    public ExchangeInfinityStoneEvent(IDs originEntity, IDs targetEntity, int[] originField,
                                        int[] targetField, IDs stoneType){
        super(EventType.ExchangeInfinityStoneEvent);
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.stoneType = stoneType;
    }

}
