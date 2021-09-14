/**
 * This message is sent to all clients at the beginning of a new move and informs the clients about the current amount
 * of moves and the next active character
 *
 * @author Sarah Engele
 *
 */

public class TurnEvent : Message, GameEvent 
{
    /**
     * The amount of moves during this round
     */
    public int turnCount;
    /**
     * The next active character that is allowed to make a move
     */
    public IDs nextCharacter;

    /**
     * the constructor of the TurnEvent-Class. Creates a TurnEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param turnCount The amount of moves during this round
     * @param nextCharacter The next active character that is allowed to make a move
     */

    public TurnEvent(int turnCount, IDs nextCharacter) : base(EventType.TurnEvent)
    {
     
        this.turnCount = turnCount;
        this.nextCharacter = nextCharacter;
    }

    public void Execute()
    {
        Character character = IDTracker.Get(nextCharacter) as Character;
        if (character == null) return;
        character.AP = character.maxAP;
        character.MP = character.maxMP;
        Game.State().SetCurrentTurn(character);
        if (!character.enemy) Info.Set()
            .Text("It's your turn!")
            .Cooldown(500)
            .NewRandomSprite()
            .Show();
    }
}
