using System;
using UnityEngine;
using UnityEngine.Tilemaps;

public class InfinityStoneActionDisplayer : MonoBehaviour
{
    public LineRenderer LineRenderer;
    [Range(0,6)]
    public int active;
    
    public Color colorYellowStone;
    public Color colorVioletStone;
    public TileMarker tileMarkerPort;
    public TileMarker tileMarkerGrass;
    public TileMarker tileMarkerStone;
    public TileMarker tileMarkerRevive;

    private Action<Vector2Int> callback;
    private TileMarker _tileMarker;
    private int redMode;
    private Vector2Int lastPosition;

    
    public void Deactivate()
    {
        active = 0;
        callback = null;
        LineRenderer.positionCount = 0;
        if (_tileMarker) Destroy(_tileMarker.gameObject);
        redMode = 0;
        lastPosition = new Vector2Int(-1, -1);
        if (Game.Controller().AttackDisplayer.active)
            Game.Controller().AttackDisplayer.Deactivate();
    }

    public void Blue()
    {
        active = 1;
    }

    public void Yellow()
    {
        active = 2;
        Game.Controller().AttackDisplayer.SetMaxLength(Game.State().CurrentTurn().rangeCombatReach - 1);
        Game.Controller().AttackDisplayer.SetColor(colorYellowStone);
        Game.Controller().AttackDisplayer.Activate();
        Game.Controller().AttackDisplayer.OnSelected(position =>
        {
            callback(position);
            Game.Controller().AttackDisplayer.Deactivate();
            Deactivate();
        });
    }

    public void Red()
    {
        active = 3;
    }

    public void Violet()
    {
        active = 4;
        Game.Controller().AttackDisplayer.SetMaxLength(1);
        Game.Controller().AttackDisplayer.SetColor(colorVioletStone);
        Game.Controller().AttackDisplayer.Activate();
        Game.Controller().AttackDisplayer.OnSelected(position =>
        {
            callback(position);
            Game.Controller().AttackDisplayer.Deactivate();
            Deactivate();
        });
    }

    public void Green()
    {
        active = 5;
        callback(Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID));
        Deactivate();
    }

    public void Orange()
    {
        active = 6;
    }

    public void OnSelected(Action<Vector2Int> callback) => this.callback = callback;
    
    public bool Confirmable()
    {
        if (active == 0 || active == 2 || active == 4) return true;
        return lastPosition.x != -1;
    }


    private void Update()
    {
        // collecting all data

        Tilemap tm = Game.Controller().GroundLoader.tilemap;
        Vector3 mPos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        Vector3Int mPoint3 = tm.WorldToCell(mPos);
        mPoint3 = new Vector3Int(mPoint3.x, mPoint3.y, 0);
        Vector2Int mPoint2 = new Vector2Int(mPoint3.x, mPoint3.y);

        //general drawing and preview

        switch (active)
        {
            case 1:
                if (!Game.State().IsOutOfBounds(mPoint2) && Game.State()[mPoint2.x, mPoint2.y].IsEmpty())
                {
                    if (!_tileMarker) _tileMarker = Instantiate(tileMarkerPort);
                    if (_tileMarker) _tileMarker.SetPosition(mPoint2);
                    lastPosition = mPoint2;
                }
                break;
            
            case 3:
                if (!Game.State().IsOutOfBounds(mPoint2) 
                    && Game.State()[mPoint2.x, mPoint2.y].IsEmpty()
                    && Distance(Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID), mPoint2) == 1)
                {
                    if (redMode != 1)
                    {
                        var oldTile = _tileMarker;
                        _tileMarker = Instantiate(tileMarkerStone);
                        if (oldTile) Destroy(oldTile.gameObject);
                        redMode = 1;
                    }
                    if (_tileMarker) _tileMarker.SetPosition(mPoint2);
                    lastPosition = mPoint2;
                }
                if (!Game.State().IsOutOfBounds(mPoint2) && 
                    Game.State()[mPoint2.x, mPoint2.y].tile == MapTile.ROCK
                    && Distance(Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID), mPoint2) == 1)
                {
                    if (redMode != 2)
                    {
                        var oldTile = _tileMarker;
                        _tileMarker = Instantiate(tileMarkerGrass);
                        if (oldTile) Destroy(oldTile.gameObject);
                        redMode = 2;
                    }
                    if (_tileMarker) _tileMarker.SetPosition(mPoint2);
                    lastPosition = mPoint2;
                }
                break;
            
            case 6:
                if (!Game.State().IsOutOfBounds(mPoint2) 
                    && Game.State()[mPoint2.x, mPoint2.y].item is Character {HP: 0, enemy: false} 
                    && Distance(Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID), mPoint2) == 1)
                {
                    if (!_tileMarker) _tileMarker = Instantiate(tileMarkerRevive);
                    if (_tileMarker) _tileMarker.SetPosition(mPoint2);
                    lastPosition = mPoint2;
                }
                break;
        }

        // on click

        if (Input.GetMouseButtonDown(0))
        {
            switch (active)
            {
                case 1:
                    if (!Game.State().IsOutOfBounds(lastPosition) && Game.State()[lastPosition.x, lastPosition.y].IsEmpty())
                    {
                        callback(lastPosition);
                        Deactivate();
                    }
                    break;
                case 3:
                    if (!Game.State().IsOutOfBounds(lastPosition) 
                        && (Game.State()[lastPosition.x, lastPosition.y].IsEmpty() || Game.State()[lastPosition.x, lastPosition.y].tile == MapTile.ROCK) 
                        && Distance(Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID), lastPosition) == 1)
                    {
                        callback(lastPosition);
                        Deactivate();
                    }
                    break;
                case 6:
                    if (!Game.State().IsOutOfBounds(lastPosition) 
                        && Game.State()[lastPosition.x, lastPosition.y].item is Character {HP: 0, enemy: false} 
                        && Distance(Game.State().FindHeroPosition(Game.State().CurrentTurn().characterID), lastPosition) == 1)
                    {
                        callback(mPoint2);
                        Deactivate();
                    }
                    break;
            }
        }
    }

    private int Distance(Vector2Int from, Vector2Int to)
    {
        return Mathf.Max(Mathf.Abs(Mathf.Abs(from.x) - Mathf.Abs(to.x)),
            Mathf.Abs(Mathf.Abs(from.y) - Mathf.Abs(to.y)));
    }

    private Color LighterColor(Color color) => new Color(color.r - 100/255f, color.g - 100/255f, color.b - 100/255f);
}