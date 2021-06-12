package communication.messages.events.game;

import com.google.gson.Gson;
import communication.messages.*;
import communication.messages.enums.EntityID;
import communication.messages.enums.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEventTest {

    /**
     *
     * TestCase for the DisconnectEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void disconnectedEventTest(){
        DisconnectEvent disconnectEvent = new DisconnectEvent();
        Gson gson = new Gson();
        String jsonString = gson.toJson(disconnectEvent, DisconnectEvent.class);

        assertEquals("{" +
                "\"eventType\":\"DisconnectEvent\"" +
                "}",jsonString);
    }


    /**
     *
     * TestCase for the PauseStartEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void pauseStartEventTest(){
        PauseStartEvent pauseStartEvent = new PauseStartEvent();
        Gson gson = new Gson();
        String jsonString = gson.toJson(pauseStartEvent, PauseStartEvent.class);

        assertEquals("{" +
                "\"eventType\":\"PauseStartEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the PauseStopEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void pauseStopRequestTest(){
        PauseStopEvent pauseStopEvent = new PauseStopEvent();
        Gson gson = new Gson();
        String jsonString = gson.toJson(pauseStopEvent, PauseStopEvent.class);

        assertEquals("{" +
                "\"eventType\":\"PauseStopEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the RoundSetupEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void roundSetupEventTest(){
        int roundCount = 3;
        IDs firstID = new IDs(EntityID.NPC, 2);
        IDs secondID  = new IDs(EntityID.P1,3 );
        IDs thirdID = new IDs(EntityID.P2, 4);
        IDs fourthID = new IDs(EntityID.P2, 2);
        IDs fifthID = new IDs(EntityID.P2, 1);
        IDs sixthID = new IDs(EntityID.P1, 4);
        IDs[] characterOrder = {firstID, secondID, thirdID, fourthID, fifthID, sixthID};
        RoundSetupEvent roundSetupEvent = new RoundSetupEvent(roundCount, characterOrder);
        Gson gson = new Gson();
        String jsonString = gson.toJson(roundSetupEvent, RoundSetupEvent.class);

        assertEquals("{" +
                "\"roundCount\":3,\"characterOrder\":[" +
                "{\"entityID\":\"NPC\",\"ID\":2}," +
                "{\"entityID\":\"P1\",\"ID\":3}," +
                "{\"entityID\":\"P2\",\"ID\":4}," +
                "{\"entityID\":\"P2\",\"ID\":2}," +
                "{\"entityID\":\"P2\",\"ID\":1}," +
                "{\"entityID\":\"P1\",\"ID\":4}]," +
                "\"eventType\":\"RoundSetupEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the TimeoutEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void timeoutEvent(){
        String message = "You have been disconnected.";
        TimeoutEvent timeoutEvent = new TimeoutEvent(message);
        Gson gson = new Gson();
        String jsonString = gson.toJson(timeoutEvent, TimeoutEvent.class);

        assertEquals("{" +
                "\"message\":\"You have been disconnected.\"," +
                "\"eventType\":\"TimeoutEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the TimeoutWarning-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void timeoutWarningEventTest(){
        String message = "You will be disconnected soon.";
        int timeLeft = 1337;
        TimeoutWarningEvent timeoutWarningEvent = new TimeoutWarningEvent(message,timeLeft);
        Gson gson = new Gson();
        String jsonString = gson.toJson(timeoutWarningEvent, TimeoutWarningEvent.class);

        assertEquals("{" +
                "\"message\":\"You will be disconnected soon.\"," +
                "\"timeLeft\":1337," +
                "\"eventType\":\"TimeoutWarningEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the TurnEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void turnEventTest(){
        int turnCount = 6;
        IDs nextCharacter = new IDs(EntityID.P2,4);
        TurnEvent turnEvent = new TurnEvent(turnCount, nextCharacter);
        Gson gson = new Gson();
        String jsonString = gson.toJson(turnEvent, TurnEvent.class);

        assertEquals("{" +
                "\"turnCount\":6," +
                "\"nextCharacter\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"eventType\":\"TurnEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the TurnTimeoutEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void turnTimeoutEventTest(){
        TurnTimeoutEvent turnTimeoutEvent = new TurnTimeoutEvent();
        Gson gson = new Gson();
        String jsonString = gson.toJson(turnTimeoutEvent, TurnTimeoutEvent.class);

        assertEquals("{" +
                "\"eventType\":\"TurnTimeoutEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the WinEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void winEventTest(){
        WinEvent winEvent = new WinEvent(1);
        Gson gson = new Gson();
        String jsonString = gson.toJson(winEvent, WinEvent.class);

        assertEquals("{" +
                "\"playerWon\":1," +
                "\"eventType\":\"WinEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the GameEvent-Class inside a MessageStructure and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void messageStructureGameEventTest(){

        DisconnectEvent disconnectEvent = new DisconnectEvent();
        Gson gson = new Gson();
        String jsonString = gson.toJson(disconnectEvent, DisconnectEvent.class);

        assertEquals("{" +
                "\"eventType\":\"DisconnectEvent\"" +
                "}",jsonString);

        PauseStartEvent pauseStartEvent = new PauseStartEvent();
        jsonString = gson.toJson(pauseStartEvent, PauseStartEvent.class);

        assertEquals("{" +
                "\"eventType\":\"PauseStartEvent\"" +
                "}",jsonString);

        PauseStopEvent pauseStopEvent = new PauseStopEvent();
         jsonString = gson.toJson(pauseStopEvent, PauseStopEvent.class);

        assertEquals("{" +
                "\"eventType\":\"PauseStopEvent\"" +
                "}",jsonString);

        int roundCount = 3;
        IDs firstID = new IDs(EntityID.NPC, 2);
        IDs secondID  = new IDs(EntityID.P1,3 );
        IDs thirdID = new IDs(EntityID.P2, 4);
        IDs fourthID = new IDs(EntityID.P2, 2);
        IDs fifthID = new IDs(EntityID.P2, 1);
        IDs sixthID = new IDs(EntityID.P1, 4);
        IDs[] characterOrder = {firstID, secondID, thirdID, fourthID, fifthID, sixthID};
        RoundSetupEvent roundSetupEvent = new RoundSetupEvent(roundCount, characterOrder);
        jsonString = gson.toJson(roundSetupEvent, RoundSetupEvent.class);

        assertEquals("{" +
                "\"roundCount\":3,\"characterOrder\":[" +
                "{\"entityID\":\"NPC\",\"ID\":2}," +
                "{\"entityID\":\"P1\",\"ID\":3}," +
                "{\"entityID\":\"P2\",\"ID\":4}," +
                "{\"entityID\":\"P2\",\"ID\":2}," +
                "{\"entityID\":\"P2\",\"ID\":1}," +
                "{\"entityID\":\"P1\",\"ID\":4}]," +
                "\"eventType\":\"RoundSetupEvent\"" +
                "}",jsonString);

        String message = "You have been disconnected.";
        TimeoutEvent timeoutEvent = new TimeoutEvent(message);
        jsonString = gson.toJson(timeoutEvent, TimeoutEvent.class);

        assertEquals("{" +
                "\"message\":\"You have been disconnected.\"," +
                "\"eventType\":\"TimeoutEvent\"" +
                "}",jsonString);

        message = "You will be disconnected soon.";
        int timeLeft = 1337;
        TimeoutWarningEvent timeoutWarningEvent = new TimeoutWarningEvent(message,timeLeft);
        jsonString = gson.toJson(timeoutWarningEvent, TimeoutWarningEvent.class);

        assertEquals("{" +
                "\"message\":\"You will be disconnected soon.\"," +
                "\"timeLeft\":1337," +
                "\"eventType\":\"TimeoutWarningEvent\"" +
                "}",jsonString);

        int turnCount = 6;
        IDs nextCharacter = new IDs(EntityID.P2,4);
        TurnEvent turnEvent = new TurnEvent(turnCount, nextCharacter);
        jsonString = gson.toJson(turnEvent, TurnEvent.class);

        assertEquals("{" +
                "\"turnCount\":6," +
                "\"nextCharacter\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"eventType\":\"TurnEvent\"" +
                "}",jsonString);

        TurnTimeoutEvent turnTimeoutEvent = new TurnTimeoutEvent();
        jsonString = gson.toJson(turnTimeoutEvent, TurnTimeoutEvent.class);

        assertEquals("{" +
                "\"eventType\":\"TurnTimeoutEvent\"" +
                "}",jsonString);


        WinEvent winEvent = new WinEvent(1);
        jsonString = gson.toJson(winEvent, WinEvent.class);

        assertEquals("{" +
                "\"playerWon\":1," +
                "\"eventType\":\"WinEvent\"" +
                "}",jsonString);

        Message[] messages = {disconnectEvent, pauseStartEvent, pauseStopEvent, roundSetupEvent, timeoutEvent,
                timeoutWarningEvent, turnEvent, turnTimeoutEvent, winEvent};
        MessageStructure messageStructure = new MessageStructure(MessageType.EVENTS, messages, null, null);
        jsonString = gson.toJson(messageStructure, MessageStructure.class);
        assertEquals("{" +
                "\"messageType\":\"EVENTS\"," +
                "\"messages\":[" +

                "{" +
                "\"eventType\":\"DisconnectEvent\"" +
                "},"+

                "{" +
                "\"eventType\":\"PauseStartEvent\"" +
                "},"+

                "{" +
                "\"eventType\":\"PauseStopEvent\"" +
                "},"+

                "{" +
                "\"roundCount\":3,\"characterOrder\":[" +
                "{\"entityID\":\"NPC\",\"ID\":2}," +
                "{\"entityID\":\"P1\",\"ID\":3}," +
                "{\"entityID\":\"P2\",\"ID\":4}," +
                "{\"entityID\":\"P2\",\"ID\":2}," +
                "{\"entityID\":\"P2\",\"ID\":1}," +
                "{\"entityID\":\"P1\",\"ID\":4}]," +
                "\"eventType\":\"RoundSetupEvent\"" +
                "},"+

                "{" +
                "\"message\":\"You have been disconnected.\"," +
                "\"eventType\":\"TimeoutEvent\"" +
                "},"+

                "{" +
                "\"message\":\"You will be disconnected soon.\"," +
                "\"timeLeft\":1337," +
                "\"eventType\":\"TimeoutWarningEvent\"" +
                "},"+

                "{" +
                "\"turnCount\":6," +
                "\"nextCharacter\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"eventType\":\"TurnEvent\"" +
                "},"+

                "{" +
                "\"eventType\":\"TurnTimeoutEvent\"" +
                "},"+

                "{" +
                "\"playerWon\":1," +
                "\"eventType\":\"WinEvent\"" +
                "}"+

                "]" +
                "}",jsonString);

        ExtractorMessageStructure extractorMessageStructure = gson.fromJson(jsonString, ExtractorMessageStructure.class);
        MessageStructure messageStructure1 = extractorMessageStructure.toMessageStructure();
        String jsonString2 = gson.toJson(messageStructure1, MessageStructure.class);
        assertEquals(jsonString, jsonString2);

    }

}