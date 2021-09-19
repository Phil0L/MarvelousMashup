/**
 *  Message-Class, which is responsible to send MeleeAttackRequests to the server
 *  if the user wants to do an melee attack
 *
 *  @author Sarah Engele
 *
 */

public class MeleeAttackRequest : Message {

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
    public int value;

    /**
     *
     * the constructor of the MeleeAttackRequest-Class. Creates a MeleeAttackRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to start a melee attack
     * @param targetEntity The entity the originEntity wants to attack
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param value The amount of damage that is going to be caused to the target entity
     *
     */

    public MeleeAttackRequest(IDs originEntity, IDs targetEntity, int[] originField,
                              int[] targetField, int value) : base(RequestType.MeleeAttackRequest){
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.value = value;
    }

}
