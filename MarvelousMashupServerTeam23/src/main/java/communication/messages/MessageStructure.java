package communication.messages;

import communication.messages.enums.MessageType;

import java.util.Arrays;
import java.util.Objects;

/**
 * This message is sent during InGame whenever the client wants to send request(s) to the server
 * or whenever the server wants to send events to the client(s)
 *
 * @author Sarah Engele
 *
 */

public class MessageStructure {

    /**
     * the type of the messages that are in the messages array (EVENTS or REQUESTS)
     */
    public MessageType messageType;
    /**
     * the messages, the server or client wants to send
     */
    public Message[] messages;
    /**
     * can contain additional information
     */
    public String customContentType;
    /**
     *  can contain additional information
     */
    public Object customContent;

    /**
     * the constructor of the MessageStructure-Class. Creates a MessageStructure-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param messageType the type of the messages that are in the messages array (EVENTS or REQUESTS)
     * @param messages the messages, the server or client wants to send
     * @param customContentType can contain additional information
     * @param customContent can contain additional information
     */

    public MessageStructure(MessageType messageType, Message[] messages, String customContentType,
                            Object customContent){
        this.messageType = messageType;
        this.messages = messages;
        this.customContentType = customContentType;
        this.customContent = customContent;
    }

    /**
     * This methode compares two MessageStructure-Objects with each other
     *
     * @author Sarah Engele
     *
     * @param o a MessageStructure-Object that is going to be compared to the MessageStructure
     * @return boolean which is true when both objects are equal (messageType, messages, customContentType, customContent)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageStructure)) return false;
        MessageStructure that = (MessageStructure) o;
        return messageType == that.messageType && Arrays.equals(messages, that.messages) &&
                Objects.equals(customContentType, that.customContentType) &&
                Objects.equals(customContent, that.customContent);
    }

}
