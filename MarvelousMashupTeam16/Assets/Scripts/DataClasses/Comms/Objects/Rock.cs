public class Rock : Entities, IID, IFieldContent
{
    
    /**
     * Initial number of Health Points. Used for a Character and a Rock.
     */
    public int HP;



    /**
     *
     * the constructor of the Rock-Class.
     * Creates a Rock-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param ID the ID of this specific Rock (every Rock/Entity has an ID, see also in IDs class)
     * @param HP the amount of Health Points (HP) the Rock currently has. If the HP are equal to zero,
     *           the Rock turns into GRASS
     * @param position the position where the rock is placed on the map
     *
     */

    public Rock(int ID, int HP, int[] position) : base(EntityType.Rock, ID, position){

        this.HP = HP;

    }

    /**
     * This methode compares two entities (Rocks) with each other
     *
     * @author Sarah Engele
     *
     * @param o an object (most likely a Rock) that is going to be compared to the Rock
     * @return boolean which is true when both objects are equal (HP, entityType, ID, position)
     */
    public override bool Equals(object o) {
        if (this == o) return true;
        if (!(o is Rock)) return false;
        if (!base.Equals(o)) return false;
        Rock rock = (Rock) o;
        return HP == rock.HP;
    }

}