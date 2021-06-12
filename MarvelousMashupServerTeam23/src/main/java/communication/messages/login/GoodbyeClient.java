package communication.messages.login;

import communication.messages.BasicMessage;
import communication.messages.enums.MessageType;

/**
 *
 * GoodbyeClient is used to close a server-connection if a few conditions are given. The server sends this message to a client.
 *
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class GoodbyeClient extends BasicMessage {

    /**
     * contains the message
     */
    public String message;

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
    public GoodbyeClient(String optionals, String message){
        super(MessageType.GOODBYE_CLIENT,optionals);
        this.message = message;
    }

}
