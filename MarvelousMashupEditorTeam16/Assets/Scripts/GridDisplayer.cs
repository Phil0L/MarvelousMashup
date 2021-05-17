using System;
using UnityEditor;
using UnityEngine;
using UnityEngine.UI;

public class GridDisplayer : MonoBehaviour
{

    [Header("References:")]
    public GridLayoutGroup gridLayoutGroup;
    public GameObject prefabTile;
    public MapStore mapStore;

    [Header("Values:")] 
    public float cellSpacing;
    public float cellCornerRadius;
    
    void Start()
    {
        mapStore.AddListener(MapChanged);
        MapChanged(mapStore.GetMap());
    }

    public void MapChanged(Map map)
    {
        if (map == null)
        {
            map = mapStore.GetMap();
        }
        
        RectTransform rt = gridLayoutGroup.GetComponent<RectTransform>();
        float gridWidth = rt.rect.width;
        float gridHeight = rt.rect.height;

        int mapHeight = map.height;
        int mapWidth = map.width;

        float cellWidth = gridWidth / mapWidth - cellSpacing;
        float cellHeight = gridHeight / mapHeight - cellSpacing;
        float cellSize = Math.Min(cellWidth, cellHeight);

        gridLayoutGroup.cellSize = new Vector2(cellSize, cellSize);
        gridLayoutGroup.constraintCount = mapWidth;
        gridLayoutGroup.spacing = new Vector2(cellSpacing, cellSpacing);

        foreach (Transform child in gridLayoutGroup.transform)
        {
            Destroy(child.gameObject);
        }

        for (int i = 0; i < mapHeight * mapWidth; i++)
        {
            GameObject child = Instantiate(prefabTile, gridLayoutGroup.transform);
            child.name = "MapTile " + (i / 10) + "|" + (i % 10);
            child.GetComponent<Image>().pixelsPerUnitMultiplier = 1 / cellCornerRadius * 50;
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
            myScript.MapChanged(null);
        }
    }
}
