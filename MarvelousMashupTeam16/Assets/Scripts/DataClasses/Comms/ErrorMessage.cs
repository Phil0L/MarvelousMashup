
/**
 * Message-Class, which is responsible for Error-Messages sent by the server
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class ErrorMessage : BasicMessage {

    /**
     * contains the error message as a String
     */
    public string message;
    /**
     * specifies the error type
     */
    public int type;

    /**
     * Constructor of the Error-Class. Creates a Error-MessageObject
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional String
     * @param message contains the error message as a String
     * @param type specifies the error type
     *
     */
    public ErrorMessage(string optionals, string message, int type) : base(MessageType.ERROR, optionals){
        // messagetype is ERROR
        this.message = message;
        this.type = type;
    }

}
