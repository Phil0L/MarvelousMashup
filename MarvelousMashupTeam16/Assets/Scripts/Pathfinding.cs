using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.Tilemaps;

public class Pathfinding : MonoBehaviour
{
    public bool debugging;

    private List<Vector2Int> _lastSuccessfulPath;

    public List<Vector2Int> GetPath() => _lastSuccessfulPath;
    public List<Vector2Int> PathFind(Vector2Int from, Vector2Int to)
    {
        List<Vector2Int> points = new List<Vector2Int>();
        GameState gs = Game.State();
        bool[,] world = new bool[gs.Width(), gs.Height()];
        for (int x = 0; x < gs.Width(); x++)
        {
            for (int y = 0; y < gs.Height(); y++)
            {
                world[x, y] = gs[x, y].IsWalkable() && gs[x, y].tile != MapTile.PORTAL;
            }
        }
        if (gs[to.x, to.y].tile == MapTile.PORTAL)
            world[to.x, to.y] = true;

        var start = new Tile {X = from.x, Y = from.y};
        var finish = new Tile {X = to.x, Y = to.y};

        start.SetDistance(finish.X, finish.Y);

        var activeTiles = new List<Tile> {start};
        var visitedTiles = new List<Tile>();


        while (activeTiles.Any())
        {
            var checkTile = activeTiles.OrderBy(x => x.CostDistance).First();

            if (checkTile.X == finish.X && checkTile.Y == finish.Y)
            {
                //We can actually loop through the parents of each tile to find our exact path which we will show shortly. 

                if (checkTile.X == finish.X && checkTile.Y == finish.Y)
                {
                    //We found the destination and we can be sure (Because the the OrderBy above)
                    //That it's the most low cost option. 
                    var tile = checkTile;
                    while (tile != null)
                    {
                        //Debug.Log(tile.X + " : " + tile.Y);
                        points.Add(new Vector2Int(tile.X, tile.Y));
                        tile = tile.Parent;
                    }

                    points.Reverse();
                    _lastSuccessfulPath = points;
                    return points;
                }
            }

            visitedTiles.Add(checkTile);
            activeTiles.Remove(checkTile);

            var walkableTiles = GetWalkableTiles(world, checkTile, finish);

            foreach (var walkableTile in walkableTiles)
            {
                //We have already visited this tile so we don't need to do so again!
                if (visitedTiles.Any(x => x.X == walkableTile.X && x.Y == walkableTile.Y))
                    continue;

                //It's already in the active list, but that's OK, maybe this new tile has a better value (e.g. We might zigzag earlier but this is now straighter). 
                if (activeTiles.Any(x => x.X == walkableTile.X && x.Y == walkableTile.Y))
                {
                    var existingTile = activeTiles.First(x => x.X == walkableTile.X && x.Y == walkableTile.Y);
                    if (existingTile.CostDistance > checkTile.CostDistance)
                    {
                        activeTiles.Remove(existingTile);
                        activeTiles.Add(walkableTile);
                    }
                }
                else
                {
                    //We've never seen this tile before so add it to the list. 
                    activeTiles.Add(walkableTile);
                }
            }
        }
        Debug.LogWarning("Pathfinding Failed");
        return new List<Vector2Int>();
    }

    private static List<Tile> GetWalkableTiles(bool[,] map, Tile currentTile, Tile targetTile)
    {
        var possibleTiles = new List<Tile>
        {
            new Tile {X = currentTile.X, Y = currentTile.Y - 1, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X, Y = currentTile.Y + 1, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X - 1, Y = currentTile.Y, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X + 1, Y = currentTile.Y, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X - 1, Y = currentTile.Y - 1, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X + 1, Y = currentTile.Y + 1, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X - 1, Y = currentTile.Y + 1, Parent = currentTile, Cost = currentTile.Cost + 1},
            new Tile {X = currentTile.X + 1, Y = currentTile.Y - 1, Parent = currentTile, Cost = currentTile.Cost + 1},
        };

        possibleTiles.ForEach(tile => tile.SetDistance(targetTile.X, targetTile.Y));

        var maxX = map.GetLength(0);
        var maxY = map.GetLength(1);

        return possibleTiles
            .Where(tile => tile.X >= 0 && tile.X <= maxX)
            .Where(tile => tile.Y >= 0 && tile.Y <= maxY)
            .Where(tile => map[tile.X, tile.Y])
            .ToList();
    }

    private class Tile
    {
        public int X { get; set; }
        public int Y { get; set; }
        public int Cost { get; set; }
        public int Distance { get; set; }
        public int CostDistance => Cost + Distance;
        public Tile Parent { get; set; }

        //The distance is essentially the estimated distance, ignoring walls to our target. 
        //So how many tiles left and right, up and down, ignoring walls, to get there. 
        public void SetDistance(int targetX, int targetY)
        {
            Distance = Math.Abs(targetX - X) + Math.Abs(targetY - Y);
        }
    }

    
    // DEBUGGING
    private Vector2Int _right = new Vector2Int(-1, -1);
    private Vector2Int _left = new Vector2Int(-1, -1);
    private void OnDrawGizmos()
    {
        if (debugging && Game.IsInstantiated())
        {
            if (_lastSuccessfulPath != null)
            {
                Debug.Log("Drawing:");
                Tilemap tm = Game.Controller().GroundLoader.tilemap;
                Gizmos.color = Color.red;
                Vector2Int before = new Vector2Int(-1, -1);
                foreach (var point in _lastSuccessfulPath)
                {
                    Gizmos.DrawSphere(tm.GetCellCenterWorld(new Vector3Int(point.x, point.y, 0)), 0.05f);
                    if (before.x != -1)
                    {
                        Gizmos.DrawLine(
                            tm.GetCellCenterWorld(new Vector3Int(before.x, before.y, 0)),
                            tm.GetCellCenterWorld(new Vector3Int(point.x, point.y, 0))
                        );
                        Debug.Log("Drawn");
                    }

                    before = point;
                }
            }
        }
    }

    private void LateUpdate()
    {
        if (debugging)
        {
            if (Input.GetMouseButtonDown(0))
            {
                Tilemap tm = Game.Controller().GroundLoader.tilemap;
                Vector3 pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                Vector3Int point = tm.WorldToCell(pos);
                _left = new Vector2Int(point.x, point.y);
                Debug.Log($"First position set to [{point.x}, {point.y}]");
            }
            
            if (Input.GetMouseButtonDown(1))
            {
                Tilemap tm = Game.Controller().GroundLoader.tilemap;
                Vector3 pos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                Vector3Int point = tm.WorldToCell(pos);
                _right = new Vector2Int(point.x, point.y);
                Debug.Log($"Second position set to [{point.x}, {point.y}]");
            }

            if (_left.x != -1 && _right.x != -1)
            {
                PathFind(_left, _right);
            }
        }
    }
}