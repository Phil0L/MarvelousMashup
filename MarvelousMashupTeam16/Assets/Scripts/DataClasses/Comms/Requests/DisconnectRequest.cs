/**
 *  Message-Class, which is responsible to send DisconnectRequests to the server
 *  if the user wants to disconnect from the server
 *
 *  @author Sarah Engele
 *
 */

public class DisconnectRequest : Message {


    /**
     *
     * the constructor of the DisconnectRequest-Class.
     * Creates a DisconnectRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public DisconnectRequest() : base(RequestType.DisconnectRequest){
        
    }

}
