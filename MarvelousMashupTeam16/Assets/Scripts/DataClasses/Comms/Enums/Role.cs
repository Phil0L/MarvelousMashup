using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

/**
 *
 * lists all roles, which are defined by the network standard document
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 */
[JsonConverter(typeof(StringEnumConverter))]
public enum Role {
    PLAYER,
    KI,
    SPECTATOR
}
