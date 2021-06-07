using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

public class MapStore : MonoBehaviour, IStore
{
    public enum MapAction
    {
        Initial,
        SizeChange,
        TileChange,
        Load
    }

    private Map _grid = new Map(10, 10);
    private List<Action<Map, MapAction>> _listeners = new List<Action<Map, MapAction>>();

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

    public void SetNewMap(Map newMap, MapAction action)
    {
        _grid = newMap;
        foreach (Action<Map, MapAction> listener in _listeners)
        {
            listener(_grid, action);
        }
    }

    public void AddListener(Action<Map, MapAction> listener)
    {
        _listeners.Add(listener);
        if (_grid != null)
        {
            listener(_grid, MapAction.Initial);
        }
    }
    
    void Start()
    {
        //unnessecary?
        _grid ??= new Map(10, 10);
    }

    public void LoadJson(string json)
    {
        try
        {
            Map loadedMap = JsonConvert.DeserializeObject<Map>(json);
            loadedMap.width = loadedMap.scenario.GetLength(0);
            loadedMap.height = loadedMap.scenario.GetLength(1);
            Debug.Log(loadedMap.ToString());
            SetNewMap(loadedMap, MapAction.Load);
        }
        catch (Exception e)
        {
            Debug.Log("Load canceled due to errors");
            Debug.LogError(e);
            throw;
        }
    }

    public string ToJson()
    {
        return this._grid.ToJson();
    }

    public bool Savable()
    {
        return true;
    }
}
