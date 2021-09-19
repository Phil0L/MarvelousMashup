using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

[JsonConverter(typeof(StringEnumConverter))]
public enum MessageType
{
    NULL,
    HELLO_CLIENT,
    HELLO_SERVER,
    RECONNECT,
    PLAYER_READY,
    GAME_ASSIGNMENT,
    GENERAL_ASSIGNMENT,
    CHARACTER_SELECTION,
    CONFIRM_SELECTION,
    GAME_STRUCTURE,
    GOODBYE_CLIENT,
    ERROR,
    EVENTS,
    REQUESTS     
}