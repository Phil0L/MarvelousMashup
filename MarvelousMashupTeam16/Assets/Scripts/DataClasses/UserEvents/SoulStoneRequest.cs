using UnityEngine;

public class SoulStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Character revived;
    public Vector2Int revivePosition;

    public SoulStoneRequest(InfinityStone infinityStone, Character character, Character revived, Vector2Int revivePosition) : base(UserAction.Orange)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.revived = revived;
        this.revivePosition = revivePosition;
    }
}