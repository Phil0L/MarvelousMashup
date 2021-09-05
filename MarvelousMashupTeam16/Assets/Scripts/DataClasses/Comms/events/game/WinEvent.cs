using UnityEngine.SceneManagement;

/**
 * This message is sent to all clients if one of the players has won the game
 *
 * @author Sarah Engele
 *
 */

public class WinEvent : Message, GameEvent 
{
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

    public WinEvent(int playerWon) : base(EventType.WinEvent)
    {
        this.playerWon = playerWon;
    }

    public void Execute()
    {
        string winnerName = PartyConfigStore.Winner(playerWon);
        PopUp.Create()
            .Title($"{winnerName} Wins!!!")
            .Bubble(PopUp.Bubbles.Action)
            .AddAction(new PopUp.PopUpAction("Main Menu", up =>
            {
                up.Destroy();
                SceneManager.LoadScene("MainMenu");
            }))
            .Show();
    }
}
