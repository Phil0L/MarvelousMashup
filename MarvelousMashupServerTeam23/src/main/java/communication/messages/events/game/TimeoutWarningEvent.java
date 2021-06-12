package communication.messages.events.game;

import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message warns the client about being reconnected soon due to not being active. The remaining time is sent in
 * seconds.
 *
 * @author Sarah Engele
 */
public class TimeoutWarningEvent extends Message {

    /**
     * might be something like "You will be disconnected soon."
     */
    public String message;
    /**
     * the remaining time before being disconnected in seconds. During this time the client can execute a (random)
     * Action to stay connected
     */
    public int timeLeft;

    /**
     * the constructor of the TimeoutWarningEvent-Class. Creates a TimeoutWarningEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param message might be something like "You will be disconnected soon."
     * @param timeLeft the remaining time before being disconnected in seconds. During this time the client can execute
     *                a (random) Action to stay connected
     */
    public TimeoutWarningEvent(String message, int timeLeft){
        super(EventType.TimeoutWarningEvent);
        this.message = message;
        this.timeLeft = timeLeft;
    }
}
