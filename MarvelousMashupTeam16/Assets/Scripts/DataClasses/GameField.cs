public class GameField
{
    public MapTile tile;
    public int tileData;
    public IFieldContent item;

    public bool IsWalkable()
    {
        return tile != MapTile.ROCK;
    }

    public bool IsAttackable()
    {
        return item is Character {enemy: true} || (tile == MapTile.ROCK && tileData > 0);
    }

    public bool IsBlockingLine()
    {
        return tile == MapTile.ROCK || tile == MapTile.PORTAL || item is Character;
    }

    public bool IsEmpty()
    {
        return tile == MapTile.GRASS && item == null;
    }
}

public interface IFieldContent
{
}