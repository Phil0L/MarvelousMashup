package communication.messages.login;


import communication.messages.BasicMessage;
import communication.messages.enums.MessageType;
import communication.messages.enums.Role;

/**
 *  Message-Class, which is responsible to response to HelloClient-Message sent by the server
 *
 *  @author Matthias Ruf
 *  @author Sarah Engele
 *
 *
 */
public class PlayerReady extends BasicMessage {

    /**
     * decides whether the player has entered a game. true = Player has entered a game, false = Player switched to
     * main Menu
     */
    public Boolean startGame;
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
    public PlayerReady(String optionals, Boolean startGame,Role role){

        super(MessageType.PLAYER_READY,optionals);
        this.startGame = startGame;
        this.role = role;

    }


}
