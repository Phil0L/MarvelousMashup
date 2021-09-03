using System.Collections.Generic;
using UnityEngine;

public class CharacterMoveRequest : UserRequest
{
    public Character character;
    public Vector2Int previousPosition;
    public Vector2Int newPosition;
    public List<Vector2Int> path;

    public CharacterMoveRequest(Character character, Vector2Int previousPosition, Vector2Int newPosition, List<Vector2Int> path) : base(UserAction.Move)
    {
        this.character = character;
        this.previousPosition = previousPosition;
        this.newPosition = newPosition;
        this.path = path;
    }
}