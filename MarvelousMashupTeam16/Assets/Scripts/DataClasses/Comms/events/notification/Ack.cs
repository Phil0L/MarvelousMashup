/**
 *  Message-Class, which is sent at the beginning of a Message[] inside the MessageStructure from the server to the client who has sent
 *  the request which was processed and accepted by the server
 *
 *  @author Sarah Engele
 *
 */

public class Ack : Message {


    /**
     *
     * the constructor of the Ack-Class.
     * Creates a Ack-MessageObject.
     *
     * @author Sarah Engele
     *
     */

    public Ack() : base(EventType.Ack)
    {
        
    }
}
