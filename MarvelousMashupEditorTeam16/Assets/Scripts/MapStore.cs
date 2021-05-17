using System;
using System.Collections.Generic;
using UnityEngine;

public class MapStore : MonoBehaviour
{
    private Map _grid = new Map(10, 10);
    private List<Action<Map>> _listeners = new List<Action<Map>>();

    public Map GetMap()
    {
        return _grid;
    }

    public void SetNewMap(Map newMap)
    {
        _grid = newMap;
        foreach (var listener in _listeners)
        {
            listener(_grid);
        }
    }

    public void AddListener(Action<Map> listener)
    {
        _listeners.Add(listener);
        if (_grid != null)
        {
            listener(_grid);
        }
    }
    
    void Start()
    {
        //unnessecary?
        _grid ??= new Map(10, 10);
    }
}
