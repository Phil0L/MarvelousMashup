
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
    public string customContentType;
    /**
     *  can contain additional information
     */
    public object customContent;

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

    public MessageStructure(MessageType messageType, Message[] messages, string customContentType,
                            object customContent){
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
    
    
    public override bool Equals(object o) {
        if (this == o) return true;
        if (!(o is MessageStructure)) return false;
        MessageStructure that = (MessageStructure) o;
        return messageType == that.messageType && messages.Equals(that.messages) &&
                object.Equals(customContentType, that.customContentType) &&
                object.Equals(customContent, that.customContent);
    }

}
