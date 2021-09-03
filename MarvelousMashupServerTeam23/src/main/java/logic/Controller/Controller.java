package logic.Controller;

import communication.NetworkHandler;
import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EntityID;
import communication.messages.events.entity.DestroyedEntityEvent;
import communication.messages.events.game.DisconnectEvent;
import communication.messages.events.game.RoundSetupEvent;
import communication.messages.events.game.TurnEvent;
import communication.messages.events.game.WinEvent;
import communication.messages.events.gamestate.GamestateEvent;
import communication.messages.objects.Entities;
import communication.messages.requests.*;
import logic.gameObjects.*;
import logic.model.Model;
import logic.model.Player;
import logic.timer.SimpleTimer;
import org.java_websocket.WebSocket;
import parameter.ConfigHero;
import parameter.Configuration;
import parameter.GrassRockEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * The Controller implements the game logic and alters the game model. It also reacts to
 * incoming messages from the NetworkHandler.
 * @author Adrian
 * @author Luka Stoehr
 */
public class Controller {
    /**
     * True, if a game is running
     */
    public boolean runningGame = false;
    /**
     * Model for this match, not needed if the currentGamesList is used, which is currently not the case.
     */
    public Model model;
    /**
     * Configuration object containing the config files
     */
    public Configuration configuration;
    /**
     * This is the SimpleTimer object used to limit the turn time of a player
     */
    public SimpleTimer turnTimer;
    /**
     * List of all Players waiting for a game
     */
    public ArrayList<Player> playerReadyList;

    //public ArrayList<Model> currentGamesList;    //A List (or another data structure) to store the ongoing games

    /**
     * How many characters the players can choose from
     */
    public int charOptLength = 12;
    /**
     * Length of each player team
     */
    public int heroTeamLength = 6;
    /**
     * List of WebSocket containing all Spectators
     */
    public ArrayList<WebSocket> SpectatorList;
    /**
     * True if the login phase is finished
     */
    public Boolean loginFinished;
    /**
     * The order of all heroes and NPCs in this round
     */
    public ArrayList<Placeable> turnOrder = new ArrayList<>();
    /**
     * Index to which Hero or NPCs turn it is. Current turn: turnOrder.get(turnCount)
     */
    public int turnCount;
    /**
     * List of all Events that have occurred and have to be sent to the clients
     */
    public ArrayList<Message> eventList = new ArrayList<>();
    /**
     * True if the match has ended
     */
    public boolean matchEnded = false;

    /**
     * Constructor for the Controller class.
     * @author Luka Stoehr
     * @param configuration Configuration object that contains all the parameters from the config files
     */
    public Controller(Configuration configuration){
        playerReadyList = new ArrayList<>();

        SpectatorList = new ArrayList<>();

        loginFinished = false;

        this.configuration = configuration;
    }

