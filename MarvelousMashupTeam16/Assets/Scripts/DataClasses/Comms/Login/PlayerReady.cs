/**
 *  Message-Class, which is responsible to response to HelloClient-Message sent by the server
 *
 *  @author Matthias Ruf
 *  @author Sarah Engele
 *
 *
 */
public class PlayerReady : BasicMessage
{
    /**
     * decides whether the player has entered a game. true = Player has entered a game, false = Player switched to
     * main Menu
     */
    public bool startGame;

    /**
     * The users Role (Ki, Player, Spectator)
     */
    public Role role;


    /**
     *
     * the constructor of the PlayerReady-Class. Creates a PlayerReady-MessageObject.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param startGame decides whether the player has entered a game. true = Player has entered a game, false = Player
     *                 switched to main Menu
     * @param role The users Role (Ki, Player, Spectator)
     *
     */
    public PlayerReady(string optionals, bool startGame, Role role) : base(MessageType.PLAYER_READY, optionals)
    {
        this.startGame = startGame;
        this.role = role;
    }
}