package communication.messages.login;

import com.google.gson.Gson;
import communication.messages.ErrorMessage;
import communication.messages.enums.Role;
import communication.messages.events.notification.Ack;
import org.junit.jupiter.api.Test;
import parameter.ConfigHero;
import parameter.GrassRockEnum;
import parameter.MatchConfig;
import parameter.ScenarioConfig;

import static org.junit.jupiter.api.Assertions.*;

class LoginMessagesTest {

    /**
     *
     * TestCase for the CharacterSelection-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void characterSelectionTest(){

        String optionals = "no optionals";
        Boolean[] characters = {false, false, true, false, true, false, true, true, true, true, false, false};
        CharacterSelection characterSelection = new CharacterSelection(optionals, characters);
        Gson gson = new Gson();
        String jsonString = gson.toJson(characterSelection,CharacterSelection.class);

        assertEquals("{" +
                "\"characters\":[" +
                "false,false,true,false,true,false,true,true,true,true,false,false]," +
                "\"messageType\":\"CHARACTER_SELECTION\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the ConfirmSelection-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void confirmSelectionTest(){

        String optionals = "no optionals";
        boolean selectionComplete = true;
        ConfirmSelection confirmSelection = new ConfirmSelection(optionals, selectionComplete);
        Gson gson = new Gson();
        String jsonString = gson.toJson(confirmSelection,ConfirmSelection.class);

        assertEquals("{" +
                "\"selectionComplete\":true," +
                "\"messageType\":\"CONFIRM_SELECTION\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the GameAssignment-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void gameAssignmentTest(){

        String optionals = "no optionals";
        String gameID = "6a39c3cf-26d8-409e-a309-45590f38ec4f";
        ConfigHero rocketRacoon = new ConfigHero(1,"Rocket Racoon", 100, 2, 2,10,
                30,5);
        ConfigHero gamora = new ConfigHero(2,"Gamora", 200, 9,3,5,
                15,10);
        ConfigHero[] characterSelection = {rocketRacoon, gamora};
        GameAssignment gameAssignment = new GameAssignment(optionals, gameID, characterSelection);
        Gson gson = new Gson();
        String jsonString = gson.toJson(gameAssignment,GameAssignment.class);

        assertEquals("{" +
                "\"gameID\":\"6a39c3cf-26d8-409e-a309-45590f38ec4f\"," +
                "\"characterSelection\":[" +
                "{\"characterID\":1,\"name\":\"Rocket Racoon\",\"HP\":100,\"MP\":2," +
                "\"AP\":2,\"meleeDamage\":10,\"rangeCombatDamage\":30,\"rangeCombatReach\":5}," +
                "{\"characterID\":2,\"name\":\"Gamora\",\"HP\":200,\"MP\":9," +
                "\"AP\":3,\"meleeDamage\":5,\"rangeCombatDamage\":15,\"rangeCombatReach\":10}]," +
                "\"messageType\":\"GAME_ASSIGNMENT\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the GameStructure-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void gameStructureTest(){

        String optionals = "no optionals";
        String gameID = "6a39c3cf-26d8-409e-a309-45590f38ec4f";
        ConfigHero rocketRacoon = new ConfigHero(1,"Rocket Racoon", 100, 2, 2,10,
                30,5);
        ConfigHero gamora = new ConfigHero(2,"Gamora", 200, 9,3,5,
                15,10);
        ConfigHero[] playerOneCharacters = {rocketRacoon, gamora};
        ConfigHero[] playerTwoCharacters = {gamora, rocketRacoon};
        String assignment = "PlayerOne";
        String playerOneName = "Hans";
        String playerTwoName = "Peter";
        GrassRockEnum[][] scenario ={{GrassRockEnum.GRASS, GrassRockEnum.GRASS, GrassRockEnum.ROCK},
                {GrassRockEnum.ROCK, GrassRockEnum.GRASS, GrassRockEnum.GRASS},
                {GrassRockEnum.GRASS, GrassRockEnum.ROCK, GrassRockEnum.ROCK}};
        ScenarioConfig scenarioconfig = new ScenarioConfig(scenario, playerOneName, playerTwoName);
        MatchConfig matchconfig = new MatchConfig(6,2,4,42,5,
                5,5,5,5,5,66,56,
                54);
        GameStructure gameStructure = new GameStructure(assignment, playerOneName, playerTwoName, playerOneCharacters,
                playerTwoCharacters, matchconfig, scenarioconfig);
        Gson gson = new Gson();
        String jsonString = gson.toJson(gameStructure,GameStructure.class);

        assertEquals("{" +

                "\"playerOneCharacters\":[" +
                "{\"characterID\":1,\"name\":\"Rocket Racoon\",\"HP\":100,\"MP\":2," +
                "\"AP\":2,\"meleeDamage\":10,\"rangeCombatDamage\":30,\"rangeCombatReach\":5}," +
                "{\"characterID\":2,\"name\":\"Gamora\",\"HP\":200,\"MP\":9," +
                "\"AP\":3,\"meleeDamage\":5,\"rangeCombatDamage\":15,\"rangeCombatReach\":10}]," +

                "\"playerTwoCharacters\":[" +
                "{\"characterID\":2,\"name\":\"Gamora\",\"HP\":200,\"MP\":9," +
                "\"AP\":3,\"meleeDamage\":5,\"rangeCombatDamage\":15,\"rangeCombatReach\":10}," +
                "{\"characterID\":1,\"name\":\"Rocket Racoon\",\"HP\":100,\"MP\":2," +
                "\"AP\":2,\"meleeDamage\":10,\"rangeCombatDamage\":30,\"rangeCombatReach\":5}]," +

                "\"assignment\":\"PlayerOne\"," +
                "\"playerOneName\":\"Hans\"," +
                "\"playerTwoName\":\"Peter\"," +

                "\"matchconfig\":" +
                "{\"maxRounds\":6,\"maxRoundTime\":2,\"maxGameTime\":4,\"maxAnimationTime\":42," +
                "\"spaceStoneCD\":5,\"mindStoneCD\":5,\"realityStoneCD\":5,\"powerStoneCD\":5," +
                "\"timeStoneCD\":5,\"soulStoneCD\":5,\"mindStoneDMG\":66,\"maxPauseTime\":56," +
                "\"maxResponseTime\":54}," +

                "\"scenarioconfig\":{" +
                "\"scenario\":[" +
                "[\"GRASS\",\"GRASS\",\"ROCK\"]," +
                "[\"ROCK\",\"GRASS\",\"GRASS\"]," +
                "[\"GRASS\",\"ROCK\",\"ROCK\"]]," +
                "\"name\":\"Hans\"," +
                "\"author\":\"Peter\"}," +

                "\"messageType\":\"GAME_STRUCTURE\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the GeneralAssignment-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void generalAssignmentTest(){

        String optionals = "no optionals";
        String gameID = "6a39c3cf-26d8-409e-a309-45590f38ec4f";
        GeneralAssignment generalAssignment = new GeneralAssignment(optionals, gameID);
        Gson gson = new Gson();
        String jsonString = gson.toJson(generalAssignment, GeneralAssignment.class);

        assertEquals("{" +
                "\"gameID\":\"6a39c3cf-26d8-409e-a309-45590f38ec4f\"," +
                "\"messageType\":\"GENERAL_ASSIGNMENT\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the GoodbyeClient-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void goodbyeClientTest(){

        String message = "Goodbye Client!";
        String optionals = "no optionals";
        GoodbyeClient goodbyeClient = new GoodbyeClient(optionals, message);
        Gson gson = new Gson();
        String jsonString = gson.toJson(goodbyeClient, GoodbyeClient.class);

        assertEquals("{" +
                "\"message\":\"Goodbye Client!\"," +
                "\"messageType\":\"GOODBYE_CLIENT\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the HelloClient-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void helloClientTest(){
        Boolean runningGame = false;
        String optionals = "no optionals";
        HelloClient helloClient = new HelloClient(optionals, runningGame);
        Gson gson = new Gson();
        String jsonString = gson.toJson(helloClient, HelloClient.class);

        assertEquals("{" +
                "\"runningGame\":false," +
                "\"messageType\":\"HELLO_CLIENT\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the HelloServer-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void helloServerTest(){

        String name = "Peter";
        String deviceID = "127.0.0.1";
        String optionals = "no optionals";
        HelloServer helloServer = new HelloServer(name, deviceID, optionals);
        Gson gson = new Gson();
        String jsonString = gson.toJson(helloServer, HelloServer.class);

        assertEquals("{" +
                "\"messageType\":\"HELLO_SERVER\"," +
                "\"name\":\"Peter\"," +
                "\"deviceID\":\"127.0.0.1\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the PlayerReady-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void playerReadyTest(){

        Boolean startGame = true;
        String optionals = "no optionals";
        Gson gson = new Gson();
        String jsonString;

        //PlayerReady with player
        PlayerReady playerReadyPLAYER = new PlayerReady(optionals, startGame, Role.PLAYER);
        jsonString = gson.toJson(playerReadyPLAYER, PlayerReady.class);

        assertEquals("{" +
                "\"startGame\":true," +
                "\"role\":\"PLAYER\"," +
                "\"messageType\":\"PLAYER_READY\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);

        //PlayerReady with spectator
        PlayerReady playerReadySPECTATOR = new PlayerReady(optionals, startGame, Role.SPECTATOR);
        jsonString = gson.toJson(playerReadySPECTATOR, PlayerReady.class);

        assertEquals("{" +
                "\"startGame\":true," +
                "\"role\":\"SPECTATOR\"," +
                "\"messageType\":\"PLAYER_READY\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);

        //PlayerReady with KI
        PlayerReady playerReadyKI = new PlayerReady(optionals, startGame, Role.KI);
        jsonString = gson.toJson(playerReadyKI, PlayerReady.class);

        assertEquals("{" +
                "\"startGame\":true," +
                "\"role\":\"KI\"," +
                "\"messageType\":\"PLAYER_READY\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the Reconnect-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void reconnectTest(){

        Boolean reconnect = false;
        String optionals = "no optionals";
        Reconnect reconnectMessageObject = new Reconnect(optionals, reconnect);
        Gson gson = new Gson();
        String jsonString = gson.toJson(reconnectMessageObject, Reconnect.class);

        assertEquals("{" +
                "\"reconnect\":false," +
                "\"messageType\":\"RECONNECT\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the ErrorMessage-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void errorMessageTest(){
        String message = "This is an error.";
        String optionals = "no optionals";
        int type = 3;
        ErrorMessage errorMessage = new ErrorMessage(optionals, message, type);
        Gson gson = new Gson();
        String jsonString = gson.toJson(errorMessage, ErrorMessage.class);

        assertEquals("{" +
                "\"message\":\"This is an error.\"," +
                "\"type\":3," +
                "\"messageType\":\"ERROR\"," +
                "\"optionals\":\"no optionals\"" +
                "}",jsonString);
    }
}