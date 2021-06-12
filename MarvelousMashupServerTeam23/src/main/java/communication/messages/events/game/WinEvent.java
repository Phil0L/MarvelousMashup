package communication.messages.events.game;


import communication.messages.Message;
import communication.messages.enums.EventType;

/**
 * This message is sent to all clients if one of the players has won the game
 *
 * @author Sarah Engele
 *
 */

public class WinEvent extends Message {

    /**
     * integer which contains the number of the player that has won the game
     */
    public int playerWon;

    /**
     * the constructor of the WinEvent-Class. Creates a WinEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param playerWon integer which contains the number of the player that has won the game
     */

    public WinEvent(int playerWon){
        super(EventType.WinEvent);
        this.playerWon = playerWon;
    }
}