    /**
     * This method starts a new match. It spawns all Rocks and Heroes, creates a turnOrder
     * for the first round and calls the goose() method for the first time.
     * If this method is called while a match is already running, it returns a HandleReturn
     * object containing requestSuccessful=false.
     * @author Luka Stoehr
     * @return HandleReturn object containing all events that have occurred
     */
    public synchronized HandleReturn startGame(){
        if(!this.runningGame) {
            this.runningGame = true;
            this.loginFinished = true;

            //Place all Rocks on the field
            /* The x and y coordinate in scenario config are swapped compared to model.field array
             * This is because in model first array is x and inner is y-coordinate. The scenario config
             * is the other way around, so that you see a typical x-y-field when you open it in a
             * text editor.
             */
            for (int x = 0; x < configuration.scenarioConfig.scenario[0].length; x++) {
                for (int y = 0; y < configuration.scenarioConfig.scenario.length; y++) {
                    if (configuration.scenarioConfig.scenario[y][x] == GrassRockEnum.ROCK) {
                        Rock rock = new Rock(model);
                        rock.place(new Position(x, y));
                    }
                }
            }

            //Place all Portals on the field
            for (int x = 0; x < configuration.scenarioConfig.scenario[0].length; x++) {
                for (int y = 0; y < configuration.scenarioConfig.scenario.length; y++) {
                    if (configuration.scenarioConfig.scenario[y][x] == GrassRockEnum.PORTAL) {
                        Portal portal = new Portal(model);
                        portal.place(new Position(x, y));
                    }
                }
            }


            //Place all Heroes on the field randomly
            LinkedList<Position> freePosList = new LinkedList<>();
            for (int x = 0; x < model.field.length; x++) {
                for (int y = 0; y < model.field[0].length; y++) {
                    Position pos = new Position(x, y);
                    if (model.isFree(pos)) freePosList.add(pos);
                }
            }
            if (freePosList.size() < 12)
                throw new IllegalArgumentException("The scenario config must contain at least 12 gras fields to be able to place the heroes");
            Collections.shuffle(freePosList);
            for (Hero hero : model.playerOne.playerTeam) {
                hero.place(freePosList.pop());
            }
            for (Hero hero : model.playerTwo.playerTeam) {
                hero.place(freePosList.pop());
            }

            // runninggame true

            this.turnOrder.clear();
            //In first round all Heroes and Goose can do something
            this.turnOrder.addAll(Arrays.asList(model.playerOne.playerTeam));
            this.turnOrder.addAll(Arrays.asList(model.playerTwo.playerTeam));
            Collections.shuffle(this.turnOrder);
            model.goose = new Goose(model);
            this.turnOrder.add(0, model.goose);
            this.turnCount = 0;

            // Create RoundSetupEvent
            IDs[] orderIDs = new IDs[turnOrder.size()];
            for(int i = 0; i < turnOrder.size(); i++){
                orderIDs[i] = turnOrder.get(i).getIDs();
            }
            this.eventList.add(
                    new RoundSetupEvent(model.round, orderIDs)
            );

            // Carry out Gooses turn
            model.goose.gooseTurn();
            nextTurn();
            return handleReturn(true);
        }else{
            return handleReturn(false);
        }
    }

    /**
     * This method starts the next round. It decides on a new turnOrder randomly, resets
     * MPs and APs of Heroes and resets the turnCount and creates a RoundSetupEvent. Furthermore
     * it checks which NPCs are active in this round, places them in the turnOrder and calls
     * checkForNextTurn() to check if the current turn can be handled by the server itself.
     * @author Luka Stoehr
     */
    public void nextRound(){
        model.round++;
        // Shuffle heroes for new round and reset MP and AP
        this.turnOrder.clear();
        for(Hero hero: model.playerOne.playerTeam){
            this.turnOrder.add(hero);
            hero.restoreAP();
            hero.restoreMP();
        }
        for(Hero hero: model.playerTwo.playerTeam){
            this.turnOrder.add(hero);
            hero.restoreAP();
            hero.restoreMP();
        }
        //Handle Thanos
        if(model.round == configuration.matchConfig.maxRounds){
            // First time Thanos shows up
            model.thanos = new Thanos(model);
            // Destroy all portals
            for(int x = 0; x < model.field.length; x++){
                for(int y = 0; y < model.field[0].length; y++){
                    if(model.field[x][y] instanceof Portal){
                        Portal portal = (Portal) model.field[x][y];
                        this.eventList.add(
                                new DestroyedEntityEvent(portal.getPosAsArray(), portal.getIDs())
                        );
                        model.field[x][y] = null;
                    }
                }
            }
            //Place thanos
            LinkedList<Position> freePosList = new LinkedList<>();
            for(int x = 0; x < model.field.length; x++){
                for(int y = 0; y < model.field[0].length; y++){
                    Position pos = new Position(x,y);
                    if(model.isFree(pos)) freePosList.add(pos);
                }
            }
            Collections.shuffle(freePosList);
            model.thanos.place(freePosList.getFirst());

            turnOrder.add(model.thanos);
        }
        if(model.round > configuration.matchConfig.maxRounds){
            //Thanos continues playing, but was there before
            turnOrder.add(model.thanos);
            //Reset MPs to one more than last round
            model.thanos.currentMaxMovementPoints++;
            model.thanos.movementPoints = model.thanos.currentMaxMovementPoints;
        }

        Collections.shuffle(this.turnOrder);    // Shuffle the turn order

        this.turnCount = 0;     //Reset turn counts

        // Check if Goose or StanLee are active in this round
        if(model.round >= 0 && model.round <=5){
            this.turnOrder.add(0, model.goose);
        }
        if(model.round == 6){
            model.stanLee = new StanLee(model);
            this.turnOrder.add(0, model.stanLee);
        }

        // Create RoundSetupEvent
        IDs[] orderIDs = new IDs[turnOrder.size()];
        for(int i = 0; i < turnOrder.size(); i++){
            orderIDs[i] = turnOrder.get(i).getIDs();
        }
        this.eventList.add(
                new RoundSetupEvent(model.round, orderIDs)
        );
        this.eventList.add(
                new TurnEvent(this.turnCount, this.turnOrder.get(turnCount).getIDs())
        );
        // Check if next turn can be started automatically
        checkForNextTurn();
    }

