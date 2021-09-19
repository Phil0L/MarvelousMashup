using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

[JsonConverter(typeof(StringEnumConverter))]
public enum RequestType
{
    Null,
    MeleeAttackRequest,
    RangedAttackRequest,
    MoveRequest,
    Req,
    ExchangeInfinityStoneRequest,
    UseInfinityStoneRequest,
    EndRoundRequest,
    PauseStartRequest,
    PauseStopRequest,
    DisconnectRequest
}