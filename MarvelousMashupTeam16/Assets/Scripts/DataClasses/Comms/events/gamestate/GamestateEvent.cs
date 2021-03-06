using System;
using System.Collections.Generic;
using UnityEngine;

/**
 *  Message-Class, which is sent at the end of EACH AND EVERY message from the server to inform
 *  the client about the current GameState on the map
 *
 *  @author Sarah Engele
 *
 */
public class GamestateEvent : Message
{
    /**
     * describes where Characters, Rocks and Infinity Stones are placed on the map. If there is no Entity on a field,
     * the field is a GRASS field
     */
    public Entities[] entities;

    /**
     * describes how big the map is (x-axis and y-axis)
     */
    public int[] mapSize;

    /**
     * describes in which order the different Characters on the map can make their moves. It does not matter which team
     * they belong to. The order is decided by the server
     */
    public IDs[] turnOrder;

    /**
     * points out which character is allowed to make a move
     */
    public IDs activeCharacter;

    //The stoneCooldowns array has 6 entries (Space, Mind, Reality, Power, Time, Soul)
    /**
     * describes which Infinity Stones are currently not able to be used since they  have to cooldown first
     */
    public int[] stoneCooldowns;

    /**
     * checks if there is a winner (true if there is a winner, false if not)
     */
    public bool winCondition;

    /**
     *
     * the constructor of the GamestateEvent-Class.
     * Creates a GamestateEvent-MessageObject.
     *
     * @author Sarah Engele
     *
     * @param entities describes where Characters, Rocks and Infinity Stones are placed on the map.
     *                 If there is no Entity on a field, the field is a GRASS field
     * @param mapSize describes how big the map is (x-axis and y-axis)
     * @param turnOrder describes in which order the different Characters on the map can make their
     *                  moves. It does not matter which team they belong to. The order is decided by the
     *                  server
     * @param activeCharacter points out which character is allowed to make a move
     * @param stoneCooldowns describes which Infinity Stones are currently not able to be used since they
     *                       have to cooldown first
     * @param winCondition checks if there is a winner (true if there is a winner, false if not)
     */
    public GamestateEvent(Entities[] entities, int[] mapSize, IDs[] turnOrder, IDs activeCharacter,
        int[] stoneCooldowns, bool winCondition) : base(EventType.GamestateEvent)
    {
        this.entities = entities;
        this.mapSize = mapSize;
        this.turnOrder = turnOrder;
        this.activeCharacter = activeCharacter;
        this.stoneCooldowns = stoneCooldowns;
        this.winCondition = winCondition;
    }

    /**
     * Compares two GamestateEvent objects with each other
     * @param o a Message-Object that is going to be compared to the Message
     * @return
     */
    public override bool Equals(object o)
    {
        if (this == o) return true;
        if (!(o is GamestateEvent)) return false;
        GamestateEvent that = (GamestateEvent) o;
        return winCondition == that.winCondition && Array.Equals(entities, that.entities) &&
               Array.Equals(mapSize, that.mapSize) && Array.Equals(turnOrder, that.turnOrder) &&
               activeCharacter.Equals(that.activeCharacter) && Array.Equals(stoneCooldowns, that.stoneCooldowns);
    }

    public void Execute()
    {
        //TODO: parse entities (optional (theoretical)) (infinitystones are getting parsed)
        foreach (var entity in entities)
        {
            if (entity is InGameCharacter chara)
            {
                var character = Game.State().FindHero(chara.name);
                foreach (var stoneInt in chara.stones)
                {
                    var inf = IDTracker.Get(new IDs(EntityID.InfinityStones, stoneInt)) as InfinityStone;
                    if (!character.infinityStones.Contains(inf))
                        character.infinityStones.Add(inf);
                }
            }
        }

        //TODO: parse mapsize (optional (theoretical))
        //TODO: parse turnorder

        // current turn
        Character currentTurnCharacter = IDTracker.Get(activeCharacter) as Character;
        if (currentTurnCharacter != null) Game.State().SetCurrentTurn(currentTurnCharacter);
        else Debug.LogWarning("Current character was not been able to determine!");

        // infinity stone cooldowns
        int i = 0;
        foreach (var cd in stoneCooldowns)
        {
            int stone = 0;
            if (i == 0) stone = InfinityStone.BLUE;
            if (i == 1) stone = InfinityStone.YELLOW;
            if (i == 2) stone = InfinityStone.RED;
            if (i == 3) stone = InfinityStone.PURPLE;
            if (i == 4) stone = InfinityStone.GREEN;
            if (i == 5) stone = InfinityStone.ORANGE;

            var infinityStone = Game.State().FindInfinityStone(stone);
            if (infinityStone != null)
                infinityStone.cooldown = cd;

            i++;
        }
        //TODO: parse wincondition (optional (theoretical))
    }
}