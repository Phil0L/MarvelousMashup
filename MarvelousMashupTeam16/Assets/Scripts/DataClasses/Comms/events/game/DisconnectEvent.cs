/**
 * This message lets the client know that the servers next move will be disconnecting the client. This message is the
 * regular way e.g. in case of the end of a game (WinEvent)
 *
 * @author Sarah Engele
 */
public class DisconnectEvent : Message {


    /**
     * the constructor of the Disconnect-Class. Creates a Disconnect-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public DisconnectEvent() : base(EventType.DisconnectEvent)
    {
        
    }
}