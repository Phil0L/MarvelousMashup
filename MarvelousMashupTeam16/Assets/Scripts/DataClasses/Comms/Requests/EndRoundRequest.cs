/**
 *  Message-Class, which is responsible to send EndRoundRequests to the server
 *  if the user wants to stop a turn with his character before the turn officially ends
 *
 *  @author Sarah Engele
 *
 */

public class EndRoundRequest : Message {


    /**
     *
     * the constructor of the EndRoundRequest-Class. Creates a EndRoundRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public EndRoundRequest() : base(RequestType.EndRoundRequest)
    {
       
    }

}
