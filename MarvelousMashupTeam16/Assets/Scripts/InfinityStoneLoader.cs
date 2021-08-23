using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.Tilemaps;

public class InfinityStoneLoader : MonoBehaviour
{
    public Tilemap tileMap;
    public SpriteRenderer prefab;
    public float dropSpeed;
    public float dropHeight;
    public Vector3 generalTileOffset = new Vector3(0, 0.8f, -0.55f);
    public List<DisplayedInfinityStone> sprites;

    private Dictionary<Transform, Tuple<Vector3, Action>> drops = new Dictionary<Transform, Tuple<Vector3, Action>>();

    public Sprite GetSprite(int stone)
    {
        foreach (var sp in sprites)
        {
            if (sp.stone == stone)
                return sp.image;
        }
        return sprites[0].image;
    }
    
    public void InfinityStoneToPosition(InfinityStone inf, Vector2Int position,
        AnimationType animationType = AnimationType.Instant, Action callback = null)
    {
        if (animationType == AnimationType.Instant)
        {
            Transform current = transform.Find(inf.ToString());
            if (current == null)
            {
                var chr = Instantiate(prefab, transform);
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                chr.transform.position = pos;
                chr.sprite = GetSprite(inf.stone);
                chr.name = inf.ToString(); // <- error
                callback?.Invoke();
            }
            else
            {
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                current.transform.position = pos;
                callback?.Invoke();
            }
        }

        if (animationType == AnimationType.Drop)
        {
            Transform current = transform.Find(inf.ToString());
            if (current == null)
            {
                var chr = Instantiate(prefab, transform);
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                chr.sprite = GetSprite(inf.stone);
                chr.name = inf.ToString();
                var transform1 = chr.transform;
                transform1.position = pos + new Vector3(0, dropHeight, 0);
                drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
            }
            else
            {
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                var transform1 = current.transform;
                transform1.position = pos + new Vector3(0, dropHeight, 0);
                drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
            }
        }
    }

    private void FixedUpdate()
    {
        foreach (var d in drops.ToList())
        {
            d.Key.position = Vector3.MoveTowards(d.Key.position, d.Value.Item1, dropSpeed);
            if (Vector3.Distance(d.Key.position, d.Value.Item1) < dropSpeed)
            {
                d.Key.position = d.Value.Item1;
                drops.Remove(d.Key);
                d.Value.Item2?.Invoke();
            }
        }
    }

    public enum AnimationType
    {
        Instant,
        Drop
    }
    
    [Serializable]
    public class DisplayedInfinityStone
    {
        public int stone;
        public string name;
        public Sprite image;
    }
}


