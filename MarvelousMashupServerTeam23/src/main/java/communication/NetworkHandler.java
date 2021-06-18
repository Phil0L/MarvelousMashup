package communication;

import com.google.gson.JsonSyntaxException;
import communication.messages.ExtractorMessage;
import communication.messages.ExtractorMessageStructure;
import communication.messages.Message;
import communication.messages.MessageStructure;
import communication.messages.enums.MessageType;
import communication.messages.enums.Role;
import communication.messages.events.game.PauseStartEvent;
import communication.messages.events.game.PauseStopEvent;
import communication.messages.events.game.TurnTimeoutEvent;
import communication.messages.events.notification.Ack;
import communication.messages.events.notification.Nack;
import communication.messages.login.*;
import communication.messages.requests.*;
import logic.Controller.Controller;
import logic.Controller.HandleReturn;
import logic.model.Player;
import logic.timer.SimpleTimer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;


import com.google.gson.Gson;
import parameter.Configuration;
import parameter.ConfigHero;

/**
 * This is the class that implements the server used for the communication with the clients
 * @author Adrian Groeber
 * @author Luka Stoehr
 */

public class NetworkHandler extends WebSocketServer {


    private final Gson gson_; // Gson object is initialised in the Constructor
    public Controller controller;
    public ArrayList<Profile> profileList = new ArrayList<>();

    //if a player sends a PauseStartRequest this Boolean is set to true
    public Boolean gamePaused = false;

    public WebSocket pauseSocket;

    Configuration configuration;

    private Boolean lock = false;

    public Boolean messageLock = false;


    /**
     * Constructor of NetworkHandler calls the Constructor of the WebSocketServer
     * @author Adrian Groeber
     * @author Luka Stoehr
     */
    public NetworkHandler(Configuration configuration){
        super(new InetSocketAddress("localhost", 1218));
        this.configuration = configuration;
        gson_ = new Gson();
        controller = new Controller(configuration);
    }

    /**
     * Constructor of NetworkHandler calls the Constructor of the WebSocketServer
     * @author Sarah Engele
     *
     * @param configuration the Configuration of the upcoming game
     * @param port the port of the Websocket server
     */
    public NetworkHandler(Configuration configuration, int port){
        super(new InetSocketAddress("localhost", port));
        this.configuration = configuration;
        gson_ = new Gson();
        controller = new Controller(configuration);
    }