    /**
     * This method starts the next turn by creating a TurnEvent and incrementing turnCount.
     * Before doing so, it also checks if one of the players has one.
     * If the next turn can be handled automatically (i.e. StanLee, Goose, Thanos,
     * knocked out character) the turn is handled and nextTurn() is called again.
     * If the current turnOrder is completed, nextRound() is called.
     * @author Luka Stoehr
     */
    public void nextTurn(){
        checkForWinEvent();
        if(!this.matchEnded) {
            this.turnCount++;
            if (turnCount >= this.turnOrder.size()) {
                //Time for next round
                nextRound();

            } else {
                model.controller.eventList.add(
                        new TurnEvent(this.turnCount, this.turnOrder.get(turnCount).getIDs())
                );
                //Check if next turn has to be handled automatically
                checkForNextTurn();
            }
            startTurnTimer();
        }
    }

    /**
     * This method checks if the next turn can be started automatically if it is currently
     * a heroes turn. This is the case if both the MovementPoints and the ActionPoints of a
     * Hero are used up. In this case the server does not wait for an EndRoundRequest, but
     * starts the next turn without waiting for the Player. This method is called after every
     * processed request.
     * @author Luka Stoehr
     */
    public void checkForNextTurn(){
        if(this.turnOrder.get(turnCount) instanceof Hero){
            Hero hero = (Hero) this.turnOrder.get(turnCount);
            if(hero.getMovementPoints() == 0 && hero.getActionPoints() == 0){
                turnTimer.stop();
                nextTurn();
            }else if(hero.getHealthPoints() == 0){
                turnTimer.stop();
                //Knocked out characters can't make moves
                nextTurn();
            }
        }else if(this.turnOrder.get(turnCount) instanceof Thanos){
            model.thanos.thanosTurn();
            nextTurn();
        }else if(this.turnOrder.get(turnCount) instanceof Goose){
            model.goose.gooseTurn();
            nextTurn();
        }else if(this.turnOrder.get(turnCount) instanceof StanLee){
            model.stanLee.stanLeeTurn();
            nextTurn();
        }
    }

    /**
     * This method lets the other player win if one of the players was kicked from the match, for
     * example due to a protocol violation. It is similar to the method
     * handleRequest(Player player, DisconnectRequest request).
     * @author Luka Stoehr
     * @param player The player that has been kicked
     * @return Handle Return containing success=true and all the events that have occurred
     */
    public HandleReturn playerKicked(Player player){
        int playerWon;
        if(player.equals(model.playerOne)){
            playerWon = 2;
        }else{
            playerWon = 1;
        }
        this.matchEnded = true;
        eventList.add(
                new WinEvent(playerWon)
        );
        eventList.add(
                new DisconnectEvent()
        );
        return handleReturn(true);
    }

