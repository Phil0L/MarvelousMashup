using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

[Serializable]
[JsonConverter(typeof(StringEnumConverter))]
public enum MapTile
{
    UNDEFINED,
    GRASS,
    ROCK,
    PORTAL
    
}