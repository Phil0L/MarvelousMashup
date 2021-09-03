using UnityEngine;

public class RealityStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Vector2Int teleportPosition;

    public RealityStoneRequest(InfinityStone infinityStone, Character character, Vector2Int teleportPosition) : base(UserAction.Red)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.teleportPosition = teleportPosition;
    }
}