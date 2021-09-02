using System;
using UnityEngine;
using UnityEngine.UI;

public class CurrentTurnInfo : MonoBehaviour
{
    public HealthDisplayerUI HealthDisplayer;
    public Image Sprite;

    public CharacterMultipleAction Move;
    public CharacterMultipleAction CloseRange;
    public CharacterMultipleAction LongRange;
    public CharacterMultipleAction StonePass;
    public CharacterMultipleAction NextCharacter;

    public CharacterTimeoutAction Red;
    public CharacterTimeoutAction Orange;
    public CharacterTimeoutAction Yellow;
    public CharacterTimeoutAction Green;
    public CharacterTimeoutAction Blue;
    public CharacterTimeoutAction Violet;

    private void Start()
    {
        Move.OnClick(() =>
        {
            Game.Controller().PathDisplayer.Activate();
            Game.Controller().PathDisplayer.OnSelected(pos =>
            {
                Game.Controller().PathDisplayer.Deactivate();
                CharacterMoveRequest cmr = new CharacterMoveRequest(
                    Game.State().CurrentTurn(),
                    Game.State().FindHeroPosition(Game.State().CurrentTurn()),
                    pos,
                    Game.Controller().Pathfinding.GetPath());
                GameState.SubscriptionCaller.CallAllSubscriptions(cmr);
            });
        });
        CloseRange.OnClick(() =>
        {
            Game.Controller().AttackDisplayer.SetMaxLength(1);
            Game.Controller().AttackDisplayer.SetColor(Game.Controller().AttackDisplayer.attackColor);
            Game.Controller().AttackDisplayer.Activate();
            Game.Controller().AttackDisplayer.OnSelected(pos =>
            {
                Game.Controller().AttackDisplayer.Deactivate();
                CloseRangeAttackRequest crar = new CloseRangeAttackRequest(
                    Game.State().CurrentTurn(),
                    pos,
                    Game.State()[pos.x,pos.y].item);
                GameState.SubscriptionCaller.CallAllSubscriptions(crar);
            });
        });
        LongRange.OnClick(() =>
        {
            Game.Controller().AttackDisplayer.SetMaxLength(CharacterConfigStore.Character(Game.State().CurrentTurn()).rangeCombatReach);
            Game.Controller().AttackDisplayer.SetColor(Game.Controller().AttackDisplayer.attackColor);
            Game.Controller().AttackDisplayer.Activate();
            Game.Controller().AttackDisplayer.OnSelected(pos =>
            {
                Game.Controller().AttackDisplayer.Deactivate();
                LongRangeAttackRequest lrar = new LongRangeAttackRequest(
                    Game.State().CurrentTurn(),
                    pos,
                    Game.State()[pos.x,pos.y].item);
                GameState.SubscriptionCaller.CallAllSubscriptions(lrar);
            });
        });
        StonePass.OnClick(() =>
        {
            Game.Controller().InfinityStonePassDisplayer.Activate();
            Game.Controller().InfinityStonePassDisplayer.OnSelected((pos, stone) =>
            {
                Game.Controller().InfinityStonePassDisplayer.Deactivate();
                StonePassRequest spr = new StonePassRequest(
                    Game.State().FindInfinityStone(stone),
                    Game.State().CurrentTurn(),
                    Game.State()[pos.x, pos.y].item as Character,
                    pos,
                    Game.State().FindHeroPosition(Game.State().CurrentTurn()));
                GameState.SubscriptionCaller.CallAllSubscriptions(spr);
            });
            
        });
        NextCharacter.OnClick(() =>
        {
            EndTurnRequest etr = new EndTurnRequest(Game.State().CurrentTurn());
            GameState.SubscriptionCaller.CallAllSubscriptions(etr);
        });
        Red.OnClick(() =>
        {
            Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
            {
                Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                RealityStoneRequest rsr = new RealityStoneRequest(
                    GetStone(Game.State().CurrentTurn(), InfinityStone.RED),
                    Game.State().CurrentTurn(),
                    pos);
                GameState.SubscriptionCaller.CallAllSubscriptions(rsr);
            });
            Game.Controller().InfinityStoneActionDisplayer.Red();
        });
        Orange.OnClick(() =>
        {
            Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
            {
                Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                SoulStoneRequest ssr = new SoulStoneRequest(
                    GetStone(Game.State().CurrentTurn(), InfinityStone.ORANGE),
                    Game.State().CurrentTurn(),
                    Game.State()[pos.x, pos.y].item as Character,
                    pos);
                GameState.SubscriptionCaller.CallAllSubscriptions(ssr);
            });
            Game.Controller().InfinityStoneActionDisplayer.Orange();
        });
        Yellow.OnClick(() =>
        {
            Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
            {
                Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                MindStoneRequest msr = new MindStoneRequest(
                    GetStone(Game.State().CurrentTurn(), InfinityStone.YELLOW),
                    Game.State().CurrentTurn(),
                    pos,
                    Game.State()[pos.x, pos.y].item);
                GameState.SubscriptionCaller.CallAllSubscriptions(msr);
            });
            Game.Controller().InfinityStoneActionDisplayer.Yellow();
        });
        Green.OnClick(() =>
        {
            Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
            {
                Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                TimeStoneRequest tsr = new TimeStoneRequest(
                    GetStone(Game.State().CurrentTurn(), InfinityStone.GREEN),
                    Game.State().CurrentTurn());
                GameState.SubscriptionCaller.CallAllSubscriptions(tsr);
            });
            Game.Controller().InfinityStoneActionDisplayer.Green();
        });
        Blue.OnClick(() =>
        {
            Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
            {
                Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                SpaceStoneRequest ssr = new SpaceStoneRequest(
                    GetStone(Game.State().CurrentTurn(), InfinityStone.BLUE),
                    Game.State().CurrentTurn(),
                    pos);
                GameState.SubscriptionCaller.CallAllSubscriptions(ssr);
            });
            Game.Controller().InfinityStoneActionDisplayer.Blue();
        });
        Violet.OnClick(() =>
        {
            Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
            {
                Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                PowerStoneRequest psr = new PowerStoneRequest(
                    GetStone(Game.State().CurrentTurn(), InfinityStone.PURPLE),
                    Game.State().CurrentTurn(),
                    pos,
                    Game.State()[pos.x, pos.y].item as Character);
            });
            Game.Controller().InfinityStoneActionDisplayer.Violet();
        });
    }

    private void Update()
    {
        if (Game.State().CurrentTurn() == null) return;
        IDs characterID = Game.State().CurrentTurn();
        Character character = CharacterConfigStore.Character(characterID);
        
        HealthDisplayer.SetHealth(character.HP, character.maxHP);
        Sprite.sprite = Game.Controller().CharacterLoader.GetSprite(character.characterID);
        
        Move.Set(character.MP, !character.enemy);
        CloseRange.Set(character.AP, !character.enemy);
        LongRange.Set(character.AP, !character.enemy);
        if (character.infinityStones.Count == 0) StonePass.gameObject.SetActive(false);
        else StonePass.gameObject.SetActive(true);
        StonePass.Set(character.infinityStones.Count, !character.enemy);
        NextCharacter.Set(1, !character.enemy);
        
        if (HasStone(characterID, InfinityStone.RED))
        {
            Red.gameObject.SetActive(true);
            Red.Set(GetStone(characterID, InfinityStone.RED).cooldown, !character.enemy);
        }
        else Red.gameObject.SetActive(false);
        
        if (HasStone(characterID, InfinityStone.ORANGE))
        {
            Orange.gameObject.SetActive(true);
            Orange.Set(GetStone(characterID, InfinityStone.ORANGE).cooldown, !character.enemy);
        }
        else Orange.gameObject.SetActive(false);
        
        if (HasStone(characterID, InfinityStone.YELLOW))
        {
            Yellow.gameObject.SetActive(true);
            Yellow.Set(GetStone(characterID, InfinityStone.YELLOW).cooldown, !character.enemy);
        }
        else Yellow.gameObject.SetActive(false);
        
        if (HasStone(characterID, InfinityStone.GREEN))
        {
            Green.gameObject.SetActive(true);
            Green.Set(GetStone(characterID, InfinityStone.GREEN).cooldown, !character.enemy);
        }
        else Green.gameObject.SetActive(false);
        
        if (HasStone(characterID, InfinityStone.BLUE))
        {
            Blue.gameObject.SetActive(true);
            Blue.Set(GetStone(characterID, InfinityStone.BLUE).cooldown, !character.enemy);
        }
        else Blue.gameObject.SetActive(false);
        
        if (HasStone(characterID, InfinityStone.PURPLE))
        {
            Violet.gameObject.SetActive(true);
            Violet.Set(GetStone(characterID, InfinityStone.PURPLE).cooldown, !character.enemy);
        }
        else Violet.gameObject.SetActive(false);
        
    }

    private static bool HasStone(IDs characterID, int stone)
    {
        foreach (var ist in CharacterConfigStore.Character(characterID).infinityStones)
        {
            if (ist.stone == stone) return true;
        }
        return false;
    }
    
    private static InfinityStone GetStone(IDs characterID, int stone)
    {
        foreach (var ist in CharacterConfigStore.Character(characterID).infinityStones)
        {
            if (ist.stone == stone) return ist;
        }
        return null;
    }
}
