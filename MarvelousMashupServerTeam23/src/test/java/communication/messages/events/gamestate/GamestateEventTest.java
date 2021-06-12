package communication.messages.events.gamestate;

import com.google.gson.Gson;
import communication.messages.IDs;
import communication.messages.enums.EntityID;
import communication.messages.objects.Entities;
import communication.messages.objects.InGameCharacter;
import communication.messages.objects.InfinityStone;
import communication.messages.objects.Rock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamestateEventTest {


    /**
     *
     * TestCase for the GamestateEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void gamestateEventTest(){

        int[] stonesGamora = {};
        int[] positionGamora = {1,0};
        InGameCharacter gamora = new InGameCharacter("Gamora", 1, 4, 200, 9, 3,
                stonesGamora, positionGamora );
        int[] stonesIronman = {0,4};
        int[] positionIronman = {2,1};
        InGameCharacter ironman = new InGameCharacter("Ironman",2,4,200,5,8, stonesIronman,
                positionIronman);
        int[] positionInfStone = {1,2};
        InfinityStone mind = new InfinityStone(4, positionInfStone);
        int[] positionRock = {0,3};
        Rock rock42 = new Rock(45,170,positionRock);
        Entities[] entities = {gamora,ironman, mind, rock42};
        int[] mapSize = {2,3};
        IDs one = new IDs(EntityID.P1, 3);
        IDs two = new IDs(EntityID.P1, 4);
        IDs three = new IDs(EntityID.P2, 2);
        IDs four = new IDs(EntityID.P2, 3);
        IDs five = new IDs(EntityID.P2, 5);
        IDs six = new IDs(EntityID.P1, 2);

        IDs[] turnOrder = {one, two, three, four, five, six};
        IDs activeCharacter = two;
        int[] stoneCooldowns = {0,0,2,0,3,1};
        boolean winCondition = false;


        GamestateEvent gamestateEvent = new GamestateEvent(entities, mapSize, turnOrder, activeCharacter, stoneCooldowns
                , winCondition);

        Gson gson = new Gson();
        String jsonString = gson.toJson(gamestateEvent, GamestateEvent.class);

        assertEquals("{" +

                "\"entities\":[" +

                "{\"name\":\"Gamora\",\"PID\":1,\"HP\":200," +
                "\"MP\":9,\"AP\":3,\"stones\":[]," +
                "\"entityType\":\"Character\",\"ID\":4,\"position\":[1,0]}," +

                "{\"name\":\"Ironman\",\"PID\":2,\"HP\":200," +
                "\"MP\":5,\"AP\":8,\"stones\":[0,4]," +
                "\"entityType\":\"Character\",\"ID\":4,\"position\":[2,1]}," +

                "{" +
                "\"entityType\":\"InfinityStone\",\"ID\":4,\"position\":[1,2]}," +

                "{\"HP\":170," +
                "\"entityType\":\"Rock\",\"ID\":45,\"position\":[0,3]}]," +

                "\"mapSize\":[2,3]," +
                "\"turnOrder\":[" +
                "{\"entityID\":\"P1\",\"ID\":3}," +
                "{\"entityID\":\"P1\",\"ID\":4}," +
                "{\"entityID\":\"P2\",\"ID\":2}," +
                "{\"entityID\":\"P2\",\"ID\":3}," +
                "{\"entityID\":\"P2\",\"ID\":5}," +
                "{\"entityID\":\"P1\",\"ID\":2}]," +

                "\"activeCharacter\":{\"entityID\":\"P1\",\"ID\":4}," +
                "\"stoneCooldowns\":[0,0,2,0,3,1]," +
                "\"winCondition\":false," +

                "\"eventType\":\"GamestateEvent\"" +
                "}",jsonString);

        ExtractorGamestateEvent extractorGamestateEvent = gson.fromJson(jsonString,ExtractorGamestateEvent.class);

        GamestateEvent gamestateEvent1 = extractorGamestateEvent.toGamestateEvent();

        String jsonString2 = gson.toJson(gamestateEvent1, GamestateEvent.class);

        System.out.println(jsonString2);

        assertEquals(jsonString2, jsonString);

        assert gamestateEvent1.equals(gamestateEvent);

    }
}