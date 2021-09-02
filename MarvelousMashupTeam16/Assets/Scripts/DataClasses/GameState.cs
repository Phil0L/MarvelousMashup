using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

// ReSharper disable once CheckNamespace
public class GameState
{
    public GameState()
    {
        Map map = MapConfigStore.Map();
        _arr = new GameField[map.width, map.height];
        _subscriptions = new List<Action<UserRequest>>();
        for (int x = 0; x < map.width; x++)
        {
            for (int y = 0; y < map.height; y++)
            {
                var field = new GameField { tile = map.scenario[x, y], tileData = Game.Controller().GroundLoader.defaultRockHealth };
                this[x, y] = field;
            }
        }
    }
    
    private readonly GameField[,] _arr;
    private readonly List<Action<UserRequest>> _subscriptions;


    private IDs _turnsCharacter;
    
    public GameField this[int i, int j]
    {
        get => _arr[i,j];
        private set => _arr[i,j] = value;
    }

    public void Subscribe(Action<UserRequest> callback) => _subscriptions.Add(callback);

    public int Height() => _arr.GetLength(1);
    
    public int Width() => _arr.GetLength(0);

    public bool IsOutOfBounds(Vector2Int position) => position.x < 0 || position.y < 0 || position.x >= Width() || position.y >= Height();

    public Character FindHero(IDs characterID)
    {
        foreach (GameField gameField in _arr)
        {
            if (gameField.item is Character item && item.characterID.Equals(characterID))
                return item;
        }
        return null;
    }

    public InfinityStone FindInfinityStone(int stone)
    {
        foreach (var car in CurrentCharactersDetail().ToArray())
        {
            foreach (var inf in car.infinityStones)
            {
                if (inf.stone == stone) return inf;
            }
        }
        foreach (GameField gameField in _arr)
        {
            if (gameField.item is InfinityStone item && item.stone == stone) return item;
        }
        return null;
    }
    
    public Vector2Int FindHeroPosition(IDs characterID)
    {
        for (int x = 0; x < Width(); x++)
        {
            for (int y = 0; y < Height(); y++)
            {
                if (_arr[x, y].item is Character item && item.characterID.Equals(characterID))
                    return new Vector2Int(x, y);
            }
        }
        return new Vector2Int(-1, -1);
    }

    public Transform TransformOf(String character) =>
        Game.Controller().GroundLoader.tilemap.transform.Find(character);

    public Transform TransformOf(InfinityStone infinityStone) =>
        Game.Controller().GroundLoader.tilemap.transform.Find(infinityStone.ToString());

    public void SummonHero(Character character, Vector2Int position, Action callback = null)
    {
        GameField field = this[position.x, position.y];
        if (field.IsEmpty())
        {
            var controller = Game.Controller().CharacterLoader.CharacterToPosition(character.characterID, position,
                    CharacterLoader.AnimationType.Drop, callback);
            controller.character = character;
            this[position.x, position.y].item = character;
        }
    }
    
    public void SummonInfinityStone(InfinityStone infinityStone, Vector2Int position, Action callback = null)
    {
        GameField field = this[position.x, position.y];
        if (field.IsEmpty())
        {
            Game.Controller().InfinityStoneLoader.InfinityStoneToPosition(infinityStone, position,
                InfinityStoneLoader.AnimationType.Drop, callback);
            this[position.x, position.y].item = infinityStone;
        }
    }

    public void MoveHero(IDs characterID, Vector2Int position, Action callback = null)
    {
        var heroPosition = FindHeroPosition(characterID);
        GameField previous = this[heroPosition.x, heroPosition.y];
        if (previous.item is Character characterItem && characterItem.characterID.Equals(characterID))
        {
            GameField field = this[position.x, position.y];
            if (field.IsWalkable())
            {
                Game.Controller().CharacterLoader.CharacterToPosition(characterID, position,
                    CharacterLoader.AnimationType.Move, callback);
                this[position.x, position.y].item = characterItem;
                this[heroPosition.x, heroPosition.y].item = null;
            }
        }
    }

    public void TeleportHero(IDs characterID, Vector2Int position, Action callback = null)
    {
        var heroPosition = FindHeroPosition(characterID);
        GameField previous = this[heroPosition.x, heroPosition.y];
        if (previous.item is Character characterItem && characterItem.characterID == characterID)
        {
            GameField field = this[position.x, position.y];
            if (field.IsWalkable())
            {
                Game.Controller().CharacterLoader.CharacterToPosition(characterID, position,
                    CharacterLoader.AnimationType.Drop, callback);
                this[position.x, position.y].item = characterItem;
                this[heroPosition.x, heroPosition.y].item = null;
            }
        }
    }

    public void SetCurrentTurn(IDs characterID) => _turnsCharacter = characterID;

    public IDs CurrentTurn() => _turnsCharacter;

    /**
	 * Lists all Characters which are in the Grid.
	*/
    public List<Character> CurrentCharactersDetail()
    {
        var chars = new List<Character>();
        foreach (var field in _arr)
        {
            if (field.item is Character ch)
            {
                chars.Add(ch);
            }
        }

        return chars;
    }

    public void AttackLongRange(Character attacker, Vector2Int attackerPosition, IFieldContent attacked, Vector2Int attackedPosition, int damage, Action callback = null)
    {
        Game.Controller().ArrowDispenser.SummonArrow(attackerPosition, attackedPosition, () =>
        {
            AttackIndicator.Summon(attackedPosition);
            if (attacked is Character c)
                c.HP -= damage;
            else
            {
                Game.State()[attackedPosition.x, attackedPosition.y].tileData -= damage;
                if (Game.State()[attackedPosition.x, attackedPosition.y].tileData <= 0)
                    Game.State()[attackedPosition.x, attackedPosition.y].tile = MapTile.GRASS;
                Game.Controller().GroundLoader.UpdateTile(attackedPosition);
            }

            
            attacker.AP -= 1;
            callback?.Invoke();
        });
    }

    public void AttackCloseRange(Character attacker, Vector2Int attackerPosition, IFieldContent attacked, Vector2Int attackedPosition, int damage, Action callback = null)
    {
        AttackIndicator.Summon(attackedPosition);
        if (attacked is Character c)
            c.HP -= damage;
        else
        {
            Game.State()[attackedPosition.x, attackedPosition.y].tileData -= damage;
            if (Game.State()[attackedPosition.x, attackedPosition.y].tileData <= 0)
                Game.State()[attackedPosition.x, attackedPosition.y].tile = MapTile.GRASS;
            Game.Controller().GroundLoader.UpdateTile(attackedPosition);
        }
        attacker.AP -= 1;
        callback?.Invoke();
    }

    public List<Character> YourCharacters() => CurrentCharactersDetail().Where(c => !c.enemy).ToList();
    
    public List<Character> OpponentCharacters() => CurrentCharactersDetail().Where(c => c.enemy).ToList();

    public string YourName() => PartyConfigStore.You();
    
    public string OpponentName() => PartyConfigStore.Opponent();
    
    public static class SubscriptionCaller
    {
        public static void CallAllSubscriptions(UserRequest request)
        {
            GameState gameState = Game.State();
            foreach (var sub in gameState._subscriptions)
            {
                sub(request);
            }
        }
    }
    
}
