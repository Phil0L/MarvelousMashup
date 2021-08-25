/**
 *  Message-Class, which is sent at the beginning of a message from the server to the client who has sent
 *  the request which is invalid
 *  The request was NOT approved by the server
 *
 *  @author Sarah Engele
 *
 */

public class Nack : Message {


    /**
     *
     * the constructor of the Nack-Class.
     * Creates a Nack-MessageObject.
     *
     * @author Sarah Engele
     *
     */

    public Nack() : base(EventType.Nack)
    {
        
    }
}
