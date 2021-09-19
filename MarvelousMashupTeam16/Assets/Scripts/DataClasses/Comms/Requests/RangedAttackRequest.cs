/**
 *  Message-Class, which is responsible to send RangedAttackRequests to the server
 *  if the user wants to do an ranged attack
 *
 *  @author Sarah Engele
 *
 */

public class RangedAttackRequest : Message {

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
    public int value;

    /**
     *
     * the constructor of the RangedAttackRequest-Class. Creates a RangedAttackRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to start a ranged attack
     * @param targetEntity The entity the originEntity wants to attack
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param value The amount of damage that is going to be caused to the target entity
     *
     */

    public RangedAttackRequest(IDs originEntity, IDs targetEntity, int[] originField,
                              int[] targetField, int value) : base(RequestType.RangedAttackRequest){
     
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.value = value;
    }

}
