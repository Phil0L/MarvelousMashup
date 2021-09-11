using UnityEngine;

public class RealityStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;
    public Vector2Int stonePosition;

    public RealityStoneRequest(InfinityStone infinityStone, Character character, Vector2Int stonePosition) : base(UserAction.Red)
    {
        this.infinityStone = infinityStone;
        this.character = character;
        this.stonePosition = stonePosition;
    }
}