    /**
     * This method is called by the NetworkHandler, if a Request should be processed. This method then calls
     * a handleRequest2() method, according to the received Request. This method (handleRequest(Player, Message)
     * only exists for synchronization purposes.
     * @author Luka Stoehr
     * @param player Player object, if processing request requires it. Null otherwise
     * @param request Message Object containing the request
     * @return HandleReturn containing a boolean indication whether the request was processed successful and all occurred Events
     */
    public HandleReturn handleRequest(Player player, Message request){
        synchronized (this){
            if(request instanceof DisconnectRequest){
                return handleRequest2(player, (DisconnectRequest) request);
            }else if(request instanceof EndRoundRequest){
                return handleRequest2(player, (EndRoundRequest) request);
            }else if(request instanceof ExchangeInfinityStoneRequest){
                return handleRequest2(player, (ExchangeInfinityStoneRequest) request);
            }else if(request instanceof MeleeAttackRequest){
                return handleRequest2(player, (MeleeAttackRequest) request);
            }else if(request instanceof MoveRequest){
                return handleRequest2(player, (MoveRequest) request);
            }else if(request instanceof RangedAttackRequest){
                return handleRequest2(player, (RangedAttackRequest) request);
            }else if(request instanceof Req){
                return handleRequest2(player, (Req) request);
            }else if(request instanceof UseInfinityStoneRequest){
                return handleRequest2(player, (UseInfinityStoneRequest) request);
            }else{
                return handleReturn(false);
            }
        }
    }

    /**
     * Handles a DisconnectRequest by a player and lets the other player win.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return Handle Return containing success=true and all the events that have occurred
     */
    public HandleReturn handleRequest2(Player player, DisconnectRequest request){
        int playerWon;
        if(player.equals(model.playerOne)){
            playerWon = 2;
        }else{
            playerWon = 1;
        }
        this.matchEnded = true;
        eventList.add(
                new WinEvent(playerWon)
        );
        eventList.add(
                new DisconnectEvent()
        );
        return handleReturn(true);
    }

