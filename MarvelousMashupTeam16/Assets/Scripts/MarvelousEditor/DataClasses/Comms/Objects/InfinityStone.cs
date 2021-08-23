public class InfinityStone
{
    

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
    public override bool Equals(object o) {
        return super.equals(o);
    }
    
}