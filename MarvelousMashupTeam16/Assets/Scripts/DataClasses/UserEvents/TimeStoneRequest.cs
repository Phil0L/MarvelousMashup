public class TimeStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public Character character;

    public TimeStoneRequest(InfinityStone infinityStone, Character character) : base(UserAction.Green)
    {
        this.infinityStone = infinityStone;
        this.character = character;
    }
}