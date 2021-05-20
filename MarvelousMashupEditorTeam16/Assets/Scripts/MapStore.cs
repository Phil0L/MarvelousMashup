using System;
using System.Collections.Generic;
using UnityEngine;

public class MapStore : MonoBehaviour
{
    public enum MapAction
    {
        Initial,
        SizeChange,
        TileChange
    }

    private Map _grid = new Map(10, 10);
    private List<Action<Map, MapAction>> _listeners = new List<Action<Map, MapAction>>();

    public Map GetMap()
    {
        return _grid;
    }

    public MapTile GetTile(int x, int y)
    {
        return _grid.tiles[x, y];
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
}
