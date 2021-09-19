/**
 *  Message-Class, which is responsible to send UseInfinityStoneRequests to the server
 *  if the user wants one of his characters to use his or her Infinity Stone
 *
 *  @author Sarah Engele
 *
 */
public class UseInfinityStoneEvent : Message, CharacterEvent
{
    /**
     * The entity that wants to use the Infinity Stone stoneType and owns this specific Infinity Stone
     */
    public IDs originEntity;

    /**
     * The entity the stone will be used against. Some stones do not need a targetEntity. If so the targetEntity is
     * equal to the originEntity. example: The Reality stone wants to create a new rock: originEntity = targetEntity
     */
    public IDs targetEntity;

    /**
     * The position of the originEntity on the game field
     */
    public int[] originField;

    /**
     * The position of the targetEntity on the game field
     */
    public int[] targetField;

    /**
     * The Infinity Stone that is about to be used
     */
    public IDs stoneType;

    /**
     *
     * the constructor of the UseInfinityStoneEvent-Class.
     * Creates a UseInfinityStoneEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param originEntity The entity that wants to use the Infinity Stone stoneType and owns this specific Infinity Stone
     * @param targetEntity The entity the stone will be used against. Some stones do not need a targetEntity.
     *                     If so the targetEntity is equal to the originEntity.
     *                     example: The Reality stone wants to create a new rock: originEntity = targetEntity
     * @param originField The position of the originEntity on the game field
     * @param targetField The position of the targetEntity on the game field
     * @param stoneType The Infinity Stone that is about to be used
     *
     */
    public UseInfinityStoneEvent(IDs originEntity, IDs targetEntity, int[] originField, int[] targetField,
        IDs stoneType) : base(EventType.UseInfinityStoneEvent)
    {
        this.originEntity = originEntity;
        this.targetEntity = targetEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.stoneType = stoneType;
    }

    public void Execute()
    {
        InfinityStone infinityStone = IDTracker.Get(stoneType) as InfinityStone;
        if (infinityStone == null) return;

        Character origin = IDTracker.Get(originEntity) as Character;
        if (origin == null) return;

        Character target = IDTracker.Get(targetEntity) as Character;
        if (target == null) return;

        switch (infinityStone.stone)
        {
            case InfinityStone.RED:
                var pos = targetField.ToVector();
                if (Game.State()[pos.x, pos.y].tile == MapTile.ROCK)
                    Game.State()[pos.x, pos.y].tile = MapTile.GRASS;
                else if (Game.State()[pos.x, pos.y].tile == MapTile.GRASS)
                    Game.State()[pos.x, pos.y].tile = MapTile.ROCK;
                Game.Controller().GroundLoader.UpdateTile(pos);
                break;
            case InfinityStone.BLUE:
                Game.State().TeleportHero(origin.characterID, targetField.ToVector());
                break;
            case InfinityStone.YELLOW:
                Game.State().AttackLongRange(
                    origin,
                    originField.ToVector(),
                    Game.State()[originField[1], originField[0]].item,
                    originField.ToVector(),
                    origin.rangeCombatDamage * 2);
                break;
            case InfinityStone.GREEN:
                origin.AP = origin.maxAP;
                origin.MP = origin.maxMP;
                break;
            case InfinityStone.ORANGE:
                origin.HP = origin.maxHP;
                break;
            case InfinityStone.PURPLE:
                Game.State().AttackCloseRange(
                    origin,
                    originField.ToVector(),
                    Game.State()[originField[1], originField[0]].item,
                    targetField.ToVector(),
                    origin.meleeDamage * 2);
                break;
        }
        
        infinityStone.cooldown = infinityStone.defaultcooldownTime;
    }
}