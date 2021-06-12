package communication.messages.events.game;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent to the client who took too much time to make his move. With this message, the server ends the
 * players/characters move (and moves on to the next character)
 *
 * @author Sarah Engele
 *
 */
public class TurnTimeoutEvent extends Message {


    /**
     * the constructor of the TurnTimeoutEvent-Class. Creates a TurnTimeoutEvent-MessageObject.
     *
     * @author Sarah Engele
     */
    public TurnTimeoutEvent(){
        super(EventType.TurnTimeoutEvent);
    }
}
