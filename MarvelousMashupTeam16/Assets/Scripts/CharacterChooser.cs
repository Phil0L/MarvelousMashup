using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.UI;

public class CharacterChooser : MonoBehaviour
{
    [Header("Refs")]
    public SelectableCard[] cards;
    public Image[] team;
    public Button readyButton;

    [Header("Values")]
    public List<Character> characters;
    public List<int> selected;

    public List<DisplayedCharacter> sprites;
    
    private void Awake()
    {
        characters = new List<Character>();
        selected = new List<int>();
        readyButton.gameObject.SetActive(false);
    }

    private void Update()
    {
        for (int i = 0; i < 12; i++)
        {
            if (i >= characters.Count) break;
            cards[i].Character = characters[i];
            cards[i].Selected = selected.Contains(i);
        }
        readyButton.gameObject.SetActive(selected.Count == 6);

        int count = 0;
        foreach (var sel in selected)
        {
            Character character = characters[sel];
            Sprite sprite = GetSprite(character.characterID);
            team[count].sprite = sprite;
            team[count].gameObject.SetActive(true);
            count++;
        }

        for (int i = count; i < 6; i++)
        {
            team[i].gameObject.SetActive(false);
            team[i].sprite = null;
        }
    }

    public void Clicked(int index)
    {
        if (selected.Contains(index))
        {
            selected.Remove(index);
        }
        else
        {
            if (selected.Count >= 6) return;
            selected.Add(index);
        }
    }

    public void Ready()
    {
        List<Character> heroes = new List<Character>();
        foreach (var s in selected) heroes.Add(characters[s]);
        var waitPopUp = PopUp.Create()
            .Title("Waiting...")
            .Text("Your opponent hasn't chosen his Heroes yet.\nYou have to wait until he has decided.");
        waitPopUp.Show();
    }
    
    private Sprite GetSprite(Character.Characters chr)
    {
        foreach (var sp in sprites)
        {
            if (sp.character == chr)
                return sp.image;
        }
        return sprites[0].image;
    }
    
    [Serializable]
    public class DisplayedCharacter
    {
        public Character.Characters character;
        public Sprite image;
        
    }
}
