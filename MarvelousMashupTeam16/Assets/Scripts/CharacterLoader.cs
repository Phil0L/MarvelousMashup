using System;
using System.Collections;
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
                switch (character)
                {
                    case Character.Characters.AntMan:
                        var charPos = chr.GetComponent<CharacterController>().healthDisplayer.transform.position;
                        charPos.y -= 0.4f;
                        chr.GetComponent<CharacterController>().healthDisplayer.transform.position = charPos;
                        break;
                    case Character.Characters.RocketRacoon:
                        var charPos2 = chr.GetComponent<CharacterController>().healthDisplayer.transform.position;
                        charPos2.y -= 0.15f;
                        chr.GetComponent<CharacterController>().healthDisplayer.transform.position = charPos2;
                        break;
                }
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
                switch (character)
                {
                    case Character.Characters.AntMan:
                        var charPos = chr.GetComponent<CharacterController>().healthDisplayer.transform.position;
                        charPos.y -= 0.4f;
                        chr.GetComponent<CharacterController>().healthDisplayer.transform.position = charPos;
                        break;
                    case Character.Characters.RocketRacoon:
                        var charPos2 = chr.GetComponent<CharacterController>().healthDisplayer.transform.position;
                        charPos2.y -= 0.15f;
                        chr.GetComponent<CharacterController>().healthDisplayer.transform.position = charPos2;
                        break;
                }
                drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
                return chr.GetComponent<CharacterController>();
            }
            else
            {
                var pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0)) + generalTileOffset;
                var transform1 = current.transform;
                if (moves.ContainsKey(transform1))
                {
                    StartCoroutine(WaitUntilFree(transform1, () =>
                    {
                        transform1.position = pos + new Vector3(0, dropHeight, 0);
                        drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
                    }));
                }
                else
                {
                    transform1.position = pos + new Vector3(0, dropHeight, 0);
                    drops.Add(transform1, new Tuple<Vector3, Action>(pos, callback));
                }
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
            if (!moves.ContainsKey(current))
                moves.Add(current, new Tuple<List<Vector2Int>, int, Action>(path, 0, callback));
            else 
                moves[current].Item1.AddRange(path.GetRange(1, path.Count-1));
            return current.GetComponent<CharacterController>();
        }
        return null;
    }

    private IEnumerator WaitUntilFree(Transform transform, Action callback)
    {
        yield return new WaitWhile(() => moves.ContainsKey(transform));
        callback();
    }

    private void FixedUpdate()
    {
        foreach (var d in drops.ToList())
        {
            if (!d.Key)
            {
                drops.Remove(d.Key);
                continue;
            }
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
            if (!m.Key)
            {
                drops.Remove(m.Key);
                continue;
            }
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


