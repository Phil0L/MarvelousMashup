/**
 * GameAssignment is used by the Server if a client joined a game. The job of this message-class is to inform the client
 * which game the client has joined and send 12 different Characters for the character/hero selection to the client.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class GameAssignment : BasicMessage {


    /**
     * the individual ID of the game.
     */
    public string gameID;


    /**
     * selection of 12 different characters from which the player has to choose 6.
     */
    public Character[] characterSelection;


    /**
     *
     * The constructor of the GameAssignment-Class. It creates GameAssignment-MessageObjects.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param gameID the individual ID of the game.
     * @param characterSelection selection of 12 different characters from which the player has to choose 6.
     *
     *
     */
    public GameAssignment(string optionals, string gameID, Character[] characterSelection) : base(MessageType.GAME_ASSIGNMENT,optionals)
    {

        // the messageType is always MessageType.GAME_ASSIGNMENT
        this.characterSelection = characterSelection;
        this.gameID = gameID;

    }

}