    /**
     * Ends the turn for the current Character.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, EndRoundRequest request){
        turnTimer.stop();
        if(turnOrder.get(turnCount) instanceof Hero && player.oneOfMyHeroes((Hero)turnOrder.get(turnCount))){
            nextTurn();
            return handleReturn(true);
        }else{
            // Its not this players turn
            return handleReturn(false);
        }
    }

    /**
     * Handles an ExchangeInfinityStone Request by trying to transfer the stone form the
     * origin entity to the target entity.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, ExchangeInfinityStoneRequest request){
        if(!(turnOrder.get(turnCount) instanceof Hero)) return handleReturn(false);
        Hero originHero = (Hero) turnOrder.get(turnCount);
        Hero targetHero = null;

        for(Hero hero: player.playerTeam){
            if(hero.compareIDs(request.targetEntity)){
                targetHero = hero;
                break;
            }
        }
        if(targetHero == null){
            //Check if the target hero is from the opponent team
            Player opponent = player.equals(model.playerOne) ? model.playerTwo : model.playerOne;
            for(Hero hero: opponent.playerTeam){
                if(hero.compareIDs(request.targetEntity)){
                    targetHero = hero;
                    break;
                }
            }
        }

        // Check if it is the origin entities and the Players turn
        if(player.oneOfMyHeroes(originHero) &&
                originHero.compareIDs(request.originEntity) &&
                targetHero != null){
            // Check the positions
            if(!(checkPositionConsistency(originHero, targetHero, request.originField, request.targetField))){
                return handleReturn(false);
            }
            if(request.stoneType.entityID != EntityID.InfinityStones){
                return handleReturn(false); // The stoneType entityID is not an InfinityStone -> Error
            }
            boolean success = originHero.giveInfinityStone(targetHero, request.stoneType.ID);
            return handleReturn(success);
        }else{
            //Not your turn or something else went wrong
            return handleReturn(false);
        }
    }

    /**
     * Handles a MeleeAttackRequest, and carries it out if the request was valid.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, MeleeAttackRequest request){
        if(!(turnOrder.get(turnCount) instanceof Hero)) return handleReturn(false);
        Hero originHero = (Hero) turnOrder.get(turnCount);
        Placeable target = null;

        Placeable onTargetField = this.model.field[request.targetField[0]][request.targetField[1]];
        if(onTargetField instanceof Attackable &&
                onTargetField.compareIDs(request.targetEntity)){
            target = onTargetField;
        }

        // Check if it is the origin entities and the Players turn
        if(player.oneOfMyHeroes(originHero) &&
                originHero.compareIDs(request.originEntity) &&
                target != null){
            // Check the positions
            if(!(checkPositionConsistency(originHero, target, request.originField, request.targetField))){
                return handleReturn(false);
            }
            boolean success = originHero.nearAttack(target.getPosition());
            return handleReturn(success);
        }else{
            //Not your turn or something else went wrong
            return handleReturn(false);
        }
    }

    /**
     * Handles a MoveRequest by checking if it is the Players and its respective Heroes turn
     * and then calling the Hero.move() method if the request was valid.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, MoveRequest request){
        if(!(turnOrder.get(turnCount) instanceof Hero)) return handleReturn(false);
        Hero originHero = (Hero) turnOrder.get(turnCount);

        // Check if it is the origin entities and the Players turn
        if(player.oneOfMyHeroes(originHero) &&
                originHero.compareIDs(request.originEntity) &&
                checkPositionConsistency(originHero, request.originField)){
            boolean success = originHero.move(new Position(request.targetField[0], request.targetField[1]));
            return handleReturn(success);
        }else{
            //Not your turn or something else went wrong
            return handleReturn(false);
        }
    }

    /**
     * Handles a RangedAttackRequest by checking the request and starting the
     * RangedAttack (also called farAttack in Model) if the request was valid.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, RangedAttackRequest request){
        if(!(turnOrder.get(turnCount) instanceof Hero)) return handleReturn(false);
        Hero originHero = (Hero) turnOrder.get(turnCount);
        Placeable target = null;

        Placeable onTargetField = this.model.field[request.targetField[0]][request.targetField[1]];
        if(onTargetField instanceof Attackable &&
                onTargetField.compareIDs(request.targetEntity)){
            target = onTargetField;
        }

        // Check if it is the origin entities and the Players turn
        if(player.oneOfMyHeroes(originHero) &&
                originHero.compareIDs(request.originEntity) &&
                target != null){
            // Check the positions
            if(!(checkPositionConsistency(originHero, target, request.originField, request.targetField))){
                return handleReturn(false);
            }
            boolean success = originHero.farAttack(target.getPosition());
            return handleReturn(success);
        }else{
            //Not your turn or something else went wrong
            return handleReturn(false);
        }
    }

    /**
     * Is called when a client sends a "Req" request, asking for a GameState. This is always allowed, as long as
     * the Login-Phase is finished and a Game is running.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player null
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, Req request){
        if(this.runningGame && this.loginFinished){
            //Request was valid, return true.
            return handleReturn(true);
        }else{
            return handleReturn(false);
        }
    }

    /**
     * Handles an UseInfinityStoneRequest by calling the Hero.infinteraction() method
     * if the request is valid.
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param player the player who sent the request to the server
     * @param request the request object itself
     * @return HandleReturn containing the boolean success and an EventList with all new Events
     */
    public HandleReturn handleRequest2(Player player, UseInfinityStoneRequest request){
        if(!(turnOrder.get(turnCount) instanceof Hero)) return handleReturn(false);
        Hero originHero = (Hero) turnOrder.get(turnCount);
        InfinityStone stone = null;
        for(InfinityStone stoneInInventory: originHero.inventory){
            if(stoneInInventory.compareIDs(request.stoneType)) stone = stoneInInventory;
        }

        // Check if it is the origin entities and the Players turn
        if(player.oneOfMyHeroes(originHero) &&
                originHero.compareIDs(request.originEntity) &&
                checkPositionConsistency(originHero, request.originField) &&
                stone != null){
            boolean success = originHero.infinteraction(stone, new Position(request.targetField[0], request.targetField[1]));
            return handleReturn(success);
        }else{
            //Not your turn or something else went wrong
            return handleReturn(false);
        }
    }

