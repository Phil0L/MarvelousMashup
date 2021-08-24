public class SpawnEntityEvent : Message
{
    /**
     * the entity that is about to be spawned on the map (e.g. Thanos or Goose, or Infinity Stones)
     */
    public Entities entity;

    /**
     * the constructor of the SpawnEntityEvent-Class. Creates a SpawnEntityEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param entity the entity that is about to be spawned on the map (e.g. Thanos or Goose, or Infinity Stones)
     */
    public SpawnEntityEvent(Entities entity) : base(EventType.SpawnEntityEvent){
        this.entity = entity;
    }
}