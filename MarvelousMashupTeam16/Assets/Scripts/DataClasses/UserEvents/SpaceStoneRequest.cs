using UnityEngine;

public class SpaceStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public IDs characterID;
    public Vector2Int teleportPosition;

    public SpaceStoneRequest(InfinityStone infinityStone, IDs characterID, Vector2Int teleportPosition) : base(UserAction.Blue)
    {
        this.infinityStone = infinityStone;
        this.characterID = characterID;
        this.teleportPosition = teleportPosition;
    }
}