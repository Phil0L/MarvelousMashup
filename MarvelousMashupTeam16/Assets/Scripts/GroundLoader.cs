using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Tilemaps;

public class GroundLoader : MonoBehaviour
{
    public Tilemap tilemap;
    public TileBase rockBase;
    public TileBase grassBase;
    public TileBase portalBase;
    
    public void LoadMap()
    {
        LoadMap(Game.State());
    }
    
    public void LoadMap(GameState state)
    {
        tilemap.ClearAllTiles();
        for (int x = 0; x < state.Width(); x++)
        {
            for (int y = 0; y < state.Height(); y++)
            {
                var pos = new Vector3Int(x, y, 0);
                switch (state[x, y].tile)
                {
                    case MapTile.ROCK:
                        tilemap.SetTile(pos, rockBase);
                        Matrix4x4 matrix = Matrix4x4.TRS(new Vector3(0,0.13f,0), Quaternion.identity, Vector3.one);
                        tilemap.SetTransformMatrix(pos, matrix);
                        break;
                    case MapTile.GRASS:
                        tilemap.SetTile(pos, grassBase);
                        Matrix4x4 matrixA = Matrix4x4.TRS(new Vector3(0,0,1), Quaternion.identity, Vector3.one);
                        tilemap.SetTransformMatrix(pos, matrixA);
                        break;
                    case MapTile.PORTAL:
                        tilemap.SetTile(pos, portalBase);
                        Matrix4x4 matrixB = Matrix4x4.TRS(new Vector3(0,0,1), Quaternion.identity, Vector3.one);
                        tilemap.SetTransformMatrix(pos, matrixB);
                        break;
                }
            }
        }
    }

    public void UpdateTile(Vector2Int position, Map map)
    {
        var pos = new Vector3Int(position.x, position.y, 0);
        switch (map.scenario[position.x,position.y])
        {
            case MapTile.ROCK:
                tilemap.SetTile(pos, rockBase);
                Matrix4x4 matrix = Matrix4x4.TRS(new Vector3(0,0.13f,0), Quaternion.identity, Vector3.one);
                tilemap.SetTransformMatrix(pos, matrix);
                break;
            case MapTile.GRASS:
                tilemap.SetTile(pos, grassBase);
                break;
            case MapTile.PORTAL:
                tilemap.SetTile(pos, portalBase);
                break;
        }
    }
    
}
