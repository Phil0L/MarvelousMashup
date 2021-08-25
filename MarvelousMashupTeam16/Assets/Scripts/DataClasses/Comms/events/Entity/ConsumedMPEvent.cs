public class ConsumedMPEvent : Message
{
    public IDs targetEntity;
    public int[] targetField;
    public int amount;


    /**
     * the constructor of the ConsumedAPEvent-Class. Creates a ConsumedAPEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetEntity The Entity that is about to consume its MP
     * @param targetField The field where the targetEntity is placed on the map
     * @param amount The amount of movement points the targetEntity consumes
     *
     */

    public ConsumedMPEvent(IDs targetEntity, int[] targetField, int amount) : base(EventType.ConsumedMPEvent){
        
        this.targetEntity = targetEntity;
        this.targetField = targetField;
        this.amount = amount;
    }

}