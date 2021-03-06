/**
 *  Message-Class, which is responsible to send ExchangeInfinityStoneEvents to the clients
 *  if the user wants one of his characters to exchange an Infinity Stone with another character
 *   and this was approved by the server to do so
 *
 *  @author Sarah Engele
 *
 */

public class ExchangeInfinityStoneEvent : Message, CharacterEvent
{

    /**
     * The entity that owns an Infinity Stone and wants to hand it over to the targetEntity
     */
    public IDs originEntity;
    /**
     * The entity that will receive the Infinity Stone
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
     * The Infinity Stone that is about to be exchanged
     */
    public IDs stoneType;

    /**
     *
     * the constructor of the ExchangeInfinityStoneEvent-Class.
     * Creates a ExchangeInfinityStoneEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that owns an Infinity Stone and wants to hand
     *                     it over to the targetEntity
     * @param targetEntity The entity that will receive the Infinity Stone
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param stoneType The Infinity Stone that is about to be exchanged
     *
     */

    public ExchangeInfinityStoneEvent(IDs originEntity, IDs targetEntity, int[] originField,
                                        int[] targetField, IDs stoneType) : base(EventType.ExchangeInfinityStoneEvent)
    {
     this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.stoneType = stoneType;
    }

    public void Execute()
    {
        InfinityStone infinityStone = IDTracker.Get(stoneType) as InfinityStone;
        if (infinityStone == null) return;
        
        Character origin = IDTracker.Get(originEntity) as Character;
        if (origin == null) return;
        
        Character target = IDTracker.Get(targetEntity) as Character;
        if (target == null) return;

        origin.infinityStones.Remove(infinityStone);
        target.infinityStones.Add(infinityStone);
    }
}
