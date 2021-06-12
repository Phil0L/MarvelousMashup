package communication.messages.events.game;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message lets the client know that the servers next move will be disconnecting the client. This message is the
 * regular way e.g. in case of the end of a game (WinEvent)
 *
 * @author Sarah Engele
 */
public class DisconnectEvent extends Message {


    /**
     * the constructor of the Disconnect-Class. Creates a Disconnect-MessageObject.
     *
     * @author Sarah Engele
     *
     */
    public DisconnectEvent(){
        super(EventType.DisconnectEvent);
    }
}