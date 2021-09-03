using UnityEngine;

public class StonePassRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Character receivingCharacter;
    public Vector2Int position;
    public Vector2Int fromPosition;

    public StonePassRequest(InfinityStone infinityStone, Character character, Character receivingCharacter, Vector2Int position, Vector2Int fromPosition) : base(UserAction.StonePass)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.receivingCharacter = receivingCharacter;
        this.position = position;
        this.fromPosition = fromPosition;
    }
}