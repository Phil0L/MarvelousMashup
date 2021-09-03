using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

[JsonConverter(typeof(StringEnumConverter))]
public enum EntityType
{
    InfinityStone,
    NPC,
    Rock,
    Character,
    Portal
}