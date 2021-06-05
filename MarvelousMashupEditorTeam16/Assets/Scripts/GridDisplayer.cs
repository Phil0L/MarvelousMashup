using System;
using System.Collections;
using UnityEditor;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.UI;

public class GridDisplayer : MonoBehaviour
{

    [Header("References:")]
    public GridLayoutGroup gridLayoutGroup;
    public MapTileInfo prefabTile;
    public MapStore mapStore;

    [Header("Values:")] 
    public float cellSpacing;
    public float cellCornerRadius;
    
    void Start()
    {
        mapStore.AddListener(MapChanged);
        MapChanged(mapStore.GetMap(), MapStore.MapAction.Initial);
    }

    public void MapChanged(Map map, MapStore.MapAction action)
    {
        if (map == null)
        {
            map = mapStore.GetMap();
        }

        if (action == MapStore.MapAction.SizeChange || action == MapStore.MapAction.Initial || action == MapStore.MapAction.Load)
        {
            RectTransform rt = gridLayoutGroup.GetComponent<RectTransform>();
            float gridWidth = rt.rect.width;
            float gridHeight = rt.rect.height;

            int mapHeight = map.height;
            int mapWidth = map.width;

            float cellWidth = gridWidth / mapHeight - cellSpacing;
            float cellHeight = gridHeight / mapWidth - cellSpacing;
            float cellSize = Math.Min(cellWidth, cellHeight);

            gridLayoutGroup.cellSize = new Vector2(cellSize, cellSize);
            gridLayoutGroup.constraintCount = mapHeight;
            gridLayoutGroup.spacing = new Vector2(cellSpacing, cellSpacing);

            foreach (Transform child in gridLayoutGroup.transform)
            {
                Destroy(child.gameObject);
            }

            for (int x = 0; x < mapWidth; x++)
            {
                for (int y = 0; y < mapHeight; y++)
                {
                    MapTileInfo child = Instantiate(prefabTile, gridLayoutGroup.transform);
                    child.name = "MapTile " + x + "|" + y;
                    child.position = new Vector2(x, y);
                    child.GetComponent<Image>().pixelsPerUnitMultiplier = 1 / cellCornerRadius * 50;
                    child.SetTile(map.scenario[(int) child.position.x, (int) child.position.y]);
                }
            }
        }

        if (action == MapStore.MapAction.TileChange)
        {
            try
            {
                foreach (Transform tf in gridLayoutGroup.transform)
                {
                    MapTileInfo mti = tf.GetComponent<MapTileInfo>();
                    mti.SetTile(map.scenario[(int) mti.position.x, (int) mti.position.y]);
                }
            }
            catch (Exception e)
            {
                StartCoroutine(FixTileChange());
            }
        }
    }

    IEnumerator FixTileChange()
    {
        yield return 0;
        foreach (Transform tf in gridLayoutGroup.transform)
        {
            MapTileInfo mti = tf.GetComponent<MapTileInfo>();
            mti.SetTile(mapStore.GetMap().scenario[(int) mti.position.x, (int) mti.position.y]);
        }
    }
}

[CustomEditor(typeof(GridDisplayer))]
public class GridDisplayerEditor : Editor
{
    public override void OnInspectorGUI()
    {
        DrawDefaultInspector();

        GridDisplayer myScript = (GridDisplayer) target;
        if(GUILayout.Button("Update Grid"))
        {
            myScript.MapChanged(null, MapStore.MapAction.Initial);
        }
    }
}