    /**
     * The onOpen methode is called every time a WebSocket connection is opened.
     * @author Adrian Groeber
     * @param conn the opened WebSocket
     * @param handshake the corresponding ClientHandshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("SERVER: Server accepted connection "+ conn.getRemoteSocketAddress());
        Attachment attachment = new Attachment(null , null);    //Attachment is created for future use
        conn.setAttachment(attachment);     // the Attachment is attached to the WebSocket
    }

    /**
     * The onClose methode is called every time a WebSocket connection is closed.
     * @author Adrian Groeber
     * @param conn the WebSocket which is closed
     * @param code error code. For a more detailed overview {@link org.java_websocket.framing.CloseFrame}
     * @param reason the reason for the closing of the connection
     * @param remote Returns whether or not the closing of the connection was initiated by the remote host
     *
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        controller.SpectatorList.remove(conn);
        System.out.println("SERVER: Connection to " + ((conn.getAttachment()) == null ? "unknown remote" :
            ((Attachment) (conn.getAttachment())).profile == null ? "unknown profile" :
                ((Attachment) (conn.getAttachment())).profile.name + ":" +
                        ((Attachment) (conn.getAttachment())).profile.deviceID) + " closed");

        //if the disconnected WebSocket was associated with a Player, start the timer for the reconnect
        if(conn.getAttachment() != null){
            if (controller != null && controller.turnTimer != null)
                controller.turnTimer.stop();
            Attachment attachment = conn.getAttachment();
            if(attachment != null && attachment.profile != null && (checkPlayerIdentity(attachment.profile)) != null){
                Player player = checkPlayerIdentity(attachment.profile);
                player.timeoutTimer = new SimpleTimer(configuration.matchConfig.maxResponseTime) {
                    @Override
                    public void execute() {
                        timeoutAction(player);
                    }
                };
            }
        }
    }

    /**
     * The onMessage method is called whenever a message is received by the WebSocket connection.
     * It also specifies what measures have to be taken depending on the message type.
     * @author Adrian Groeber
     * @author Benno Hoelz
     * @author Sarah Engele
     * @author Matthias Ruf
     * @param conn the WebSocket on which a message arrived
     * @param message the message String
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("SERVER: Message Received:"+message);

        Attachment attachment = conn.getAttachment();
        Profile profile;
        try{
            ExtractorMessageType extractorMessageType = gson_.fromJson(message, ExtractorMessageType.class);

            if(!attachment.loginFinished){     //if there is no attachment to the WebSocket yet or if if the Login is not finished yet check for Login messages

                switch (extractorMessageType.messageType) {

                    case HELLO_SERVER:

                        if(attachment.profile != null){ // a HelloServer message is only viable if there hasn't been any other message before which means there should be no Attachment
                            protocolViolation(conn, attachment.profile);
                            return;
                        }

                        // parse MessageObject and store some useful information:
                        HelloServer helloServerObject = gson_.fromJson(message, HelloServer.class);
                        profile = checkProfileList(conn, helloServerObject.name, helloServerObject.deviceID);

                        attachment = new Attachment(MessageType.HELLO_SERVER, profile);
                        conn.setAttachment(attachment); // the conn Attachment is used to check the correct succession of the messages as defined in the protocol

                        String optionals1 = "";
                        conn.send(gson_.toJson(new HelloClient(optionals1, controller.runningGame)));
                        System.out.println("SERVER: Sending Hello Client");
                        break;

                    case RECONNECT:

                        if(!checkAttachment(conn, MessageType.HELLO_SERVER, MessageType.RECONNECT)){ // the last message sent by the client has to be a HelloServer message otherwise a protocol violation is detected
                            protocolViolation(conn, attachment.profile);
                            return;
                        }
                        profile = attachment.profile;

                        Reconnect reconnectObject = gson_.fromJson(message, Reconnect.class);
                        String optionals2 = "";
                        if(reconnectObject.reconnect){  // What to do if the client wants to reconnect to his game? --> verify that the client is a player
                            if(controller.model.playerOne.profile.equals(profile)){   // check first player identity
                                controller.model.playerOne.profile = profile;

                                //stop the reconnect Timer
                                controller.model.playerOne.timeoutTimer.stop();
                                // send client general assignment message
                                conn.send(gson_.toJson(new GeneralAssignment(optionals2, controller.model.gameID)));
                                // send GameStructure Message to Client after Reconnect:
                                conn.send(gson_.toJson(new GameStructure(
                                        "PlayerOne",
                                        this.controller.model.playerOne.profile.name,
                                        this.controller.model.playerTwo.profile.name,
                                        controller.toConfigHeroArray(this.controller.model.playerOne.playerTeam),
                                        controller.toConfigHeroArray(this.controller.model.playerTwo.playerTeam),
                                        this.configuration.matchConfig,
                                        this.configuration.scenarioConfig
                                )));
                                // change loginFinished to true:
                                attachment.loginFinished = true;
                                conn.setAttachment(attachment);
                            }
                            else if(controller.model.playerTwo.profile.equals(profile)){   // check second player identity
                                controller.model.playerTwo.profile = profile;

                                //stop the reconnect Timer
                                controller.model.playerTwo.timeoutTimer.stop();
                                // send client general assignment message:
                                conn.send(gson_.toJson(new GeneralAssignment(optionals2, controller.model.gameID)));
                                // send GameStructure Message to Client after Reconnect:
                                conn.send(gson_.toJson(new GameStructure(
                                        "PlayerTwo",
                                        this.controller.model.playerOne.profile.name,
                                        this.controller.model.playerTwo.profile.name,
                                        controller.toConfigHeroArray(this.controller.model.playerOne.playerTeam),
                                        controller.toConfigHeroArray(this.controller.model.playerTwo.playerTeam),
                                        this.configuration.matchConfig,
                                        this.configuration.scenarioConfig
                                )));
                                // change loginFinished to true:
                                attachment.loginFinished = true;
                                conn.setAttachment(attachment);
                            }
                            else{ // client is not a player
                                conn.send(gson_.toJson(new GoodbyeClient(optionals2,
                                        "You are not registered as a player. Connection is closed.")));
                                conn.close();
                                profileList.remove(profile);
                                break;
                            }
                        }
                        else{
                            // What to do if the client wants to start a new game while one game is still going on:
                            // send a winner message and follow the instructions of the client
                            Player player = checkPlayerIdentity(profile);
                            if(player != null){
                                DisconnectRequest request = new DisconnectRequest();
                                HandleReturn controlReturn = controller.handleRequest(player, request);
                                MessageStructure outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                                broadcast(outgoingMessage, player);

                                // disconnect all clients except for reconnecting client:
                                if(player.equals(controller.model.playerOne)){
                                    controller.model.playerTwo.profile.conn.send(gson_.toJson(new GoodbyeClient("", "Disconnect request received. Connection is closed. Good bye!")));
                                    controller.model.playerTwo.profile.conn.close();
                                } else if(player.equals(controller.model.playerTwo)){
                                    controller.model.playerOne.profile.conn.send(gson_.toJson(new GoodbyeClient("", "Disconnect request received. Connection is closed. Good bye!")));
                                    controller.model.playerOne.profile.conn.close();
                                }
                                controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(new GoodbyeClient("", "Disconnect request received. Connection is closed. Good bye!"))));
                                controller.SpectatorList.forEach(WebSocket::close);
                            } else{
                                conn.send(gson_.toJson(new GoodbyeClient(optionals2,
                                        "You are not registered as a player. Connection is closed.")));
                                conn.close();
                                profileList.remove(profile);
                                break;
                            }

                        }

                        break;

                    case PLAYER_READY:

                        if(!checkAttachment(conn, MessageType.HELLO_SERVER, MessageType.PLAYER_READY)){    //the last message sent by the client has to be a HelloServer message otherwise a protocol violation is detected
                            protocolViolation(conn, attachment.profile);
                            return;
                        }
                        profile = attachment.profile;

                        PlayerReady playerReadyObject = gson_.fromJson(message,PlayerReady.class);

                        String optionals3 = "";

                        if(playerReadyObject.startGame && (playerReadyObject.role == Role.PLAYER || playerReadyObject.role == Role.KI)){   //Case 1: Player wants to start new game as a Player
                            Player player = new Player(profile, playerReadyObject.role);   //Player object is created
                            controller.playerReadyList.add(player);  //player is added to the waiting List

                            if(controller.playerReadyList.size() > 1 && !controller.runningGame){   //if 2 or more players are ready a game is started if there is no current game going on
                                Player playerOne = controller.playerReadyList.remove(0);
                                Player playerTwo = controller.playerReadyList.remove(0);

                                controller.createModel(playerOne, playerTwo); //model is created --> game exists, Players have been removed form waiting List

                                ConfigHero[] configCharArray = controller.configuration.characterConfig.characters.clone();

                                //At this point the array of ConfigHeroes is shuffled so that the players get different sets of characters each game
                                Collections.shuffle(Arrays.asList(configCharArray));

                                //The players each get disjoint Arrays of 12 ConfigHeroes
                                controller.model.playerOne.teamOptions = Arrays.copyOfRange(configCharArray, 0, 12, ConfigHero[].class);
                                controller.model.playerTwo.teamOptions = Arrays.copyOfRange(configCharArray, 12, 24, ConfigHero[].class);

                                //The Arrays are sent to the players
                                playerOne.profile.conn.send(gson_.toJson(new GameAssignment(optionals3, controller.model.gameID, controller.model.playerOne.teamOptions)));
                                playerTwo.profile.conn.send(gson_.toJson(new GameAssignment(optionals3, controller.model.gameID, controller.model.playerTwo.teamOptions)));

                                controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(new GeneralAssignment("", controller.model.gameID))));
                            }
                        }
                        else if(playerReadyObject.startGame && playerReadyObject.role == Role.SPECTATOR){   //Case 2: Player wants to start new game as a Spectator
                            if(controller.runningGame){
                                conn.send(gson_.toJson(new GeneralAssignment("", controller.model.gameID)));
                                conn.send(gson_.toJson(new GameStructure(
                                        "Spectator", controller.model.playerOne.profile.name,
                                        controller.model.playerTwo.profile.name,
                                        controller.toConfigHeroArray(controller.model.playerOne.playerTeam),
                                        controller.toConfigHeroArray(controller.model.playerTwo.playerTeam),
                                        controller.configuration.matchConfig, controller.configuration.scenarioConfig
                                )));
                                Message[] gameState = {controller.getGameState()};
                                MessageStructure outgoingMessage = new MessageStructure(MessageType.EVENTS, gameState , "", null);
                                conn.send(gson_.toJson(outgoingMessage));
                            } else if(!controller.runningGame && controller.model != null){
                                conn.send(gson_.toJson(new GeneralAssignment("", controller.model.gameID)));
                            }

                            attachment.loginFinished = true;
                            conn.setAttachment(attachment);
                            controller.SpectatorList.add(conn);

                        }
                        else{       //Case 3: Player went back to main menu --> No game is created and the connection is closed
                            conn.send(gson_.toJson(new GoodbyeClient(optionals3, "Connection is closed.")));
                            profileList.remove(profile);
                            conn.close();
                        }
                        break;

                    case CHARACTER_SELECTION:

                        if(!checkAttachment(conn, MessageType.PLAYER_READY, MessageType.CHARACTER_SELECTION)){

                            protocolViolation(conn, attachment.profile);
                            return;
                        }
                        profile = attachment.profile;

                        CharacterSelection characterSelectionObject = gson_.fromJson(message, CharacterSelection.class);

                        ConfigHero[] heroOptions;

                        if(controller.model.playerOne.profile.equals(profile)){   //check first player identity
                            heroOptions = controller.model.playerOne.teamOptions;
                        }
                        else if(controller.model.playerTwo.profile.equals(profile)){   //check second player identity
                            heroOptions = controller.model.playerTwo.teamOptions;
                        }
                        else{      //profile doesn't belong to a player --> protocol violation
                            protocolViolation(conn, profile);
                            return;
                        }

                        if(characterSelectionObject.characters.length != controller.charOptLength){    //check array length
                            protocolViolation(conn, profile);
                            return;
                        }
                        if(Arrays.stream(characterSelectionObject.characters).filter((Boolean i) -> i).count() != controller.heroTeamLength){
                            protocolViolation(conn, profile);
                            return;
                        }
                        int trueCount = 0;

                        ConfigHero[] heroSelection = new ConfigHero[controller.heroTeamLength];
                        for(int iterator = 0; iterator < controller.charOptLength; iterator++){    //fill the accepted Heroes in a array with proper length
                            if(characterSelectionObject.characters[iterator]){
                                heroSelection[trueCount] = heroOptions[iterator];
                                trueCount++;
                            }
                        }

                        synchronized (lock){

                            if(!lock){
                                lock = true;
                                if(!controller.model.setPlayerTeam(profile, heroSelection)){
                                    protocolViolation(conn, profile);
                                    return;
                                }
                                lock = false;
                            }

                            if(controller.model.playerOne.selectionConfirmed && controller.model.playerTwo.selectionConfirmed){
                                controller.model.playerOne.profile.conn.send(gson_.toJson(
                                        new GameStructure("PlayerOne", controller.model.playerOne.profile.name,
                                                controller.model.playerTwo.profile.name,
                                                controller.toConfigHeroArray(controller.model.playerOne.playerTeam),
                                                controller.toConfigHeroArray(controller.model.playerTwo.playerTeam),
                                                controller.configuration.matchConfig, controller.configuration.scenarioConfig)));

                                attachment = controller.model.playerOne.profile.conn.getAttachment();
                                attachment.loginFinished = true;
                                controller.model.playerOne.profile.conn.setAttachment(attachment);

                                controller.model.playerTwo.profile.conn.send(gson_.toJson(
                                        new GameStructure("PlayerTwo", controller.model.playerOne.profile.name,
                                                controller.model.playerTwo.profile.name,
                                                controller.toConfigHeroArray(controller.model.playerOne.playerTeam),
                                                controller.toConfigHeroArray(controller.model.playerTwo.playerTeam),
                                                controller.configuration.matchConfig, controller.configuration.scenarioConfig)));

                                attachment = controller.model.playerTwo.profile.conn.getAttachment();
                                attachment.loginFinished = true;
                                controller.model.playerTwo.profile.conn.setAttachment(attachment);

                                for (WebSocket specConn:controller.SpectatorList) {
                                    specConn.send(gson_.toJson(
                                            new GameStructure("Spectator", controller.model.playerOne.profile.name,
                                                    controller.model.playerTwo.profile.name,
                                                    controller.toConfigHeroArray(controller.model.playerOne.playerTeam),
                                                    controller.toConfigHeroArray(controller.model.playerTwo.playerTeam),
                                                    controller.configuration.matchConfig, controller.configuration.scenarioConfig)));
                                }

                                HandleReturn handleReturn = controller.startGame();
                                if(handleReturn.requestSuccessful){
                                    MessageStructure outgoingMessage = new MessageStructure(MessageType.EVENTS, handleReturn.eventList.toArray(new Message[0]), "", null);
                                    controller.model.playerOne.profile.conn.send(gson_.toJson(outgoingMessage));
                                    controller.model.playerTwo.profile.conn.send(gson_.toJson(outgoingMessage));
                                    controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(outgoingMessage)));
                                }


                            }
                            else{
                                String optionals4 = "";
                                conn.send(gson_.toJson(new ConfirmSelection(optionals4, true)));
                            }
                        }
                        break;

                    case ERROR:

                    default:
                        protocolViolation(conn, attachment.profile);    //if message is not familiar a protocol violation is triggered
                }
            }
            //expect inGame messages if game is running and if the received ingameMessageType equals REQUESTS
            else if (controller.runningGame && extractorMessageType.messageType == MessageType.REQUESTS) {

                ExtractorMessageStructure messageStructure = gson_.fromJson(message, ExtractorMessageStructure.class);

                ArrayList<ExtractorMessage> inGameMessageArray = new ArrayList<>(Arrays.asList(messageStructure.messages));

                inGameMessageArray.forEach((ExtractorMessage requestMessage)-> processRequest(conn, requestMessage));
                messageLock = false;
            }

            else if(controller.runningGame && extractorMessageType.messageType != MessageType.REQUESTS){
                protocolViolation(conn, attachment.profile);
            }

        }
        catch(JsonSyntaxException e){
            System.err.println("SERVER: Error while trying to parse message has occurred.\nCannot parse:\n  " + message.replaceAll("\n", "\n  "));
        }
    }

    /**
     * The onError method is called when an error occurs. If an error causes the WebSocket connection to fail, onClose
     * will be called additionally.
     * @author Adrian
     * @param conn the WebSocket connection which the error occurred on
     * @param ex the Exception which has been detected
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {

        ex.printStackTrace();
        Attachment attachment = conn.getAttachment();
        profileList.remove(attachment.profile);
    }

    /**
     * The onStart method is called when the Server is started.
     * @author Adrian
     */
    @Override
    public void onStart() {
        System.out.println("SERVER: Server started successfully: " + getAddress());
    }

