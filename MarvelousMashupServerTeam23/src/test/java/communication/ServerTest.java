package communication;

import communication.mock.TestClient;
import org.junit.jupiter.api.Test;
import parameter.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerTest {

  private static NetworkHandler server;
  private static Configuration configuration;

  public static void main(String[] args){
    String scenarioConfigPath = "asgard.json";
    String characterConfigPath = "marvelheros.json";
    String matchConfigPatch = "matchconfig_1.json";

    try {
      configuration = new Configuration(scenarioConfigPath, characterConfigPath, matchConfigPatch);
    } catch (IOException e) {
      fail("IOException during the Configuration parsing");
    }

    server = new NetworkHandler(configuration);
    server.start();

  }

  @Test
  public void connectClient(){
    // create a TestClient instance
    TestClient client = null;
    try {
      client = new TestClient(new URI(String.format("ws://%s:%d/","localhost", 1218)));
    } catch (URISyntaxException e) {
      fail("Test client object could not be created");
    }
    // Check whether a TestClient has been created
    assertNotNull(client);
    // connect the client to the server
    client.connect(20000);
  }

}
