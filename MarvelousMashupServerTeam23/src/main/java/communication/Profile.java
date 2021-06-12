package communication;


import org.java_websocket.WebSocket;

import java.util.Objects;

/**
 * This class is used to store the data to identify and refer to a client
 * @author Adrian Groeber
 */
public class Profile {
    public WebSocket conn;
    public final String name;
    public final String deviceID;

    /**
     * Constructor of the Profile class
     * @author Adrian Groeber
     * @param conn WebSocket of the connected Client
     * @param name the name of the client
     * @param deviceID the device ID of the client
     */
    public Profile(WebSocket conn, String name, String deviceID) {
        this.conn = conn;
        this.name = name;
        this.deviceID = deviceID;
    }

    /**
     * This method is used to compare 2 profiles and check if they have the same name and deviceID. If they have, they
     * belong to the same client.
     *
     * @author Adrian Groeber
     * @param o Object which is compared
     * @return returns true if the compared class parameters are similar else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return name.equals(profile.name) && deviceID.equals(profile.deviceID);
    }


}
