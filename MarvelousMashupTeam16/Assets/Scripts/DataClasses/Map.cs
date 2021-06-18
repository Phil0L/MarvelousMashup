using System;
using System.Linq;
using System.Text.RegularExpressions;
using Newtonsoft.Json;

[Serializable]
public class Map
{
    [NonSerialized] public int width;
    [NonSerialized] public int height;

    public MapTile[,] scenario;
    public string author;
    public string name = "My Map";

    [JsonConstructor]
    public Map()
    {
        
    }

    public Map(int width, int height)
    {
        this.scenario = new MapTile[width, height];
        this.width = width;
        this.height = height;
        for (int ii = 0; ii < scenario.GetLength(0); ii++)
        {
            for (int ij = 0; ij < scenario.GetLength(1); ij++)
            {
                scenario[ii, ij] = MapTile.GRASS;
            }
        }
    }

    public Map(MapTile[,] scenario)
    {
        this.scenario = scenario;
        this.width = scenario.GetLength(0);
        this.height = scenario.GetLength(1);
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

        this.scenario = aTiles;
        this.width = tiles.Length;
        this.height = tiles[0].Length;
    }
    
    public override string ToString()
    {
        string s = width + ", " + height + Environment.NewLine;
        for (int ii = 0; ii < scenario.GetLength(0); ii++)
        {
            for (int ij = 0; ij < scenario.GetLength(1); ij++)
            {
                s += scenario[ii, ij].ToString() + " ";
            }
            s += Environment.NewLine;
        }
        s += name + Environment.NewLine;
        s += author + Environment.NewLine;
        return s;
    }
}