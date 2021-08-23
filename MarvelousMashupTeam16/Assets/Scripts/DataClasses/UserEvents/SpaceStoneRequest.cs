using UnityEngine;

public class SpaceStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Vector2Int teleportPosition;

    public SpaceStoneRequest(InfinityStone infinityStone, Character character, Vector2Int teleportPosition) : base(UserAction.Blue)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.teleportPosition = teleportPosition;
    }
}