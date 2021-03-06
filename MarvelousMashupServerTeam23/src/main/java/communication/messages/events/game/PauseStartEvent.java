package communication.messages.events.game;


import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent to all clients to inform them about the beginning of a pause
 *
 * @author Sarah Engele
 *
 */
public class PauseStartEvent extends Message {


    /**
     * the constructor of the PauseStartEvent-Class. Creates a PauseStartEvent-MessageObject.
     *
     * @author Sarah Engele
     */
    public PauseStartEvent(){
       super(EventType.PauseStartEvent);
    }
}
