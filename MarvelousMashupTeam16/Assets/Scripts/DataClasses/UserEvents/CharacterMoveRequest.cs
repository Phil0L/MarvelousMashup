using System.Collections.Generic;
using UnityEngine;

public class CharacterMoveRequest : UserRequest
{
    public IDs characterID;
    public Vector2Int previousPosition;
    public Vector2Int newPosition;
    public List<Vector2Int> path;

    public CharacterMoveRequest(IDs characterID, Vector2Int previousPosition, Vector2Int newPosition, List<Vector2Int> path) : base(UserAction.Move)
    {
        this.characterID = characterID;
        this.previousPosition = previousPosition;
        this.newPosition = newPosition;
        this.path = path;
    }
}