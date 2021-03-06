package communication.messages.events.game;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent to all clients at the beginning of a new move and informs the clients about the current amount
 * of moves and the next active character
 *
 * @author Sarah Engele
 *
 */

public class TurnEvent extends Message {

    /**
     * The amount of moves during this round
     */
    public int turnCount;
    /**
     * The next active character that is allowed to make a move
     */
    public IDs nextCharacter;

    /**
     * the constructor of the TurnEvent-Class. Creates a TurnEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param turnCount The amount of moves during this round
     * @param nextCharacter The next active character that is allowed to make a move
     */

    public TurnEvent(int turnCount, IDs nextCharacter){
        super(EventType.TurnEvent);
        this.turnCount = turnCount;
        this.nextCharacter = nextCharacter;
    }
}
