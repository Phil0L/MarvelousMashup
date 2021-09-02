public class EndTurnRequest : UserRequest
{
    public IDs characterID;

    public EndTurnRequest(IDs characterID) : base(UserAction.EndTurn)
    {
        this.characterID = characterID;
    }
}