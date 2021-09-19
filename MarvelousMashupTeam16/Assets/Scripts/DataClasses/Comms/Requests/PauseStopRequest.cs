/**
 *  Message-Class, which is responsible to send PauseStopRequests to the server
 *  if the user wants to end a pause
 *
 *  @author Sarah Engele
 *
 */
public class PauseStopRequest : Message
{
    /**
     *
     * the constructor of the PauseStopRequest-Class.
     * Creates a PauseStopRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public PauseStopRequest() : base(RequestType.PauseStopRequest)
    {
    }
}