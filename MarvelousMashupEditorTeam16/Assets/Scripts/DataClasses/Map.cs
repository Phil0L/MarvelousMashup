using System;
using System.Text.RegularExpressions;
using Newtonsoft.Json;

[Serializable]
public class Map
{
    [NonSerialized] public int width;
    [NonSerialized] public int height;

    public MapTile[,] tiles;
    public string author;
    public string name = "Testname";

    public Map(int width, int height)
    {
        this.tiles = new MapTile[width, height];
        this.width = width;
        this.height = height;
        for (int ii = 0; ii < tiles.GetLength(0); ii++)
        {
            for (int ij = 0; ij < tiles.GetLength(1); ij++)
            {
                tiles[ii, ij] = MapTile.GRASS;
            }
        }
    }

    public Map(MapTile[,] tiles)
    {
        this.tiles = tiles;
        this.width = tiles.GetLength(0);
        this.height = tiles.GetLength(1);
    }

    public Map(MapTile[][] tiles)
    {
        MapTile[,] aTiles = new MapTile[tiles.Length, tiles[0].Length];
        for (int ii = 0; ii < tiles.Length; ii++)
        {
            for (int ij = 0; ij < tiles[0].Length; ij++)
            {
                aTiles[ii, ij] = tiles[ii][ij];
            }
        }
        this.tiles = aTiles;
        this.width = tiles.Length;
        this.height = tiles[0].Length;
    }

    public string ToJson()
    {
        string currentMapJson = JsonConvert.SerializeObject(this, Formatting.Indented);
        currentMapJson = Regex.Replace(currentMapJson, @"\s*\n\s*1", " \"GRASS\"");
        currentMapJson = Regex.Replace(currentMapJson, @"\s*\n\s*2", " \"STONE\"");
        currentMapJson = Regex.Replace(currentMapJson, @"[^]]\n\s*]", "]");
        currentMapJson = Regex.Replace(currentMapJson, "]]", "]\n  ]");
        currentMapJson = Regex.Replace(currentMapJson, "\"author\": null,\\n\\s*", "]");

        return currentMapJson;
    }
}