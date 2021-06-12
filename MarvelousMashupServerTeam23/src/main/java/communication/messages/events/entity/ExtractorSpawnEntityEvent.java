package communication.messages.events.entity;

import communication.messages.Message;
import communication.messages.enums.EventType;
import communication.messages.objects.Entities;
import communication.messages.objects.ExtractorEntities;

/**
 * This message is sent when a new entity is spawned on the map (e.g. Goose and Thanos) or a Infinity Stone was dropped
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */

public class ExtractorSpawnEntityEvent extends Message {

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
    public ExtractorSpawnEntityEvent(ExtractorEntities entity){
        super(EventType.SpawnEntityEvent);
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
