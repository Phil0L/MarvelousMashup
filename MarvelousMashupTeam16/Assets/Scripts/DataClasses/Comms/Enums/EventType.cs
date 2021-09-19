using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

[JsonConverter(typeof(StringEnumConverter))]
public enum EventType
{
    Null,
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
    PauseStopEvent,
    TeleportedEvent

}