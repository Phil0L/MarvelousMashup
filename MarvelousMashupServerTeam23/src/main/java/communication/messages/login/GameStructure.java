package communication.messages.login;

import communication.messages.BasicMessage;
import communication.messages.enums.MessageType;
import parameter.MatchConfig;
import parameter.ScenarioConfig;
import parameter.ConfigHero;

/**
 * This message is sent to all to the server connected clients once both clients have decided on their teams.
 * This message is the first message during the game.
 * It can also be used as the first message after a client has reconnected to the server and the server
 * lets him know about his team and the configurations (game and scenarios)
 *
 * @author Sarah Engele
 *
 */

public class GameStructure extends BasicMessage {


    /**
     * The characters player one chose to be on his team
     */
    public ConfigHero[] playerOneCharacters;
    /**
     * The characters player two chose to be on his team
     */
    public ConfigHero[] playerTwoCharacters;

    /**
     * describes which role the user has that this message is sent to
     */
    public String assignment;

    /**
     * the name of player one
     */
    public String playerOneName;
    /**
     * the name of player two
     */
    public String playerTwoName;

    /**
     * the configuration of the game (e.g. when Thanos appears)
     */
    public MatchConfig matchconfig;
    /**
     * the configuration of the scenario (e.g. how big the map is)
     */
    public ScenarioConfig scenarioconfig;


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

    public GameStructure(String assignment, String playerOneName, String playerTwoName,
                         ConfigHero[] playerOneCharacters, ConfigHero[] playerTwoCharacters,
                         MatchConfig matchconfig, ScenarioConfig scenarioconfig){
        super(MessageType.GAME_STRUCTURE, null);
        this.assignment = assignment;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.playerOneCharacters = playerOneCharacters;
        this.playerTwoCharacters = playerTwoCharacters;
        this.matchconfig = matchconfig;
        this.scenarioconfig = scenarioconfig;
    }
}
