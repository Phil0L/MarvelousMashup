package communication.messages.objects;

import communication.messages.enums.EntityType;

import java.util.Arrays;
import java.util.Objects;

/**
 *  Message-Class, which is responsible to describe the Entity NPC which can be placed on
 *  the map
 *  NPCs are needed e.g. for GameStateEvents
 *
 *  @author Sarah Engele
 *
 */

public class NPC extends Entities{

    /**
     * Initial number of Movement Points. Used for a Character and a NPC.
     */
    public int MP;
    /**
     * ID of the infinity Stones, which an Character has in his inventory. Used for a Character and a NPC.
     */
    public int[] stones;

    /**
     * the constructor of the Entities-Class. Creates an Entities-Object.
     *
     * @author Sarah Engele
     *
     * @param ID         the specific ID of the entity
     * @param position   the position of the entitiy on the map
     * @param MP         the MP the NPC has (only important for Thanos)
     * @param stones     the inventory with stones the NPC has (only important for Thanos)
     * @author Sarah Engele
     */
    public NPC(int ID, int[] position, int MP, int[] stones) {
        super(EntityType.NPC, ID, position);
        this.MP = MP;
        this.stones = stones;

    }

    /**
     * This methode compares two entities (NPCs) with each other
     *
     * @author Sarah Engele
     *
     * @param o an object (most likely an NPC) that is going to be compared to the NPC
     * @return boolean which is true when both objects are equal (MP, stones, entityType, ID, position)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NPC)) return false;
        if (!super.equals(o)) return false;
        NPC npc = (NPC) o;
        return MP == npc.MP && Arrays.equals(stones, npc.stones);
    }

}
