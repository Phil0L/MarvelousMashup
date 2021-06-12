package communication.messages.events.notification;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 *  Message-Class, which is sent at the beginning of a Message[] inside the MessageStructure from the server to the client who has sent
 *  the request which was processed and accepted by the server
 *
 *  @author Sarah Engele
 *
 */

public class Ack extends Message {


    /**
     *
     * the constructor of the Ack-Class.
     * Creates a Ack-MessageObject.
     *
     * @author Sarah Engele
     *
     */

    public Ack(){
        super(EventType.Ack);
    }
}
