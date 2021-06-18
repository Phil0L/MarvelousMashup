using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using UnityEngine;

public class MapConfigStore : MonoBehaviour, IConfigStore
{
    
    private Map _grid = new Map(10, 10);

    public Map GetMap()
    {
        return _grid;
    }

    public MapTile GetTile(int x, int y)
    {
        return _grid.scenario[x, y];
    }

    public void SetName(string name)
    {
        _grid.name = name;
    }

    public void SetAuthor(string author)
    {
        _grid.author = author;
    }

    public void SetNewMap(Map newMap)
    {
        _grid = newMap;
        
    }
    
    public void LoadJson(string json)
    {
        try
        {
            Map loadedMap = JsonConvert.DeserializeObject<Map>(json);
            loadedMap.width = loadedMap.scenario.GetLength(0);
            loadedMap.height = loadedMap.scenario.GetLength(1);
            SetNewMap(loadedMap);
        }
        catch (Exception)
        {
            Debug.Log("Load canceled due to errors");
        }
    }
    
}