    /**
     * This method is called if a protocol violation has been detected. It will send a GoodByeClient message and then
     * close the connection and delete the profile from the profileList.
     * @author Adrian Groeber
     * @param conn the WebSocket the violation has been detected on
     * @param profile the profile of the client which has caused the violation
     */
    public void protocolViolation(WebSocket conn, Profile profile){
        conn.send(gson_.toJson(new GoodbyeClient("", "connection is closed because a protocol violation occurred")));
        if(profile != null){
            profileList.remove(profile);
            Player player = checkPlayerIdentity(profile);
            controller.playerReadyList.remove(player);
            if(controller.runningGame && player != null){

                HandleReturn controlReturn = controller.playerKicked(player);
                MessageStructure outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                broadcast(outgoingMessage, player);
                controller.model.playerOne.profile.conn.close();
                controller.model.playerTwo.profile.conn.close();
                controller.SpectatorList.forEach(WebSocket::close);
            }
        }
        if(conn.isOpen()){
            conn.close(-1);
        }
    }

    /**
     * This method is called in the onMessage method when a helloServer message has been received to check if there is
     * already a profile for the client in the profileList.
     * If this is the case, the already existing profile is returned. Else a new profile is created and returned.
     * @author Adrian Groeber
     * @param conn the WebSocket of the new/reconnecting client
     * @param name the name of the new/reconnecting client
     * @param deviceID the deviceID of the new/reconnecting client
     * @return method returns the final profile for the client
     */
    public Profile checkProfileList(WebSocket conn, String name, String deviceID){
        for(Profile listProfile: profileList){    //the profileList is checked if there is already a profile in the list for the client with for each loop
            if(listProfile.equals(new Profile(conn, name, deviceID))){
                listProfile.conn = conn;    //in case there is already a profile existing, the WebSocket is changed to the new one
                return listProfile;     //the "old" profile is returned
            }
        }
        Profile profile = new Profile(conn, name, deviceID);    //in case the client is new, a new profile is created
        profileList.add(profile);
        return profile;
    }

