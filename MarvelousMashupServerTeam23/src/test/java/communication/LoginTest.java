package communication;

import com.google.gson.Gson;
import communication.messages.enums.MessageType;
import communication.messages.enums.Role;
import communication.messages.login.*;
import communication.mock.TestClient;
import logic.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parameter.ConfigHero;
import parameter.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case for the connection establishment. The Test checks the following cases:
 *
 *  * normal login process (work in process)
 *
 *
 * @author Matthias Ruf
 */
class LoginTest {

    private NetworkHandler server;

    private  Configuration configuration;

    private Gson gson;

    /**
     * Time to wait until the TestClient and Server finish to communicat
     */
    private static final long COMPUTATION_DURATION = 500;

    /**
     * The setup methode should be executed before each @Test method in the current class.
     * That this happens the @BeforeEach annotation is used.
     *
     * @author Matthias Ruf
     */
    @BeforeEach
    void setUp() {
        String scenarioConfigPath = "asgard.json";
        String characterConfigPath = "marvelheros.json";
        String matchConfigPatch = "matchconfig_1.json";
        try {
            this.configuration = new Configuration(scenarioConfigPath, characterConfigPath, matchConfigPatch);
        } catch (IOException e) {
            fail("IOException during the Configuration parsing");
        }
        this.server = new NetworkHandler(this.configuration);
        this.server.start();

        //create a gson parser object
        this.gson = new Gson();
    }

