/**
 *  Message-Class, which is responsible to send PauseStartRequests to the server
 *  if the user wants to have a pause
 *
 *  @author Sarah Engele
 *
 */

public class PauseStartRequest : Message {


    /**
     *
     * the constructor of the PauseStartRequest-Class.
     * Creates a PauseStartRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public PauseStartRequest() : base(RequestType.PauseStartRequest){
        
    }
}
