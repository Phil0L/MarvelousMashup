using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class SelectableCard : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler
{
    [Header("Refs")]
    public Image sprite;
    public Text name;
    public Text hp;
    public Text mp;
    public Text ap;
    public Text range;
    public Text damage;
    public Text rangeDamage;
    public Transform selectable;
    public Transform info;

    [Header("Values")]
    public Character Character;
    public bool Selected;
    
    [Header("static")]
    public List<DisplayedCharacter> sprites;


    private void Start()
    {
        info.gameObject.SetActive(false);
    }

    public void OnPointerEnter(PointerEventData eventData)
    {
        info.gameObject.SetActive(true);
    }

    public void OnPointerExit(PointerEventData eventData)
    {
        info.gameObject.SetActive(false);
    }

    private void Update()
    {
        selectable.gameObject.SetActive(Selected);
        
        if (Character == null) return;
        name.text = Character.name;
        hp.text = Character.HP.ToString();
        mp.text = Character.MP.ToString();
        ap.text = Character.AP.ToString();
        range.text = Character.rangeCombatReach.ToString();
        damage.text = Character.meleeDamage.ToString();
        rangeDamage.text = Character.rangeCombatDamage.ToString();
        sprite.sprite = GetSprite(Character.characterID);

    }
    
    private Sprite GetSprite(IDs characterID)
    {
        foreach (var sp in sprites)
        {
            if (sp.characterID.Equals(characterID))
                return sp.image;
        }
        return sprites[0].image;
    }
    
    [Serializable]
    public class DisplayedCharacter
    {
        public IDs characterID;
        public Sprite image;
        
    }
}
