using UnityEngine;

public class PowerStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Vector2Int attackPosition;
    public IFieldContent attacked;
    

    public PowerStoneRequest(InfinityStone infinityStone, Character character, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.Violet)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}