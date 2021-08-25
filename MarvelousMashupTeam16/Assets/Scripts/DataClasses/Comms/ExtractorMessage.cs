
/**
 *
 * The ExtractorMessage class is used to extract all objects, which are subclasses of Message in case they are stored
 * in an MessageArray (for Example in a MessageStructure object). This class solves the problems that the Message class
 * is to general (information from subclasses may get lost).
 *
 * (This class only exists for gson/json parsing)
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class ExtractorMessage {


    /**
     * Type of an Event, is used to assign a Message object to a Message type. Used for all Message object subclasses
     * with type Event.
     */
    public EventType eventType;

    /**
     * typ of an Request, is used to assign a Message object to a Message type. Used for all Message object subclasses
     * with type Request.
     */
    public RequestType requestType;


    /**
     * ID of the acting Entity. Used for the MeleeAttackRequest, RangedAttackRequest,ExchangeInfinityStoneRequest
     * UseInfinityStoneRequest, ExchangeInfinityStoneEvent, UseInfinityStoneEvent, MeleeAttackEvent, MoveEvent.
     */
    public IDs originEntity;

    /**
     * ID of the target Entity. Used for the MeleeAttackRequest, RangeAttackRequest,ExchangeInfinityStoneRequest,
     * ExchangeInfinityStoneEvent, UseInfinityStoneEvent, MeleeAttackEvent, TakenDamageEvent, ConsumedMPEvent,
     * HealedEvent, DestroyedEntityEvent, ConsumedAPEvent.
     */
    public IDs targetEntity;

    /**
     * Position of the acting Entity. In case of a MoveRequest start position of the move.
     *
     * Used for the MeleeAttackRequest, RangeAttackRequest, MoveRequest,ExchangeInfinityStoneRequest,
     * UseInfinityStoneRequest, ExchangeInfinityStoneEvent, UseInfinityStoneEvent, MeleeAttackEvent, MoveEvent.
     */
    public int[] originField;

    /**
     * Position of the target Entity. In case of a MoveRequest target position of the move.
     * Used for the MeleeAttackRequest, RangeAttackRequest, MoveRequest,ExchangeInfinityStoneRequest,
     * UseInfinityStoneRequest, ExchangeInfinityStoneEvent, UseInfinityStoneEvent, MeleeAttackEvent, TakenDamageEvent,
     * ConsumedMPEvent, HealedEvent, MoveEvent, DestroyedEntityEvent, ConsumedAPEvent.
     */
    public int[] targetField;

    /**
     * value field of a request message. Used for the MeleeAttackRequest, RangeAttackRequest.
     */
    public int value;

    /**
     * IDs of the InfinityStone. Used for the ExchangeInfinityStoneRequest, UseInfinityStoneRequest,
     * ExchangeInfinityStoneEvent, UseInfinityStoneEvent.
     */
    public IDs stoneType;


    /**
     * Contains all Entities on the GameField. Used for the GameStateEvent
     */
    public ExtractorEntities[] entities;

    /**
     * describes the size of the map. Used for the GameStateEvent
     */
    public int[] mapSize;

    /**
     * contains the turn order in form of a array. Used for the GameStateEvent.
     */
    public IDs[] turnOrder;

    /**
     * the ID of the activeCharacter. Used for the GameStateEvent.
     */
    public IDs activeCharacter;


    /**
     * The stoneCooldown array has 6 entries (Space, Mind, Reality, Power, Time, Soul). It contains the cooldown of all
     * infinityStones. Used for the GameStateEvent.
     */
    public int[] stoneCooldowns;

    /**
     * Shows whether a win condition is fulfilled. Used in the GameStateEvent
     */
    public bool winCondition;

    /**
     * damage amount of a attack. Used in RangedAttackEvent, MeleeAttackEvent, TakenDamageEvent, ConsumedMPEvent,
     * HealedEvent, ConsumedAPEvent.
     */
    public int amount;

    /**
     * additive String message for timeout messages. Used in TimeoutWarningEvent, TimeoutEvent
     */
    public string message;

    /**
     * remaining time. Used in TimeoutWarningEvent.
     */
    public int timeLeft;

    /**
     * actual game round. Used in the RoundSetupEvent
     */
    public int roundCount;

    /**
     * turn oder of the characters. Used in the RoundSetupEvent
     */
    public IDs[] characterOrder;

    /**
     * current number of moves. Used in TurnEvent.
     */
    public int turnCount;

    /**
     * The id of the character whose turn it is. Used in TurnEvent.
     */
    public IDs nextCharacter;

    /**
     *  New spawn Entity. Used in the SpawnEntityEvent
     */
    public ExtractorEntities entity;

    /**
     * contains the number of the player who waon the game. Used in WinEvent.
     */
    public int playerWon;

    /**
     *
     * creates a Message object by using the attributes of the ExtractorMessage object. The exact class of the object
     * is determined by the eventType or the requestType of the object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a Message object created by the attributes of the ExtractorMessage object.
     */
    public Message toMessage()
    {

        if(this.eventType != EventType.Null){
            // Message belong to the Events
            switch (this.eventType) {
                case EventType.GamestateEvent:
                    return this.toGamestateEvent();
                case EventType.ExchangeInfinityStoneEvent:
                    return this.toExchangeInfinityStoneEvent();
                case EventType.UseInfinityStoneEvent:
                    return this.toUseInfinityStoneEvent();
                case EventType.RangedAttackEvent:
                    return this.toRangedAttackEvent();
                case EventType.MeleeAttackEvent:
                    return this.toMeleeAttackEvent();
                case EventType.TakenDamageEvent:
                    return this.toTakenDamageEvent();
                case EventType.ConsumedMPEvent:
                    return this.toConsumedMPEvent();
                case EventType.HealedEvent:
                    return this.toHealedEvent();
                case EventType.MoveEvent:
                    return this.toMoveEvent();
                case EventType.TimeoutWarningEvent:
                    return this.toTimeoutWarningEvent();
                case EventType.TimeoutEvent:
                    return this.toTimeoutEvent();
                case EventType.DestroyedEntityEvent:
                    return this.toDestroyedEntityEvent();
                case EventType.RoundSetupEvent:
                    return this.toRoundSetupEvent();
                case EventType.TurnEvent:
                    return this.toTurnEvent();
                case EventType.Ack:
                    return this.toAck();
                case EventType.Nack:
                    return this.toNack();
                case EventType.ConsumedAPEvent:
                    return this.toConsumedAPEvent();
                case EventType.PauseStopEvent:
                    return this.toPauseStopEvent();
                case EventType.DisconnectEvent:
                    return this.toDisconnectEvent();
                case EventType.PauseStartEvent:
                    return this.toPauseStartEvent();
                case EventType.TurnTimeoutEvent:
                    return this.toTurnTimeoutEvent();
                case EventType.SpawnEntityEvent:
                    return this.toSpawnEntityEvent();
                case EventType.WinEvent:
                    return this.toWinEvent();
                default:
                    return null;
            }
        }
        if(this.requestType != RequestType.Null){
            // Message belongs to the Requests
            switch (this.requestType) {
                case RequestType.MeleeAttackRequest:
                    return this.toMeleeAttackRequest();
                case RequestType.RangedAttackRequest:
                    return this.toRangeAttackRequest();
                case RequestType.MoveRequest:
                    return this.toMoveRequest();
                case RequestType.ExchangeInfinityStoneRequest:
                    return this.toExchangeInfinityStoneRequest();
                case RequestType.UseInfinityStoneRequest:
                    return this.toUseInfinityStoneRequest();
                case RequestType.DisconnectRequest:
                    return this.toDisconnectRequest();
                case RequestType.EndRoundRequest:
                    return this.toEndRoundRequest();
                case RequestType.PauseStartRequest:
                    return this.toPauseStartRequest();
                case RequestType.PauseStopRequest:
                    return this.toPauseStopRequest();
                case RequestType.Req:
                    return this.toReq();
                default:
                    return null;
            }
        }else{
            return null;
        }

    }

    /**
     * Creates a ConsumedMPEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a ConsumedMPEvent created by using the attributes of the ExtractorMessage class
     */
    public ConsumedAPEvent toConsumedAPEvent() {
        return new ConsumedAPEvent(this.targetEntity,this.targetField,this.amount);
    }


    /**
     * Creates a MeleeAttackRequest object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a MeleeAttackRequest created by using the attributes of the ExtractorMessage class
     */
    public MeleeAttackRequest toMeleeAttackRequest(){
        return new MeleeAttackRequest(this.originEntity,this.targetEntity,this.originField,this.targetField,this.value);
    }

    /**
     * Creates a RangedAttackRequest object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a RangedAttackRequest created by using the attributes of the ExtractorMessage class
     */
    public RangedAttackRequest toRangeAttackRequest(){
        return new RangedAttackRequest(this.originEntity,this.targetEntity,this.originField,this.targetField,this.value);
    }

    /**
     * Creates a MoveRequest object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a MoveRequest created by using the attributes of the ExtractorMessage class
     */
    public MoveRequest toMoveRequest(){
        return new MoveRequest(this.originEntity,this.originField,this.targetField);
    }

    /**
     * Creates a ExchangeInfinityStoneRequest object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a ExchangeInfinityStoneRequest created by using the attributes of the ExtractorMessage class
     */
    public ExchangeInfinityStoneRequest toExchangeInfinityStoneRequest(){
        return new ExchangeInfinityStoneRequest(this.originEntity,this.targetEntity,this.originField,this.targetField,
                this.stoneType);
    }

    /**
     * Creates a UseInfinityStoneRequest object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a UseInfinityStoneRequest created by using the attributes of the ExtractorMessage class
     */
    public UseInfinityStoneRequest toUseInfinityStoneRequest() {
        return new UseInfinityStoneRequest(this.originEntity, this.originField, this.targetField,
                this.stoneType);
    }

    /**
     * Creates a DisconnectRequest object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a DisconnectRequest created by using the attributes of the ExtractorMessage class
     */
    public DisconnectRequest toDisconnectRequest() {
        return new DisconnectRequest();
    }

    /**
     * Creates a PauseStartRequest object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a PauseStartRequest created by using the attributes of the ExtractorMessage class
     */
    public PauseStartRequest toPauseStartRequest() {
        return new PauseStartRequest();
    }
    /**
     * Creates a PauseStopRequest object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a PauseStopRequest created by using the attributes of the ExtractorMessage class
     */
    public PauseStopRequest toPauseStopRequest() {
        return new PauseStopRequest();
    }

    /**
     * Creates a Req object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a Req created by using the attributes of the ExtractorMessage class
     */
    public Req toReq() {
        return new Req();
    }

    /**
     * Creates a EndRoundRequest object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a EndRoundRequest created by using the attributes of the ExtractorMessage class
     */
    public EndRoundRequest toEndRoundRequest() {
        return new EndRoundRequest();
    }

    /**
     * Creates a GamestateEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a GamestateEvent created by using the attributes of the ExtractorMessage class
     */
    public GamestateEvent toGamestateEvent(){
        int nullCount = 0;
        for (int i = 0; i < this.entities.Length; i++) {
            if(this.entities[i] == null){
                nullCount++;
            }
        }
        // cast all Entities (by using the toEntities methode)
        Entities[] entites = new Entities[this.entities.Length - nullCount];
        nullCount = 0;
        for (int i = 0; i < entites.Length ; i++) {
            if (this.entities[i] != null) {
                entites[i - nullCount] = this.entities[i].toEntities();
            }else {
                nullCount++;
            }
        }
        return new GamestateEvent(entites,this.mapSize,this.turnOrder,this.activeCharacter,
                this.stoneCooldowns,this.winCondition);
    }

    /**
     * Creates a ExchangeInfinityStoneEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a ExchangeInfinityStoneEvent created by using the attributes of the ExtractorMessage class
     */
    public ExchangeInfinityStoneEvent toExchangeInfinityStoneEvent() {
        return new ExchangeInfinityStoneEvent(this.originEntity,this.targetEntity,
                this.originField,this.targetField,this.stoneType);
    }


    /**
     * Creates a UseInfinityStoneEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a UseInfinityStoneEvent created by using the attributes of the ExtractorMessage class
     */
    public UseInfinityStoneEvent toUseInfinityStoneEvent() {
        return new UseInfinityStoneEvent(this.originEntity,this.targetEntity,
                this.originField,this.targetField,this.stoneType);
    }

    /**
     * Creates a MeleeAttackEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a MeleeAttackEvent created by using the attributes of the ExtractorMessage class
     */
    public MeleeAttackEvent toMeleeAttackEvent() {
        return new MeleeAttackEvent(this.originEntity,this.targetEntity,
                this.originField,this.targetField,this.amount);
    }

    /**
     * Creates a RangedAttackEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a RangedAttackEvent created by using the attributes of the ExtractorMessage class
     */
    public RangedAttackEvent toRangedAttackEvent() {
        return new RangedAttackEvent(this.originEntity,this.targetEntity,
                this.originField,this.targetField,this.amount);
    }

    /**
     * Creates a TakenDamageEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a TakenDamageEvent created by using the attributes of the ExtractorMessage class
     */
    public TakenDamageEvent toTakenDamageEvent() {
        return new TakenDamageEvent(this.targetEntity,
                this.targetField,this.amount);
    }

    /**
     * Creates a ConsumedMPEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a ConsumedMPEvent created by using the attributes of the ExtractorMessage class
     */
    public ConsumedMPEvent toConsumedMPEvent() {
        return new ConsumedMPEvent(this.targetEntity,
                this.targetField,this.amount);
    }





    /**
     * Creates a HealedEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a HealedEvent created by using the attributes of the ExtractorMessage class
     */
    public HealedEvent toHealedEvent() {
        return new HealedEvent(this.targetEntity,
                this.targetField,this.amount);
    }


    /**
     * Creates a MoveEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a MoveEvent created by using the attributes of the ExtractorMessage class
     */
    public MoveEvent toMoveEvent() {
        return new MoveEvent(this.originEntity,this.originField,
                this.targetField);
    }

    /**
     * Creates a TimeoutWarningEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a TimeoutWarningEvent created by using the attributes of the ExtractorMessage class
     */
    public TimeoutWarningEvent toTimeoutWarningEvent() {
        return new TimeoutWarningEvent(this.message,this.timeLeft);
    }

    /**
     * Creates a TimeoutEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a TimeoutEvent created by using the attributes of the ExtractorMessage class
     */
    public TimeoutEvent toTimeoutEvent() {
        return new TimeoutEvent(this.message);
    }


    /**
     * Creates a DestroyedEntityEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a DestroyedEntityEvent created by using the attributes of the ExtractorMessage class
     */
    public DestroyedEntityEvent toDestroyedEntityEvent() {
        return new DestroyedEntityEvent(this.targetField,this.targetEntity);
    }

    /**
     * Creates a RoundSetupEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a RoundSetupEvent created by using the attributes of the ExtractorMessage class
     */
    public RoundSetupEvent toRoundSetupEvent() {
        return new RoundSetupEvent(this.roundCount,this.characterOrder);
    }

    /**
     * Creates a TurnEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a TurnEvent created by using the attributes of the ExtractorMessage class
     */
    public TurnEvent toTurnEvent() {
        return new TurnEvent(this.turnCount,this.nextCharacter);
    }

    /**
     * Creates a SpawnEntityEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a SpawnEntityEvent created by using the attributes of the ExtractorMessage class
     */
    public SpawnEntityEvent toSpawnEntityEvent(){
        return new SpawnEntityEvent(entity.toEntities());
    }


    /**
     * Creates a WinEvent object by using the attributes of the ExtractorMessage class.
     *
     * @author Matthias Ruf
     * @return a WinEvent created by using the attributes of the ExtractorMessage class
     */
    public WinEvent toWinEvent(){
        return new WinEvent(this.playerWon);
    }

    /**
     * Creates a TurnTimeoutEvent object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a TurnTimeoutEvent created by using the attributes of the ExtractorMessage class
     */
    public TurnTimeoutEvent toTurnTimeoutEvent(){
        return new TurnTimeoutEvent();
    }

    /**
     * Creates a Ack object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a Ack created by using the attributes of the ExtractorMessage class
     */
    public Ack toAck(){
        return new Ack();
    }


    /**
     * Creates a Nack object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a Nack created by using the attributes of the ExtractorMessage class
     */
    public Nack toNack(){
        return new Nack();
    }

    /**
     * Creates a DisconnectEvent object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a DisconnectEvent created by using the attributes of the ExtractorMessage class
     */
    public DisconnectEvent toDisconnectEvent(){
        return new DisconnectEvent();
    }

    /**
     * Creates a PauseStartEvent object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a PauseStartEvent created by using the attributes of the ExtractorMessage class
     */
    public PauseStartEvent toPauseStartEvent(){
        return new PauseStartEvent();
    }


    /**
     * Creates a PauseStopEvent object. This Methode only calls the constructor and doesn't need attributes from the
     * ExtractorMessage class. The only reason this methode exists is the reason "completeness".
     *
     * @author Matthias Ruf
     * @return a PauseStopEvent created by using the attributes of the ExtractorMessage class
     */
    public PauseStopEvent toPauseStopEvent(){
        return new PauseStopEvent();
    }

    /**
     * constructor of the Message-class if the Message is an event
     *
     * @author Sarah Engele
     *
     * @param eventType the type of the event
     */
    public ExtractorMessage(EventType eventType){
        this.eventType = eventType;
    }

    /**
     * constructor of the Message-class if the Message is a request
     *
     * @author Sarah Engele
     *
     * @param requestType the type of the request
     */
    public ExtractorMessage(RequestType requestType){
        this.requestType = requestType;
    }



}
