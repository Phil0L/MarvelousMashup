/**
 * GeneralAssignment is used by the Server if a client joined a game as spectator or a player rejoins his game.
 * The job of this message-class is to inform the client which game the client has joined.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class GeneralAssignment : BasicMessage {


    /**
     * the individual ID of the game.
     */
    public string gameID;

    /**
     *
     * The constructor of the GeneralAssignment-Class. It creates GeneralAssignment-MessageObjects.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param gameID the individual ID of the game.
     *
     */
    public GeneralAssignment(string optionals, string gameID) : base(MessageType.GENERAL_ASSIGNMENT,optionals){

        // the messageType is always MessageType.GENERAL_ASSIGNMENT
        this.gameID = gameID;

    }

}
