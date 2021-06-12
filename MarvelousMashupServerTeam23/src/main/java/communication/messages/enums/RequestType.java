package communication.messages.enums;

/**
 *
 * @author Sarah Engele
 *
 * lists all RequestTypes, which are defined by the network standard document
 *
 */

public enum RequestType {
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
