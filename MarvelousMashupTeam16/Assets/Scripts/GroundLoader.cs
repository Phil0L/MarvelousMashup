using UnityEngine;
using UnityEngine.Tilemaps;

public class GroundLoader : MonoBehaviour
{
    public Tilemap tilemap;
    public TileBase rockBase;
    public TileBase grassBase;
    public TileBase portalBase;
    public TileBase crackedRockBase;
    public TileBase damagedRockBase;
    public TileBase destroyedRockBase;

    public int defaultRockHealth;
    
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

    public void UpdateTile(Vector2Int position)
    {
        if (Game.State().IsOutOfBounds(position)) return;
        var pos = new Vector3Int(position.x, position.y, 0);
        GameField field = Game.State()[position.x, position.y];
        MapTile tile = field.tile;
        switch (tile)
        {
            case MapTile.ROCK:
                tilemap.SetTile(pos, rockBase);
                if (field.tileData < defaultRockHealth * 0.75) tilemap.SetTile(pos, crackedRockBase);
                if (field.tileData < defaultRockHealth * 0.5) tilemap.SetTile(pos, damagedRockBase);
                if (field.tileData < defaultRockHealth * 0.25) tilemap.SetTile(pos, destroyedRockBase);
                Matrix4x4 matrix = Matrix4x4.TRS(new Vector3(0,0.13f,0), Quaternion.identity, Vector3.one);
                tilemap.SetTransformMatrix(pos, matrix);
                break;
            case MapTile.GRASS:
                tilemap.SetTile(pos, grassBase);
                matrix = Matrix4x4.TRS(new Vector3(0,0f,0), Quaternion.identity, Vector3.one);
                tilemap.SetTransformMatrix(pos, matrix);
                break;
            case MapTile.PORTAL:
                tilemap.SetTile(pos, portalBase);
                matrix = Matrix4x4.TRS(new Vector3(0,0f,0), Quaternion.identity, Vector3.one);
                tilemap.SetTransformMatrix(pos, matrix);
                break;
        }
    }
    
}
