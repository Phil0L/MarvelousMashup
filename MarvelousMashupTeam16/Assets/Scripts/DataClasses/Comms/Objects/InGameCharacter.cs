public class InGameCharacter : Entities
{
    /**
     * Name of the Entities used for a Character
     */
    public string name;
    /**
     * Player ID of the Entities (indicate the related player) used for a Character
     */
    public int PID;

    /**
     * Initial number of Health Points. Used for a Character and a Rock.
     */
    public int HP;
    /**
     * Initial number of Movement Points. Used for a Character and a NPC.
     */
    public int MP;
    /**
     * Initial number of Movement Points. Used for a Character.
     */
    public int AP;
    /**
     * ID of the infinity Stones, which an Character has in his inventory. Used for a Character and a NPC.
     */
    public int[] stones;


    /**
     *
     * the constructor of the Character-Class. Creates a Character-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param name the name of the Character (e.g. Wonder Woman)
     * @param PID might be the Player ID (Player1 or Player2) that the Character belongs to
     * @param ID the ID of this specific Character (every Character has an ID, see also in IDs class)
     * @param HP the amount of Health Points (HP) the Character currently has
     * @param MP the amount of Movement Points (MP) the Character currently has
     * @param AP the amount of Action Points (AP) the Character currently has
     * @param stones the inventory of the Character that might contain Infinity Stones
     * @param position the position where the character is placed on the map
     */
    public InGameCharacter(string name, int PID, int ID, int HP, int MP, int AP, int[] stones, int[] position) : base(EntityType.Character, ID, position){
        this.name = name;
        this.PID = PID;

        this.HP = HP;
        this.MP = MP;
        this.AP = AP;
        this.stones = stones;

    }

    /**
     * This methode compares two entities (InGameCharacters) with each other
     *
     * @author Sarah Engele
     *
     * @param o an object (most likely an InGameCharacter) that is going to be compared to the InGameCharacter
     * @return boolean which is true when both objects are equal (name, PID, HP, MP, AP, stones, entityType, ID,
     * position)
     */
    public override bool Equals(object o) {
        if (this == o) return true;
        if (!(o is InGameCharacter)) return false;
        if (!super.Equals(o)) return false;
        InGameCharacter that = (InGameCharacter) o;
        return PID == that.PID && HP == that.HP && MP == that.MP && AP == that.AP && name.equals(that.name) && Arrays.equals(stones, that.stones);
    }

}