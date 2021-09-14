using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

[JsonConverter(typeof(StringEnumConverter))]
public enum EntityID
{
    NPC,
    P1,
    P2,
    Rocks,
    InfinityStones,
    Portals
}