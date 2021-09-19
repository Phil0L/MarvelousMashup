/**
 * HelloClient is used by the Server to welcome a client. The message tells the client his userID for the first time.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 */
public class HelloClient : BasicMessage {

    /**
     * true in case of the same ID is already attending a game
     */
    public bool runningGame;

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
    public HelloClient(string optionals, bool runningGame) : base(MessageType.HELLO_CLIENT,optionals){
        // the messageType is always MessageType.HELLO_CLIENT
        this.runningGame = runningGame;
    }

}
