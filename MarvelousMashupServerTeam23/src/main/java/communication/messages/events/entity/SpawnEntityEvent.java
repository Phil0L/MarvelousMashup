package communication.messages.events.entity;

import communication.messages.Message;
import communication.messages.enums.EventType;
import communication.messages.objects.Entities;

/**
 * This message is sent when a new entity is spawned on the map (e.g. Goose and Thanos) or a Infinity Stone was dropped
 *
 * @author Sarah Engele
 *
 */

public class SpawnEntityEvent extends Message {

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

    public SpawnEntityEvent(Entities entity){
        super(EventType.SpawnEntityEvent);
        this.entity = entity;
    }
}
