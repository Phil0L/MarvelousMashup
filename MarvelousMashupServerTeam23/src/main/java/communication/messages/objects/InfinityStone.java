package communication.messages.objects;

import communication.messages.enums.EntityType;

/**
 *  Message-Class, which is responsible to describe the Entity Infinity Stone which can be placed on
 *  the map. Infinity Stones are only called entities if they are placed on the map. If a character has
 *  an Infinity Stone placed in his or her inventory, the Infinity Stone is not to be found as an entity,
 *  but inside the inventory of an entity.
 *  Infinity Stones are needed e.g. for GameStateEvents
 *
 *  @author Sarah Engele
 *
 */

public class InfinityStone extends Entities{



    /**
     * the constructor of the InfinityStone-Class. Creates a InfinityStone-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param ID the ID of this specific InfinityStone (every InfinityStone has an ID,
     *           see also in IDs class)
     * @param position the position where the InfinityStone is placed on the map
     */

    public InfinityStone(int ID, int[] position){

        super(EntityType.InfinityStone, ID, position);

    }

    /**
     * This methode compares two entities (Infinity Stones) with each other
     *
     * @author Sarah Engele
     *
     * @param o an object (most likely an Infinity Stone) that is going to be compared to the Infinity Stone
     * @return boolean which is true when both objects are equal (entityType, ID, position)
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
