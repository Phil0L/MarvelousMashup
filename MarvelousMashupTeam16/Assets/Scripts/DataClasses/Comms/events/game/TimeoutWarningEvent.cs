/**
 * This message warns the client about being reconnected soon due to not being active. The remaining time is sent in
 * seconds.
 *
 * @author Sarah Engele
 */
public class TimeoutWarningEvent : Message, GameEvent
{
    /**
     * might be something like "You will be disconnected soon."
     */
    public string message;

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
    public TimeoutWarningEvent(string message, int timeLeft) : base(EventType.TimeoutWarningEvent)
    {
        this.message = message;
        this.timeLeft = timeLeft;
    }

    public void Execute()
    {
        if (Server.IsAttached())
            Server.Connection.Send(new Req());
    }
}