using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using UnityEngine;
using UnityEngine.Tilemaps;

public class AttackDisplayer : MonoBehaviour
{
    public LineRenderer LineRenderer;
    public bool active;
    private bool debug;
    public float lineWidth;
    public Color attackColor;
    public TileMarker tmPrefab;

    private Vector2Int lastPosition = new Vector2Int(-1, -1);
    private Action<Vector2Int> callback;
    private int maxLength = 100;
    private List<Vector3> debugPoints = new List<Vector3>();
    private Color color;
    private bool hasCollision;
    private TileMarker _tileMarker;
    private TileMarker _errorMarker;

    private void Start()
    {
        color = attackColor;
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
        if (_errorMarker) Destroy(_errorMarker.gameObject);
        lastPosition = new Vector2Int(-1, -1);
        callback = null;
        Render();
    }

    public void OnSelected(Action<Vector2Int> callback) => this.callback = callback;

    public void SetMaxLength(int length) => maxLength = length;

    public int MaxLength() => maxLength;
    
    public void SetColor(Color color) => this.color = color;

    public bool Confirmable()
    {
        if (!active) return true;
        return lastPosition.x != -1 && !hasCollision;
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
        if (turn == null || turn.enemy)
        {
            Deactivate();
            return;
        }

        var from = Game.State().FindHeroPosition(turn.characterID);
        if (from.x == -1)
        {
            Deactivate();
            return;
        }

        Tilemap tm = Game.Controller().GroundLoader.tilemap;
        Vector3 pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        Vector3Int point3 = tm.WorldToCell(pos);
        Vector2Int point2 = new Vector2Int(point3.x, point3.y);
        
        LineRenderer.positionCount = 2;
        LineRenderer.widthMultiplier = lineWidth;
        LineRenderer.startColor = color;
        LineRenderer.endColor = LighterColor(color);
        if (Distance(from, point2) <= maxLength && Distance(from, point2) > 0 && !Game.State().IsOutOfBounds(point2))
        {
            LineRenderer.SetPositions(new[]
                {
                    tm.GetCellCenterWorld(new Vector3Int(from.x, from.y, 0)), 
                    tm.GetCellCenterWorld(new Vector3Int(point3.x, point3.y, 0))
                });
            lastPosition = point2;
        }
        else if (lastPosition.x == -1)
        {
            LineRenderer.positionCount = 0;
            return;
        }
        
        Vector2Int cPoint2 = GetCollisionPoint(from, lastPosition);
        Vector3Int cPoint3 = new Vector3Int(point2.x, point2.y, 0);
        
        if (!_tileMarker && lastPosition.x != -1) _tileMarker = Instantiate(tmPrefab);
        if (_tileMarker) _tileMarker.SetPosition(lastPosition);
        if (_tileMarker) _tileMarker.SetColor(LighterColor(color));
        
        hasCollision = cPoint2 != lastPosition;

        if (_errorMarker && !hasCollision) Destroy(_errorMarker.gameObject);
        if (!_errorMarker && hasCollision && lastPosition.x != -1) _errorMarker = Instantiate(tmPrefab);
        if (_errorMarker && hasCollision) _errorMarker.SetPosition(cPoint2);
        if (_errorMarker && hasCollision) _errorMarker.SetColor(Color.red);
    }

    private int Distance(Vector2Int from, Vector2Int to)
    {
        return Mathf.Max(Mathf.Abs(Mathf.Abs(from.x) - Mathf.Abs(to.x)),
            Mathf.Abs(Mathf.Abs(from.y) - Mathf.Abs(to.y)));
    }

    private void Update()
    {
        if (!active)
            return;
        Tilemap tm = Game.Controller().GroundLoader.tilemap;
        Vector3 pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        Vector3Int point = tm.WorldToCell(pos);
        Vector2Int arrPos = new Vector2Int(point.x, point.y);
        if (!Game.State().IsOutOfBounds(arrPos) && Game.State()[point.x, point.y].IsAttackable())
        {
            if (lastPosition != arrPos)
            {
                Render();
            }
        }

        if (Input.GetMouseButtonDown(0))
        {
            if (lastPosition.x != -1 && !hasCollision)
            {
                LineRenderer.positionCount = 0;
                LineRenderer.SetPositions(new Vector3[0]);
                if (_tileMarker) Destroy(_tileMarker.gameObject);
                if (_errorMarker) Destroy(_errorMarker.gameObject);
                callback(lastPosition);
                lastPosition = new Vector2Int(-1, -1);
            }
        }
    }

    private Vector2Int GetCollisionPoint(Vector2Int p0, Vector2Int p1)
    {
        if (debug)
            debugPoints.Clear();
        List<Vector2Int> list;
        if (p0.x > p1.x)
        {
            list = GetCollisionPoints(p1, p0);
            list.Reverse();
        }
        else
        {
            list = GetCollisionPoints(p0, p1);
        }
        if (list.Count > 1)
            list.RemoveAt(0);
            

        foreach (var lp in list)
        {
            if (debug)
            {
                Tilemap tm = Game.Controller().GroundLoader.tilemap;
                Vector3 p = tm.GetCellCenterWorld(new Vector3Int(lp.x, lp.y, 0));
                debugPoints.Add(p);
            }

            GameField gf = Game.State()[lp.x, lp.y];
            if (!gf.IsWalkable())
                return new Vector2Int(lp.x, lp.y);
            
        }

        return p1;
    }

    private List<Vector2Int> GetCollisionPoints(Vector2Int start, Vector2Int end)
    {
        var outputArray = new List<Vector2Int>();

        float dx = end.x - start.x;
        float dy = end.y - start.y;
        float absdx = Mathf.Abs(dx);
        float absdy = Mathf.Abs(dy);

        int x = start.x;
        int y = start.y;
        outputArray.Add(new Vector2Int(x, y)); // add starting points

        // slope < 1
        if (absdx > absdy)
        {
            float d = 2 * absdy - absdx;

            for (int i = 0; i < absdx; i++)
            {
                x = dx < 0 ? x - 1 : x + 1;
                if (d < 0)
                {
                    d = d + 2 * absdy;
                }
                else
                {
                    y = dy < 0 ? y - 1 : y + 1;
                    d = d + (2 * absdy - 2 * absdx);
                }

                outputArray.Add(new Vector2Int(x, y));
            }
        }
        else
        {
            // case when slope is greater than or equals to 1
            float d = 2 * absdx - absdy;

            for (int i = 0; i < absdy; i++)
            {
                y = dy < 0 ? y - 1 : y + 1;
                if (d < 0)
                    d = d + 2 * absdx;
                else
                {
                    x = dx < 0 ? x - 1 : x + 1;
                    d = d + (2 * absdx) - (2 * absdy);
                }

                outputArray.Add(new Vector2Int(x, y));
            }
        }

        return outputArray;
    }

    private void OnDrawGizmos()
    {
        if (debug)
        {
            foreach (var dp in debugPoints)
            {
                Gizmos.DrawSphere(dp, 0.05f);
            }
        }
        else
        {
            debugPoints.Clear();
        }
    }
    
    private Color LighterColor(Color color) => new Color(color.r - 100/255f, color.g - 100/255f, color.b - 100/255f);
}