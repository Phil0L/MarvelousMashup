/**
 *  Message-Class, which is responsible to send MoveRequests to the clients
 *  if the user wants his character to move to another field and this was approved by the server to do so
 *
 *  @author Sarah Engele
 *
 */
public class MoveEvent : Message, CharacterEvent
{
    /**
     * The entity that wants to move
     */
    public IDs originEntity;

    /**
     * The position of the originEntity on the game field
     */
    public int[] originField;

    /**
     * The position on the game field where the originEntity wants to go next
     */
    public int[] targetField;

    /**
     *
     * the constructor of the MoveEvent-Class. Creates a MoveEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to move
     * @param originField The position of the originEntity on the game field
     * @param targetField The position on the game field where the originEntity wants to go next

     *
     */
    public MoveEvent(IDs originEntity, int[] originField, int[] targetField) : base(EventType.MoveEvent)
    {
        this.originEntity = originEntity;
        this.originField = originField;
        this.targetField = targetField;
    }

    public void Execute()
    {
        Character origin = IDTracker.Get(originEntity) as Character;
        if (origin == null) return;
        
        Game.State().MoveHero(origin.characterID, targetField.ToVector());
    }
}