public class ConsumedAPEvent : Message {

    /**
     * The Entity that is about to consume its AP
     */
  
    public IDs targetEntity;
  
    /**
     * The field where the targetEntity is placed on the map
     */ 
    public int[] targetField;
  
    /**
     * The amount of action points the targetEntity consumes
     */ 
    public int amount;
  
  
   /**
    * the constructor of the ConsumedAPEvent-Class. Creates a ConsumedAPEvent-MessageObject.
    *
    * @author Sarah Engele
    *
    * @param targetEntity The Entity that is about to consume its AP
    * @param targetField The field where the targetEntity is placed on the map
    * @param amount The amount of action points the targetEntity consumes
    *
    */ 
   public ConsumedAPEvent(IDs targetEntity, int[] targetField, int amount) : base(EventType.ConsumedAPEvent){ 
    
       this.targetEntity = targetEntity;
       this.targetField = targetField;
       this.amount = amount;
   }

}