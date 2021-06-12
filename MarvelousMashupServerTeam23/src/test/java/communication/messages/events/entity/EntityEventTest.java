package communication.messages.events.entity;

import com.google.gson.Gson;
import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.MessageStructure;
import communication.messages.enums.EntityID;
import communication.messages.enums.MessageType;
import communication.messages.objects.InGameCharacter;
import communication.messages.objects.InfinityStone;
import communication.messages.objects.NPC;
import communication.messages.objects.Rock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityEventTest {

    /**
     *
     * TestCase for the ConsumedAPEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void ConsumedAPEventTest(){
        IDs targetEntity = new IDs(EntityID.P1, 5);
        int[] targetField = {0,1};
        int amount = 1;
        ConsumedAPEvent consumedAPEvent = new ConsumedAPEvent(targetEntity, targetField, amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(consumedAPEvent,ConsumedAPEvent.class);

        assertEquals("{" +

                "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":5}," +
                "\"targetField\":[0,1]," +
                "\"amount\":1," +
                "\"eventType\":\"ConsumedAPEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the ConsumedMPEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void ConsumedMPEventTest(){
        IDs targetEntity = new IDs(EntityID.P2, 2);
        int[] targetField = {0,0};
        int amount = 1;
        ConsumedMPEvent consumedMPEvent = new ConsumedMPEvent(targetEntity, targetField, amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(consumedMPEvent,ConsumedMPEvent.class);

        assertEquals("{" +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":2}," +
                "\"targetField\":[0,0]," +
                "\"amount\":1," +
                "\"eventType\":\"ConsumedMPEvent\"" +
                "}",jsonString);
    }


    /**
     *
     * TestCase for the DestroyEntityEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void destroyEntityEventTest(){

        IDs targetEntity = new IDs(EntityID.P2, 2);
        int[] targetField = {0,0};
        DestroyedEntityEvent destroyedEntityEvent = new DestroyedEntityEvent(targetField, targetEntity);
        Gson gson = new Gson();
        String jsonString = gson.toJson(destroyedEntityEvent, DestroyedEntityEvent.class);

        assertEquals("{" +
                "\"targetField\":[0,0]," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":2}," +
                "\"eventType\":\"DestroyedEntityEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the HealedEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void healedEventTest(){

        IDs targetEntity = new IDs(EntityID.P2, 1);
        int[] targetField = {5,7};
        int amount = 15;
        HealedEvent healedEvent = new HealedEvent(targetEntity, targetField, amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(healedEvent, HealedEvent.class);

        assertEquals("{" +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"targetField\":[5,7]," +
                "\"amount\":15," +
                "\"eventType\":\"HealedEvent\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the SpawnEntityEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void spawnEntityEventTest() {
        //spawned entity is an InGameCharacter
        String name = "Silver Surfer";
        int PID = 1;
        int HP = 200;
        int MP = 2;
        int AP = 1;
        int[] stones = {1, 2};
        int ID = 20;
        int[] position = {3, 5};
        InGameCharacter inGameCharacter = new InGameCharacter(name, PID, ID, HP, MP, AP, stones, position);
        SpawnEntityEvent spawnEntityEvent = new SpawnEntityEvent(inGameCharacter);
        Gson gson = new Gson();
        String jsonString = gson.toJson(spawnEntityEvent, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{\"name\":\"Silver Surfer\",\"PID\":1,\"HP\":200," +
                "\"MP\":2,\"AP\":1,\"stones\":[1,2]," +
                "\"entityType\":\"Character\",\"ID\":20,\"position\":[3,5]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        //spawned entity is an NPC
        int ID_npc = 2;
        int[] position_npc = {4, 4};
        int MP_npc = 9;
        int[] stones_npc = {2, 4, 3, 5};
        NPC npc = new NPC(ID_npc, position_npc, MP_npc, stones_npc);
        SpawnEntityEvent spawnEntityEventTwo = new SpawnEntityEvent(npc);
        jsonString = gson.toJson(spawnEntityEventTwo, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{\"MP\":9,\"stones\":[2,4,3,5]," +
                "\"entityType\":\"NPC\",\"ID\":2,\"position\":[4,4]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        //spawned entity is a Rock
        int ID_rock = 42;
        int HP_rock = 98;
        int[] position_rock = {0,0};
        Rock rock = new Rock(ID_rock, HP_rock, position_rock);
        SpawnEntityEvent spawnEntityEventThree = new SpawnEntityEvent(rock);
        jsonString = gson.toJson(spawnEntityEventThree, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{\"HP\":98," +
                "\"entityType\":\"Rock\",\"ID\":42,\"position\":[0,0]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        //spawned entity is an InfinityStone
        int ID_infStone = 0;
        int[] position_infStone = {4,3};
        InfinityStone infinityStone = new InfinityStone(ID_infStone, position_infStone);
        SpawnEntityEvent spawnEntityEventFour = new SpawnEntityEvent(infinityStone);
        jsonString = gson.toJson(spawnEntityEventFour, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{" +
                "\"entityType\":\"InfinityStone\",\"ID\":0,\"position\":[4,3]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);
    }

    /**
     *
     * TestCase for the TakenDamageEvent-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void takenDamageEvent(){
        IDs targetEntity = new IDs(EntityID.P2, 1);
        int[] targetField = {7,2};
        int amount = 2;
        TakenDamageEvent takenDamageEvent = new TakenDamageEvent(targetEntity, targetField, amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(takenDamageEvent, TakenDamageEvent.class);

        assertEquals("{" +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"targetField\":[7,2]," +
                "\"amount\":2," +
                "\"eventType\":\"TakenDamageEvent\"" +
                "}",jsonString);
    }


    /**
     *
     * TestCase for the EntityEvent-Class inside a MessageStructure and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void entityEventMessageStructureTest(){

        IDs targetEntity = new IDs(EntityID.P1, 5);
        int[] targetField = {0,1};
        int amount = 1;
        ConsumedAPEvent consumedAPEvent = new ConsumedAPEvent(targetEntity, targetField, amount);
        Gson gson = new Gson();
        String jsonString = gson.toJson(consumedAPEvent,ConsumedAPEvent.class);

        assertEquals("{" +

                "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":5}," +
                "\"targetField\":[0,1]," +
                "\"amount\":1," +
                "\"eventType\":\"ConsumedAPEvent\"" +
                "}",jsonString);

        targetEntity = new IDs(EntityID.P2, 2);
        targetField = new int[]{0, 0};
        amount = 1;
        ConsumedMPEvent consumedMPEvent = new ConsumedMPEvent(targetEntity, targetField, amount);
        jsonString = gson.toJson(consumedMPEvent,ConsumedMPEvent.class);

        assertEquals("{" +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":2}," +
                "\"targetField\":[0,0]," +
                "\"amount\":1," +
                "\"eventType\":\"ConsumedMPEvent\"" +
                "}",jsonString);

        targetEntity = new IDs(EntityID.P2, 2);
        targetField = new int[]{0, 0};
        DestroyedEntityEvent destroyedEntityEvent = new DestroyedEntityEvent(targetField, targetEntity);
        jsonString = gson.toJson(destroyedEntityEvent, DestroyedEntityEvent.class);

        assertEquals("{" +
                "\"targetField\":[0,0]," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":2}," +
                "\"eventType\":\"DestroyedEntityEvent\"" +
                "}",jsonString);

        targetEntity = new IDs(EntityID.P2, 1);
        targetField = new int[]{5, 7};
        amount = 15;
        HealedEvent healedEvent = new HealedEvent(targetEntity, targetField, amount);
        jsonString = gson.toJson(healedEvent, HealedEvent.class);

        assertEquals("{" +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"targetField\":[5,7]," +
                "\"amount\":15," +
                "\"eventType\":\"HealedEvent\"" +
                "}",jsonString);

        //spawned entity is an InGameCharacter
        String name = "Silver Surfer";
        int PID = 1;
        int HP = 200;
        int MP = 2;
        int AP = 1;
        int[] stones = {1, 2};
        int ID = 20;
        int[] position = {3, 5};
        InGameCharacter inGameCharacter = new InGameCharacter(name, PID, ID, HP, MP, AP, stones, position);
        SpawnEntityEvent spawnEntityEvent = new SpawnEntityEvent(inGameCharacter);
        jsonString = gson.toJson(spawnEntityEvent, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{\"name\":\"Silver Surfer\",\"PID\":1,\"HP\":200," +
                "\"MP\":2,\"AP\":1,\"stones\":[1,2]," +
                "\"entityType\":\"Character\",\"ID\":20,\"position\":[3,5]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        //spawned entity is an NPC
        int ID_npc = 2;
        int[] position_npc = {4, 4};
        int MP_npc = 9;
        int[] stones_npc = {2, 4, 3, 5};
        NPC npc = new NPC(ID_npc, position_npc, MP_npc, stones_npc);
        SpawnEntityEvent spawnEntityEventTwo = new SpawnEntityEvent(npc);
        jsonString = gson.toJson(spawnEntityEventTwo, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{\"MP\":9,\"stones\":[2,4,3,5]," +
                "\"entityType\":\"NPC\",\"ID\":2,\"position\":[4,4]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        //spawned entity is a Rock
        int ID_rock = 42;
        int HP_rock = 98;
        int[] position_rock = {0,0};
        Rock rock = new Rock(ID_rock, HP_rock, position_rock);
        SpawnEntityEvent spawnEntityEventThree = new SpawnEntityEvent(rock);
        jsonString = gson.toJson(spawnEntityEventThree, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{\"HP\":98," +
                "\"entityType\":\"Rock\",\"ID\":42,\"position\":[0,0]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        //spawned entity is an InfinityStone
        int ID_infStone = 0;
        int[] position_infStone = {4,3};
        InfinityStone infinityStone = new InfinityStone(ID_infStone, position_infStone);
        SpawnEntityEvent spawnEntityEventFour = new SpawnEntityEvent(infinityStone);
        jsonString = gson.toJson(spawnEntityEventFour, SpawnEntityEvent.class);

        assertEquals("{" +
                "\"entity\":{" +
                "\"entityType\":\"InfinityStone\",\"ID\":0,\"position\":[4,3]}," +
                "\"eventType\":\"SpawnEntityEvent\"" +
                "}", jsonString);

        targetEntity = new IDs(EntityID.P2, 1);
        targetField = new int[]{7, 2};
        amount = 2;
        TakenDamageEvent takenDamageEvent = new TakenDamageEvent(targetEntity, targetField, amount);
        jsonString = gson.toJson(takenDamageEvent, TakenDamageEvent.class);

        assertEquals("{" +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"targetField\":[7,2]," +
                "\"amount\":2," +
                "\"eventType\":\"TakenDamageEvent\"" +
                "}",jsonString);

        Message[] messages = {consumedAPEvent, consumedMPEvent, destroyedEntityEvent, healedEvent, spawnEntityEvent,
                spawnEntityEventTwo, spawnEntityEventThree, spawnEntityEventFour, takenDamageEvent};
        MessageStructure messageStructure = new MessageStructure(MessageType.EVENTS, messages, null, null);

        jsonString = gson.toJson(messageStructure, MessageStructure.class);

        assertEquals("{" +
                "\"messageType\":\"EVENTS\"," +
                "\"messages\":[" +
                        "{" +

                        "\"targetEntity\":{\"entityID\":\"P1\",\"ID\":5}," +
                        "\"targetField\":[0,1]," +
                        "\"amount\":1," +
                        "\"eventType\":\"ConsumedAPEvent\"" +
                        "},"+

                        "{" +
                        "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":2}," +
                        "\"targetField\":[0,0]," +
                        "\"amount\":1," +
                        "\"eventType\":\"ConsumedMPEvent\"" +
                        "},"+

                        "{" +
                        "\"targetField\":[0,0]," +
                        "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":2}," +
                        "\"eventType\":\"DestroyedEntityEvent\"" +
                        "},"+

                        "{" +
                        "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                        "\"targetField\":[5,7]," +
                        "\"amount\":15," +
                        "\"eventType\":\"HealedEvent\"" +
                        "},"+

                        "{" +
                        "\"entity\":{\"name\":\"Silver Surfer\",\"PID\":1,\"HP\":200," +
                        "\"MP\":2,\"AP\":1,\"stones\":[1,2]," +
                        "\"entityType\":\"Character\",\"ID\":20,\"position\":[3,5]}," +
                        "\"eventType\":\"SpawnEntityEvent\"" +
                        "},"+

                        "{" +
                        "\"entity\":{\"MP\":9,\"stones\":[2,4,3,5]," +
                        "\"entityType\":\"NPC\",\"ID\":2,\"position\":[4,4]}," +
                        "\"eventType\":\"SpawnEntityEvent\"" +
                        "},"+

                        "{" +
                        "\"entity\":{\"HP\":98," +
                        "\"entityType\":\"Rock\",\"ID\":42,\"position\":[0,0]}," +
                        "\"eventType\":\"SpawnEntityEvent\"" +
                        "},"+

                        "{" +
                        "\"entity\":{" +
                        "\"entityType\":\"InfinityStone\",\"ID\":0,\"position\":[4,3]}," +
                        "\"eventType\":\"SpawnEntityEvent\"" +
                        "},"+

                        "{" +
                        "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                        "\"targetField\":[7,2]," +
                        "\"amount\":2," +
                        "\"eventType\":\"TakenDamageEvent\"" +
                        "}"+

                "]" +
                "}",jsonString);
    }


}