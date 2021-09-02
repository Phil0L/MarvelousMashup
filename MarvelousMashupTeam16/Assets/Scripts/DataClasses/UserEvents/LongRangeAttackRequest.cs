using System.Collections.Generic;
using UnityEngine;

public class LongRangeAttackRequest : UserRequest
{
    public IDs characterID;
    public Vector2Int attackPosition;
    public IFieldContent attacked;

    public LongRangeAttackRequest(IDs characterID, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.LongRange)
    {
        this.characterID = characterID;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}