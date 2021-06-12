package communication;


import communication.messages.enums.MessageType;

/**
 * This class is used to attach a MessageType and a profile to a WebSocket in the NetworkHandler.
 * @author Adrian Groeber
 */
public class Attachment {
    public MessageType checkPoint;
    public Profile profile;
    public Boolean loginFinished;

    /**
     * This is the constructor of the Attachment class.
     * @author Adrian Groeber
     *
     * @param checkPoint the checkPoint represents the last message sent by the client to the server. It is used to verify the right succession of messages as defined in the Network standard.
     * @param profile
     */
    public Attachment(MessageType checkPoint, Profile profile) {
        this.checkPoint = checkPoint;
        this.profile = profile;
        loginFinished = false;
    }
}
