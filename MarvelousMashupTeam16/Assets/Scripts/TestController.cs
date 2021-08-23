using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;
using UnityEngine.PlayerLoop;
using UnityEngine.Tilemaps;
using Random = System.Random;

public class TestController : MonoBehaviour
{
    public List<Tuple<Func<Vector2Int, bool>, Action<Vector2Int>>> listener =
        new List<Tuple<Func<Vector2Int, bool>, Action<Vector2Int>>>();

    private void Start()
    {
        Game.State().Subscribe(request => Debug.Log($"Client issued {request.GetType().Name}"));
    }

    public void OnGridSelected(Func<Vector2Int, bool> condition, Action<Vector2Int> callback)
    {
        condition ??= field => true;
        listener.Add(new Tuple<Func<Vector2Int, bool>, Action<Vector2Int>>(condition, callback));
    }

    private void Update()
    {
        if (Input.GetMouseButtonDown(0))
        {
            Tilemap tm = Game.Controller().GroundLoader.tilemap;
            Vector3 pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
            Vector3Int point3 = tm.WorldToCell(pos);
            Vector2Int point2 = new Vector2Int(point3.x, point3.y);
            foreach (var lis in listener.ToArray())
            {
                if (lis.Item1(point2))
                {
                    lis.Item2(point2);
                    listener.Remove(lis);
                }
            }
        }
    }
}

#if UNITY_EDITOR
[CustomEditor(typeof(TestController))]
public class TestControllerEditor : Editor
{
    public Character.Characters characterSummon;
    public int stoneSummon;
    public Character.Characters characterTurn;

    private int _characterTurnI;

    public override void OnInspectorGUI()
    {
        DrawDefaultInspector();
        if (!Game.IsInstantiated())
            return;
        TestController controller = (TestController) target;

        GUILayout.BeginHorizontal();
        characterSummon = (Character.Characters) EditorGUILayout.EnumPopup("Summon Character", characterSummon);
        if (GUILayout.Button("Summon"))
        {
            controller.OnGridSelected(
                pos => !Game.State().IsOutOfBounds(pos) && Game.State()[pos.x, pos.y].IsWalkable(),
                pos =>
                {
                    Game.State().SummonHero(new Character(characterSummon) {name = characterSummon.ToString()}, pos);
                });
        }

        GUILayout.EndHorizontal();

        GUILayout.BeginHorizontal();
        stoneSummon = EditorGUILayout.Popup("Summon IS", stoneSummon,
            new[] {"Green", "Yellow", "Purple", "Blue", "Red", "Orange"});
        if (GUILayout.Button("Summon"))
        {
            controller.OnGridSelected(
                pos => !Game.State().IsOutOfBounds(pos) && Game.State()[pos.x, pos.y].IsWalkable(),
                pos => { Game.State().SummonInfinityStone(new InfinityStone {stone = stoneSummon + 1}, pos); });
        }

        GUILayout.EndHorizontal();

        GUILayout.BeginHorizontal();
        var allCharacters = Game.State().CurrentCharacters();
        if (allCharacters.Count != 0)
        {
            _characterTurnI = EditorGUILayout.Popup("Set current Turn", _characterTurnI,
                Game.State().CurrentCharacters().Select(c => c.ToString()).ToArray());
            characterTurn = Game.State().CurrentCharacters()[_characterTurnI];
            if (GUILayout.Button("Set"))
            {
                Game.State().SetCurrentTurn(Game.State().FindHero(characterTurn));
            }
        }

        GUILayout.EndHorizontal();

        if (Game.State().CurrentTurn() != null)
        {
            Character current = Game.State().CurrentTurn();
            GUILayout.Label(Game.State().CurrentTurn().name + ":");

            GUILayout.BeginHorizontal();
            GUILayout.Label("Enemy");
            current.enemy = GUILayout.Toggle(current.enemy, "");
            GUILayout.EndHorizontal();

            GUILayout.BeginHorizontal();
            GUILayout.Label("HP");
            current.HP = GUILayout.TextField(current.HP.ToString()).ToInt();
            current.maxHP = GUILayout.TextField(current.maxHP.ToString()).ToInt();
            GUILayout.EndHorizontal();

            GUILayout.BeginHorizontal();
            GUILayout.Label("MP");
            current.MP = GUILayout.TextField(current.MP.ToString()).ToInt();
            current.maxMP = GUILayout.TextField(current.maxMP.ToString()).ToInt();
            GUILayout.EndHorizontal();

            GUILayout.BeginHorizontal();
            GUILayout.Label("AP");
            current.AP = GUILayout.TextField(current.AP.ToString()).ToInt();
            current.maxAP = GUILayout.TextField(current.maxAP.ToString()).ToInt();
            GUILayout.EndHorizontal();

            GUILayout.BeginHorizontal();
            if (GUILayout.Button("Move"))
            {
                Game.Controller().PathDisplayer.Activate();
                Game.Controller().PathDisplayer.OnSelected(pos =>
                {
                    InfinityStone infinityStone = null;
                    if (Game.State()[pos.x, pos.y].item is InfinityStone inf)
                    {
                        infinityStone = inf;
                    }

                    Game.State().MoveHero(Game.State().CurrentTurn().characterID, pos, () =>
                    {
                        if (infinityStone != null)
                        {
                            Game.State().CurrentTurn().infinityStones.Add(infinityStone);
                            Destroy(Game.State().TransformOf(infinityStone).gameObject);
                        }
                    });
                    Game.Controller().PathDisplayer.Deactivate();
                });
            }

            if (GUILayout.Button("LR Attack"))
            {
                Game.Controller().AttackDisplayer.SetMaxLength(1000);
                Game.Controller().AttackDisplayer.SetColor(Game.Controller().AttackDisplayer.attackColor);
                Game.Controller().AttackDisplayer.Activate();
                Game.Controller().AttackDisplayer.OnSelected(pos =>
                {
                    Game.Controller().AttackDisplayer.Deactivate();
                    Game.State().AttackLongRange(
                        Game.State().CurrentTurn(),
                        Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID),
                        Game.State()[pos.x, pos.y].item,
                        pos,
                        20);
                });
            }

            if (GUILayout.Button("CR Attack"))
            {
                Game.Controller().AttackDisplayer.SetMaxLength(1);
                Game.Controller().AttackDisplayer.SetColor(Game.Controller().AttackDisplayer.attackColor);
                Game.Controller().AttackDisplayer.Activate();
                Game.Controller().AttackDisplayer.OnSelected(pos =>
                {
                    Game.Controller().AttackDisplayer.Deactivate();
                    Game.State().AttackCloseRange(
                        Game.State().CurrentTurn(),
                        Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID),
                        Game.State()[pos.x, pos.y].item,
                        pos,
                        20);
                });
            }

