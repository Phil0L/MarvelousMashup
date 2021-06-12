package communication.messages;

import communication.messages.enums.EventType;
import communication.messages.enums.RequestType;

import java.util.Objects;

/**
 * The super class of all InGame messages. InGame messages are either requests and have a RequestType or events and
 * have an EventType. The other unused Enum is in both cases null.
 *
 * @author Sarah Engele
 */

public class Message {
    /**
     * the type of the event
     */
    public EventType eventType;
    /**
     * the type of the request
     */
    public RequestType requestType;

    /**
     * constructor of the Message-class if the Message is an event
     *
     * @author Sarah Engele
     *
     * @param eventType the type of the event
     */
    public Message(EventType eventType){
        this.eventType = eventType;
    }

    /**
     * constructor of the Message-class if the Message is a request
     *
     * @author Sarah Engele
     *
     * @param requestType the type of the request
     */

    public Message(RequestType requestType){
        this.requestType = requestType;
    }


    /**
     * This methode compares two Message-Objects with each other
     *
     * @author Sarah Engele
     *
     * @param o a Message-Object that is going to be compared to the Message
     * @return boolean which is true when both objects are equal (eventType, requestType)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return eventType == message.eventType && requestType == message.requestType;
    }


}
