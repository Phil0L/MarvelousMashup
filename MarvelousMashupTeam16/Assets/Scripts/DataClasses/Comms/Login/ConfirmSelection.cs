/**
 * ConfirmSelection is sent by the Server to the first client, which CharacterSelection the server received first, and
 * informs it that the server received and accepted its CharacterSelection.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class ConfirmSelection : BasicMessage {


    /**
     * tells the client whether the characterSelection was successful or not. It is true if the characterSelection was
     * successful.
     */
    public bool selectionComplete;


    /**
     *
     * The constructor of the ConfirmSelection-Class. It creates ConfirmSelection-MessageObjects.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param selectionComplete tells the client whether the characterSelection was successful or not. It is true if
     *                          the characterSelection was successful.
     *
     */
    public ConfirmSelection(string optionals, bool selectionComplete) : base(MessageType.CONFIRM_SELECTION,optionals){
        // the messageType is always MessageType.CONFIRM_SELECTION
        this.selectionComplete = selectionComplete;

    }

}
