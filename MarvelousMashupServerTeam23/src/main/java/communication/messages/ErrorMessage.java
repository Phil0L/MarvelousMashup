package communication.messages;


import communication.messages.enums.MessageType;

/**
 * Message-Class, which is responsible for Error-Messages sent by the server
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class ErrorMessage extends BasicMessage {

    /**
     * contains the error message as a String
     */
    public String message;
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
    public ErrorMessage(String optionals, String message, int type){
        // messagetype is ERROR
        super(MessageType.ERROR,optionals);
        this.message = message;
        this.type = type;
    }

}
