using UnityEngine;

public class RealityStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public IDs characterID;
    public Vector2Int teleportPosition;

    public RealityStoneRequest(InfinityStone infinityStone, IDs characterID, Vector2Int teleportPosition) : base(UserAction.Red)
    {
        this.infinityStone = infinityStone;
        this.characterID = characterID;
        this.teleportPosition = teleportPosition;
    }
}