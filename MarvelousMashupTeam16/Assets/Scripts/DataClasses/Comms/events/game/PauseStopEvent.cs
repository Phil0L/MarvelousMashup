/**
 * This message is sent to all clients to inform them about the end of a pause
 *
 * @author Sarah Engele
 *
 */
public class PauseStopEvent : Message {


    /**
     * the constructor of the PauseStopEvent-Class. Creates a PauseStopEvent-MessageObject.
     *
     * @author Sarah Engele
     */
    public PauseStopEvent() : base(EventType.PauseStopEvent)
    {
       
    }
}

