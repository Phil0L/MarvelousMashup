public class DestroyedEntityEvent : Message
{
    public int[] targetField;
    public IDs targetEntity;

    /**
     * the constructor of the DestroyEntityEvent-Class. Creates a DestroyEntityEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetField The field on the map where the entity was placed before being destroyed
     * @param targetEntity The entity that is about to be destroyed
     */
    public DestroyedEntityEvent(int[] targetField, IDs targetEntity){
        super(EventType.DestroyedEntityEvent);
        this.targetEntity = targetEntity;
        this.targetField = targetField;

    }
}