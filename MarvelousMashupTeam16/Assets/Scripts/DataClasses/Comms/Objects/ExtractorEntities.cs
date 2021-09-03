public class ExtractorEntities
{
    
    /**
     * Type of an Entities, is used to assign a Entities object to an Entities type. Used for all Entities objects.
     */
    public EntityType entityType;

    /**
     * ID of an Entities object. Used for all Entities objects. 
     */
    public int ID;

    /**
     * Position of the Entities on the gameField. Used for all Entities objects.
     */
    public int[] position;

    /**
     * Name of the Entities used for a Character
     */
    public string name;

    /**
     * Player ID of the Entities (indicate the related player) used for a Character (P1 or P2)
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
     * creates a Entities object by using the attributes of the ExtractorEntities object. The exact class of the object
     * is determined by the entityType of the object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a Entities object created by the attributes of the ExtractorEntities object.
     */
    public Entities toEntities(){
        switch (this.entityType) {
            case EntityType.Character:
                return this.toCharacter();
            case EntityType.NPC:
                return this.toNPC();
            case EntityType.Rock:
                return this.toRock();
            case EntityType.InfinityStone:
                return this.toInfinityStone();
            default:
                return null;
        }

    }

    /**
     *
     * creates a InGameCharacter object by using the attributes of the ExtractorEntities object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a InGameCharacter object
     */
    public InGameCharacter toCharacter(){
        return new InGameCharacter(this.name,this.PID,this.ID,this.HP,this.MP,this.AP,this.stones,this.position);
    }

    /**
     *
     * creates a Rock object by using the attributes of the ExtractorEntities object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a Rock object
     */
    public Rock toRock(){
        return new Rock(this.ID,this.HP,this.position);
    }

    /**
     *
     * creates a NPC object by using the attributes of the ExtractorEntities object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a NPC object
     */
    public NPC toNPC(){
        return new NPC(this.ID,this.position,this.MP,this.stones);
    }


    /**
     *
     * creates a Rock object by using the attributes of the ExtractorEntities object.
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @return a Rock object
     */
    public InfinityStone toInfinityStone(){
        return new InfinityStone(this.ID,this.position);
    }

}