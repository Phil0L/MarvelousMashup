public class ExtractorMessageStructure
{
    /**
     * the type of the messages that are in the messages array (EVENTS or REQUESTS)
     */
    public MessageType messageType;

    /**
     * the messages, the server or client wants to send
     */
    public ExtractorMessage[] messages;

    /**
     * can contain additional information
     */
    public string customContentType;

    /**
     * can contain additional information
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
    public ExtractorMessageStructure(MessageType messageType, ExtractorMessage[] messages, string customContentType,
        object customContent)
    {
        this.messageType = messageType;
        this.messages = messages;
        this.customContentType = customContentType;
        this.customContent = customContent;
    }

    /**
     * creates a MessageStructure object by using the attributes of the ExtractorMessageStructure object. The exact
     * class of the object is determined by the eventType or the requestType of the object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a MessageStructure object created by the attributes of the ExtractorMessage object.
     *
     */
    public MessageStructure toMessageStructure()
    {
        /*
         * count null values from the message list
         */
        int nullCount = 0;
        for (int i = 0; i < this.messages.Length; i++)
        {
            if (this.messages[i] == null)
            {
                nullCount++;
            }
        }

        Message[] messages = new Message[this.messages.Length - nullCount];

        // cast all messages (by using the toMessage methode)
        nullCount = 0;
        for (int i = 0; i < this.messages.Length; i++)
        {
            if (this.messages[i] != null)
            {
                messages[i - nullCount] = this.messages[i].toMessage();
            }
            else
            {
                nullCount++;
            }
        }

        return new MessageStructure(this.messageType, messages, this.customContentType, this.customContent);
    }
}