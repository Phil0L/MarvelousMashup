package communication.messages;

import communication.messages.enums.MessageType;

import java.util.Objects;

/**
 *
 * BasicMessage is one of the Message Types, which other communication.messages extend from. It is for
 * communication.messages from the client to the server.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 */
public abstract class BasicMessage {

    /**
     * The MessageType of the message
     */
    public MessageType messageType;
    /**
     * optional field, most times null
     */
    public String optionals;


    /**
     *
     * Constructor of the BasicMessage-Class. Creates a BasicMessage-Object. Most times called by MessageTypes,
     * which extends from BasicMessage.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param messageType The MessageType of the message
     * @param optionals optional field, most times null
     *
     */
    public BasicMessage(MessageType messageType,String optionals){
        this.messageType = messageType;
        this.optionals = optionals;
    }


}
