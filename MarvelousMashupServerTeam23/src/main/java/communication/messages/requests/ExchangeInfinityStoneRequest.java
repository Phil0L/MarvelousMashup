package communication.messages.requests;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.RequestType;

/**
 *  Message-Class, which is responsible to send ExchangeInfinityStoneRequests to the server
 *  if the user wants one of his characters to exchange an Infinity Stone with another character
 *  of his team
 *
 *  @author Sarah Engele
 *
 */

public class ExchangeInfinityStoneRequest extends Message {

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
     * the constructor of the ExchangeInfinityStoneRequest-Class.
     * Creates a ExchangeInfinityStoneRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that owns an Infinity Stone and wants to hand it over to the targetEntity
     * @param targetEntity The entity that will receive the Infinity Stone
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param stoneType The Infinity Stone that is about to be exchanged
     *
     */

    public ExchangeInfinityStoneRequest(IDs originEntity, IDs targetEntity, int[] originField,
                                        int[] targetField, IDs stoneType){
        super(RequestType.ExchangeInfinityStoneRequest);
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.stoneType = stoneType;
    }

}
