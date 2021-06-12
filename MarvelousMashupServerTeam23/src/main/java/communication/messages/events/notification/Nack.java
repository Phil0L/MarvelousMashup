package communication.messages.events.notification;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 *  Message-Class, which is sent at the beginning of a message from the server to the client who has sent
 *  the request which is invalid
 *  The request was NOT approved by the server
 *
 *  @author Sarah Engele
 *
 */

public class Nack extends Message {


    /**
     *
     * the constructor of the Nack-Class.
     * Creates a Nack-MessageObject.
     *
     * @author Sarah Engele
     *
     */

    public Nack(){
        super(EventType.Nack);
    }
}
