public class EndTurnRequest : UserRequest
{
    public Character character;

    public EndTurnRequest(Character character) : base(UserAction.EndTurn)
    {
        this.character = character;
    }
}