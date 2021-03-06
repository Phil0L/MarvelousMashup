package communication.messages.events.gamestate;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EntityID;
import communication.messages.enums.EventType;
import communication.messages.objects.Entities;

import java.util.Arrays;

/**
 *  Message-Class, which is sent at the end of EACH AND EVERY message from the server to inform
 *  the client about the current GameState on the map
 *
 *  @author Sarah Engele
 *
 */

public class GamestateEvent extends Message {

    /**
     * describes where Characters, Rocks and Infinity Stones are placed on the map. If there is no Entity on a field,
     * the field is a GRASS field
     */
    public Entities[] entities;
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
    //The stoneCooldowns array has 6 entries (Space, Mind, Reality, Power, Time, Soul)
    /**
     * describes which Infinity Stones are currently not able to be used since they  have to cooldown first
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

    public GamestateEvent(Entities[] entities, int[] mapSize, IDs[] turnOrder, IDs activeCharacter,
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
     * Compares two GamestateEvent objects with each other
     * @param o a Message-Object that is going to be compared to the Message
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamestateEvent)) return false;
        GamestateEvent that = (GamestateEvent) o;
        return winCondition == that.winCondition && Arrays.equals(entities, that.entities) && Arrays.equals(mapSize, that.mapSize) && Arrays.equals(turnOrder, that.turnOrder) && activeCharacter.equals(that.activeCharacter) && Arrays.equals(stoneCooldowns, that.stoneCooldowns);
    }

}
