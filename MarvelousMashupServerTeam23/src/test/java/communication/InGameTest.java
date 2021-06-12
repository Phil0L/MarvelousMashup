package communication;

import com.google.gson.Gson;
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
 * Integrated test for InGame Requests
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class InGameTest {

    private NetworkHandler server;

    private  Configuration configuration;

    private Gson gson;

    TestClient client = null;

    TestClient client2 = null;

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

        // create a TestClient instance

        try {
            client = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218) ));
            client.ingame = true;
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
        --------------------------------------- Second Client connects -------------------------------------------------
         */
        // create a second TestClient instance

        try {
            client2 = new TestClient(new URI( String.format("ws://%s:%d/","localhost",1218) ));
            client2.ingame = true;
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
        ------------------------------ send a characterSelection from both of the clients ------------------------------
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

        assertTrue(server.controller.loginFinished);


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
     *
     */
    @Test
    public void inGameTest(){

        // at the beginning of a match all values are on the initial position
       /* assertEquals(0,this.server.controller.model.causedDamagePlayerOne);
        assertEquals(0,this.server.controller.model.causedDamagePlayerTwo);
        assertEquals(0,this.server.controller.model.knockedOpHeroPlayerOne);
        assertEquals(0,this.server.controller.model.knockedOpHeroPlayerTwo);
        assertEquals(0,this.server.controller.model.round);
        */

        try {
            Thread.sleep(COMPUTATION_DURATION);
        } catch (InterruptedException e) {
            fail("InterruptedException: " + e);
        }


        while (!client.getSocket().isClosed() && !client2.isClosed());

    }


}
