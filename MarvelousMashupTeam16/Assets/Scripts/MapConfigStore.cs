using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using UnityEngine;

public class MapConfigStore : MonoBehaviour, IConfigStore
{
    
    private static Map _grid = new Map(10, 10);

    public Map GetMap() => _grid;
    public static Map Map() => _grid;


    public static void SetMap(Map newMap)
    {
        _grid = newMap;
        _grid.width = newMap.scenario.GetLength(0);
        _grid.height = newMap.scenario.GetLength(1);
    }
    
    public void LoadJson(string json)
    {
        try
        {
            Map loadedMap = JsonConvert.DeserializeObject<Map>(json);
            loadedMap.width = loadedMap.scenario.GetLength(0);
            loadedMap.height = loadedMap.scenario.GetLength(1);
            SetMap(loadedMap);
        }
        catch (Exception)
        {
            Debug.Log("Load canceled due to errors");
        }
    }
    
    private void Awake()
    {
        if (GetComponent<FileLoader>())
        {
            LoadJson(GetComponent<FileLoader>().GetContent());
        }
    }
}
