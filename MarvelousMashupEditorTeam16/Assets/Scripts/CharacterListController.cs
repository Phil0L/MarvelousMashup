using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.UI;
using Random = System.Random;

public class CharacterListController : MonoBehaviour
{

    public VerticalLayoutGroup content;
    public CharacterListElement listPrefab;
    public CharacterStore store;
    public Color selectedColor;

    public CharacterListElement selectedElement;
    
    private List<Character.Characters> available = Enum
        .GetValues(typeof(Character.Characters))
        .Cast<Character.Characters>()
        .Where(c => c != Character.Characters.Unassigned)
        .ToList();

    public Character getNewAvaliableCharacter()
    {
        if (available.Count > 0)
        {
            var rnd = new Random();
            int r = rnd.Next(available.Count);
            Character.Characters c = available[r];
            available.RemoveAt(r);
            foreach (var ch in store.defaultValues)
            {
                if (ch.characterID == c)
                    return new Character(ch);
            }
        }
        return new Character(Character.Characters.Unassigned);
    }

    public void RemoveElementClicked(CharacterListElement element)
    {
        Destroy(element.gameObject);
        store.CharacterRemoved(element.character);
        if (!available.Contains(element.character.characterID) && element.character.characterID != Character.Characters.Unassigned)
            available.Add(element.character.characterID);
        if (selectedElement == element)
        {
            selectedElement = null;
        }
    }

    public void ElementSelected(CharacterListElement element)
    {
        if (selectedElement != null)
            selectedElement.GetComponent<Image>().color = Color.white;
        selectedElement = element;
        selectedElement.GetComponent<Image>().color = selectedColor;
    }

    public void AddButtonClicked()
    {
        var cle = Instantiate(listPrefab, content.transform);
        cle.SetController(this);
        Character character = getNewAvaliableCharacter();
        cle.SetCharacter(character);
        cle.GetComponent<Image>().color = Color.white;
        store.CharacterAdded(character);
    }
    
    // Start is called before the first frame update
    void Start()
    {
        foreach (Transform elem in content.transform)
        {
            var cle = elem.GetComponent<CharacterListElement>();
            cle.SetController(this);
            Character character = getNewAvaliableCharacter();
            cle.SetCharacter(character);
            cle.GetComponent<Image>().color = Color.white;
            store.CharacterAdded(character);
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
