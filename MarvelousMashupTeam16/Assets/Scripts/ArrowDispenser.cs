using System;
using UnityEngine;

public class ArrowDispenser : MonoBehaviour
{
    public Arrow arrowPrefab;

    public void SummonArrow(Vector2Int startingPosition, Vector2Int endPosition, Action callback = null)
    {
        Vector3 start = Game.Controller().GroundLoader.tilemap.GetCellCenterWorld(new Vector3Int(startingPosition.x, startingPosition.y, 0));
        Vector3 end = Game.Controller().GroundLoader.tilemap.GetCellCenterWorld(new Vector3Int(endPosition.x, endPosition.y, 0));
        
        var arrow = Instantiate(arrowPrefab, start, Quaternion.identity);
        arrow.target = end;
        arrow.Hit(callback);
    }
}
