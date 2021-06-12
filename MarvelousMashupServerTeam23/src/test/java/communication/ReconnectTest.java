package communication;

import com.google.gson.Gson;
import communication.messages.BasicMessage;
import communication.messages.ExtractorMessage;
import communication.messages.ExtractorMessageStructure;
import communication.messages.MessageStructure;
import communication.messages.enums.EventType;
import communication.messages.enums.MessageType;
import communication.messages.enums.Role;
import communication.messages.login.*;
import communication.mock.TestClient;
import org.java_websocket.framing.CloseFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parameter.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrated test for a reconnection situation
 *
 * @author Benno Hoelz
 * @author Matthias Ruf
 *
 */
public class ReconnectTest {

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

    /**
     * Time to wait until the TestClient and Server finish to communicate
     */
    private static final long COMPUTATION_DURATION = 500;

    /**
     * initial port to use for testing (will be incremented after each test)
     */
    private static int port = 1218;


    /**
     * The setup methode should be executed before each @Test method in the current class.
     * That this happens the @BeforeEach annotation is used.
     *
     * @author Matthias Ruf
     */
    @BeforeEach
    void setUp() {
        // show which port is being used:
        System.out.println("Port: "+port);

        // create a new game-configuration:
        String scenarioConfigPath = "asgard.json";
        String characterConfigPath = "marvelheros.json";
        String matchConfigPatch = "matchconfig_1.json";
        try {
            this.configuration = new Configuration(scenarioConfigPath, characterConfigPath, matchConfigPatch);
        } catch (IOException e) {
            fail("IOException during the Configuration parsing");
        }
        this.server = new NetworkHandler(this.configuration, port);
        this.server.start();

        //create a gson parser object
        this.gson = new Gson();

        // create a TestClient instance (PLAYER)
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);
        // connect the client to the server
        assert client.connect(200);


