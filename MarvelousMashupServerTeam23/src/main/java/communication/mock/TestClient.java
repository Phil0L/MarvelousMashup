package communication.mock;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import communication.ExtractorMessageType;
import communication.messages.ExtractorMessageStructure;
import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.MessageStructure;
import communication.messages.enums.EntityID;
import communication.messages.enums.MessageType;
import communication.messages.enums.Role;
import communication.messages.events.game.TurnEvent;
import communication.messages.events.gamestate.GamestateEvent;
import communication.messages.login.*;
import communication.messages.objects.*;
import communication.messages.requests.DisconnectRequest;
import communication.messages.requests.EndRoundRequest;
import communication.messages.requests.MeleeAttackRequest;
import communication.messages.requests.MoveRequest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import parameter.ConfigHero;
import parameter.GrassRockEnum;
import parameter.MatchConfig;
import parameter.ScenarioConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This TestClient class is used for testing the communication with the server.
 *
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 * @author Adrian Groeber
 */
public class TestClient extends WebSocketClient {

    /**
     * Json parser
     */
    private final Gson gson_;

    /**
     * stores the received messages
     */
    private final List<String> messagesIn;

    /**
     * stores the outgoing messages
     */
     private final List<String> messagesOut;

    /**
     * Stores the name of the user
     */
     public String username = "TestClient";

    /**
     * A unique ID, in our case a random number between 0 and 99 (will be sent to the server)
     */
    public String deviceID = ""+(int)(Math.random()*100);

    /**
     * stores whatever should be send in the optionals field
     */
    public String optionals;

    /**
     * the role of the TestClient, by default Role.Player
     */
    public Role role = Role.PLAYER;

    /**
     * decides whether the client wants to reconnect to a existing game or start a new game, if the server sends a
     * HelloClient message with boolean runningGame = true. This variable is false by default.
     */
    public boolean reconnect = false;


    /**
     * contains the twelve ConfigHeros to choose from
     */
    public ConfigHero[] heroSet;

    /**
     * The heroes selected by the player in the characterSelection
     */
    public List<ConfigHero> chosenHeroes;

    /**
     *  defines which number the client has, in relation to the game.
     *  0 represents Spectator
     *  1 represents PlayerOne
     *  2 represents PlayerTwo
     *
     */
    public int playerNumber;

    /**
     * checks hows turn it is
     */
    boolean yourTurn;

    /**
     * Username of Player 2 (received in the GameStructure message)
     */
     public String playerTwoName;

    /**
     * Username of Player 1 (received in the GameStructure message)
     */
    public String playerOneName;


    /**
     * The HeroSet of Player1 during the game
     */
    public ConfigHero[] playerOneHeroes;


    /**
     * The HeroSet of Player2 during the game
     */
    public ConfigHero[] playerTwoHeroes;

    /**
     * MatchConfig of the game
     */
    public MatchConfig matchConfig;

    /**
     * ScenarioConfig of the game
     */
    public ScenarioConfig scenarioConfig;


    /**
     * gameField, which is the equivalent of model in the test client
     */
    public Entities[][] gameField;

    /**
     * counts the messages in the logFile
     */
    public static int logMessageCount = 0;

    /**
     * indicates whether client is in inGame Phase (wants to send requests)
     */
    public boolean ingame = false;


    /**
     * The constructor of the TestClient class. It calls the constructor of the super class.
     * @author Matthias Ruf
     *
     * @param server_uri the uri to connect
     *
     */
    public TestClient(URI server_uri) {
        //calls the constructor of the super-class
        super(server_uri);

        // initialize the message logs
        messagesIn = new LinkedList<>();
        messagesOut = new LinkedList<>();

        // initialize the json parser
        gson_ = new Gson();


    }

    /**
     * The constructor of the TestClient class. It calls the constructor of the super class.
     * @author Matthias Ruf
     *
     * @param server_uri the uri to connect
     *
     */
    public TestClient(URI server_uri, Role role, String name) {
        //calls the constructor of the super-class
        super(server_uri);

        this.role = role;
        this.username = name;

        // initialize the message logs
        messagesIn = new LinkedList<>();
        messagesOut = new LinkedList<>();

        // initialize the json parser
        gson_ = new Gson();

    }

