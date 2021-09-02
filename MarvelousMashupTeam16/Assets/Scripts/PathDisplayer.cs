using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using UnityEngine;
using UnityEngine.Tilemaps;

public class PathDisplayer : MonoBehaviour
{
    public LineRenderer LineRenderer;
    public bool active;
    public float lineWidth;
    public Color moveColor;
    public TileMarker tmPrefab;

    private Vector2Int lastPosition = new Vector2Int(-1, -1);
    private Action<Vector2Int> callback;
    private int maxLength = 100;
    private Color color;
    private TileMarker _tileMarker;

    private void Start()
    {
        SetColor(moveColor);
    }

    public void Activate()
    {
        active = true;
        Render();
    }

    public void Deactivate()
    {
        active = false;
        if (_tileMarker) Destroy(_tileMarker.gameObject);
        lastPosition = new Vector2Int(-1, -1);
        callback = null;
        Render();
    }

    public void OnSelected(Action<Vector2Int> callback) => this.callback = callback;

    public void SetMaxLength(int length) => maxLength = length;

    public void SetColor(Color color) => this.color = color;
    
    public bool Confirmable()
    {
        if (!active) return true;
        return lastPosition.x != -1;
    }

    public void Render()
    {
        if (!active)
        {
            LineRenderer.SetPositions(new Vector3[0]);
            LineRenderer.positionCount = 0;
            return;
        }

        var turn = Game.State().CurrentTurn();
        if (turn == null || CharacterConfigStore.Character(turn).enemy)
        {
            Deactivate();
            return;
        }

        var from = Game.State().FindHeroPosition(turn);
        if (from.x == -1)
        {
            Deactivate();
            return;
        }

        Tilemap tm = Game.Controller().GroundLoader.tilemap;

        List<Vector2Int> points;
        if (lastPosition.x != -1)
            points = Game.Controller().Pathfinding.PathFind(from, lastPosition);
        else return;
        var positions = points.Select(p => new Vector3Int(p.x, p.y, 0)).Select(p => tm.GetCellCenterWorld(p)).ToArray();
        LineRenderer.startColor = color;
        LineRenderer.endColor = LighterColor(color);
        LineRenderer.positionCount = positions.Length;
        LineRenderer.widthMultiplier = lineWidth;

        if (!_tileMarker && lastPosition.x != -1) _tileMarker = Instantiate(tmPrefab);
        if (_tileMarker) _tileMarker.SetPosition(lastPosition);
        if (_tileMarker) _tileMarker.SetColor(LighterColor(color));

        if (positions.Length > maxLength + 1)
        {
            LineRenderer.positionCount = maxLength + 1;
            lastPosition = points[maxLength];
        }

        LineRenderer.SetPositions(positions);
    }

    private void Update()
    {
        if (!active) return;
        Tilemap tm = Game.Controller().GroundLoader.tilemap;
        Vector3 pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        Vector3Int point = tm.WorldToCell(pos);
        Vector2Int arrPos = new Vector2Int(point.x, point.y);
        if (!Game.State().IsOutOfBounds(arrPos) && Game.State()[point.x, point.y].IsWalkable())
        {
            if (lastPosition != arrPos)
            {
                lastPosition = arrPos;
                Render();
            }
        }

        if (Input.GetMouseButtonDown(0))
        {
            if (lastPosition.x != -1)
            {
                LineRenderer.positionCount = 0;
                LineRenderer.SetPositions(new Vector3[0]);
                if (_tileMarker) Destroy(_tileMarker.gameObject);
                callback(lastPosition);
            }
        }
    }

    private Color LighterColor(Color color) =>
        new Color(color.r - 100 / 255f, color.g - 100 / 255f, color.b - 100 / 255f);
}