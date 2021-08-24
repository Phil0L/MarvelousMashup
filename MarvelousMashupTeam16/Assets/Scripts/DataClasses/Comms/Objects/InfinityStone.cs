public class InfinityStone : Entities, IFieldContent
{
    
    public const int GREEN = 1;
    public const int YELLOW = 2;
    public const int PURPLE = 3;
    public const int BLUE = 4;
    public const int RED = 5;
    public const int ORANGE = 6;


    public int stone;
    public int cooldown;
    public int defaultcooldownTime;

    /**
     * the constructor of the InfinityStone-Class. Creates a InfinityStone-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param ID the ID of this specific InfinityStone (every InfinityStone has an ID,
     *           see also in IDs class)
     * @param position the position where the InfinityStone is placed on the map
     */

    public InfinityStone(int ID, int[] position) : base(EntityType.InfinityStone, ID, position){
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
        return base.Equals(o);
    }
    
    public override string ToString()
    {
        return (stone == GREEN ? "Green" :
                   stone == YELLOW ? "Yellow" :
                   stone == PURPLE ? "Purple" :
                   stone == BLUE ? "Blue" :
                   stone == RED ? "Red" :
                   stone == ORANGE ? "Orange" : "Undefined")
               + " InfinityStone";
    }
    
}