    /**
     * This method checks if the client follows the correct succession of messages as specified in the network protocol.
     * In detail, it looks into the Attachment object attached to the WebSocket and checks if the checkpoint variable has
     * stored the correct messageType. If the Attachment object is null or if the messageType is wrong it returns false.
     * EXAMPLE: If the onMessage method receives a PlayerReady message, the previous message of the client had to be a
     * HelloServer message which means that this method checks if the checkPoint variable equals the HELLO_SERVER type.
     * If the attachment is valid the checkpoint is updated and the Attachment is reattached to the WebSocket.
     * @author Adrian Groeber
     *
     * @param conn the WebSocket for which the attachment object should be verified
     * @param previous the messageType which is expected to be in the checkPoint variable of the Attachment
     * @param current the messageType which has to be filled into the checkPoint variable of the Attachment
     * @return returns true if the Attachment is valid which means there is no protocol violation
     */
    public boolean checkAttachment(WebSocket conn, MessageType previous, MessageType current){
        Attachment attachment = conn.getAttachment();

        if(attachment == null || attachment.checkPoint != previous){    //the last message sent by the client has to be a HelloServer message otherwise a protocol violation is detected
            return false;
        }
        else{
            attachment.checkPoint = current;
            conn.setAttachment(attachment);
            return true;
        }
    }