    /**
     * This methode connects the TestClient to the server. Therefore this methode uses the arguments given in the
     * constructor and the connectBlocking methode of the super class.
     *
     * @author Matthias Ruf
     * @param timeout time in milliseconds until the timeout
     * @return was the WebSocket connection establishment successful
     */
    public boolean connect(long timeout){

        try {
            this.connectBlocking(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return  false;
        }
        return true;
    }

    /**
     * onOpen is called when the connection to the Server is established. It sends a HelloServer-Message Object.
     * @author Matthias Ruf
     *
     * @param handshake_data unused in our implementation. In my understanding the attribut contains information about
     *                       the WebSocket handshake with the server.
     */
    @Override
    public void onOpen(ServerHandshake handshake_data) {

        //print the successful connection establishment
        System.out.println("TESTCLIENT:\n Connection opened successfully to: " + getConnection().getLocalSocketAddress());

        //sending a HelloServer message (created by using attributs) to the server
        send(gson_.toJson(new HelloServer(this.username, this.deviceID, this.optionals)));
    }

    /**
     * The onMessage method is called whenever a message is received by the WebSocket connection.
     * It also specifies what measures have to be taken depending on the message type.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     * @author Benno Hoelz
     * @param message the incoming message (JSON String specified in the network standard document).
     */
    @Override
    public void onMessage(String message) {

        this.messagesIn.add(message);
        this.logMessage(message,false);
        System.out.println(this.username + ": " + message);


        try {
            ExtractorMessageType extractorMessageType = gson_.fromJson(message, ExtractorMessageType.class);


            if(extractorMessageType.messageType != null) {
                // Message belongs to the LoginMessages

                switch (extractorMessageType.messageType) {
                    case HELLO_CLIENT:

                        // unzip incoming message
                        HelloClient helloClient = gson_.fromJson(message, HelloClient.class);

                        if (!helloClient.runningGame) {
                            // There is no running game (by the server) with the username and deviceID of the client
                            PlayerReady obj_playerReady = new PlayerReady(null, true, role);
                            String msg_playerReady = gson_.toJson(obj_playerReady);
                            this.send(msg_playerReady);

                        } else if(role.equals(Role.SPECTATOR)){
                            PlayerReady obj_playerReady = new PlayerReady(null, true, role);
                            String msg_playerReady = gson_.toJson(obj_playerReady);
                            this.send(msg_playerReady);

                        } else if (!reconnect) {
                            // There is a running game (boolean runningGame was true) and the client does not want to
                            // reconnect
                            Reconnect obj_reconnect = new Reconnect(null, false);
                            String msg_reconnect = gson_.toJson(obj_reconnect);
                            this.send(msg_reconnect);
                        } else {
                            // There is a running game (boolean runningGame was true) and the client wants to reconnect
                            Reconnect obj_reconnect = new Reconnect(null, true);
                            String msg_reconnect = gson_.toJson(obj_reconnect);
                            this.send(msg_reconnect);

                        }
                        break;


                    case GAME_ASSIGNMENT:

                        GameAssignment gameAssignment = gson_.fromJson(message, GameAssignment.class);
                        this.heroSet = gameAssignment.characterSelection;

                        break;
                    case GENERAL_ASSIGNMENT:
                        GeneralAssignment generalAssignment = gson_.fromJson(message, GeneralAssignment.class);
                        //System.out.println(message);

                        break;
                    case CONFIRM_SELECTION:
                        ConfirmSelection confirmSelection = gson_.fromJson(message, ConfirmSelection.class);
                        if (!confirmSelection.selectionComplete) {
                            System.err.println("selection not confirmed");
                        }

                        break;
                    case GAME_STRUCTURE:

                        // store useful information
                        GameStructure gameStructure = gson_.fromJson(message, GameStructure.class);
                        switch (gameStructure.assignment) {

                            case "Spectator":
                                playerNumber = 0;
                                break;
                            case "PlayerOne":
                                playerNumber = 1;
                                break;
                            case "PlayerTwo":
                                playerNumber = 2;
                                break;
                        }
                        this.playerOneName = gameStructure.playerOneName;
                        this.playerTwoName = gameStructure.playerTwoName;
                        this.playerOneHeroes = gameStructure.playerOneCharacters;
                        this.playerTwoHeroes = gameStructure.playerTwoCharacters;
                        this.matchConfig = gameStructure.matchconfig;
                        this.scenarioConfig = gameStructure.scenarioconfig;

                        this.processGameField();


                        break;

                    case ERROR:

                        //  not supported
                        this.close();
                        break;
                    case GOODBYE_CLIENT:

                        //close the connection
                        this.close();
                        break;

                    case EVENTS:


                        ExtractorMessageStructure exMessageStruct = gson_.fromJson(message,ExtractorMessageStructure.class);
                        MessageStructure messageStruct  = exMessageStruct.toMessageStructure();


                        for (Message msg: messageStruct.messages){
                            if(ingame){
                                //processes the messages in the messageStructure
                                handleEvent(msg);
                            }

                        }


                        break;
                    case REQUESTS:

                        this.close(-1);
                        throw new IllegalArgumentException("MessageTyp not allowed: " +
                                extractorMessageType.messageType);

                    default:

                        // not supportet
                        this.close(-1);
                        throw new UnsupportedOperationException("MessageTyp not supported: " +
                                extractorMessageType.messageType);

                }
            }else{
                // not supportet
                this.close();
                throw new UnsupportedOperationException("MessageTyp not supported");
            }


        }catch (JsonSyntaxException e){
            //disconnect
            Message[] msg = {new DisconnectRequest()};
            this.send(gson_.toJson(new MessageStructure(MessageType.REQUESTS,msg,null,null)));
            close();

            e.printStackTrace();
            System.err.println("TESTCLIENT: Error while trying to typecast message has occurred. Connection closed.");
        }catch (Exception e){
            //disconnect
            Message[] msg = {new DisconnectRequest()};
            this.send(gson_.toJson(new MessageStructure(MessageType.REQUESTS,msg,null,null)));
            close();

            e.printStackTrace();
            System.err.println("TESTCLIENT: An Exception was detected in the onMessage methode");

        }
    }


    /**
     * handels incoming inGame Events. Therefore it gets a message object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     * @param message the incoming message object
     */
    private void handleEvent(Message message) {
        switch (message.eventType){
            case GamestateEvent:
                this.yourTurn = (( ((GamestateEvent) message).activeCharacter.entityID == EntityID.P1) &&
                        this.playerNumber == 1) || ((((GamestateEvent) message).activeCharacter.entityID == EntityID.P2)
                        && this.playerNumber == 2);
                updateGameField((GamestateEvent) message);
                break;
            case TurnEvent:
                if (message instanceof TurnEvent) {
                    this.yourTurn = (( ((TurnEvent) message).nextCharacter.entityID == EntityID.P1) &&
                            this.playerNumber == 1) || ((((TurnEvent) message).nextCharacter.entityID == EntityID.P2)
                            && this.playerNumber == 2);
                    processTurn((TurnEvent) message);
                }
                else System.err.println("Message is not a instance of TurnEvent");

                yourTurn = false;
                break;
            case WinEvent:
                this.close();
                break;
            case Nack:

                if(this.yourTurn){
                    Message[] messages = {new EndRoundRequest()};
                    this.send(gson_.toJson(new MessageStructure(MessageType.REQUESTS,messages,null,
                            null)));
                }
                break;

        }

    }

    /**
     * calculates a network standard correct  turn, based on the given informations.
     *
     */
    public void processTurn(TurnEvent turnEvent){

        List<Message> messages = new LinkedList<>();

        if ((playerNumber == 1 && turnEvent.nextCharacter.entityID == EntityID.P1  )||(
                playerNumber == 2 && turnEvent.nextCharacter.entityID == EntityID.P2)){

            // calc the position of the hero
            int[] pos = new int[2];
            for (int x = 0; x < gameField.length; x++) {
                for (int y = 0; y < gameField[0].length; y++) {

                    // check whether the IDs are equal
                    if ( gameField[x][y] != null && gameField[x][y].ID==turnEvent.nextCharacter.ID){

                        //check whether the Entity is a InGameCharacter
                        if(gameField[x][y] instanceof InGameCharacter){

                            // check whether the PlayerIDs are equal
                            if (((InGameCharacter)gameField[x][y]).PID == playerNumber ){
                                pos[0] = x;
                                pos[1] = y;
                            }

                        }

                    }

                }
            }

            int[][] relativePositions = {{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1},{0,1}};

            int index = (int) (Math.random()*8);
            int xPos = pos[0] + relativePositions[index][0];
            int yPos = pos[1] + relativePositions[index][1];


            if (xPos == -1){
                xPos = 1;
            }
            if (yPos == -1){
                yPos = 1;
            }
            if (xPos == gameField.length){
                xPos = gameField.length - 2;
            }
            if (yPos == gameField[0].length){
                yPos = gameField[0].length - 2;
            }


            if(gameField[xPos][yPos] != null){

                //handle a interaction with a entity
                if (gameField[xPos][yPos] instanceof InfinityStone ){
                    int[] targetField = {xPos,yPos};
                    MoveRequest moveRequest = new MoveRequest(turnEvent.nextCharacter,pos,targetField);
                    messages.add(moveRequest);



                }else if(gameField[xPos][yPos] instanceof Rock){
                    int[] targetField = {xPos,yPos};
                    MeleeAttackRequest meleeAttackRequest = new MeleeAttackRequest(turnEvent.nextCharacter,
                            new IDs(EntityID.Rocks,gameField[xPos][yPos].ID) ,  pos ,targetField,1 );
                    //TODO 1 is definitly false maybe this caused a error
                    // --> server takes the value from the configuration
                    messages.add(meleeAttackRequest);

                }else if (gameField[xPos][yPos] instanceof InGameCharacter){

                    if(((InGameCharacter) gameField[xPos][yPos]).PID == this.playerNumber ){
                        int[] targetField = {xPos,yPos};
                        MoveRequest moveRequest = new MoveRequest(turnEvent.nextCharacter,pos,targetField);
                        messages.add(moveRequest);

                    }else{
                        int[] targetField = {xPos,yPos};
                        MeleeAttackRequest meleeAttackRequest = new MeleeAttackRequest(turnEvent.nextCharacter,
                                new IDs(EntityID.Rocks,gameField[xPos][yPos].ID) ,  pos ,targetField,1 );
                        messages.add(meleeAttackRequest);
                        //TODO 1 is definitly false maybe this caused a error
                        // --> case now the server takes the value from the configuration

                    }


                }

            }else {
                //move to the position
                int[] targetField = {xPos,yPos};
                MoveRequest moveRequest = new MoveRequest(turnEvent.nextCharacter,pos,targetField);
                messages.add(moveRequest);
            }


            messages.add(new EndRoundRequest());
            this.send(gson_.toJson(new MessageStructure(MessageType.REQUESTS,messages.toArray(new Message[0]),null,null)));

        }


    }


    /**
     * Creates and fills the gameField Array by using the information of the GameStructure attribute
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return was the operation successful
     *
     */
    private boolean processGameField() {
        if (this.scenarioConfig == null){
            return false;
        }
        // create the basic for the gameField
        this.gameField = new Entities[this.scenarioConfig.scenario.length][this.scenarioConfig.scenario[0].length];

        //Place all Rocks on the field
        for (int x = 0; x < this.scenarioConfig.scenario.length; x++) {
            for (int y = 0; y < this.scenarioConfig.scenario[0].length; y++) {
                if (this.scenarioConfig.scenario[x][y] == GrassRockEnum.ROCK) {
                    int[] pos ={x,y};
                    gameField[x][y] = new Rock(x*3+y*7,100,pos);
                }
            }
        }
        return true;
    }

    /**
     * Updates the gameField Array by using the information of the last received GameStateEvent
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     */
    private boolean updateGameField(GamestateEvent gamestateEvent){

        this.gameField = new Entities[gamestateEvent.mapSize[0]][gamestateEvent.mapSize[1]];
        //place all entitys on the gamefield
        for (Entities entity : gamestateEvent.entities ) {
            this.gameField[entity.position[0]][entity.position[1]]  = entity;
        }
        return true;
    }

    public String printGameField(){

        if (this.gameField == null){
            return null;
        }

        // initialize
        String out = "Gamefield: \n";

        // occupy array
        for (int x = 0; x < this.gameField.length; x++) {
            for (int y = 0; y < this.gameField[x].length; y++) {
                String type;
                if (gameField[x][y] != null) {
                    switch (gameField[x][y].entityType) {
                        case Character:
                            type = ((InGameCharacter) gameField[x][y]).name;
                            break;
                        case Rock:
                            type = "rock" + ((Rock) gameField[x][y]).HP;
                            break;
                        case NPC:
                            type = ((NPC) gameField[x][y]).ID == 0 ? "Goose" : ((NPC) gameField[x][y]).ID == 1 ?
                                    " StanLee" : "Thanos";
                            break;
                        case InfinityStone:
                            type = "stone " + gameField[x][y].ID;
                            break;
                        default:
                            type = "grass";
                    }

                }else{
                    type = "grass";
                }
                out += "[" + String.format(" %-16s",type) + "]";
            }
            out += "\n";
        }
        return out;
    }

    /**
     * Called after the websocket connection has been closed
     *
     * @author Matthias Ruf
     * @param code error code. For a more detailed overview {@link org.java_websocket.framing.CloseFrame}
     * @param reason additional information about the reason of the error
     * @param remote Returns whether or not the closing of the connection was initiated by the remote host.
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {

        if(code == CloseFrame.NORMAL){
            System.out.println("TESTCLIENT: Connection closed normally.");
        }else {
            System.out.println("TESTCLIENT: Connection closed with code: " + code + ".");
        }

    }

    /**
     * The onStart method is called when the Server is started.
     *
     * @author Matthias Ruf
     * @param ex The exception causing this error
     */
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }


