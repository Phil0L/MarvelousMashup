public class HealedEvent : Message
{
    
    /**
     * The Entity that is about to be healed and that receives its health points back
     */
    public IDs targetEntity;
    /**
     * The field where the targetEntity is placed on the map
     */
    public int[] targetField;
    /**
     * The amount of health points the targetEntity receives
     */
    public int amount;


    /**
     * the constructor of the HealedEvent-Class. Creates a HealedEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetEntity The Entity that is about to be healed and that receives its health points back
     * @param targetField The field where the targetEntity is placed on the map
     * @param amount The amount of health points the targetEntity receives
     *
     */

    public HealedEvent(IDs targetEntity, int[] targetField, int amount){
        super(EventType.HealedEvent);
        this.targetEntity = targetEntity;
        this.targetField = targetField;
        this.amount = amount;
    }

}