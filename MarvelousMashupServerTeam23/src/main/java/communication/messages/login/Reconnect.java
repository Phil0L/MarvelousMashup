package communication.messages.login;


import communication.messages.BasicMessage;
import communication.messages.enums.MessageType;

/**
 *
 * Message-Class, which is a response from the client to the server to a HelloClient-Message in
 * which the Boolean runningGame is true.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class Reconnect extends BasicMessage {

    /**
     * decides whether the client wants to reconnect or start a new game. Is true if the client wants to reconnect,
     * false if the client wants to start a new game
     */
    public Boolean reconnect;

    /**
     *
     * the constructor of the Reconnect-Class. It creates a Reconnect-MessageObject with the given parameters
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param reconnect decides whether the client wants to reconnect or start a new game. Is true if the client wants
     *                  to reconnect, false if the client wants to start a new game
     */
    public Reconnect( String optionals ,Boolean reconnect){
        // messageType is always MessageType.RECONNECT
        super(MessageType.RECONNECT,optionals);
        this.reconnect = reconnect;
    }

}

