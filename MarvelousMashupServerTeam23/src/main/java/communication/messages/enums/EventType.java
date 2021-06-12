package communication.messages.enums;

/**
 *
 * @author Sarah Engele
 *
 * lists all EventTypes, which are defined by the network standard document
 *
 */

public enum EventType {
    Ack,
    Nack,
    GamestateEvent,
    CustomEvent,
    ConsumedAPEvent,
    ConsumedMPEvent,
    DestroyedEntityEvent,
    HealedEvent,
    TakenDamageEvent,
    SpawnEntityEvent,
    MeleeAttackEvent,
    RangedAttackEvent,
    MoveEvent,
    UseInfinityStoneEvent,
    ExchangeInfinityStoneEvent,
    TimeoutEvent,
    TimeoutWarningEvent,
    WinEvent,
    RoundSetupEvent,
    TurnEvent,
    TurnTimeoutEvent,
    DisconnectEvent,
    PauseStartEvent,
    PauseStopEvent
}
