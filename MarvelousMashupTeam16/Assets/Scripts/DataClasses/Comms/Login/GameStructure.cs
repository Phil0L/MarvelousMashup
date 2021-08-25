/**
 * This message is sent to all to the server connected clients once both clients have decided on their teams.
 * This message is the first message during the game.
 * It can also be used as the first message after a client has reconnected to the server and the server
 * lets him know about his team and the configurations (game and scenarios)
 *
 * @author Sarah Engele
 *
 */
public class GameStructure : BasicMessage
{
    /**
     * The characters player one chose to be on his team
     */
    public Character[] playerOneCharacters;

    /**
     * The characters player two chose to be on his team
     */
    public Character[] playerTwoCharacters;

    /**
     * describes which role the user has that this message is sent to
     */
    public string assignment;

    /**
     * the name of player one
     */
    public string playerOneName;

    /**
     * the name of player two
     */
    public string playerTwoName;

    /**
     * the configuration of the game (e.g. when Thanos appears)
     */
    public Party matchconfig;

    /**
     * the configuration of the scenario (e.g. how big the map is)
     */
    public Map scenarioconfig;


    /**
     *
     * The constructor of the GameStructure-Class. It creates GameStructure-MessageObjects.
     *
     * @author Sarah Engele
     *
     * @param assignment describes which role the user has that this message is sent to
     * @param playerOneName the name of player one
     * @param playerTwoName the name of player two
     * @param playerOneCharacters The characters player one chose to be on his team
     * @param playerTwoCharacters The characters player two chose to be on his team
     * @param matchconfig the configuration of the game (e.g. when Thanos appears)
     * @param scenarioconfig the configuration of the scenario (e.g. how big the map is)
     */
    public GameStructure(string assignment, string playerOneName, string playerTwoName,
        Character[] playerOneCharacters, Character[] playerTwoCharacters,
        Party matchconfig, Map scenarioconfig) : base(MessageType.GAME_STRUCTURE, null)
    {
        this.assignment = assignment;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.playerOneCharacters = playerOneCharacters;
        this.playerTwoCharacters = playerTwoCharacters;
        this.matchconfig = matchconfig;
        this.scenarioconfig = scenarioconfig;
    }
}