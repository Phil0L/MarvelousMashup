package communication.messages.login;

import communication.messages.enums.MessageType;

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
    public String name;
    /**
     * the deviceID of the client
     */
    public String deviceID;
    /**
     * optional String that might be used
     */
    public String optionals;


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
    public HelloServer(String name, String deviceID, String optionals){
        this.name = name;
        this.deviceID = deviceID;
        this.optionals = optionals;

    }

}