    /**
     *
     * Getter of the messagesIn attribut. Returns a list of all messages the testClient received.
     *
     * @author Matthias Ruf
     * @return a (JSON string formatted list representation) of all received messages.
     */
    public List<String> getMessagesIn(){
        return this.messagesIn;
    }

    /**
     * Getter of the messagesOut attribut. Returns a list of all sent messages.
     *
     * @author Matthias Ruf
     * @return a (JSON string formatted list representation) of all sent messages.
     */
    public List<String> getMessagesOut(){
        return this.messagesOut;
    }

    /**
     * Sends text to the connected websocket server by using the super methode. It also logs the outgoing message by
     * adding it to the messageOut list.
     *
     * @author Matthias Ruf
     * @param message The json string representation of the message.
     */
    public void send(String message) {
        if ( isOpen() && !this.isClosed()) {
            this.messagesOut.add(message);
            super.send(message);
            this.logMessage(message,true);
        }else{
            System.err.println("[WARNING] Could not send the message. May the connection is closed. \nisOpen: " + this.isOpen() +
                    "\nisCloseds: " + this.isClosed()  );
            this.close();
        }
    }

    /**
     *
     * Sends a CharacterSelection message object created by using the given parameters to the server. The optional field
     * of the message is filled by a null value
     *
     * @author Matthias Ruf
     *
     * @param characterSelection true if the player has chosen the character, false if not. The size of this array is 12
     *                           by definition.
     */
    public void sendCharacterSelection(Boolean[] characterSelection){
        // stores the chosen heros
        this.chosenHeroes = new ArrayList<>();
        for (int i = 0; i < characterSelection.length;i++) {
            if(characterSelection[i]){
                chosenHeroes.add(this.heroSet[i]);
            }
        }
        // sends the message
        this.send(gson_.toJson( new CharacterSelection(null,characterSelection)));
    }


    /**
     *
     * Sends a DisconnectRequest to the server. This will initiate the disconnection process.
     *
     * @author Matthias Ruf
     *
     *
     */
    public void sendDisconnectRequest(){
        if(this.getConnection().isOpen()){
            this.send(gson_.toJson(new DisconnectRequest()));
        }
    }

    /**
     *
     * this methode logs incomming and outgoing messages into the TestServerLog
     *
     * @param message the message, which caused the call of the methode
     * @param outgoing Is the message an outgoing message (true) or an incomming
     * (false)
     *
     * @author Matthias Ruf
     *
     * @return was the log-operation successful
     */
    private boolean logMessage(String message, boolean outgoing){

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(this.username + "_LogFile.log",true))) {
            writer.write("\n"+(logMessageCount++)+(outgoing? " outgoing: " + message.contains("EndRoundRequest") :" incomming: " + message.contains("Nack"))
                    + "\n" + message +"\n "+ this.yourTurn);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException ex) {
          ex.printStackTrace();
        }

        return false;
    }


}
