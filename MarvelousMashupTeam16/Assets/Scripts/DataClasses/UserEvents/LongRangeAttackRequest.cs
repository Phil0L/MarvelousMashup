using System.Collections.Generic;
using UnityEngine;

public class LongRangeAttackRequest : UserRequest
{
    public Character character;
    public Vector2Int characterPosition;
    public Vector2Int attackPosition;
    public IFieldContent attacked;

    public LongRangeAttackRequest(Character character, Vector2Int characterPosition, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.LongRange)
    {
        this.character = character;
        this.characterPosition = characterPosition;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}