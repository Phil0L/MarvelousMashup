using System;
using System.Text.RegularExpressions;
using Newtonsoft.Json;

[Serializable]
public class Map
{
    [NonSerialized]
    public int width;
    [NonSerialized]
    public int height;
    
    public MapTile[,] tiles;
    public string author;
    public string name = "Testname";
    
    public Map(int width, int height)
    {
        this.tiles = new MapTile[width,height];
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

    public string ToJson()
    {
        string currentMapJson = JsonConvert.SerializeObject(this, Formatting.Indented);
        currentMapJson = Regex.Replace(currentMapJson, @"\s*\n\s*0", " \"GRASS\"");
        currentMapJson = Regex.Replace(currentMapJson, @"\s*\n\s*1", " \"STONE\"");
        // currentMapJson = Regex.Replace(currentMapJson, @"", "");

        return currentMapJson;
    }
}
