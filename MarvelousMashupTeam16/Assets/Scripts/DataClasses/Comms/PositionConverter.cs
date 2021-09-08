using UnityEngine;

public static class PositionConverter
{
    public static int[] ToArray(this Vector2Int position) => new[] {position.y, position.x};

    public static Vector2Int ToVector(this int[] position) => new Vector2Int(position[1], position[0]);
}