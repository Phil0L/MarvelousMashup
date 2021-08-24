public class Entities
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
     * the constructor of the Entities-Class. Creates a Entities-Object.
     *
     * @author Sarah Engele
     *
     * @param entityType The type of the entity (Rock, InGameCharacter, NPC and InfinityStone)
     * @param ID the specific ID of the entity
     * @param position the position of the entity on the map
     */
    public Entities(EntityType entityType, int ID, int[] position){
        this.entityType = entityType;
        this.ID = ID;
        this.position = position;
    }

    /**
     * This methode compares two entities  with each other
     *
     * @author Sarah Engele
     *
     * @param o an object (most likely an Entities) that is going to be compared to the Entities
     * @return boolean which is true when both objects are equal (entityType, ID, position)
     */
    public override bool Equals(object o) {
        if (this == o) return true;
        if (!(o is Entities)) return false;
        Entities entities = (Entities) o;
        return ID == entities.ID && entityType == entities.entityType && Arrays.equals(position, entities.position);
    }

    
}