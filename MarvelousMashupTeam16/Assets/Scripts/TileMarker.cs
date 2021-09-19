using UnityEngine;
using UnityEngine.Tilemaps;

public class TileMarker : MonoBehaviour
{

    public Tilemap tm;
    public Vector2Int position;
    
    // Start is called before the first frame update
    void Start()
    {
        tm = Game.Controller().GroundLoader.tilemap;
    }

    public void SetPosition(int x, int y)
    {
        SetPosition(new Vector2Int(x,y));
    }

    public void SetPosition(Vector2Int pos)
    {
        position = pos;
    }

    public void SetColor(Color color)
    {
        GetComponent<SpriteRenderer>().color = color;
    }

    // Update is called once per frame
    void Update()
    {
        var tmpos = tm.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0));
        transform.position = tmpos;
    }
}
