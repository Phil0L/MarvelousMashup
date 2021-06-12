package communication;

import communication.messages.enums.MessageType;

/**
 * This class is needed to distinguish the message types of incoming messages with a switch case in the onMessage method.
 * @author Adrian Groeber
 */
public class ExtractorMessageType {
    //Made this public for the TestClient
    public MessageType messageType;
}

