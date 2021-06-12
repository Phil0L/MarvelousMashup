package communication.messages.events.notification;

import com.google.gson.Gson;
import communication.messages.events.game.PauseStartEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationEventTest {

    /**
     *
     * TestCase for the Ack-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void ackTest(){
        Ack ack = new Ack();
        Gson gson = new Gson();
        String jsonString = gson.toJson(ack, Ack.class);

        assertEquals("{" +
                "\"eventType\":\"Ack\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the Nack-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void nackTest(){
        Nack nack = new Nack();
        Gson gson = new Gson();
        String jsonString = gson.toJson(nack, Nack.class);

        assertEquals("{" +
                "\"eventType\":\"Nack\"" +
                "}",jsonString);
    }

}