    /**
     * The createModel method is used to create a new game model when a new game is started.
     * This happens as soon as two players are ready and no current game is being played.
     * At this point the Players have not chosen their Hero-Teams yet!
     * @author Adrian Groeber
     * @author Luka Stoehr
     * @param playerOne The first player
     * @param playerTwo The other player
     */
    public void createModel(Player playerOne, Player playerTwo){
        /* The x and y coordinate in scenario config are swapped compared to model.field array
         * This is because in model first array is x and inner is y-coordinate. The scenario config
         * is the other way around, so that you see a typical x-y-field when you open it in a
         * text editor.
         */
        model = new Model(this.configuration.scenarioConfig.scenario[0].length,
                this.configuration.scenarioConfig.scenario.length,
                this.configuration.matchConfig.maxRounds, playerOne, playerTwo, this);
    }

    /**
     * This method turns an Array of Hero objects and turns returns a corresponding Array of ConfigHeroes
     *
     * @author Adrian Groeber
     * @author Matthias Ruf
     * @author Benno Hoelz
     * @param heroArray an Array of Heroes
     * @return returns the converted Array of ConfigHeroes
     */
    public ConfigHero[] toConfigHeroArray(Hero[] heroArray){

        // create a new ArrayList which temporarily stores the heroes
        ArrayList<ConfigHero> configHeroes = new ArrayList<>();

        // uses stream API to transform a Hero into a ConfigHero and stores it into the list
        Arrays.stream(heroArray).forEach((Hero hero)-> configHeroes.add(hero.toConfigHero()));

        // Casts the List into a Array which the suitable Typ (ATTENTION toArray() without the Array as a parameter
        // and cast afterwords does not work.)
        return configHeroes.toArray(new ConfigHero[configHeroes.size()]);
    }

    /**
     * Checks if the Positions of the Placeables match the Positions given in the field arrays.
     * This needs to be checked several times when working with requests.
     * @author Luka Stoehr
     * @param origin Origin Placeable
     * @param target Target Placeable
     * @param originField Given position for origin Placeable
     * @param targetField Given position for target Placeable
     * @return True if all the position are correct
     */
    private boolean checkPositionConsistency(Placeable origin, Placeable target, int[] originField, int[] targetField){
        return (origin.getPosition().getX() == originField[0] &&
                origin.getPosition().getY() == originField[1] &&
                target.getPosition().getX() == targetField[0] &&
                target.getPosition().getY() == targetField[1]);
    }

    /**
     * Checks if the Position of the Placeable matches the Position given in the field array.
     * This needs to be checked several times when working with requests.
     * @author Luka Stoehr
     * @param origin Origin Placeable
     * @param originField Given position for origin Placeable
     * @return True if all the position are correct
     */
    private boolean checkPositionConsistency(Placeable origin, int[] originField){
        return (origin.getPosition().getX() == originField[0] &&
                origin.getPosition().getY() == originField[1]);
    }

    /**
     * Creates a new HandleReturn object with the boolean success and a copy of the current eventList,
     * after adding a GamestateEvent, which is sent with every server message.
     * It also clears the eventList in this class afterwards. This method is called by every
     * handleRequest() method after handling the request is done and therefore also checks
     * if the next turn can be started automatically.
     * @author Luka Stoehr
     * @param success Whether handling the request was successful
     * @return HandleReturn object
     */
    @SuppressWarnings("unchecked")      //This is necessary because of the cast (ArrayList<Message>) eventList.clone(), because clone() returns an Object
    private HandleReturn handleReturn(boolean success){
        //Check if next turn can already be started
        checkForNextTurn();
        //Create GamestateEvent because this is sent with every server message
        this.eventList.add(getGameState());
        ArrayList<Message> eventListCopy = (ArrayList<Message>) eventList.clone();
        HandleReturn hr = new HandleReturn(success, eventListCopy);
        eventList.clear();
        return hr;
    }

