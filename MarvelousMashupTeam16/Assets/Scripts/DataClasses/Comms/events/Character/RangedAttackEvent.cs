/**
 *  Message-Class, which is responsible to send RangedAttackEvents to the client
 *  if the user wants to do an ranged attack and is allowed to do so by the server
 *
 *  @author Sarah Engele
 *
 */

public class RangedAttackEvent : Message {

    /**
     * The entity that wants to start a ranged attack
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
     * the constructor of the RangedAttackEvent-Class. Creates a RangedAttackEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to start a ranged attack
     * @param targetEntity The entity the originEntity wants to attack
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param amount The amount of damage that is going to be caused to the target entity
     *
     *
     */

    public RangedAttackEvent(IDs originEntity, IDs targetEntity, int[] originField,
                               int[] targetField, int amount) : base(EventType.RangedAttackEvent)
    {
     
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.amount = amount;
    }

}
