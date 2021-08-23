using System.Collections.Generic;
using UnityEngine;

public class CloseRangeAttackRequest : UserRequest
{
    public Character character;
    public Vector2Int attackPosition;
    public IFieldContent attacked;
    

    public CloseRangeAttackRequest(Character character, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.CloseRange)
    {
        this.character = character;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}