        // wait for a moment
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }



        // create a second TestClient instance (PLAYER)
        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
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


        // create a third TestClient instance (SPECTATOR)
        try {
            spectatorClient = new TestClient(new URI(String.format("ws://%s:%d/", "localhost", port)), Role.SPECTATOR,
                    "SpectatorClient");
        } catch (URISyntaxException e) {
            fail("Test client object could not be created - spectator test client");
        }
        // Check whether a TestClient has been created
        assertNotNull(spectatorClient);
        //client2.username = "TestClient2";
        // connect the client to the server
        assert spectatorClient.connect(200);


        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        /*
        ------------------------------ send a characterSelection from both of the clients ------------------------------
         */
        Boolean[] example_selection = {true,true,true,true,true,true,false,false,false,false,false,false};
        client.sendCharacterSelection(example_selection);
        // this pause makes that the first client gets the ConfirmSelection
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


    }

    /**
     * The setup methode should be executed after each @Test method in the current class.
     * That this happens the @AfterEach annotation is used.
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     */
    @AfterEach
    void tearDown() {
        try {
            this.server.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // increment port for next test:
        port++;
    }

    /**
     * Test method for testing the process of a client reconnecting to the server in case the client wants to resume
     * the running game (player one)
     *
     * @author Benno Hoelz
     * @author Matthias Ruf
     */
    @Test
    public void reconnectTrueTestPlayerOne(){
        // save old device id for reconnect
        String clientID = this.client.deviceID;

        // close WebSocket connection but not network standard document connection
        this.client.close();
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(this.client.getSocket().isClosed());


        // override the old client by  a new one (resets all parameters)
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
            client.deviceID = clientID;
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);

        // set reconnect:
        client.reconnect = true;

        // connect the client to the server
        assert client.connect(200);
        assertTrue(this.client.getSocket().isConnected());
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // test runningGame state of received helloClient message:
        HelloClient helloClient = gson.fromJson(client.getMessagesIn().get(0), HelloClient.class);
        assertTrue(helloClient.runningGame);

        // wait for a moment
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        // check if client received GeneralAssignment:
        BasicMessage generalAssignment = gson.fromJson(client.getMessagesIn().get(1), GeneralAssignment.class);
        assertEquals(MessageType.GENERAL_ASSIGNMENT, generalAssignment.messageType);

        // check if client received GameStructure:
        BasicMessage gameStructure = gson.fromJson(client.getMessagesIn().get(2), GameAssignment.class);
        assertEquals(MessageType.GAME_STRUCTURE, gameStructure.messageType);


        // print all incoming messages for client:
        System.out.println("----------messagesIn (reconnect==true)----------");
        for(String message : client.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("------------------------------------------------");

        // print all outgoing messages from client:
        System.out.println("----------messagesOut (reconnect==true)----------");
        for(String message : client.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("-------------------------------------------------");
    }


    /**
     * Test method for testing the process of a client reconnecting to the server in case the client wants to resume
     * the running game (player two)
     *
     * @author Benno Hoelz
     */
    @Test
    public void reconnectTrueTestPlayerTwo(){

        // save old deviceID for reconnect
        String clientID = this.client2.deviceID;

        // close WebSocket connection but not network standard document connection
        this.client2.close();
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(this.client2.getSocket().isClosed());


        // override the old client by  a new one (resets all parameters)
        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
            client2.username = "TestClient2";
            client2.deviceID = clientID;
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client2);

        // set reconnect:
        client2.reconnect = true;

        // connect the client to the server
        assert client2.connect(200);
        assertTrue(this.client2.getSocket().isConnected());
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // test runningGame state of received helloClient message:
        HelloClient helloClient = gson.fromJson(client2.getMessagesIn().get(0), HelloClient.class);
        assertTrue(helloClient.runningGame);


        // wait for a moment
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // check if client received GeneralAssignment:
        BasicMessage generalAssignment = gson.fromJson(client2.getMessagesIn().get(1), GeneralAssignment.class);
        assertEquals(MessageType.GENERAL_ASSIGNMENT, generalAssignment.messageType);


        // check if client received GameStructure:
        BasicMessage gameStructure = gson.fromJson(client2.getMessagesIn().get(2), GameAssignment.class);
        assertEquals(MessageType.GAME_STRUCTURE, gameStructure.messageType);


        // print all incoming messages for client:
        System.out.println("----------messagesIn (reconnect==true)----------");
        for(String message : client2.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("------------------------------------------------");

        // print all outgoing messages from client:
        System.out.println("----------messagesOut (reconnect==true)----------");
        for(String message : client2.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("-------------------------------------------------");
    }

    /**
     * Test method for testing the process of a client reconnecting to the server in case the client wants to resume
     * the running game and is not a registered player
     *
     * @author Benno Hoelz
     */
    @Test
    public void reconnectTrueTestNotAPlayer(){

        // close WebSocket connection but not network standard document connection
        this.client.close();
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(this.client.getSocket().isClosed());


        // override the old client by  a new one (resets all parameters), do not set device id
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);

        // set reconnect:
        client.reconnect = true;

        // connect the client to the server
        assert client.connect(200);
        assertTrue(this.client.getSocket().isConnected());
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // test runningGame state of received helloClient message:
        HelloClient helloClient = gson.fromJson(client.getMessagesIn().get(0), HelloClient.class);
        assertTrue(helloClient.runningGame);


        // wait for a moment
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // check if client received GoodbyeClient:
        BasicMessage goodbyeClient = gson.fromJson(client.getMessagesIn().get(1), GoodbyeClient.class);
        assertEquals(MessageType.GOODBYE_CLIENT, goodbyeClient.messageType);


        // print all incoming messages for client:
        System.out.println("----------messagesIn (reconnect==true)----------");
        for(String message : client.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("------------------------------------------------");

        // print all outgoing messages from client:
        System.out.println("----------messagesOut (reconnect==true)----------");
        for(String message : client.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("-------------------------------------------------");
    }


    /**
     * Test method for testing the process of a client reconnecting to the server in case the client does not want to
     * resume the running game (player one)
     *
     * @author Benno Hoelz
     */
    @Test
    public void reconnectFalseTestPlayerOne(){
        // save old deviceID for reconnect
        String clientID = this.client.deviceID;

        // close WebSocket connection but not network standard document connection
        this.client.close();
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(this.client.getSocket().isClosed());


        // override the old client by  a new one (resets all parameters)
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
            client.deviceID = clientID;
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);

        // set reconnect:
        client.reconnect = false;

        // connect the client to the server
        assert client.connect(200);
        assertTrue(this.client.getSocket().isConnected());
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // check if client received Ack and WinEvent:
        ExtractorMessageStructure extractorMessageStructure = gson.fromJson(client.getMessagesIn().get(1), ExtractorMessageStructure.class);
        MessageStructure messageStructure = extractorMessageStructure.toMessageStructure();
        assertEquals(EventType.Ack, messageStructure.messages[0].eventType);
        assertEquals(EventType.WinEvent, messageStructure.messages[1].eventType);
        assertEquals(EventType.DisconnectEvent, messageStructure.messages[2].eventType);


        // check if client2 received WinEvent:
        ExtractorMessageStructure extractorMessageStructure2 = gson.fromJson(client2.getMessagesIn().get(client2.getMessagesIn().size()-2), ExtractorMessageStructure.class);
        MessageStructure messageStructure2 = extractorMessageStructure2.toMessageStructure();
        assertEquals(EventType.WinEvent, messageStructure2.messages[0].eventType);
        assertEquals(EventType.DisconnectEvent, messageStructure2.messages[1].eventType);

        // check if client2 received GoodbyeClient:
        BasicMessage goodbyeClient = gson.fromJson(client2.getMessagesIn().get(client2.getMessagesIn().size()-1), GoodbyeClient.class);
        assertEquals(MessageType.GOODBYE_CLIENT, goodbyeClient.messageType);


        // check if spectatorClient received WinEvent:
        ExtractorMessageStructure extractorMessageStructure3 = gson.fromJson(spectatorClient.getMessagesIn().get(spectatorClient.getMessagesIn().size()-2), ExtractorMessageStructure.class);
        MessageStructure messageStructure3 = extractorMessageStructure3.toMessageStructure();
        assertEquals(EventType.WinEvent, messageStructure3.messages[0].eventType);
        assertEquals(EventType.DisconnectEvent, messageStructure3.messages[1].eventType);

        // check if spectatorClient received GoodbyeClient:
        BasicMessage goodbyeClient2 = gson.fromJson(spectatorClient.getMessagesIn().get(spectatorClient.getMessagesIn().size()-1), GoodbyeClient.class);
        assertEquals(MessageType.GOODBYE_CLIENT, goodbyeClient2.messageType);



        // print all incoming messages for client:
        System.out.println("----------messagesIn (reconnect==false)------------");
        for(String message : client.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all outgoing messages from client:
        System.out.println("----------messagesOut (reconnect==false)-----------");
        for(String message : client.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all incoming messages for client2:
        System.out.println("----------messagesIn2 (reconnect==false)-----------");
        for(String message : client2.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all outgoing messages from client2:
        System.out.println("----------messagesOut2 (reconnect==false)----------");
        for(String message : client2.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all incoming messages for spectatorClient:
        System.out.println("----------messagesInSpectator (reconnect==false)-----------");
        for(String message : spectatorClient.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("-----------------------------------------------------------");

        // print all outgoing messages from spectatorClient:
        System.out.println("----------messagesOutSpectator (reconnect==false)----------");
        for(String message : spectatorClient.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("-----------------------------------------------------------");

    }


    /**
     * Test method for testing the process of a client reconnecting to the server in case the client does not want to
     * resume the running game (player two)
     *
     * @author Benno Hoelz
     */
    @Test
    public void reconnectFalseTestPlayerTwo(){
        // save old deviceID for reconnect
        String clientID = this.client2.deviceID;

        // close WebSocket connection but not network standard document connection
        this.client2.close();
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(this.client2.getSocket().isClosed());


        // override the old client2 by  a new one (resets all parameters)
        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
            client2.deviceID = clientID;
            client2.username = "TestClient2";
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client2);

        // set reconnect:
        client2.reconnect = false;

        // connect the client2 to the server
        assert client2.connect(200);
        assertTrue(this.client2.getSocket().isConnected());
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        // check if client2 received Ack and WinEvent:
        ExtractorMessageStructure extractorMessageStructure = gson.fromJson(client2.getMessagesIn().get(1), ExtractorMessageStructure.class);
        MessageStructure messageStructure = extractorMessageStructure.toMessageStructure();
        assertEquals(EventType.Ack, messageStructure.messages[0].eventType);
        assertEquals(EventType.WinEvent, messageStructure.messages[1].eventType);
        assertEquals(EventType.DisconnectEvent, messageStructure.messages[2].eventType);


        // check if client received WinEvent:
        ExtractorMessageStructure extractorMessageStructure2 = gson.fromJson(client.getMessagesIn().get(client.getMessagesIn().size()-2), ExtractorMessageStructure.class);
        MessageStructure messageStructure2 = extractorMessageStructure2.toMessageStructure();
        assertEquals(EventType.WinEvent, messageStructure2.messages[0].eventType);
        assertEquals(EventType.DisconnectEvent, messageStructure2.messages[1].eventType);

        // check if client received GoodbyeClient:
        BasicMessage goodbyeClient = gson.fromJson(client.getMessagesIn().get(client.getMessagesIn().size()-1), GoodbyeClient.class);
        assertEquals(MessageType.GOODBYE_CLIENT, goodbyeClient.messageType);


        // check if spectatorClient received WinEvent:
        ExtractorMessageStructure extractorMessageStructure3 = gson.fromJson(spectatorClient.getMessagesIn().get(spectatorClient.getMessagesIn().size()-2), ExtractorMessageStructure.class);
        MessageStructure messageStructure3 = extractorMessageStructure3.toMessageStructure();
        assertEquals(EventType.WinEvent, messageStructure3.messages[0].eventType);
        assertEquals(EventType.DisconnectEvent, messageStructure3.messages[1].eventType);

        // check if spectatorClient received GoodbyeClient:
        BasicMessage goodbyeClient2 = gson.fromJson(spectatorClient.getMessagesIn().get(spectatorClient.getMessagesIn().size()-1), GoodbyeClient.class);
        assertEquals(MessageType.GOODBYE_CLIENT, goodbyeClient2.messageType);



        // print all incoming messages for client:
        System.out.println("----------messagesIn (reconnect==false)------------");
        for(String message : client.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all outgoing messages from client:
        System.out.println("----------messagesOut (reconnect==false)-----------");
        for(String message : client.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all incoming messages for client2:
        System.out.println("----------messagesIn2 (reconnect==false)-----------");
        for(String message : client2.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all outgoing messages from client2:
        System.out.println("----------messagesOut2 (reconnect==false)----------");
        for(String message : client2.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all incoming messages for spectatorClient:
        System.out.println("----------messagesInSpectator (reconnect==false)-----------");
        for(String message : spectatorClient.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("-----------------------------------------------------------");

        // print all outgoing messages from spectatorClient:
        System.out.println("----------messagesOutSpectator (reconnect==false)----------");
        for(String message : spectatorClient.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("-----------------------------------------------------------");

    }

    /**
     * Test method for testing the process of a client reconnecting to the server in case the client does not want to
     * resume the running game and is not a registered player
     *
     * @author Benno Hoelz
     */
    @Test
    public void reconnectFalseTestNotAPlayer(){
        // close WebSocket connection but not network standard document connection
        this.client.close();
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }
        assertTrue(this.client.getSocket().isClosed());


        // override the old client by  a new one (resets all parameters), do not set device id
        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",port) ));
        } catch (URISyntaxException e) {
            fail("Test client object could not be created");
        }
        // Check whether a TestClient has been created
        assertNotNull(client);

        // set reconnect:
        client.reconnect = false;

        // connect the client to the server
        assert client.connect(200);
        assertTrue(this.client.getSocket().isConnected());
        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        // test runningGame state of received helloClient message:
        HelloClient helloClient = gson.fromJson(client.getMessagesIn().get(0), HelloClient.class);
        assertTrue(helloClient.runningGame);

        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }

        // check if client received GoodbyeClient:
        BasicMessage goodbyeClient = gson.fromJson(client.getMessagesIn().get(1), GoodbyeClient.class);
        assertEquals(MessageType.GOODBYE_CLIENT, goodbyeClient.messageType);



        // print all incoming messages for client:
        System.out.println("----------messagesIn (reconnect==false)------------");
        for(String message : client.getMessagesIn()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

        // print all outgoing messages from client:
        System.out.println("----------messagesOut (reconnect==false)-----------");
        for(String message : client.getMessagesOut()){
            System.out.println(message);
        }
        System.out.println("---------------------------------------------------");

    }

}
