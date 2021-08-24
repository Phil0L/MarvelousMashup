public class TakenDamageEvent
{
    /**
     * The Entity that is about to be done damage to
     */
    public IDs targetEntity;
    /**
     * The field where the targetEntity is placed on the map
     */
    public int[] targetField;
    /**
     * The amount of damage that is going to be done to the targetEntity
     */
    public int amount;


    /**
     * the constructor of the TakenDamageEvent-Class. Creates a TakenDamageEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetEntity The Entity that is about to be done damage to
     * @param targetField The field where the targetEntity is placed on the map
     * @param amount The amount of damage that is going to be done to the targetEntity
     */

    public TakenDamageEvent(IDs targetEntity, int[] targetField, int amount) : base(EventType.TakenDamageEvent){
        this.targetEntity = targetEntity;
        this.targetField = targetField;
        this.amount = amount;
    }
    
}