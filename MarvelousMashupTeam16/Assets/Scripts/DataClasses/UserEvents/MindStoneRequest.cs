using UnityEngine;

public class MindStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public IDs characterID;
    public Vector2Int attackPosition;
    public IFieldContent attacked;

    public MindStoneRequest(InfinityStone infinityStone, IDs characterID, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.Yellow)
    {
        this.infinityStone = infinityStone;
        this.characterID = characterID;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}