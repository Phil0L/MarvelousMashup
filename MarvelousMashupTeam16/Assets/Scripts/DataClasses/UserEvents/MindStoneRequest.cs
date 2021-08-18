using UnityEngine;

public class MindStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Vector2Int attackPosition;
    public IFieldContent attacked;

    public MindStoneRequest(InfinityStone infinityStone, Character character, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.Yellow)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}