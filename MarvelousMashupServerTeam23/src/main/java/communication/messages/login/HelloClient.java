package communication.messages.login;

import communication.messages.BasicMessage;
import communication.messages.enums.MessageType;

/**
 * HelloClient is used by the Server to welcome a client. The message tells the client his userID for the first time.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 */
public class HelloClient extends BasicMessage {

    /**
     * true in case of the same ID is already attending a game
     */
    public Boolean runningGame;

    /**
     *
     * The constructor of the HelloClient-Class. It creates HelloClass-MessageObjects.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param runningGame true in case of the same ID is already attending a game
     *
     */
    public HelloClient(String optionals, Boolean runningGame){
        // the messageType is always MessageType.HELLO_CLIENT
        super(MessageType.HELLO_CLIENT,optionals);
        this.runningGame = runningGame;
    }

}
