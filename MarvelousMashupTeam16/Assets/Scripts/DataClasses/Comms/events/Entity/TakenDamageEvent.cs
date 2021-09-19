public class TakenDamageEvent : Message, EntityEvent
{
    /**
     * The Entity that is about to be done damage to
     */
    public IDs targetEntity;

    /**
     * The field where the targetEntity is placed on the map
     */
    public int[] targetField;

    /**
     * The amount of damage that is going to be done to the targetEntity
     */
    public int amount;


    /**
     * the constructor of the TakenDamageEvent-Class. Creates a TakenDamageEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param targetEntity The Entity that is about to be done damage to
     * @param targetField The field where the targetEntity is placed on the map
     * @param amount The amount of damage that is going to be done to the targetEntity
     */
    public TakenDamageEvent(IDs targetEntity, int[] targetField, int amount) : base(EventType.TakenDamageEvent)
    {
        this.targetEntity = targetEntity;
        this.targetField = targetField;
        this.amount = amount;
    }

    public void Execute()
    {
        // Due to the damage being applied after the animation of the attack finished, this is unnessecary
        
        /*switch (targetEntity.entityID)
        {
            case EntityID.P1:
            case EntityID.P2:
            case EntityID.NPC:
                Character character = IDTracker.Get(targetEntity) as Character;
                if (character != null)
                    character.HP -= amount;
                break;
            case EntityID.Rocks:
                Game.State()[targetField[1], targetField[0]].tileData -= amount;
                if (Game.State()[targetField[1], targetField[0]].tileData <= 0)
                    Game.State()[targetField[1], targetField[0]].tile = MapTile.GRASS;
                Game.Controller().GroundLoader.UpdateTile(targetField.ToVector());
                break;
        }*/
    }
}