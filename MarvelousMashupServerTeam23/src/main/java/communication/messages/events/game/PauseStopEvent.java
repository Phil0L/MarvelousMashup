package communication.messages.events.game;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent to all clients to inform them about the end of a pause
 *
 * @author Sarah Engele
 *
 */
public class PauseStopEvent extends Message {


    /**
     * the constructor of the PauseStopEvent-Class. Creates a PauseStopEvent-MessageObject.
     *
     * @author Sarah Engele
     */
    public PauseStopEvent(){
       super(EventType.PauseStopEvent);
    }
}

