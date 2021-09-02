using UnityEngine;

public class PowerStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public IDs characterID;
    public Vector2Int attackPosition;
    public IFieldContent attacked;
    

    public PowerStoneRequest(InfinityStone infinityStone, IDs characterID, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.Violet)
    {
        this.infinityStone = infinityStone;
        this.characterID = characterID;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}