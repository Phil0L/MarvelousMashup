/**
 *  Message-Class, which is responsible to send UseInfinityStoneRequests to the server
 *  if the user wants one of his characters to use his or her Infinity Stone
 *
 *  @author Sarah Engele
 *
 */

public class UseInfinityStoneRequest : Message {

    /**
     * The entity that wants to use the Infinity Stone stoneType and owns this specific Infinity Stone
     */
    public IDs originEntity;
    /**
     * The position of the originEntity on the game field
     */
    public int[] originField;
    /**
     * The position of the targetEntity on the game field
     */
    public int[] targetField;
    /**
     * The Infinity Stone that is about to be used
     */
    public IDs stoneType;

    /**
     *
     * the constructor of the UseInfinityStoneRequest-Class.
     * Creates a UseInfinityStoneRequest-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to use the Infinity Stone stoneType and owns this specific Infinity
     *                     Stone
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param stoneType The Infinity Stone that is about to be used
     *
     */

    public UseInfinityStoneRequest(IDs originEntity, int[] originField, int[] targetField,
                                   IDs stoneType) : base(RequestType.UseInfinityStoneRequest)
    {
     this.originEntity = originEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.stoneType = stoneType;
    }

}
