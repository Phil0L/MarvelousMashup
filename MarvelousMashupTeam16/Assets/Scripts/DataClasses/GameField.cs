public class GameField
{
    public MapTile tile;
    public int tileData;
    public IFieldContent item;

    public bool IsWalkable()
    {
        return item == null && tile != MapTile.ROCK;
    }
}

public interface IFieldContent
{
}