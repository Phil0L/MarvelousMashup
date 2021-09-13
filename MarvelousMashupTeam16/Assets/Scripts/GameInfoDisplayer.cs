using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GameInfoDisplayer : MonoBehaviour
{
    [Header("Refs")] 
    public Text roundCount;
    public Text playerOneName;
    public Text playerTwoName;
    public Image[] borderImages;
    public Image[] characterImages;
    
    [Header("static")]
    public List<DisplayedCharacter> sprites;
    public Color turnColor;
    public Color defaultColor;

    private List<Character.Characters> _charactersList;


    public void SetCharacters(List<Character.Characters> characters)
    {
        _charactersList = characters;
        for (int i = 0; i < _charactersList.Count; i++)
        {
            var character = _charactersList[i];
            var sprite = GetSprite(character);
            characterImages[i].sprite = sprite;
            borderImages[i].gameObject.SetActive(true);
        }

        for (int j = 0; j < borderImages.Length - _charactersList.Count; j++)
        {
            int revI = borderImages.Length - 1 - j;
            borderImages[revI].gameObject.SetActive(false);
        }
    }

    public void SetRoundCount(int count) => roundCount.text = count.ToString();

    public void SetNameOne(string nameOne) => playerOneName.text = nameOne;
    
    public void SetNameTwo(string nameTwo) => playerTwoName.text = nameTwo;

    private void Update()
    {
        foreach (var border in borderImages) 
            border.color = defaultColor;
        
        var current = Game.State().CurrentTurn();
        if (current == null) return;
        
        for (int i = 0; i < _charactersList.Count; i++)
        {
            if (current.characterID == _charactersList[i])
                borderImages[i].color = turnColor;
        }
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