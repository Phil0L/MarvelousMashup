package communication.messages.requests;

import com.google.gson.Gson;
import communication.messages.ExtractorMessageStructure;
import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.MessageStructure;
import communication.messages.enums.EntityID;
import communication.messages.enums.MessageType;
import communication.messages.events.character.ExchangeInfinityStoneEvent;
import communication.messages.events.character.MeleeAttackEvent;
import communication.messages.login.GeneralAssignment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    /**
     *
     * TestCase for the DisconnectRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void disconnectRequestTest(){
        DisconnectRequest disconnectRequest = new DisconnectRequest();
        Gson gson = new Gson();
        String jsonString = gson.toJson(disconnectRequest, DisconnectRequest.class);

        assertEquals("{" +
                "\"requestType\":\"DisconnectRequest\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the EndRoundRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void endRoundRequestTest(){
        EndRoundRequest endRoundRequest = new EndRoundRequest();
        Gson gson = new Gson();
        String jsonString = gson.toJson(endRoundRequest, EndRoundRequest.class);

        assertEquals("{" +
                "\"requestType\":\"EndRoundRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the ExchangeInfinityStoneRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void exchangeInfinityStoneRequestTest(){
        IDs originEntity = new IDs(EntityID.P1,3);
        IDs targetEntity = new IDs(EntityID.P2, 1);
        int[] originField = {2,4};
        int[] targetField = {2,5};
        IDs stoneType = new IDs(EntityID.InfinityStones, 5);
        ExchangeInfinityStoneRequest exchangeInfinityStoneRequest = new ExchangeInfinityStoneRequest(originEntity,
                targetEntity, originField, targetField, stoneType);
        Gson gson = new Gson();
        String jsonString = gson.toJson(exchangeInfinityStoneRequest, ExchangeInfinityStoneRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[2,5]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5},"+
                "\"requestType\":\"ExchangeInfinityStoneRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the MeleeAttackRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void meleeAttackRequestTest(){

        IDs originEntity = new IDs(EntityID.P1,3);
        IDs targetEntity = new IDs(EntityID.P2, 1);
        int[] originField = {2,4};
        int[] targetField = {2,5};
        int value = 14;
        MeleeAttackRequest meleeAttackRequest = new MeleeAttackRequest(originEntity, targetEntity, originField,
                targetField, value);
        Gson gson = new Gson();
        String jsonString = gson.toJson(meleeAttackRequest, MeleeAttackRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[2,5]," +
                "\"value\":14,"+
                "\"requestType\":\"MeleeAttackRequest\"" +
                "}",jsonString);
    }

    /**
     *
     * TestCase for the MoveRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void moveRequestTest(){
        IDs originEntity = new IDs(EntityID.P1,3);
        int[] originField = {1,4};
        int[] targetField = {2,4};
        MoveRequest moveRequest = new MoveRequest(originEntity, originField, targetField);
        Gson gson = new Gson();
        String jsonString = gson.toJson(moveRequest, MoveRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[1,4]," +
                "\"targetField\":[2,4]," +
                "\"requestType\":\"MoveRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the PauseStartRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void pauseStartRequestTest(){
        PauseStartRequest pauseStartRequest = new PauseStartRequest();
        Gson gson = new Gson();
        String jsonString = gson.toJson(pauseStartRequest, PauseStartRequest.class);

        assertEquals("{" +
                "\"requestType\":\"PauseStartRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the PauseStopRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void pauseStopRequestTest(){
        PauseStopRequest pauseStopRequest = new PauseStopRequest();
        Gson gson = new Gson();
        String jsonString = gson.toJson(pauseStopRequest, PauseStopRequest.class);

        assertEquals("{" +
                "\"requestType\":\"PauseStopRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the RangedAttackRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void rangedAttackRequestTest(){
        IDs originEntity = new IDs(EntityID.P1,3);
        IDs targetEntity = new IDs(EntityID.P2, 1);
        int[] originField = {2,4};
        int[] targetField = {4,7};
        int value = 14;
        RangedAttackRequest rangedAttackRequest = new RangedAttackRequest(originEntity, targetEntity, originField,
                targetField, value);
        Gson gson = new Gson();
        String jsonString = gson.toJson(rangedAttackRequest, RangedAttackRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[4,7]," +
                "\"value\":14,"+
                "\"requestType\":\"RangedAttackRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the Req-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void ReqTest(){
        Req req = new Req();

        Gson gson = new Gson();
        String jsonString = gson.toJson(req, Req.class);

        assertEquals("{" +
                "\"requestType\":\"Req\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the UseInfinityStoneRequest-Class and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void useInfinityStoneRequestTest(){
        IDs originEntity = new IDs(EntityID.P1,3);
        int[] originField = {2,4};
        int[] targetField = {14,17};
        IDs stoneType = new IDs(EntityID.InfinityStones, 5);
        UseInfinityStoneRequest useInfinityStoneRequest = new UseInfinityStoneRequest(originEntity, originField,
                targetField, stoneType);

        Gson gson = new Gson();
        String jsonString = gson.toJson(useInfinityStoneRequest, UseInfinityStoneRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[14,17]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5}," +
                "\"requestType\":\"UseInfinityStoneRequest\"" +
                "}",jsonString);

    }

    /**
     *
     * TestCase for the ExtractorMessageStructure-Class with Requests and gson-transformations
     *
     * @author Sarah Engele
     *
     */
    @Test
    public void requestMessageStructureTest(){
        DisconnectRequest disconnectRequest = new DisconnectRequest();
        Gson gson = new Gson();
        String jsonString = gson.toJson(disconnectRequest, DisconnectRequest.class);

        assertEquals("{" +
                "\"requestType\":\"DisconnectRequest\"" +
                "}",jsonString);

        EndRoundRequest endRoundRequest = new EndRoundRequest();
        jsonString = gson.toJson(endRoundRequest, EndRoundRequest.class);

        assertEquals("{" +
                "\"requestType\":\"EndRoundRequest\"" +
                "}",jsonString);

        IDs originEntity = new IDs(EntityID.P1,3);
        IDs targetEntity = new IDs(EntityID.P2, 1);
        int[] originField = {2,4};
        int[] targetField = {2,5};
        IDs stoneType = new IDs(EntityID.InfinityStones, 5);
        ExchangeInfinityStoneRequest exchangeInfinityStoneRequest = new ExchangeInfinityStoneRequest(originEntity,
                targetEntity, originField, targetField, stoneType);
        jsonString = gson.toJson(exchangeInfinityStoneRequest, ExchangeInfinityStoneRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[2,5]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5},"+
                "\"requestType\":\"ExchangeInfinityStoneRequest\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3);
        targetEntity = new IDs(EntityID.P2, 1);
        originField = new int[]{2, 4};
        targetField = new int[]{2, 5};
        int value = 14;
        MeleeAttackRequest meleeAttackRequest = new MeleeAttackRequest(originEntity, targetEntity, originField,
                targetField, value);
        jsonString = gson.toJson(meleeAttackRequest, MeleeAttackRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[2,5]," +
                "\"value\":14,"+
                "\"requestType\":\"MeleeAttackRequest\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3);
        originField = new int[]{1, 4};
        targetField = new int[]{2, 4};
        MoveRequest moveRequest = new MoveRequest(originEntity, originField, targetField);
        jsonString = gson.toJson(moveRequest, MoveRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[1,4]," +
                "\"targetField\":[2,4]," +
                "\"requestType\":\"MoveRequest\"" +
                "}",jsonString);

        PauseStartRequest pauseStartRequest = new PauseStartRequest();
        jsonString = gson.toJson(pauseStartRequest, PauseStartRequest.class);

        assertEquals("{" +
                "\"requestType\":\"PauseStartRequest\"" +
                "}",jsonString);

        PauseStopRequest pauseStopRequest = new PauseStopRequest();
        jsonString = gson.toJson(pauseStopRequest, PauseStopRequest.class);

        assertEquals("{" +
                "\"requestType\":\"PauseStopRequest\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3);
        targetEntity = new IDs(EntityID.P2, 1);
        originField = new int[]{2, 4};
        targetField = new int[]{4, 7};
        value = 14;
        RangedAttackRequest rangedAttackRequest = new RangedAttackRequest(originEntity, targetEntity, originField,
                targetField, value);
        jsonString = gson.toJson(rangedAttackRequest, RangedAttackRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[4,7]," +
                "\"value\":14,"+
                "\"requestType\":\"RangedAttackRequest\"" +
                "}",jsonString);

        Req req = new Req();

        jsonString = gson.toJson(req, Req.class);

        assertEquals("{" +
                "\"requestType\":\"Req\"" +
                "}",jsonString);

        originEntity = new IDs(EntityID.P1,3);
        originField = new int[]{2, 4};
        targetField = new int[]{14, 17};
        stoneType = new IDs(EntityID.InfinityStones, 5);
        UseInfinityStoneRequest useInfinityStoneRequest = new UseInfinityStoneRequest(originEntity, originField,
                targetField, stoneType);

        jsonString = gson.toJson(useInfinityStoneRequest, UseInfinityStoneRequest.class);

        assertEquals("{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[14,17]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5}," +
                "\"requestType\":\"UseInfinityStoneRequest\"" +
                "}",jsonString);

        Message[] messages = {disconnectRequest, endRoundRequest,exchangeInfinityStoneRequest,meleeAttackRequest,
                moveRequest, pauseStartRequest, pauseStopRequest, rangedAttackRequest, req, useInfinityStoneRequest};
        MessageStructure messageStructure = new MessageStructure(MessageType.REQUESTS, messages, null, null);

        jsonString = gson.toJson(messageStructure,MessageStructure.class);
        assertEquals("{" +
                "\"messageType\":\"REQUESTS\"," +
                "\"messages\":[" +
                "{" +
                "\"requestType\":\"DisconnectRequest\"" +
                "},"+

                "{" +
                "\"requestType\":\"EndRoundRequest\"" +
                "},"+

                "{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[2,5]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5},"+
                "\"requestType\":\"ExchangeInfinityStoneRequest\"" +
                "}," +

                "{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[2,5]," +
                "\"value\":14,"+
                "\"requestType\":\"MeleeAttackRequest\"" +
                "}," +

                "{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[1,4]," +
                "\"targetField\":[2,4]," +
                "\"requestType\":\"MoveRequest\"" +
                "},"+

                "{" +
                "\"requestType\":\"PauseStartRequest\"" +
                "},"+

                "{" +
                "\"requestType\":\"PauseStopRequest\"" +
                "},"+

                "{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"targetEntity\":{\"entityID\":\"P2\",\"ID\":1}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[4,7]," +
                "\"value\":14,"+
                "\"requestType\":\"RangedAttackRequest\"" +
                "},"+

                "{" +
                "\"requestType\":\"Req\"" +
                "},"+

                "{" +
                "\"originEntity\":{\"entityID\":\"P1\",\"ID\":3}," +
                "\"originField\":[2,4]," +
                "\"targetField\":[14,17]," +
                "\"stoneType\":{\"entityID\":\"InfinityStones\",\"ID\":5}," +
                "\"requestType\":\"UseInfinityStoneRequest\"" +
                "}" +

                "]" +
                "}",jsonString);

        ExtractorMessageStructure extractorMessageStructure = gson.fromJson(jsonString, ExtractorMessageStructure.class);

        MessageStructure messageStructure1 = extractorMessageStructure.toMessageStructure();

        String jsonString2 = gson.toJson(messageStructure1, MessageStructure.class);

        assertEquals(jsonString, jsonString2);

        assert messageStructure1.equals(messageStructure);
    }


}