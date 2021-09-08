using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.Tilemaps;

public class CharacterLoader : MonoBehaviour
{
    public Tilemap tileMap;
    public SpriteRenderer prefab;
    public float dropSpeed;
    public float dropHeight;
    public float moveSpeed;
    public Vector3 generalTileOffset = new Vector3(0, 0.8f, -0.55f);
    public List<DisplayedCharacter> sprites;

    private Dictionary<Transform, Tuple<Vector3, Action>> drops = new Dictionary<Transform, Tuple<Vector3, Action>>();
    private Dictionary<Transform, Tuple<List<Vector2Int>, int, Action>> moves = new Dictionary<Transform, Tuple<List<Vector2Int>, int, Action>>();
    
    public Sprite GetSprite(Character.Characters chr)
    {
        foreach (var sp in sprites)
        {
            if (sp.character == chr)
                return sp.image;
        }
        return sprites[0].image;
    }

    public CharacterController CharacterToPosition(Character.Characters character, Vector2Int position,
        AnimationType animationType = AnimationType.Instant, Action callback = null)
    {
        if (animationType == AnimationType.Instant)
        {
            Transform current = transform.Find(character.ToString());
            if (current == null)
            {
                var chr = Instantiate(prefab, transform);
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                chr.transform.position = pos;
                if (character == Character.Characters.Thanos || character == Character.Characters.StanLee || character == Character.Characters.Goose)
                    chr.GetComponent<CharacterController>().healthDisplayer.gameObject.SetActive(false);
                chr.sprite = GetSprite(character);
                chr.name = character.ToString();
                callback?.Invoke();
                return chr.GetComponent<CharacterController>();
            }
            else
            {
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                current.transform.position = pos;
                callback?.Invoke();
                return current.GetComponent<CharacterController>();
            }
            
            
        }

        if (animationType == AnimationType.Drop)
        {
            Transform current = transform.Find(character.ToString());
            if (current == null)
            {
                var chr = Instantiate(prefab, transform);
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                chr.sprite = GetSprite(character);
                chr.name = character.ToString();
                var transform1 = chr.transform;
                transform1.position = pos + new Vector3(0, dropHeight, 0);
                if (character == Character.Characters.Thanos || character == Character.Characters.StanLee || character == Character.Characters.Goose)
                    chr.GetComponent<CharacterController>().healthDisplayer.gameObject.SetActive(false);
                drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
                return chr.GetComponent<CharacterController>();
            }
            else
            {
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                var transform1 = current.transform;
                transform1.position = pos + new Vector3(0, dropHeight, 0);
                drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
                return current.GetComponent<CharacterController>();
            }
            
        }

        if (animationType == AnimationType.Move)
        {
            Transform current = transform.Find(character.ToString());
            Vector2Int currentPosition = Game.State().FindHeroPosition(character);
            if (current == null || currentPosition.x == -1)
            {
                return CharacterToPosition(character, position);
            }

            var path = Game.Controller().Pathfinding.PathFind(currentPosition, position);
            moves.Add(current, new Tuple<List<Vector2Int>, int, Action>(path, 0, callback));
            return current.GetComponent<CharacterController>();
        }
        return null;
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

        foreach (var m in moves.ToList())
        {
            var i = m.Value.Item2;
            var pos = new Vector3Int(m.Value.Item1[i].x, m.Value.Item1[i].y, 0);
            var posW = tileMap.GetCellCenterWorld(pos) + generalTileOffset;
            if (i == 0)
            {
                m.Key.position = posW;
                moves[m.Key] = new Tuple<List<Vector2Int>, int, Action>(m.Value.Item1, i + 1, m.Value.Item3);
            }
            else
            {
                m.Key.position = Vector3.MoveTowards(m.Key.position, posW, moveSpeed);
                // Debug.Log(string.Format($"Moving Towards {pos} in iteration {i}"));
                if (Vector3.Distance(m.Key.position, posW) < moveSpeed)
                {
                    m.Key.position = posW;
                    moves[m.Key] = new Tuple<List<Vector2Int>, int, Action>(m.Value.Item1, i + 1, m.Value.Item3);
                    if (i == m.Value.Item1.Count-1)
                    {
                        moves.Remove(m.Key);
                        m.Value.Item3?.Invoke();
                    }

                }
            }


        }
    }

    public enum AnimationType
    {
        Instant,
        Drop,
        Move
    }
    
    [Serializable]
    public class DisplayedCharacter
    {
        public Character.Characters character;
        public Sprite image;
        
        
    }
}


