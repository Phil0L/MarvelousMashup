using UnityEngine;

public class SoulStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public IDs characterID;
    public Character revived;
    public Vector2Int revivePosition;

    public SoulStoneRequest(InfinityStone infinityStone, IDs characterID, Character revived, Vector2Int revivePosition) : base(UserAction.Orange)
    {
        this.infinityStone = infinityStone;
        this.characterID = characterID;
        this.revived = revived;
        this.revivePosition = revivePosition;
    }
}