    /**
     * The setup methode should be executed after each @Test method in the current class.
     * That this happens the @AfterEach annotation is used.
     *
     * @author Matthias Ruf
     */
    @AfterEach
    void tearDown() {
        try {
            this.server.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test methode for a normal login process as a player. The following messages were sent between the client
     * component (simulated by the TestClient) and the server component (mainly represented by the NetworkHandler
     * class).
     * ----------------------------------------
     * client connects
     * ----------------------------------------
     * Client --> HelloServer --> Server
     * Server --> HelloClient --> Client
     * Client --> PlayerReady --> Server
     * ----------------------------------------
     * second client connects
     * ----------------------------------------
     * Server --> GameAssignment --> Client
     * ----------------------------------------
     * clients send CharacterSelection
     * ----------------------------------------
     * Client --> CharacterSelection --> Server
     * Server --> ConfirmSelection --> Client !
     * Server --> GameStructure --> Client
     *-----------------------------------------
     *
     * loginProcess succeeded
     *
     */
    @Test
    public void loginTestPlayer(){

        // create a TestClient instance
        TestClient client = null;
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);
        // connect the client to the server
        assert client.connect(200);


        /*
        Give client and server a few seconds time for the connection establishment
        The connection establishment involves the following steps:
        Client --> HelloServer --> Server
        Server --> HelloClient --> Client
        Client --> PlayerReady --> Server
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
           fail("InterruptedException: " + e);
        }

        /*
          --------------------------- Check first message: Client --> HelloServer --> Server ---------------------------
         */
        HelloServer helloServer = gson.fromJson(client.getMessagesOut().get(0), HelloServer.class );
        //check the message parameters (TestClient)
        assertEquals(MessageType.HELLO_SERVER,helloServer.messageType);
        assertEquals(client.username,helloServer.name);
        assertEquals(client.optionals,helloServer.optionals);
        assertEquals(client.deviceID,helloServer.deviceID);

        // search in profileList for the profile of the client
        Profile clientProfil = null;
        for (Profile prof: server.profileList ) {

            // if  name and deviceID are equal is most likely the profile we are looking for
            if (prof.name.equals(client.username) && prof.deviceID.equals(client.deviceID)){
                clientProfil = prof;
            }
        }
        // check the profile for the player (Server)
        assertNotNull(clientProfil);
        assertEquals(client.username,clientProfil.name);
        assertEquals(client.deviceID,clientProfil.deviceID);

        /*
          -------------------------- Check second message: Server --> HelloClient --> Client ---------------------------
         */
        HelloClient helloClient = gson.fromJson(client.getMessagesIn().get(0), HelloClient.class );
        //check the message parameters (Server)
        assertEquals(MessageType.HELLO_CLIENT,helloClient.messageType);
        assertFalse(helloClient.runningGame);
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
          --------------------------- Check third message: Client --> PlayerReady --> Server ---------------------------
         */
        PlayerReady playerReady = gson.fromJson(client.getMessagesOut().get(1), PlayerReady.class );
        //check the message parameters (TestClient)
        assertEquals(MessageType.PLAYER_READY,playerReady.messageType);
        assertEquals(Role.PLAYER,playerReady.role);
        assertTrue(playerReady.startGame);
        // check the playerList of the server (Server)
        assertEquals(1,server.controller.playerReadyList.size());
        Player player = server.controller.playerReadyList.get(0);
        assertEquals(clientProfil,player.profile);
        assertEquals(Role.PLAYER,player.role);

        /*
        --------------------------------------- Second Client connects -------------------------------------------------
         */
        // create a second TestClient instance
        TestClient client2 = null;
        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - second test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(client2);
        client2.username = "TestClient2";
        // connect the client to the server
        assert client2.connect(200);

        /*
        Give client2 and server a few seconds time for the connection establishment
        The reaction for the client (the first TestClient instance) should be
        Server --> GameAssignment --> Client
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
        ----------------------------------------------------------------------------------------------------------------
         */

        /*
          ------------------------ Check fourth message: Server --> GameAssignment --> Client --------------------------
         */
        GameAssignment gameAssignment = gson.fromJson(client.getMessagesIn().get(1), GameAssignment.class );
        //store values for a later use
        String gameID = gameAssignment.gameID;
        ConfigHero[] heroesToChooseFrom = gameAssignment.characterSelection;
        if(gameID.equals("Game1")){
            System.err.println("gameID maybe is equal for each match, check this");
        }
        //check the message parameters (Server)
        assertEquals(MessageType.GAME_ASSIGNMENT,gameAssignment.messageType);
        assertEquals(gameID,gameAssignment.gameID);
        assertEquals(12,gameAssignment.characterSelection.length);

        // get heroSets of the two clients
        ConfigHero[] client_configHeroes = client.heroSet;
        ConfigHero[] client2_configHeroes = client2.heroSet;
        // check whether the heroSets are disjoint
        for (ConfigHero hero: client_configHeroes) {
            assertFalse(Arrays.asList(client2_configHeroes).contains(hero));
        }

        /*
        -------------------------------- send a characterSelection from the first clients ------------------------------
         */
        Boolean[] example_selection = {true,true,true,true,true,true,false,false,false,false,false,false};
        client.sendCharacterSelection(example_selection);
        // this pause makes that the first client gets the ConfirmSelection
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
        ----------------------------------------------------------------------------------------------------------------
         */

        /*
          ---------------------- Check fifth message: Client --> CharacterSelection --> Server -------------------------
         */
        // check the message (client)
        CharacterSelection characterSelection = gson.fromJson(client.getMessagesOut().get(2),CharacterSelection.class);
        //check the Array entries
        for (int i = 0; i < characterSelection.characters.length; i++) {
            assertEquals(example_selection[i],characterSelection.characters[i]);
        }
        assertEquals(MessageType.CHARACTER_SELECTION,characterSelection.messageType);

        // check the message handling in the server class
        assertTrue(server.controller.model.playerOne.selectionConfirmed);
        // compute the ConfigHeros which were chosen by the client
        ConfigHero[] chosenHeroes = new ConfigHero[6];
        int countChosenHeros = 0;
        for (int i = 0; i < characterSelection.characters.length; i++) {
            if(characterSelection.characters[i]){
                chosenHeroes[countChosenHeros++] = heroesToChooseFrom[i];
            }
        }
        assertEquals(6,countChosenHeros);

        // if there is an AssertionException because of not given equality, check the ID or PID (this may are different)
        for (int i = 0; i < chosenHeroes.length; i++) {
            assertEquals(chosenHeroes[i].toHero(1,i,server.controller.model) ,server.controller.model.playerOne.playerTeam[i]);
        }

        /*
          ----------------------- Check sixth message: Server --> ConfirmSelection --> Client --------------------------
         */
        ConfirmSelection confirmSelection = gson.fromJson(client.getMessagesIn().get(2),ConfirmSelection.class);
        assertEquals(MessageType.CONFIRM_SELECTION,confirmSelection.messageType);
        assertTrue(confirmSelection.selectionComplete);



        // the ser
        assertFalse(server.controller.model.playerTwo.selectionConfirmed);
        client2.sendCharacterSelection(example_selection);
        /*
        Give client, client2 and server a few seconds time to process the CharacterSelection message
        The reaction for the client (the first TestClient instance) should be

        Server --> GameStructure --> client
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(server.controller.model.playerTwo.selectionConfirmed);

        /*
          ----------------------- Check seventh message: Server --> GameStructure --> Client --------------------------
         */
        GameStructure gameStructure = gson.fromJson(client.getMessagesIn().get(3),GameStructure.class);
        assertEquals(MessageType.CONFIRM_SELECTION,confirmSelection.messageType);
        assertEquals(configuration.matchConfig,gameStructure.matchconfig);
        assertEquals(configuration.scenarioConfig,gameStructure.scenarioconfig);



        assertEquals("PlayerOne",gameStructure.assignment);

        //check the chosen heroes of the two players
        for (int i = 0; i < client.chosenHeroes.size() ; i++) {
            assertEquals(client.chosenHeroes.get(i)
                    ,gameStructure.playerOneCharacters[i]);
        }
        for (int i = 0; i < client2.chosenHeroes.size() ; i++) {
            assertEquals(client2.chosenHeroes.get(i)
                    ,gameStructure.playerTwoCharacters[i]);
        }
        //check the names of the two players
        assertEquals(client.username,gameStructure.playerOneName);
        assertEquals(client2.username,gameStructure.playerTwoName);
        assertEquals(1,client.playerNumber);
        assertEquals(2,client2.playerNumber);


        assertTrue(server.controller.loginFinished);



    }

    /**
     * Test methode for a normal login process as a player. The following messages were sent between the client
     * component (simulated by the TestClient) and the server component (mainly represented by the NetworkHandler
     * class).
     * ----------------------------------------
     * client connects
     * ----------------------------------------
     * Client --> HelloServer --> Server
     * Server --> HelloClient --> Client
     * Client --> PlayerReady --> Server
     * ----------------------------------------
     * second client connects
     * ----------------------------------------
     * Server --> GameAssignment --> Client
     * ----------------------------------------
     * clients send CharacterSelection
     * ----------------------------------------
     * Client --> CharacterSelection --> Server
     * Server --> ConfirmSelection --> Client !
     * Server --> GameStructure --> Client
     *-----------------------------------------
     *
     * loginProcess succeeded
     *
     */
    @Test
    public void loginTestKI(){

        // create a TestClient instance
        TestClient client = null;
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);

        client.role = Role.KI;


        // connect the client to the server
        assert client.connect(200);


        /*
        Give client and server a few seconds time for the connection establishment
        The connection establishment involves the following steps:
        Client --> HelloServer --> Server
        Server --> HelloClient --> Client
        Client --> PlayerReady --> Server
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
          --------------------------- Check first message: Client --> HelloServer --> Server ---------------------------
         */
        HelloServer helloServer = gson.fromJson(client.getMessagesOut().get(0), HelloServer.class );
        //check the message parameters (TestClient)
        assertEquals(MessageType.HELLO_SERVER,helloServer.messageType);
        assertEquals(client.username,helloServer.name);
        assertEquals(client.optionals,helloServer.optionals);
        assertEquals(client.deviceID,helloServer.deviceID);

        // search in profileList for the profile of the client
        Profile clientProfil = null;
        for (Profile prof: server.profileList ) {

            // if  name and deviceID are equal is most likely the profile we are looking for
            if (prof.name.equals(client.username) && prof.deviceID.equals(client.deviceID)){
                clientProfil = prof;
            }
        }
        // check the profile for the player (Server)
        assertNotNull(clientProfil);
        assertEquals(client.username,clientProfil.name);
        assertEquals(client.deviceID,clientProfil.deviceID);

        /*
          -------------------------- Check second message: Server --> HelloClient --> Client ---------------------------
         */
        HelloClient helloClient = gson.fromJson(client.getMessagesIn().get(0), HelloClient.class );
        //check the message parameters (Server)
        assertEquals(MessageType.HELLO_CLIENT,helloClient.messageType);
        assertFalse(helloClient.runningGame);
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
          --------------------------- Check third message: Client --> PlayerReady --> Server ---------------------------
         */
        PlayerReady playerReady = gson.fromJson(client.getMessagesOut().get(1), PlayerReady.class );
        //check the message parameters (TestClient)
        assertEquals(MessageType.PLAYER_READY,playerReady.messageType);
        assertEquals(Role.KI,playerReady.role);
        assertTrue(playerReady.startGame);
        // check the playerList of the server (Server)
        assertEquals(1,server.controller.playerReadyList.size());
        Player player = server.controller.playerReadyList.get(0);
        assertEquals(clientProfil,player.profile);
        assertEquals(Role.KI,player.role);

        /*
        --------------------------------------- Second Client connects -------------------------------------------------
         */
        // create a second TestClient instance
        TestClient client2 = null;
        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - second test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(client2);
        client2.username = "TestClient2";
        // connect the client to the server
        assert client2.connect(200);

        /*
        Give client2 and server a few seconds time for the connection establishment
        The reaction for the client (the first TestClient instance) should be
        Server --> GameAssignment --> Client
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
        ----------------------------------------------------------------------------------------------------------------
         */

        /*
          ------------------------ Check fourth message: Server --> GameAssignment --> Client --------------------------
         */
        GameAssignment gameAssignment = gson.fromJson(client.getMessagesIn().get(1), GameAssignment.class );
        //store values for a later use
        String gameID = gameAssignment.gameID;
        ConfigHero[] heroesToChooseFrom = gameAssignment.characterSelection;
        if(gameID.equals("Game1")){
            System.err.println("gameID maybe is equal for each match, check this");
        }
        //check the message parameters (Server)
        assertEquals(MessageType.GAME_ASSIGNMENT,gameAssignment.messageType);
        assertEquals(gameID,gameAssignment.gameID);
        assertEquals(12,gameAssignment.characterSelection.length);

        // get heroSets of the two clients
        ConfigHero[] client_configHeroes = client.heroSet;
        ConfigHero[] client2_configHeroes = client2.heroSet;
        // check whether the heroSets are disjoint
        for (ConfigHero hero: client_configHeroes) {
            assertFalse(Arrays.asList(client2_configHeroes).contains(hero));
        }

        /*
        -------------------------------- send a characterSelection from the first clients ------------------------------
         */
        Boolean[] example_selection = {true,true,true,true,true,true,false,false,false,false,false,false};
        client.sendCharacterSelection(example_selection);
        // this pause makes that the first client gets the ConfirmSelection
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
        ----------------------------------------------------------------------------------------------------------------
         */

        /*
          ---------------------- Check fifth message: Client --> CharacterSelection --> Server -------------------------
         */
        // check the message (client)
        CharacterSelection characterSelection = gson.fromJson(client.getMessagesOut().get(2),CharacterSelection.class);
        //check the Array entries
        for (int i = 0; i < characterSelection.characters.length; i++) {
            assertEquals(example_selection[i],characterSelection.characters[i]);
        }
        assertEquals(MessageType.CHARACTER_SELECTION,characterSelection.messageType);

        // check the message handling in the server class
        assertTrue(server.controller.model.playerOne.selectionConfirmed);
        // compute the ConfigHeros which were chosen by the client
        ConfigHero[] chosenHeroes = new ConfigHero[6];
        int countChosenHeros = 0;
        for (int i = 0; i < characterSelection.characters.length; i++) {
            if(characterSelection.characters[i]){
                chosenHeroes[countChosenHeros++] = heroesToChooseFrom[i];
            }
        }
        assertEquals(6,countChosenHeros);

        // if there is an AssertionException because of not given equality, check the ID or PID (this may are different)
        for (int i = 0; i < chosenHeroes.length; i++) {
            assertEquals(chosenHeroes[i].toHero(1,i,server.controller.model) ,server.controller.model.playerOne.playerTeam[i]);
        }

        /*
          ----------------------- Check sixth message: Server --> ConfirmSelection --> Client --------------------------
         */
        ConfirmSelection confirmSelection = gson.fromJson(client.getMessagesIn().get(2),ConfirmSelection.class);
        assertEquals(MessageType.CONFIRM_SELECTION,confirmSelection.messageType);
        assertTrue(confirmSelection.selectionComplete);



        // the ser
        assertFalse(server.controller.model.playerTwo.selectionConfirmed);
        client2.sendCharacterSelection(example_selection);
        /*
        Give client, client2 and server a few seconds time to process the CharacterSelection message
        The reaction for the client (the first TestClient instance) should be

        Server --> GameStructure --> client
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(server.controller.model.playerTwo.selectionConfirmed);

        /*
          ----------------------- Check seventh message: Server --> GameStructure --> Client --------------------------
         */
        GameStructure gameStructure = gson.fromJson(client.getMessagesIn().get(3),GameStructure.class);
        assertEquals(MessageType.CONFIRM_SELECTION,confirmSelection.messageType);
        assertEquals(configuration.matchConfig,gameStructure.matchconfig);
        assertEquals(configuration.scenarioConfig,gameStructure.scenarioconfig);



        assertEquals("PlayerOne",gameStructure.assignment);

        //check the chosen heroes of the two players
        for (int i = 0; i < client.chosenHeroes.size() ; i++) {
            assertEquals(client.chosenHeroes.get(i)
                    ,gameStructure.playerOneCharacters[i]);
        }
        for (int i = 0; i < client2.chosenHeroes.size() ; i++) {
            assertEquals(client2.chosenHeroes.get(i)
                    ,gameStructure.playerTwoCharacters[i]);
        }
        //check the names of the two players
        assertEquals(client.username,gameStructure.playerOneName);
        assertEquals(client2.username,gameStructure.playerTwoName);
        assertEquals(1,client.playerNumber);
        assertEquals(2,client2.playerNumber);

        assertTrue(server.controller.loginFinished);

    }


}