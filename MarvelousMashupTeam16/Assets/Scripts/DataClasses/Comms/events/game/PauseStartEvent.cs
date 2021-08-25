/**
 * This message is sent to all clients to inform them about the beginning of a pause
 *
 * @author Sarah Engele
 *
 */
public class PauseStartEvent : Message {


    /**
     * the constructor of the PauseStartEvent-Class. Creates a PauseStartEvent-MessageObject.
     *
     * @author Sarah Engele
     */
    public PauseStartEvent() : base(EventType.PauseStartEvent)
    {
       
    }
}
