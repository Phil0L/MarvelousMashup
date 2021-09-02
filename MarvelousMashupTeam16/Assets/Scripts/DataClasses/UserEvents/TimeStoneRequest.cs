public class TimeStoneRequest : UserRequest
{
    public InfinityStone infinityStone;
    public IDs characterID;

    public TimeStoneRequest(InfinityStone infinityStone, IDs characterID) : base(UserAction.Green)
    {
        this.infinityStone = infinityStone;
        this.characterID = characterID;
    }
}