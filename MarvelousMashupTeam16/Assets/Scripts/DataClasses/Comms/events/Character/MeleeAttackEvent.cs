/**
 *  Message-Class, which is responsible to send MeleeAttackEvents to the clients
 *  if the user wants to do an melee attack and is allowed to do so by the server
 *
 *  @author Sarah Engele
 *
 */

public class MeleeAttackEvent : Message, CharacterEvent
{

    /**
     * The entity that wants to start a melee attack
     */
    public IDs originEntity;
    /**
     * The entity the originEntity wants to attack
     */
    public IDs targetEntity;
    /**
     * The position of the originEntity on the game field
     */
    public int[] originField;
    /**
     * The position of the targetEntity on the game field
     */
    public int[] targetField;
    /**
     * The amount of damage that is going to be caused to the target entity
     */
    public int amount;

    /**
     *
     * the constructor of the MeleeAttackEvent-Class. Creates a MeleeAttackEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to start a melee attack
     * @param targetEntity The entity the originEntity wants to attack
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param amount The amount of damage that is going to be caused to the target entity
     *
     *
     */

    public MeleeAttackEvent(IDs originEntity, IDs targetEntity, int[] originField,
                              int[] targetField, int amount) : base(EventType.MeleeAttackEvent)
    {
     
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.amount = amount;
    }

    public void Execute()
    {
        Character origin = IDTracker.Get(originEntity) as Character;
        if (origin == null) return;
           
        Character target = IDTracker.Get(targetEntity) as Character;
        if (target == null) return;

        Game.State().AttackCloseRange(origin, originField.ToVector(), target, targetField.ToVector(), amount);
    }
}
