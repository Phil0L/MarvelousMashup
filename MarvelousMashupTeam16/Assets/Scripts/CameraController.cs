using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Tilemaps;
using UnityEngine.UI;
using UnityEngine.UIElements;

public class CameraController : MonoBehaviour
{

    public Tilemap tilemap;
    public bool debugMode;

    public float cameraZ = 100;
    public float distanceToEdge = 5;
    public float dragSpeed = 2;
    private Vector3 oldPos;
    private Vector3 panOrigin;
    private bool bDragging;
    public float minFov = 15f;
    public float maxFov = 90f;
    public float sensitivity = 10f;

    private void Start()
    {
        Game.Controller().ButtonInfoManager.Add("Center Map", "C");
        Game.Controller().ButtonInfoManager.Add("Overview", "F");
        Game.Controller().ButtonInfoManager.Add("Move Map", ButtonInfoManager.IconButton.RightClick);
    }

    public void FocusCamera()
    {
        FocusCamera(cameraZ);
    }

    public void FocusCameraFullView()
    {
        float z = cameraZ;
        var d = GetMapDimensionCanvas();
        int maxcount = 50;
        int count = 0;
        
        while (
            d.x > distanceToEdge &&
            d.y > distanceToEdge)
        {
            z--;
            FocusCamera(z);
            d = GetMapDimensionCanvas();
            count++;
            if (count == maxcount)
                break;
        }

        count = 0;
        while (
            d.x < distanceToEdge ||
            d.y < distanceToEdge)
        {
            z++;
            FocusCamera(z);
            d = GetMapDimensionCanvas();
            count++;
            if (count == maxcount)
                break;
        }
    }

    private void FocusCamera(float z)
    {
       var dims = GetMapDimension();
       Vector3 pos = new Vector3(dims.x + (dims.z - dims.x) / 2, dims.y + (dims.w - dims.y) / 2, Camera.main.transform.position.z);
       Camera.main.orthographicSize = z;
       Camera.main.transform.position = pos;
    }
    
    private void Update()
    {
        if (Input.GetKey(KeyCode.C))
            FocusCamera(Camera.main.orthographicSize);
        if (Input.GetKey(KeyCode.F))
            FocusCameraFullView();
        
        if(Input.GetMouseButtonDown(1))
        {
            bDragging = true;
            oldPos = transform.position;
            panOrigin = Camera.main.ScreenToViewportPoint(Input.mousePosition);                     
        }
        if(Input.GetMouseButton(1))
        {
            Vector3 pos = Camera.main.ScreenToViewportPoint(Input.mousePosition) - panOrigin;       
            transform.position = oldPos + -pos * dragSpeed;                                         
        }
        if(Input.GetMouseButtonUp(1))
        {
            bDragging = false;
        }
        
        float fov = Camera.main.orthographicSize;
        fov -= Input.GetAxis("Mouse ScrollWheel") * sensitivity;
        fov = Mathf.Clamp(fov, minFov, maxFov);
        //Camera.main.transform.position = new Vector3(Camera.main.transform.position.x, Camera.main.transform.position.y, fov);
        Camera.main.orthographicSize = fov;
    }
    
    // Update is called once per frame
    void OnDrawGizmos()
    {
        if (debugMode)
        {
            var dims = GetMapDimension();
            Gizmos.DrawLine(new Vector2(dims.x, dims.y), new Vector2(dims.z, dims.y));
            Gizmos.DrawLine(new Vector2(dims.z, dims.y), new Vector2(dims.z, dims.w));
            Gizmos.DrawLine(new Vector2(dims.z, dims.w), new Vector2(dims.x, dims.w));
            Gizmos.DrawLine(new Vector2(dims.x, dims.w), new Vector2(dims.x, dims.y));
            
            Gizmos.color = Color.red;
            Vector3Int coordinate = new Vector3Int(0, 0, 0);
            for (int i = 0; i < tilemap.size.x; i++) {             
                for (int j = 0; j < tilemap.size.y; j++) {                 
                    coordinate.x = i; coordinate.y = j;                 
                    var tilePosition = tilemap.GetCellCenterWorld(coordinate);
                    if (tilemap.GetTile(coordinate))
                        Gizmos.DrawSphere(tilePosition, 0.05f);
                }         
            }
        }
    }

    // x = left
    // y = top
    // z = right
    // w = bottom
    private Vector4 GetMapDimension()
    {
        Vector3Int coordinate = new Vector3Int(0, 0, 0);
        Vector4 dim = new Vector4(
            Single.PositiveInfinity,
            Single.PositiveInfinity,
            Single.NegativeInfinity,
            Single.NegativeInfinity);
        
        for (int i = 0; i < tilemap.size.x; i++) {             
            for (int j = 0; j < tilemap.size.y; j++) {                 
                coordinate.x = i; coordinate.y = j;                 
                var tilePosition = tilemap.GetCellCenterWorld(coordinate);
                if (tilemap.GetTile(coordinate))
                {
                    if (tilePosition.x < dim.x)
                        dim.x = tilePosition.x;
                    if (tilePosition.y < dim.y)
                        dim.y = tilePosition.y;
                    if (tilePosition.x > dim.z)
                        dim.z = tilePosition.x;
                    if (tilePosition.y > dim.w)
                        dim.w = tilePosition.y;
                }
            }         
        }
        
        return dim;
    }
    
    private Vector4 GetMapDimensionCanvas()
    {
        Vector3Int coordinate = new Vector3Int(0, 0, 0);
        Vector4 dim = new Vector4(
            Single.PositiveInfinity,
            Single.PositiveInfinity,
            Single.NegativeInfinity,
            Single.NegativeInfinity);
        
        for (int i = 0; i < tilemap.size.x; i++) {             
            for (int j = 0; j < tilemap.size.y; j++) {                 
                coordinate.x = i; coordinate.y = j;                 
                var tilePosition = tilemap.CellToWorld(coordinate);
                tilePosition = Camera.main.WorldToScreenPoint(tilePosition);
                if (tilemap.GetTile(coordinate))
                {
                    if (tilePosition.x < dim.x)
                        dim.x = tilePosition.x;
                    if (tilePosition.y < dim.y)
                        dim.y = tilePosition.y;
                    if (tilePosition.x > dim.z)
                        dim.z = tilePosition.x;
                    if (tilePosition.y > dim.w)
                        dim.w = tilePosition.y;
                }
            }         
        }
        
        return dim;
    }
}
