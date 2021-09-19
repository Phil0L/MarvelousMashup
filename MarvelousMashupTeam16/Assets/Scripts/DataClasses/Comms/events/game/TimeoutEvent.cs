using UnityEngine.SceneManagement;

/**
 * This message lets the client know that the servers next move will be disconnecting the client
 *
 * @author Sarah Engele
 */
public class TimeoutEvent : Message, GameEvent
{
    /**
     * might be something like "You have been disconnected."
     */
    public string message;

    /**
     * the constructor of the TimeoutEvent-Class. Creates a TimeoutEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param message might be something like "You have been disconnected."
     *
     */
    public TimeoutEvent(string message) : base(EventType.TimeoutEvent)
    {
        this.message = message;
    }
    
    public void Execute()
    {
        SceneManager.LoadScene("MainMenu");
    }
}
