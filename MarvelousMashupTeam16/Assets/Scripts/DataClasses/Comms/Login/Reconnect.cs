/**
 *
 * Message-Class, which is a response from the client to the server to a HelloClient-Message in
 * which the Boolean runningGame is true.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class Reconnect : BasicMessage {

    /**
     * decides whether the client wants to reconnect or start a new game. Is true if the client wants to reconnect,
     * false if the client wants to start a new game
     */
    public bool reconnect;

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
    public Reconnect(string optionals ,bool reconnect) : base(MessageType.RECONNECT,optionals){
        // messageType is always MessageType.RECONNECT
        this.reconnect = reconnect;
    }

}

