package communication;


import com.google.gson.Gson;
import communication.messages.ExtractorMessageStructure;
import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.MessageStructure;
import communication.messages.enums.EntityID;
import communication.messages.enums.EventType;
import communication.messages.enums.MessageType;
import communication.messages.enums.Role;
import communication.messages.events.character.MeleeAttackEvent;
import communication.messages.events.character.MoveEvent;
import communication.mock.TestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parameter.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This class tests single methods of the NetworkHandler.
 *
 * @author Matthias Ruf
 * @author Benno Hoelz
 * @author Sarah Engele
 */
class BroadcastTest {

    /**
     * create a server instance
     */
    private NetworkHandler server;

    /**
     * create an empty game-configuration
     */
    private  Configuration configuration;

    /**
     * create a Gson-Parser
     */
    private Gson gson;

    /**
     * create TestClients
     */
    private TestClient client;
    private TestClient client2;
    private TestClient spectatorClient;
    private TestClient spectatorClient2;
    private TestClient spectatorClient3;
    private TestClient spectatorClient4;

    /**
     * Time to wait until the TestClient and Server finish to communicat
     */
    private static final long COMPUTATION_DURATION = 200;

    /**
     * The setup methode should be executed before each @Test method in the current class.
     * That this happens the @BeforeEach annotation is used.
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     * @author Sarah Engele
     */
    @BeforeEach
    public void setUp(){
        // create a new game-configuration:
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


        // create a sixth TestClient instance (SPECTATOR)
        try {
            spectatorClient4 = new TestClient(new URI(String.format("ws://%s:%d/", "localhost", 1218)),Role.SPECTATOR, "SpectatorCLient4");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - spectator test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(spectatorClient4);
        //client2.username = "TestClient2";
        // connect the client to the server
        assert spectatorClient4.connect(200);


        // wait for a moment
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // create a TestClient instance (PLAYER)
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218)),Role.PLAYER, "TestClient");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);
        // connect the client to the server
        assert client.connect(200);


        /*
        Give client and server a few seconds time for the connection establishment
        The reaction for the client (the first TestClient instance) should be
        Server --> GameAssignment --> Client
         */
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }



        // create a third TestClient instance (SPECTATOR)
        try {
            spectatorClient = new TestClient(new URI(String.format("ws://%s:%d/", "localhost", 1218)), Role.SPECTATOR, "SpectatorClient");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - spectator test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(spectatorClient);
        //client2.username = "TestClient2";
        // connect the client to the server
        assert spectatorClient.connect(200);


        // wait for a moment
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // create a second TestClient instance (PLAYER)
        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218)),Role.PLAYER,"TestClient2");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - second test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(client2);
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



        // create a fourth TestClient instance (SPECTATOR)
        try {
            spectatorClient2 = new TestClient(new URI(String.format("ws://%s:%d/", "localhost", 1218)),Role.SPECTATOR, "SpectatorCLient2");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - spectator test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(spectatorClient2);
        //client2.username = "TestClient2";
        // connect the client to the server
        assert spectatorClient2.connect(200);

        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }



        /*
        ------------------------------ send a characterSelection from both of the player clients ------------------------------
         */
        Boolean[] example_selection = {true,true,true,true,true,true,false,false,false,false,false,false};
        client.sendCharacterSelection(example_selection);
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        client2.sendCharacterSelection(example_selection);
        // this pause makes that the first client gets the ConfirmSelection
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        // check if login finished successfully
        assertTrue(server.controller.loginFinished);

        // create a fifth TestClient instance (SPECTATOR)
        try {
            spectatorClient3 = new TestClient(new URI(String.format("ws://%s:%d/", "localhost", 1218)),Role.SPECTATOR, "SpectatorCLient3");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - spectator test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(spectatorClient3);
        //client2.username = "TestClient2";
        // connect the client to the server
        assert spectatorClient3.connect(200);

        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
    }

    /**
     * The setup methode should be executed after each @Test method in the current class.
     * That this happens the @AfterEach annotation is used.
     *
     * @author Matthias Ruf
     */
    @AfterEach
    public void tearDown(){
        try {
            this.server.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for sending a broadcast message to all clients (the two player clients and all spectator clients)
     *
     * @author Benno Hoelz
     * @author Sarah Engele
     */
    @Test
    public void broadcastTest(){
        // create a broadcast message:
        IDs originEntity = new IDs(EntityID.P1,3 );
        IDs targetEntity = new IDs(EntityID.P2, 4);
        int[] originField = {3,7};
        int[] targetField = {3,6};
        int amount = 2;
        String customContentType = "Team1553CustomContent";
        String customContent = "nothing";
        MeleeAttackEvent meleeAttackEvent = new MeleeAttackEvent(originEntity, targetEntity, originField,
                targetField,amount);

        MoveEvent moveEvent = new MoveEvent(originEntity, originField, targetField);
        Message[] messages = {meleeAttackEvent, moveEvent};
        MessageStructure messageStructure = new MessageStructure(MessageType.EVENTS, messages,
                customContentType, customContent);

        System.out.println("---BROADCAST:---");

        // send broadcast message:
        server.broadcast(messageStructure, server.checkPlayerIdentity(server.profileList.get(1)));

        // wait for a moment:
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // print messages that spectatorClient4 received:
        System.out.println("---messagesIn spectatorClient4---");
        for(String message : spectatorClient4.getMessagesIn()){
            System.out.println(message);
        }

        // check Messages that spectatorClient4 received:
        ExtractorMessageStructure extractorMessageStructure6 = gson.fromJson(spectatorClient4.getMessagesIn().get(spectatorClient4.getMessagesIn().size()-1), ExtractorMessageStructure.class);
        MessageStructure messageStructure6 = extractorMessageStructure6.toMessageStructure();
        assertEquals(EventType.MeleeAttackEvent, messageStructure6.messages[0].eventType);
        assertEquals(EventType.MoveEvent, messageStructure6.messages[1].eventType);


        // print messages that client received:
        System.out.println("---messagesIn client---");
        for(String message : client.getMessagesIn()){
            System.out.println(message);
        }

        // check Messages that client received:
        ExtractorMessageStructure extractorMessageStructure1 = gson.fromJson(client.getMessagesIn().get(client.getMessagesIn().size()-1), ExtractorMessageStructure.class);
        MessageStructure messageStructure1 = extractorMessageStructure1.toMessageStructure();
        assertEquals(EventType.Ack, messageStructure1.messages[0].eventType);
        assertEquals(EventType.MeleeAttackEvent, messageStructure1.messages[1].eventType);
        assertEquals(EventType.MoveEvent, messageStructure1.messages[2].eventType);


        // print messages that client2 received:
        System.out.println("---messagesIn client2---");
        for(String message : client2.getMessagesIn()){
            System.out.println(message);
        }

        // check Messages that client2 received:
        ExtractorMessageStructure extractorMessageStructure2 = gson.fromJson(client2.getMessagesIn().get(client2.getMessagesIn().size()-1), ExtractorMessageStructure.class);
        MessageStructure messageStructure2 = extractorMessageStructure2.toMessageStructure();
        assertEquals(EventType.MeleeAttackEvent, messageStructure2.messages[0].eventType);
        assertEquals(EventType.MoveEvent, messageStructure2.messages[1].eventType);


        // print messages that spectatorClient received:
        System.out.println("---messagesIn spectatorClient---");
        for(String message : spectatorClient.getMessagesIn()){
            System.out.println(message);
        }

        // check Messages that spectatorClient received:
        ExtractorMessageStructure extractorMessageStructure3 = gson.fromJson(spectatorClient.getMessagesIn().get(spectatorClient.getMessagesIn().size()-1), ExtractorMessageStructure.class);
        MessageStructure messageStructure3 = extractorMessageStructure3.toMessageStructure();
        assertEquals(EventType.MeleeAttackEvent, messageStructure3.messages[0].eventType);
        assertEquals(EventType.MoveEvent, messageStructure3.messages[1].eventType);


        // print messages that spectatorClient2 received:
        System.out.println("---messagesIn spectatorClient2---");
        for(String message : spectatorClient2.getMessagesIn()){
            System.out.println(message);
        }

        // check Messages that spectatorClient2 received:
        ExtractorMessageStructure extractorMessageStructure4 = gson.fromJson(spectatorClient2.getMessagesIn().get(spectatorClient2.getMessagesIn().size()-1), ExtractorMessageStructure.class);
        MessageStructure messageStructure4 = extractorMessageStructure4.toMessageStructure();
        assertEquals(EventType.MeleeAttackEvent, messageStructure4.messages[0].eventType);
        assertEquals(EventType.MoveEvent, messageStructure4.messages[1].eventType);


        // print messages that spectatorClient3 received:
        System.out.println("---messagesIn spectatorClient3---");
        for(String message : spectatorClient3.getMessagesIn()){
            System.out.println(message);
        }

        // check Messages that spectatorClient3 received:
        ExtractorMessageStructure extractorMessageStructure5 = gson.fromJson(spectatorClient3.getMessagesIn().get(spectatorClient3.getMessagesIn().size()-1), ExtractorMessageStructure.class);
        MessageStructure messageStructure5 = extractorMessageStructure5.toMessageStructure();
        assertEquals(EventType.MeleeAttackEvent, messageStructure5.messages[0].eventType);
        assertEquals(EventType.MoveEvent, messageStructure5.messages[1].eventType);

    }

}