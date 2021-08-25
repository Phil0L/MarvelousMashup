public class IDs
{
    
    /**
     * The function of the entity  (NPC, P1, P2, Rocks or InfinityStones)
     */
    public EntityID entityID;
    /**
     * The specific ID of the entity. Every entity has its own ID.
     */
    public int ID;

    /**
     * The constructor of the IDs class. Creates an IDs-Object
     *
     * @author Sarah Engele
     *
     * @param entityID The function of the entity  (NPC, P1, P2, Rocks or InfinityStones)
     * @param ID The specific ID of the entity. Every entity has its own ID.
     *
     */
    public IDs(EntityID entityID, int ID){
        this.entityID = entityID;
        this.ID = ID;
    }

    /**
     * this methode compares two IDs with each other(entityID and ID)
     * @param o another IDs that is compared the IDs
     * @return true if both are equal
     */
    public override bool Equals(object o) {
        if (this == o)
		{
			return true;
		}
        if (!(o is IDs )) {
			return false;
		}
        IDs iDs = (IDs) o;
        return ID == iDs.ID && entityID == iDs.entityID;
    }
    
}