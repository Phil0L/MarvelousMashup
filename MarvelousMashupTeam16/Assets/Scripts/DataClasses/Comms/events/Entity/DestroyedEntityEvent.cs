public class DestroyedEntityEvent : Message, EntityEvent
{
    public int[] targetField;
    public IDs targetEntity;

    /**
     * the constructor of the DestroyEntityEvent-Class. Creates a DestroyEntityEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetField The field on the map where the entity was placed before being destroyed
     * @param targetEntity The entity that is about to be destroyed
     */
    public DestroyedEntityEvent(int[] targetField, IDs targetEntity) : base(EventType.DestroyedEntityEvent)
    {
        this.targetEntity = targetEntity;
        this.targetField = targetField;
    }

    public void Execute()
    {
        switch (targetEntity.entityID)
        {
            case EntityID.P1:
            case EntityID.P2:
            case EntityID.NPC:
                Character character = IDTracker.Get(targetEntity) as Character;
                if (character != null) Game.State().DestroyObject(character);
                break;
            case EntityID.Rocks:
                Game.State()[targetField[1], targetField[0]].tile = MapTile.GRASS;
                break;
            case EntityID.InfinityStones:
                InfinityStone infinityStone = IDTracker.Get(targetEntity) as InfinityStone;
                if (infinityStone != null) Game.State().DestroyObject(infinityStone);
                break;
        }
        IDTracker.Remove(targetEntity);
    }
}