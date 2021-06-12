package communication.messages.events.game;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message lets the client know that the servers next move will be disconnecting the client
 *
 * @author Sarah Engele
 */
public class TimeoutEvent extends Message {

    /**
     * might be something like "You have been disconnected."
     */
    public String message;

    /**
     * the constructor of the TimeoutEvent-Class. Creates a TimeoutEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param message might be something like "You have been disconnected."
     *
     */
    public TimeoutEvent(String message){
        super(EventType.TimeoutEvent);
        this.message = message;
    }
}
