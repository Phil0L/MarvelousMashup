using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameState
{
    public GameState()
    {
        Map map = MapConfigStore.Map();
        arr = new GameField[map.width, map.height];
        for (int x = 0; x < map.width; x++)
        {
            for (int y = 0; y < map.height; y++)
            {
                var field = new GameField();
                field.tile = map.scenario[x, y];
                field.tileData = 100;
                this[x, y] = field;
            }
        }
    }
    
    private GameField[,] arr;
    
    public GameField this[int i, int j]
    {
        get { return arr[i,j]; }
        set { arr[i,j] = value; }
    }

    public int Height() => arr.GetLength(1);
    
    public int Width() => arr.GetLength(0);
}
