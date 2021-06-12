package communication.messages.events.character;

import com.google.gson.Gson;
import communication.messages.ExtractorMessageStructure;
import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.MessageStructure;
import communication.messages.enums.EntityID;
import communication.messages.enums.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterEventTest {

    /**
     *
     * TestCase for the ExchangeInfinityStoneEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void exchangeInfinityStoneEventTest(){
        IDs originEntity = new IDs(EntityID.P1,3 );
        IDs targetEntity = new IDs(EntityID.P1, 4);
        int[] originField = {3,7};
        int[] targetField = {3,6};
        IDs stoneType = new IDs(EntityID.InfinityStones, 5);
        ExchangeInfinityStoneEvent exchangeInfinityStoneEvent = new ExchangeInfinityStoneEvent(originEntity,
                targetEntity, originField, targetField, stoneType);

        Gson gson = new Gson();
        String jsonString = gson.toJson(exchangeInfinityStoneEvent,ExchangeInfinityStoneEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":4}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,6]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5},"+
                "\"eventType\":\"ExchangeInfinityStoneEvent\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the MeleeAttackEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void meleeAttackEventTest(){
        IDs originEntity = new IDs(EntityID.P1,3 );
        IDs targetEntity = new IDs(EntityID.P2, 4);
        int[] originField = {3,7};
        int[] targetField = {3,6};
        int amount = 2;
        MeleeAttackEvent meleeAttackEvent = new MeleeAttackEvent(originEntity, targetEntity, originField,
        targetField,amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(meleeAttackEvent,MeleeAttackEvent.class);

        assertEquals("{" +

                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,6]," +
                "\"amount\":2," +
                "\"eventType\":\"MeleeAttackEvent\"" +
                "}",jsonString);



    }

    /**
     *
     * TestCase for the MoveEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void MoveEvent(){
        IDs originEntity = new IDs(EntityID.P1,3 );
        int[] originField = {3,7};
        int[] targetField = {3,6};
        MoveEvent moveEvent = new MoveEvent(originEntity, originField, targetField);
        Gson gson = new Gson();
        String jsonString = gson.toJson(moveEvent,MoveEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,6]," +
                "\"eventType\":\"MoveEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the RangedAttackEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void RangedAttackEvent(){
        IDs originEntity = new IDs(EntityID.P1,3 );
        IDs targetEntity = new IDs(EntityID.P2, 4);
        int[] originField = {3,7};
        int[] targetField = {2,3};
        int amount = 2;
        RangedAttackEvent rangedAttackEvent = new RangedAttackEvent(originEntity, targetEntity, originField,
                targetField,amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(rangedAttackEvent,RangedAttackEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[2,3]," +
                "\"amount\":2," +
                "\"eventType\":\"RangedAttackEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the UseInfinityStoneEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void useInfinityStoneEventTest(){
        IDs originEntity = new IDs(EntityID.P1,3 );
        IDs targetEntity = new IDs(EntityID.P1, 3);
        int[] originField = {3,7};
        int[] targetField = {3,7};
        IDs stoneType = new IDs(EntityID.InfinityStones, 4);
        UseInfinityStoneEvent useInfinityStoneEvent = new UseInfinityStoneEvent(originEntity,
                targetEntity, originField, targetField, stoneType);

        Gson gson = new Gson();
        String jsonString = gson.toJson(useInfinityStoneEvent,UseInfinityStoneEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,7]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":4}," +
                "\"eventType\":\"UseInfinityStoneEvent\"" +
                "}",jsonString);
    }


    /**
     *
     * TestCase for the ExtractorMessageStructure-Class with Character Events and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void characterEventMessageStructureTest(){
        IDs originEntity = new IDs(EntityID.P1,3 );
        IDs targetEntity = new IDs(EntityID.P1, 4);
        int[] originField = {3,7};
        int[] targetField = {3,6};
        IDs stoneType = new IDs(EntityID.InfinityStones, 5);
        ExchangeInfinityStoneEvent exchangeInfinityStoneEvent = new ExchangeInfinityStoneEvent(originEntity,
                targetEntity, originField, targetField, stoneType);

        Gson gson = new Gson();
        String jsonString = gson.toJson(exchangeInfinityStoneEvent,ExchangeInfinityStoneEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":4}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,6]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5},"+
                "\"eventType\":\"ExchangeInfinityStoneEvent\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3 );
        targetEntity = new IDs(EntityID.P2, 4);
        originField = new int[]{3, 7};
        targetField = new int[]{3, 6};
        int amount = 2;
        MeleeAttackEvent meleeAttackEvent = new MeleeAttackEvent(originEntity, targetEntity, originField,
                targetField,amount);

        jsonString = gson.toJson(meleeAttackEvent,MeleeAttackEvent.class);

        assertEquals("{" +

                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,6]," +
                "\"amount\":2," +
                "\"eventType\":\"MeleeAttackEvent\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3 );
        MoveEvent moveEvent = new MoveEvent(originEntity, originField, targetField);
        jsonString = gson.toJson(moveEvent,MoveEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,6]," +
                "\"eventType\":\"MoveEvent\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3 );
        targetEntity = new IDs(EntityID.P2, 4);
        targetField = new int[]{2, 3};
        amount = 2;
        RangedAttackEvent rangedAttackEvent = new RangedAttackEvent(originEntity, targetEntity, originField,
                targetField,amount);
        jsonString = gson.toJson(rangedAttackEvent,RangedAttackEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":4}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[2,3]," +
                "\"amount\":2," +
                "\"eventType\":\"RangedAttackEvent\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3 );
        targetEntity = new IDs(EntityID.P1, 3);
        targetField = new int[]{3, 7};
        stoneType = new IDs(EntityID.InfinityStones, 4);
        UseInfinityStoneEvent useInfinityStoneEvent = new UseInfinityStoneEvent(originEntity,
                targetEntity, originField, targetField, stoneType);


        jsonString = gson.toJson(useInfinityStoneEvent,UseInfinityStoneEvent.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[3,7]," +
                "\"targetField\":[3,7]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":4}," +
                "\"eventType\":\"UseInfinityStoneEvent\"" +
                "}",jsonString);

        Message[] messages = {exchangeInfinityStoneEvent, meleeAttackEvent,moveEvent,rangedAttackEvent,
                useInfinityStoneEvent};
        MessageStructure messageStructure = new MessageStructure(MessageType.EVENTS, messages, null, null);

        jsonString = gson.toJson(messageStructure,MessageStructure.class);
        assertEquals("{" +
                "\"messageType\":\"EVENTS\"," +
                "\"messages\":[" +
                "" +
                        "{" +
                        "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                        "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":4}," +
                        "\"originField\":[3,7]," +
                        "\"targetField\":[3,6]," +
                        "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5},"+
                        "\"eventType\":\"ExchangeInfinityStoneEvent\"" +
                        "}," +
                        "{" +

                        "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                        "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":4}," +
                        "\"originField\":[3,7]," +
                        "\"targetField\":[3,6]," +
                        "\"amount\":2," +
                        "\"eventType\":\"MeleeAttackEvent\"" +
                        "},"+

                        "{" +
                        "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                        "\"originField\":[3,7]," +
                        "\"targetField\":[3,6]," +
                        "\"eventType\":\"MoveEvent\"" +
                        "}," +

                        "{" +
                        "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                        "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":4}," +
                        "\"originField\":[3,7]," +
                        "\"targetField\":[2,3]," +
                        "\"amount\":2," +
                        "\"eventType\":\"RangedAttackEvent\"" +
                        "},"+
                        "{" +
                        "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                        "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                        "\"originField\":[3,7]," +
                        "\"targetField\":[3,7]," +
                        "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":4}," +
                        "\"eventType\":\"UseInfinityStoneEvent\"" +
                        "}"+

                "]" +
                "}",jsonString);

        ExtractorMessageStructure extractorMessageStructure = gson.fromJson(jsonString, ExtractorMessageStructure.class);

        MessageStructure messageStructure1 = extractorMessageStructure.toMessageStructure();

        String jsonString2 = gson.toJson(messageStructure1, MessageStructure.class);

        assertEquals(jsonString, jsonString2);

        assert messageStructure1.equals(messageStructure);
    }





}