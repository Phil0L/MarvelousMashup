using System;
using UnityEngine;
using UnityEngine.Tilemaps;

public class InfinityStonePassDisplayer : MonoBehaviour
{
    public LineRenderer LineRenderer;
    public Transform highlighter;
    public bool active;
    public bool hasStoneSelected;
    public int selectedStone;
    
    public Color yellow, red, green, blue, orange, violet;

    private Action<Vector2Int, int> callback;
    private TileMarker _tileMarker;
    private Vector2Int lastPosition;

    
    public void Deactivate()
    {
        active = false;
        callback = null;
        LineRenderer.positionCount = 0;
        if (_tileMarker) Destroy(_tileMarker.gameObject);
        lastPosition = new Vector2Int(-1, -1);
        highlighter.gameObject.SetActive(false);
        if (Game.Controller().AttackDisplayer.active)
            Game.Controller().AttackDisplayer.Deactivate();
        Game.Controller().CurrentTurnInfo.Blue.ClearOverride();
        Game.Controller().CurrentTurnInfo.Red.ClearOverride();
        Game.Controller().CurrentTurnInfo.Yellow.ClearOverride();
        Game.Controller().CurrentTurnInfo.Orange.ClearOverride();
        Game.Controller().CurrentTurnInfo.Green.ClearOverride();
        Game.Controller().CurrentTurnInfo.Violet.ClearOverride();
    }

    
    public void Activate()
    {
        hasStoneSelected = false;
        active = true;
        lastPosition = new Vector2Int(-1, -1);
        highlighter.gameObject.SetActive(true);
        Game.Controller().CurrentTurnInfo.Blue.Override(() => StoneSelected(InfinityStone.BLUE));
        Game.Controller().CurrentTurnInfo.Red.Override(() => StoneSelected(InfinityStone.RED));
        Game.Controller().CurrentTurnInfo.Yellow.Override(() => StoneSelected(InfinityStone.YELLOW));
        Game.Controller().CurrentTurnInfo.Orange.Override(() => StoneSelected(InfinityStone.ORANGE));
        Game.Controller().CurrentTurnInfo.Green.Override(() => StoneSelected(InfinityStone.GREEN));
        Game.Controller().CurrentTurnInfo.Violet.Override(() => StoneSelected(InfinityStone.PURPLE));
        
    }

    private void StoneSelected(int stone)
    {
        Debug.Log("Selected Infinity Stone for passing");
        hasStoneSelected = true;
        selectedStone = stone;
        highlighter.gameObject.SetActive(false);
        
        Game.Controller().CurrentTurnInfo.Blue.ClearOverride();
        Game.Controller().CurrentTurnInfo.Red.ClearOverride();
        Game.Controller().CurrentTurnInfo.Yellow.ClearOverride();
        Game.Controller().CurrentTurnInfo.Orange.ClearOverride();
        Game.Controller().CurrentTurnInfo.Green.ClearOverride();
        Game.Controller().CurrentTurnInfo.Violet.ClearOverride();
        
        Game.Controller().AttackDisplayer.SetMaxLength(1);
        Game.Controller().AttackDisplayer.SetColor(GetColor(stone));
        Game.Controller().AttackDisplayer.Activate();
        Game.Controller().AttackDisplayer.FriendlyFire();
        Game.Controller().AttackDisplayer.OnSelected(position =>
        {
            callback(position, stone);
            Game.Controller().AttackDisplayer.Deactivate();
            Deactivate();
        });
    }
    
    public void OnSelected(Action<Vector2Int, int> callback) => this.callback = callback;
    
    public bool Confirmable()
    {
        if (!active) return true;
        return lastPosition.x != -1;
    }
    
    private Color GetColor(int stone)
    {
        if (stone == InfinityStone.RED) return red;
        if (stone == InfinityStone.BLUE) return blue;
        if (stone == InfinityStone.YELLOW) return yellow;
        if (stone == InfinityStone.GREEN) return green;
        if (stone == InfinityStone.ORANGE) return orange;
        if (stone == InfinityStone.PURPLE) return violet;
        return Color.black;
    }
}