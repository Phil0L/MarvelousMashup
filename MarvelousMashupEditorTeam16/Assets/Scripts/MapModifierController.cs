using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class MapModifierController : MonoBehaviour
{

    public MapStore mapStore;
    public ValueController widthController;
    public ValueController heightController;
    public MapToolsManager toolsManager;

    private List<List<MapTile>> _changableMap = new List<List<MapTile>>();

    void Start()
    {
        _changableMap.NewSize(10, 10);
        mapStore.AddListener(StoreUpdate);
    }

    void StoreUpdate(Map map, MapStore.MapAction action)
    {
        if (action == MapStore.MapAction.Load)
        {
            MapSizeChanged(map.width, map.height);
            widthController.SetValue(map.width);
            heightController.SetValue(map.height);
            for (int ii = 0; ii < map.scenario.GetLength(0); ii++)
            {
                for (int ij = 0; ij < map.scenario.GetLength(1); ij++)
                {
                    _changableMap[ii][ij] = map.scenario[ii, ij];
                }
            }
            mapStore.SetNewMap(_changableMap.ToMap(), MapStore.MapAction.TileChange);
        }
    }

    void Update()
    {
        int newWidth = widthController.GetValue();
        int newHeight = heightController.GetValue();
        int oldWidth = mapStore.GetMap().width;
        int oldHeight = mapStore.GetMap().height;

        if (newHeight != oldHeight || newWidth != oldWidth)
        {
            MapSizeChanged(newWidth, newHeight);
        }
    }

    public void MapSizeChanged(int w, int h)
    {
        Debug.Log($"Map size changed! size:({w},{h})");
        _changableMap.NewSize(w, h);
        //Debug.Log(_changableMap.ToMap().ToJson());
        mapStore.SetNewMap(_changableMap.ToMap(), MapStore.MapAction.SizeChange);
    }
    
    public void TileClicked(int x, int y)
    {
        Debug.Log($"Tile clicked! position:({x},{y})");
        switch (toolsManager.selectedTool)
        {
            case MapToolsManager.Tool.Switch:
                if (_changableMap[x][y] == MapTile.ROCK)
                    _changableMap[x][y] = MapTile.GRASS;
                else
                    _changableMap[x][y] = MapTile.ROCK;
                break;
            case MapToolsManager.Tool.Grass:
                _changableMap[x][y] = MapTile.GRASS;
                break;
            case MapToolsManager.Tool.Stone:
                _changableMap[x][y] = MapTile.ROCK;
                break;
            case MapToolsManager.Tool.Portal:
                _changableMap[x][y] = MapTile.PORTAL;
                break;

        }
        mapStore.SetNewMap(_changableMap.ToMap(), MapStore.MapAction.TileChange);
    }
}

static class MapSizeChanger
{
    public static void NewSize(this List<List<MapTile>> map, int newWidth, int newHeight)
    {
        foreach (List<MapTile> row in map)
        {
            row.Resize(newHeight, MapTile.GRASS);
        }   
        map.ResizeWithList(newWidth, Enumerable.Repeat(MapTile.GRASS, newHeight).ToList());
    }
    
    private static void Resize<T>(this List<T> list, int sz, T c)
    {
        int cur = list.Count;
        if(sz < cur)
            list.RemoveRange(sz, cur - sz);
        else if(sz > cur)
        {
            if(sz > list.Capacity)  //this bit is purely an optimisation, to avoid multiple automatic capacity changes.
                list.Capacity = sz;
            list.AddRange(Enumerable.Repeat(c, sz - cur));
        }
    }
    
    private static void ResizeWithList(this List<List<MapTile>> list, int sz, List<MapTile> c)
    {
        int cur = list.Count;
        if(sz < cur)
            list.RemoveRange(sz, cur - sz);
        else if(sz > cur)
        {
            if(sz > list.Capacity)  //this bit is purely an optimisation, to avoid multiple automatic capacity changes.
                list.Capacity = sz;
            for (int i = 0; i < sz - cur; i++)
            {
                List<MapTile> cc = c.Clone() as List<MapTile>;
                list.Add(cc);
            }
        }
    }
    
    public static IList<T> Clone<T>(this IList<T> listToClone)
    {
        return listToClone.Select(item => item).ToList();
    }
    
}

static class MapBuilder
{
    public static Map ToMap(this List<List<MapTile>> map)
    {
        return new Map(map.Select(Enumerable.ToArray).ToArray());
    }
}


