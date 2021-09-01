/**
 *
 * HelloServer is one of the Message Types. Its used from the client to initiate the connection to the server.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class HelloServer {

    /**
     * the messageType of this message is always MessageType.HELLO_SERVER
     */
    public MessageType messageType =  MessageType.HELLO_SERVER;
    /**
     * name of the client, which the user chose
     */
    public string name;
    /**
     * the deviceID of the client
     */
    public string deviceID;
    /**
     * optional String that might be used
     */
    public string optionals;


    /**
     *
     * The constructor of the HelloServer-Class. Creates a HelloServer-Object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param name name of the client, which the user chose
     * @param deviceID the deviceID of the client
     * @param optionals optional String that might be used
     *
     */
    public HelloServer(string name, string deviceID, string optionals = null){
        this.name = name;
        this.deviceID = deviceID;
        this.optionals = optionals;

    }

}
