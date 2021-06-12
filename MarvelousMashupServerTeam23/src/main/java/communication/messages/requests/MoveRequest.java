package communication.messages.requests;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.RequestType;

/**
 *  Message-Class, which is responsible to send MoveRequests to the server
 *  if the user wants his character to move to another field
 *
 *  @author Sarah Engele
 *
 */

public class MoveRequest extends Message {

    /**
     * The entity that wants to move
     */
    public IDs originEntity;
    /**
     * The position of the originEntity on the game field
     */
    public int[] originField;
    /**
     * The position on the game field where the originEntity wants to go next
     */
    public int[] targetField;

    /**
     *
     * the constructor of the MoveRequest-Class. Creates a MoveRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to move
     * @param originField The position of the originEntity on the game field
     * @param targetField The position on the game field where the originEntity wants to go next

     *
     */
    public MoveRequest(IDs originEntity, int[] originField, int[] targetField){
        super(RequestType.MoveRequest);
        this.originEntity = originEntity;
        this.originField = originField;
        this.targetField = targetField;
    }
}
