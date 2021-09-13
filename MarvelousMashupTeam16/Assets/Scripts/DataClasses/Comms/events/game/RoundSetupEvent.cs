using System.Collections.Generic;
using System.Linq;

/**
 * This message is sent to all clients to inform them at the beginning of a new round in which order their characters
 * are allowed to make moves.
 *
 * @author Sarah Engele
 *
 */
public class RoundSetupEvent : Message, GameEvent
{
    /**
     * the number of the next round that is about to start
     */
    public int roundCount;

    /**
     * array which lets the clients know the order in which the characters are allowed to make their moves
     */
    public IDs[] characterOrder;

    /**
     * the constructor of the RoundSetupEvent-Class. Creates a RoundSetupEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param roundCount the number of the next round that is about to start
     * @param characterOrder array which lets the clients know the order in which the characters are allowed to make
     *                       their moves
     */
    public RoundSetupEvent(int roundCount, IDs[] characterOrder) : base(EventType.RoundSetupEvent)
    {
        this.roundCount = roundCount;
        this.characterOrder = characterOrder;
    }

    public void Execute()
    {
        Game.Controller().GameInfoDisplayer.SetRoundCount(roundCount + 1);

        List<Character.Characters> charactersList = new List<Character.Characters>();
        foreach (var id in characterOrder)
        {
            Character character = IDTracker.Get(id) as Character;
            if (character != null)
                charactersList.Add(character.characterID);
        }

        Game.Controller().GameInfoDisplayer.SetCharacters(charactersList);
    }

    public override string ToString()
    {
        return "RoundSetupEvent: " + string.Join(", ", characterOrder.Select(co => co.ToString()));
    }
}