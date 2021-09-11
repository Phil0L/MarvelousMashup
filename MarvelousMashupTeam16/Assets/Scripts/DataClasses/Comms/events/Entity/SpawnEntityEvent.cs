using UnityEngine;

public class SpawnEntityEvent : Message, EntityEvent
{
    /**
     * the entity that is about to be spawned on the map (e.g. Thanos or Goose, or Infinity Stones)
     */
    public Entities entity;

    /**
     * the constructor of the SpawnEntityEvent-Class. Creates a SpawnEntityEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param entity the entity that is about to be spawned on the map (e.g. Thanos or Goose, or Infinity Stones)
     */
    public SpawnEntityEvent(Entities entity) : base(EventType.SpawnEntityEvent){
        this.entity = entity;
    }

    public void Execute()
    {
        if (entity == null) return;
        EntityID eId = 0;
        int iId = entity.ID;
        IID obj = null;
        switch (entity.entityType)
        {
            case EntityType.Character:
                if (entity is InGameCharacter igc)
                {
                    eId = igc.PID == 1 ? EntityID.P1 : EntityID.P2;
                    var character = CharacterConfigStore.Character(igc.name);
                    Game.State().SummonHero(character, new Vector2Int(entity.position[1], entity.position[0]));
                    obj = character;
                }
                break;
            case EntityType.NPC:
                eId = EntityID.NPC;
                if (entity is NPC npc)
                {
                    if (npc.ID == 0) 
                    {
                        Character character = new Character(Character.Characters.Goose) {MP = npc.MP};
                        Game.State().SummonHero(character, new Vector2Int(entity.position[1], entity.position[0]));
                        obj = character;
                    }
                    if (npc.ID == 1) 
                    {
                        Character character = new Character(Character.Characters.StanLee);
                        Game.State().SummonHero(character, new Vector2Int(entity.position[1], entity.position[0]));
                        obj = character;
                    }
                    if (npc.ID == 2)
                    {
                        Character character = new Character(Character.Characters.Thanos);
                        Game.State().SummonHero(character, new Vector2Int(entity.position[1], entity.position[0]));
                        obj = character;
                    }
                }
                break;
            case EntityType.InfinityStone:
                eId = EntityID.InfinityStones;
                if (entity is InfinityStone inf)
                {
                    if (entity.ID == 0)
                        inf.stone = InfinityStone.BLUE;
                    if (entity.ID == 1)
                        inf.stone = InfinityStone.YELLOW;
                    if (entity.ID == 2)
                        inf.stone = InfinityStone.RED;
                    if (entity.ID == 3)
                        inf.stone = InfinityStone.PURPLE;
                    if (entity.ID == 4)
                        inf.stone = InfinityStone.GREEN;
                    if (entity.ID == 5)
                        inf.stone = InfinityStone.ORANGE;

                    inf.cooldown = PartyConfigStore.CooldownOf(inf.stone);
                    inf.defaultcooldownTime = PartyConfigStore.CooldownOf(inf.stone);
                    
                    Game.State().SummonInfinityStone(inf, new Vector2Int(entity.position[1], entity.position[0]));
                }
                break;
            case EntityType.Rock:
                eId = EntityID.Rocks;
                obj = entity as Rock;
                Game.State()[entity.position[1], entity.position[0]].tile = MapTile.ROCK;
                Game.State()[entity.position[1], entity.position[0]].item = (IFieldContent) obj;
                Game.State()[entity.position[1], entity.position[0]].tileData = ((Rock) entity).HP;
                Game.Controller().GroundLoader.UpdateTile(entity.position.ToVector());
                break;
            //TODO: add portal
        }
        IDs id = new IDs(eId, iId);
        if (obj != null) IDTracker.Add(id, obj);
    }
}