    /**
     * This method is called to check if a Profile is connected to a Player object
     * @author Adrian Groeber
     * @param profile profile which is checked
     * @return method returns the player the profile is associated with or null if there is no player with the same profile found
     */
    public Player checkPlayerIdentity(Profile profile){
        try {
            if (controller.model.playerOne.profile.equals(profile)) {   //check first player profile
                return controller.model.playerOne;
            } else if (controller.model.playerTwo.profile.equals(profile)) {   //check second player profile
                return controller.model.playerTwo;
            } else {      //profile doesn't belong to a player --> return null
                return null;
            }
        }catch (NullPointerException e){
            return null;
        }
    }

    /**
     * This method is used to send a MessageStructure object to the Players and the Spectators.
     * It also appends the Ack message for the player who sent the request at the start of the Message Array
     * @author Adrian Groeber
     * @param messageStructure The object containing the message array
     * @param player the player who send the successful request
     */
    public void broadcast(MessageStructure messageStructure, Player player){


        List<Message> tempList = new ArrayList<Message>(Arrays.asList(messageStructure.messages));
        tempList.add(0, new Ack());
        MessageStructure tempStructure = new MessageStructure(messageStructure.messageType, tempList.toArray(new Message[0]),
                             messageStructure.customContentType,
                             messageStructure.customContent);

        if(player.equals(controller.model.playerOne)){
            controller.model.playerOne.profile.conn.send(gson_.toJson(tempStructure));
            controller.model.playerTwo.profile.conn.send(gson_.toJson(messageStructure));
        }
        else if(player.equals(controller.model.playerTwo)){
            controller.model.playerTwo.profile.conn.send(gson_.toJson(tempStructure));
            controller.model.playerOne.profile.conn.send(gson_.toJson(messageStructure));
        }
        controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(messageStructure)));
    }

    /**
     * The processRequest method is used to
     * 1. distinguish the type of the Request and and convert it into a Message object
     * 2. check if the Request was sent by a player
     * 3. pass the Request to the corresponding method of controller
     * 4. sent Ack + Events / Nack + Gamestate to the Player
     * 5 broadcast the Events to everyone else
     * @author Adrian Groeber
     * @param conn The WebSocket which received the message
     * @param requestMessage The Request which has to be handled by the RequestHandler
     */
    public void processRequest(WebSocket conn, ExtractorMessage requestMessage){
        HandleReturn controlReturn;

        MessageStructure outgoingMessage;

        //if the connection is closed or a Nack has been sent
        if(conn.isClosed() || messageLock || controller.matchEnded){    //in case the webSocket has already been closed  do nothing
            return;
        }
        Attachment attachment = conn.getAttachment();
        Profile profile = attachment.profile;
        Player player;

        if(requestMessage.requestType == null){
            protocolViolation(conn, profile);
            return;
        }

        switch (requestMessage.requestType) {

            case DisconnectRequest:
                player = checkPlayerIdentity(profile);
                if(player != null){
                    DisconnectRequest request =  requestMessage.toDisconnectRequest();
                    controlReturn = controller.handleRequest(player, request);
                    outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                    broadcast(outgoingMessage, player);
                    // send GoodbyeClient to all clients:
                    controller.model.playerOne.profile.conn.send(gson_.toJson(new GoodbyeClient("", "Disconnect request received. Connection is closed. Good bye!")));
                    controller.model.playerTwo.profile.conn.send(gson_.toJson(new GoodbyeClient("", "Disconnect request received. Connection is closed. Good bye!")));
                    controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(new GoodbyeClient("", "Disconnect request received. Connection is closed. Good bye!"))));
                    // disconnect all clients:
                    controller.model.playerOne.profile.conn.close();
                    controller.model.playerTwo.profile.conn.close();
                    controller.SpectatorList.forEach(WebSocket::close);
                }
                else if(controller.SpectatorList.contains(conn)){   //spectator wants to disconnect
                    conn.send(gson_.toJson(new GoodbyeClient("", "Disconnect Request received. Connection is closed.")));
                    conn.close();
                }
                break;

            case EndRoundRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    EndRoundRequest request = requestMessage.toEndRoundRequest();
                    controlReturn = controller.handleRequest(player, request);
                    if(controlReturn.requestSuccessful){
                        outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                        broadcast(outgoingMessage, player);
                        // the wrongMessage List is reset after each successful message
                        player.wrongMessage.clear();
                    }
                    else{
                        messageLock = true;
                        if(player.wrongMessage.contains(request)){
                            protocolViolation(conn, profile);
                        }
                        else{
                            player.wrongMessage.add(request);
                            Message[] requestDenied = {new Nack(), controller.getGameState()};
                            outgoingMessage = new MessageStructure(MessageType.EVENTS, requestDenied , "", null);
                            conn.send(gson_.toJson(outgoingMessage));
                        }
                    }
                }
                else if(player != null && gamePaused){
                    Message[] tempMessage = {new PauseStopEvent()};
                    conn.send(gson_.toJson(new MessageStructure(MessageType.EVENTS, tempMessage, "", null)));
                }
                else{
                    protocolViolation(conn, profile);
                }
                break;

            case ExchangeInfinityStoneRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    ExchangeInfinityStoneRequest request = requestMessage.toExchangeInfinityStoneRequest();
                    controlReturn = controller.handleRequest(player, request);
                    if(controlReturn.requestSuccessful){
                        outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                        broadcast(outgoingMessage, player);
                        // the wrongMessage List is reset after each successful message
                        player.wrongMessage.clear();
                    }
                    else{
                        messageLock = true;
                        if(player.wrongMessage.contains(request)){
                            protocolViolation(conn, profile);
                        }
                        else{
                            player.wrongMessage.add(request);
                            Message[] requestDenied = {new Nack(), controller.getGameState()};
                            outgoingMessage = new MessageStructure(MessageType.EVENTS, requestDenied , "", null);
                            conn.send(gson_.toJson(outgoingMessage));
                        }
                    }
                }
                else if(player != null && gamePaused){
                    Message[] tempMessage = {new PauseStopEvent()};
                    conn.send(gson_.toJson(new MessageStructure(MessageType.EVENTS, tempMessage, "", null)));
                }
                else{
                    protocolViolation(conn, profile);
                }
                break;

            case MeleeAttackRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    MeleeAttackRequest request = requestMessage.toMeleeAttackRequest();
                    controlReturn = controller.handleRequest(player, request);
                    if(controlReturn.requestSuccessful){
                        outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                        broadcast(outgoingMessage, player);
                        // the wrongMessage List is reset after each successful message
                        player.wrongMessage.clear();
                    }
                    else{
                        messageLock = true;
                        if(player.wrongMessage.contains(request)){
                            protocolViolation(conn, profile);
                        }
                        else{
                            player.wrongMessage.add(request);
                            Message[] requestDenied = {new Nack(), controller.getGameState()};
                            outgoingMessage = new MessageStructure(MessageType.EVENTS, requestDenied , "", null);
                            conn.send(gson_.toJson(outgoingMessage));
                        }
                    }
                }
                else if(player != null && gamePaused){
                    Message[] tempMessage = {new PauseStopEvent()};
                    conn.send(gson_.toJson(new MessageStructure(MessageType.EVENTS, tempMessage, "", null)));
                }
                else{
                    protocolViolation(conn, profile);
                }
                break;

            case MoveRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    MoveRequest request = requestMessage.toMoveRequest();
                    controlReturn = controller.handleRequest(player, request);
                    if(controlReturn.requestSuccessful){
                        outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                        broadcast(outgoingMessage, player);
                        // the wrongMessage List is reset after each successful message
                        player.wrongMessage.clear();
                    }
                    else{
                        messageLock = true;
                        if(player.wrongMessage.contains(request)){
                            protocolViolation(conn, profile);
                        }
                        else{
                            player.wrongMessage.add(request);
                            Message[] requestDenied = {new Nack(), controller.getGameState()};
                            outgoingMessage = new MessageStructure(MessageType.EVENTS, requestDenied , "", null);
                            conn.send(gson_.toJson(outgoingMessage));
                        }
                    }
                }
                else if(player != null && gamePaused){
                    Message[] tempMessage = {new PauseStopEvent()};
                    conn.send(gson_.toJson(new MessageStructure(MessageType.EVENTS, tempMessage, "", null)));
                }
                else{
                    protocolViolation(conn, profile);
                }

                break;

            case PauseStartRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    gamePaused = true;
                    pauseSocket = conn;

                    controller.turnTimer.stop();

                    Message[] tempMessage = {new PauseStartEvent()};
                    broadcast(new MessageStructure(MessageType.EVENTS, tempMessage, "", null), player);
                }
                else{
                    protocolViolation(conn, profile);
                }


                break;

            case PauseStopRequest:
                player = checkPlayerIdentity(profile);
                if(gamePaused){
                    if(conn.equals(pauseSocket)){
                        gamePaused = false;
                        pauseSocket = null;

                        controller.turnTimer.resume();

                        Message[] tempMessage = {new PauseStopEvent()};
                        broadcast(new MessageStructure(MessageType.EVENTS, tempMessage, "", null), player);
                    }
                    else{   // in case someone tries to cancel the pause who is not the player who started it
                        protocolViolation(conn, profile);
                    }
                }
                else{
                    protocolViolation(conn, profile);
                }

                break;

            case RangedAttackRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    RangedAttackRequest request = requestMessage.toRangeAttackRequest();
                    controlReturn = controller.handleRequest(player, request);
                    if(controlReturn.requestSuccessful){
                        outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                        broadcast(outgoingMessage, player);
                        // the wrongMessage List is reset after each successful message
                        player.wrongMessage.clear();
                    }
                    else{
                        messageLock = true;
                        if(player.wrongMessage.contains(request)){
                            protocolViolation(conn, profile);
                        }
                        else{
                            player.wrongMessage.add(request);
                            Message[] requestDenied = {new Nack(), controller.getGameState()};
                            outgoingMessage = new MessageStructure(MessageType.EVENTS, requestDenied , "", null);
                            conn.send(gson_.toJson(outgoingMessage));
                        }
                    }
                }
                else if(player != null && gamePaused){
                    Message[] tempMessage = {new PauseStopEvent()};
                    conn.send(gson_.toJson(new MessageStructure(MessageType.EVENTS, tempMessage, "", null)));
                }
                else{
                    protocolViolation(conn, profile);
                }
                break;

            case Req:
                Req reqRequest = requestMessage.toReq();
                controlReturn = controller.handleRequest(null, reqRequest);
                outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                conn.send(gson_.toJson(outgoingMessage));

                break;

            case UseInfinityStoneRequest:
                player = checkPlayerIdentity(profile);
                if(player != null && !gamePaused){
                    UseInfinityStoneRequest request = requestMessage.toUseInfinityStoneRequest();
                    controlReturn = controller.handleRequest(player, request);
                    if(controlReturn.requestSuccessful){
                        outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
                        broadcast(outgoingMessage, player);
                        // the wrongMessage List is reset after each successful message
                        player.wrongMessage.clear();
                    }
                    else{
                        messageLock = true;
                        if(player.wrongMessage.contains(request)){
                            protocolViolation(conn, profile);
                        }
                        else{
                            player.wrongMessage.add(request);
                            Message[] requestDenied = {new Nack(), controller.getGameState()};
                            outgoingMessage = new MessageStructure(MessageType.EVENTS, requestDenied , "", null);
                            conn.send(gson_.toJson(outgoingMessage));
                        }
                    }
                }
                else if(player != null && gamePaused){
                    Message[] tempMessage = {new PauseStopEvent()};
                    conn.send(gson_.toJson(new MessageStructure(MessageType.EVENTS, tempMessage, "", null)));
                }
                else{
                    protocolViolation(conn, profile);
                }
                break;
            default:
                protocolViolation(conn, attachment.profile);    //if message is not familiar a protocol violation is triggered
        }
    }

    /**
     * This method is used to define an actionListener for the case a player takes to long for his turn
     * @author Adrian Groeber
     *
     * @param player The Player the overTimeAction is set for
     * @return returns the ActionListener which can be used
     */
    public static void turnOverTimeAction(Player player, Controller controller){
        Message[] message = {new TurnTimeoutEvent()};
        player.profile.conn.send((new Gson()).toJson(new MessageStructure(MessageType.EVENTS, message, "", null)));
        HandleReturn controlReturn = controller.handleRequest(player, new EndRoundRequest());
        if(controlReturn.requestSuccessful){
            MessageStructure outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
            controller.model.playerOne.profile.conn.send((new Gson()).toJson(outgoingMessage));
            controller.model.playerTwo.profile.conn.send((new Gson()).toJson(outgoingMessage));
            controller.SpectatorList.forEach((WebSocket socket) -> socket.send((new Gson()).toJson(outgoingMessage)));
        }
    }




    /**
     * This method is used to define an actionListener for the case a player looses his connection and doesn't reconnect in time.
     * @author Adrian Groeber
     *
     * @param player The Player the timeoutAction is set for
     */
    public void timeoutAction(Player player){
        HandleReturn controlReturn = controller.playerKicked(player);
        if(controlReturn.requestSuccessful){
            MessageStructure outgoingMessage = new MessageStructure(MessageType.EVENTS, controlReturn.eventList.toArray(new Message[0]), "", null);
            if(player.equals(controller.model.playerOne)){
                controller.model.playerTwo.profile.conn.send(gson_.toJson(outgoingMessage));
                controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(outgoingMessage)));
            }
            else if(player.equals(controller.model.playerTwo)){
                controller.model.playerOne.profile.conn.send(gson_.toJson(outgoingMessage));
                controller.SpectatorList.forEach((WebSocket socket) -> socket.send(gson_.toJson(outgoingMessage)));
            }
            if(controller.model.playerOne.profile.conn.isOpen()){
                controller.model.playerOne.profile.conn.close();
            }
            if(controller.model.playerTwo.profile.conn.isOpen()){
                controller.model.playerTwo.profile.conn.close();
            }
            controller.SpectatorList.forEach(WebSocket::close);
        }
    }


}
