/*
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * Wir sind uns nicht ganz sicher, ob dieses Ding nicht unnätig ist. 
 */
public class ExtractorSpawnEntityEvent : Message
{
    /**
     * the entity that is about to be spawned on the map (e.g. Thanos or Goose, or Infinity Stones)
     */
    public ExtractorEntities entity;

    /**
     * the constructor of the ExtractorSpawnEntityEvent-Class. Creates a ExtractorSpawnEntityEvent-MessageObject.
     *
     * @author Sarah Engele
     * @author Matthias Ruf
     *
     * @param entity the entity that is about to be spawned on the map (e.g. Thanos or Goose, or Infinity Stones)
     */
    public ExtractorSpawnEntityEvent(ExtractorEntities entity) : base(EventType.SpawnEntityEvent){
        this.entity = entity;
    }

    /**
     * creates a SpawnEntityEvent message object out of an ExtractorSpawnEntityEvent message object
     *
     * @author Matthias Ruf
     *
     * @return a SpawnEntityEvent message object created by using attributes of the ExtractorSpawnEntityEvent message
     * object
     *
     */
    public SpawnEntityEvent toSpawnEntityEvent(){
        return new SpawnEntityEvent(this.entity.toEntities());
    }
    
}