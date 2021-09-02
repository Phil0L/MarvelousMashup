using System.Collections.Generic;
using UnityEngine;

public class CloseRangeAttackRequest : UserRequest
{
    public IDs characterID;
    public Vector2Int attackPosition;
    public IFieldContent attacked;
    

    public CloseRangeAttackRequest(IDs characterID, Vector2Int attackPosition, IFieldContent attacked) : base(UserAction.CloseRange)
    {
        this.characterID = characterID;
        this.attackPosition = attackPosition;
        this.attacked = attacked;
    }
}