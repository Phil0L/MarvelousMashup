using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.PlayerLoop;
using UnityEngine.Tilemaps;

public class CharacterLoader : MonoBehaviour
{
    public Tilemap tileMap;
    public SpriteRenderer prefab;
    public float dropSpeed;
    public float dropHeight;
    public List<DisplayedCharacter> sprites;

    private Dictionary<Transform, Vector3> drops = new Dictionary<Transform, Vector3>();

    private void Start()
    {
       CharacterToPosition(DisplayedCharacter.CharacterList.BlackWidow, new Vector2Int(3,3), AnimationType.Drop);
    }

    private Sprite GetSprite(DisplayedCharacter.CharacterList chr)
    {
        foreach (var sp in sprites)
        {
            if (sp.character == chr)
                return sp.image;
        }
        return sprites[0].image;
    }

    public void CharacterToPosition(DisplayedCharacter.CharacterList character, Vector2Int position,
        AnimationType animationType = AnimationType.Instant)
    {
        if (animationType == AnimationType.Instant)
        {
            var chr = Instantiate(prefab, transform);
            Vector3 pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0));
            float height = chr.size.y;
            pos = pos + new Vector3(0, height / 2, -0.55f);
            chr.transform.position = pos;
            chr.sprite = GetSprite(character);
            chr.name = character.ToString();
        }

        if (animationType == AnimationType.Drop)
        {
            var chr = Instantiate(prefab, transform);
            Vector3 pos = tileMap.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0));
            float height = chr.size.y;
            pos += new Vector3(0, height / 2, -0.55f);
            chr.sprite = GetSprite(character);
            chr.name = character.ToString();
            chr.transform.position = pos + new Vector3(0, dropHeight, 0);
            drops.Add(chr.transform, pos);
        }
    }

    private void FixedUpdate()
    {
        foreach (var d in drops.ToList())
        {
            d.Key.position = Vector3.MoveTowards(d.Key.position, d.Value, dropSpeed);
            if (Vector3.Distance(d.Key.position, d.Value) < dropSpeed)
            {
                d.Key.position = d.Value;
                drops.Remove(d.Key);
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
        public CharacterList character;
        public Sprite image;
        
        public enum CharacterList
        {
            Unassigned,
            RocketRacoon,
            Quicksilver,
            Hulk,
            BlackWidow,
            Hawkeye,
            CaptainAmerica,
            Spiderman,
            DrStrange,
            IronMan,
            BlackPanther,
            Thor,
            CaptainMarvel,
            Groot,
            Starlord,
            Gamora,
            AntMan,
            Vision,
            Deadpool,
            Loki,
            SilverSurfer,
            Mantis,
            GhostRider,
            JesicaJones,
            ScarletWitch,
            Thanos,
            StanLee,
            Goose
        }
    }
}


