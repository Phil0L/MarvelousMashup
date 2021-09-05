
/**
 * This interface is used to put all EntityEvents into one class.
 */

public interface GameEvent
{
    /**
     * This method is used to execute the changes sent from the server in the game client.
     */
    public void Execute();

}