    /**
     * This method collects all necessary information for the GamestateEvent and creates
     * the event.
     * @author Luka Stoehr
     * @return GamestateEvent that represents the current state
     */
    public GamestateEvent getGameState(){
        // Count the Placeables on the field
        int placeablesCounter = 0;
        for(Placeable[] row: this.model.field){
            for(Placeable placeable: row){
                if(placeable != null) placeablesCounter++;
            }
        }
        //Collect all entities in an array
        Entities[] entitiesArray = new Entities[placeablesCounter];
        int counter = 0;
        for(Placeable[] row: this.model.field){
            for(Placeable placeable: row){
                if(placeable != null){
                    entitiesArray[counter] = placeable.toEntity();
                    counter++;
                }
            }
        }

        //Get MapSize
        int[] mapSize = {this.model.field.length, this.model.field[0].length};

        //Get turnOrder, but as IDs array
        IDs[] turnOrderArray = new IDs[this.turnOrder.size()];
        for(int i = 0; i < this.turnOrder.size(); i++){
            turnOrderArray[i] = this.turnOrder.get(i).getIDs();
        }

        //Collect all stoneCooldowns
        int[] stoneCooldowns = new int[6];

        stoneCooldowns[0] = model.spaceStone.coolDown - (model.round - model.spaceStone.lastUsedInRound);
        if(stoneCooldowns[0] < 0 || !model.spaceStone.everUsed) stoneCooldowns[0] = 0;
        stoneCooldowns[1] = model.mindStone.coolDown - (model.round - model.mindStone.lastUsedInRound);
        if(stoneCooldowns[1] < 0 || !model.mindStone.everUsed) stoneCooldowns[1] = 0;
        stoneCooldowns[2] = model.realityStone.coolDown - (model.round - model.realityStone.lastUsedInRound);
        if(stoneCooldowns[2] < 0 || !model.realityStone.everUsed) stoneCooldowns[2] = 0;
        stoneCooldowns[3] = model.powerStone.coolDown - (model.round - model.powerStone.lastUsedInRound);
        if(stoneCooldowns[3] < 0 || !model.powerStone.everUsed) stoneCooldowns[3] = 0;
        stoneCooldowns[4] = model.timeStone.coolDown - (model.round - model.timeStone.lastUsedInRound);
        if(stoneCooldowns[4] < 0 || !model.timeStone.everUsed) stoneCooldowns[4] = 0;
        stoneCooldowns[5] = model.soulStone.coolDown - (model.round - model.soulStone.lastUsedInRound);
        if(stoneCooldowns[5] < 0 || !model.soulStone.everUsed) stoneCooldowns[5] = 0;

        //Check if there is a winner already
        boolean winner = (model.getWinner() != null);

        // Create GamestateEvent
        return new GamestateEvent(
                entitiesArray, mapSize, turnOrderArray, this.turnOrder.get(turnCount).getIDs(), stoneCooldowns, winner
        );
    }

    /**
     * This method creates a WinEvent and adds it to the eventList if one of the players has won.
     * @author Luka Stoehr
     */
    public void checkForWinEvent(){
        if(model.getWinner() != null){
            Player winner = model.getWinner();
            int playerWon = winner.equals(model.playerOne) ? 1 : 2;
            eventList.add(
                    new WinEvent(playerWon)
            );
            eventList.add(
                    new DisconnectEvent()
            );
            this.matchEnded = true;
        }
    }

    /**
     *
     */
    public void startTurnTimer(){
        if(turnOrder.get(turnCount) instanceof Hero){
            Hero hero = (Hero) turnOrder.get(turnCount);
            if(hero.PID == 1){
                turnTimer = new SimpleTimer(configuration.matchConfig.maxRoundTime * 1000L) {
                    @Override
                    public void execute() {
                        NetworkHandler.turnOverTimeAction(model.playerOne, model.controller);
                    }
                };
                turnTimer.start();
            }
            else if(hero.PID == 2){
                turnTimer = new SimpleTimer(configuration.matchConfig.maxRoundTime * 1000L) {
                    @Override
                    public void execute() {
                        NetworkHandler.turnOverTimeAction(model.playerOne, model.controller);
                    }
                };
                turnTimer.start();
            }
            else{
                return;
            }
        }
    }

}