            GUILayout.EndHorizontal();

            GUILayout.BeginHorizontal();
            if (GUILayout.Button("Blue"))
            {
                Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
                {
                    Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                    Game.State().TeleportHero(Game.State().CurrentTurn().characterID, pos);
                });
                Game.Controller().InfinityStoneActionDisplayer.Blue();
            }

            if (GUILayout.Button("Yellow"))
            {
                Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
                {
                    Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                    Game.State().AttackLongRange(
                        Game.State().CurrentTurn(),
                        Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID),
                        Game.State()[pos.x, pos.y].item,
                        pos,
                        20 * 2);
                });
                Game.Controller().InfinityStoneActionDisplayer.Yellow();
            }

            if (GUILayout.Button("Red"))
            {
                Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
                {
                    Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                    if (Game.State()[pos.x, pos.y].tile == MapTile.ROCK)
                        Game.State()[pos.x, pos.y].tile = MapTile.GRASS;
                    else if (Game.State()[pos.x, pos.y].tile == MapTile.GRASS)
                        Game.State()[pos.x, pos.y].tile = MapTile.ROCK;
                    Game.Controller().GroundLoader.UpdateTile(pos);
                });
                Game.Controller().InfinityStoneActionDisplayer.Red();
            }

            if (GUILayout.Button("Violet"))
            {
                Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
                {
                    Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                    Game.State().AttackCloseRange(
                        Game.State().CurrentTurn(),
                        Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID),
                        Game.State()[pos.x, pos.y].item,
                        pos,
                        20 * 2);
                });
                Game.Controller().InfinityStoneActionDisplayer.Violet();
            }

            if (GUILayout.Button("Green"))
            {
                Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
                {
                    Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                    Game.State().CurrentTurn().AP = Game.State().CurrentTurn().maxAP;
                    Game.State().CurrentTurn().MP = Game.State().CurrentTurn().maxMP;
                });
                Game.Controller().InfinityStoneActionDisplayer.Green();
            }

            if (GUILayout.Button("Orange"))
            {
                Game.Controller().InfinityStoneActionDisplayer.OnSelected(pos =>
                {
                    Game.Controller().InfinityStoneActionDisplayer.Deactivate();
                    var character = Game.State()[pos.x, pos.y].item as Character;
                    character.HP = character.maxHP;
                });
                Game.Controller().InfinityStoneActionDisplayer.Orange();
            }

            GUILayout.EndHorizontal();
        }

        if (GUILayout.Button("Create Random Game"))
        {
            _random = new Random();
            for (int i = 0; i < 12; i++)
            {
                var character = GetNewCharacter();
                character.enemy = i % 2 == 0;
                var position = GetEmptyField();
                Game.State().SummonHero(character, position);
            }

            for (int i = 0; i < 6; i++)
            {
                var infinityStone = GetNewInfinityStone(i);
                var position = GetEmptyField();
                Game.State().SummonInfinityStone(infinityStone, position);
            }
        }
    }

    private Random _random;
    private Character GetNewCharacter()
    {
        Array values = Enum.GetValues(typeof(Character.Characters));
        Character.Characters randomCharacter = (Character.Characters) values.GetValue(_random.Next(values.Length));
        if (randomCharacter == Character.Characters.Thanos ||
            randomCharacter == Character.Characters.Unassigned ||
            randomCharacter == Character.Characters.Goose ||
            randomCharacter == Character.Characters.StanLee || 
            randomCharacter == Character.Characters.Deadpool)
            return GetNewCharacter();
        foreach (var insChar in Game.State().CurrentCharacters().ToArray())
            if (insChar == randomCharacter) return GetNewCharacter();
        return new Character(randomCharacter)
        {
            HP = 100, maxHP = 100, AP = 3, maxAP = 3, MP = 10, maxMP = 10, 
            meleeDamage = 25, rangeCombatDamage = 10, rangeCombatReach = 5
        };
    }

    private InfinityStone GetNewInfinityStone(int i)
    {
        return new InfinityStone() {stone = i+1, cooldown = 0, defaultcooldownTime = 3};
    }

    private Vector2Int GetEmptyField()
    {
        int x = _random.Next(Game.State().Height());
        int y = _random.Next(Game.State().Width());
        var gameField = Game.State()[x, y];
        if (gameField.IsEmpty()) return new Vector2Int(x, y);
        return GetEmptyField();
    }
}

public static class IntParse
{
    public static int ToInt(this string str)
    {
        if (str == "") return 0;
        var sb = new StringBuilder();
        foreach (var c in str)
        {
            if (char.IsDigit(c)) sb.Append(c);
        }

        if (sb.Length == 0) return 0;
        return int.Parse(sb.ToString());
    }
}
#endif