/**
 *
 * GoodbyeClient is used to close a server-connection if a few conditions are given. The server sends this message to a client.
 *
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class GoodbyeClient : BasicMessage {

    /**
     * contains the message
     */
    public string message;

    /**
     *
     * The constructor of the GoodbyeClient-Class. Creates a GoodbyeClient-MessageObject
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param message contains the message
     */
    public GoodbyeClient(string optionals, string message) : base(MessageType.GOODBYE_CLIENT,optionals){
        this.message = message;
    }

}
