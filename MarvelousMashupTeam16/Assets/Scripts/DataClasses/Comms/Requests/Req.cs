/**
 *  Message-Class, which is responsible to send Reqs to the server
 *  if the user wants to receive the current GameState from the server
 *
 *  @author Sarah Engele
 *
 */

public class Req : Message {



    /**
     *
     * the constructor of the Req-Class.
     * Creates a Req-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public Req() : base(RequestType.Req)
    {
        
    }

}
