package communication.messages.events.gamestate;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EventType;
import communication.messages.objects.Entities;
import communication.messages.objects.ExtractorEntities;

/**
 * Message-Class, which is sent at the end of EACH AND EVERY message from the server to inform
 * the client about the current GameState on the map
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */

public class ExtractorGamestateEvent extends Message {


    /**
     * describes where Characters, Rocks and Infinity Stones are placed on the map. If there is no Entity on a field,
     * the field is a GRASS field
     */
    public ExtractorEntities[] entities;
    /**
     * describes how big the map is (x-axis and y-axis)
     */
    public int[] mapSize;
    /**
     * describes in which order the different Characters on the map can make their moves. It does not matter which team
     * they belong to. The order is decided by the server
     */
    public IDs[] turnOrder;
    /**
     * points out which character is allowed to make a move
     */
    public IDs activeCharacter;
    /**
     * describes which Infinity Stones are currently not able to be used since they have to cooldown first. The
     * stoneCooldowns array has 6 entries (Space, Mind, Reality, Power, Time, Soul)
     */
    public int[] stoneCooldowns;
    /**
     * checks if there is a winner (true if there is a winner, false if not)
     */
    public boolean winCondition;

    /**
     *
     * the constructor of the GamestateEvent-Class.
     * Creates a GamestateEvent-MessageObject.
     *
     * @author Sarah Engele
     * @author Matthias Ruf
     *
     * @param entities describes where Characters, Rocks and Infinity Stones are placed on the map.
     *                 If there is no Entity on a field, the field is a GRASS field
     * @param mapSize describes how big the map is (x-axis and y-axis)
     * @param turnOrder describes in which order the different Characters on the map can make their
     *                  moves. It does not matter which team they belong to. The order is decided by the
     *                  server
     * @param activeCharacter points out which character is allowed to make a move
     * @param stoneCooldowns describes which Infinity Stones are currently not able to be used since they
     *                       have to cooldown first
     * @param winCondition checks if there is a winner (true if there is a winner, false if not)
     */
    public ExtractorGamestateEvent(ExtractorEntities[] entities, int[] mapSize, IDs[] turnOrder, IDs activeCharacter,
                                   int[] stoneCooldowns, boolean winCondition){
        super(EventType.GamestateEvent);
        this.entities = entities;
        this.mapSize = mapSize;
        this.turnOrder = turnOrder;
        this.activeCharacter = activeCharacter;
        this.stoneCooldowns = stoneCooldowns;
        this.winCondition = winCondition;
    }


    /**
     * creates a GamestateEvent out of an ExtractorGamestateEvent
     *
     * @author Matthias Ruf
     *
     * @return a GamestateEvent created by using attributes of the ExtractorGamestateEvent
     *
     */
    public GamestateEvent toGamestateEvent(){
        Entities[] entities = new Entities[this.entities.length];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = this.entities[i].toEntities();
        }
        return new GamestateEvent(entities,this.mapSize,this.turnOrder,this.activeCharacter,
                this.stoneCooldowns,this.winCondition);
    }


}
