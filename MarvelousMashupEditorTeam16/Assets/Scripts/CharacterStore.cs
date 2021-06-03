using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

public class CharacterStore : MonoBehaviour, IStore
{
    private Characters _characters = new Characters();

    public List<CharacterDefaults> defaultValues;
    
    public void CharacterAdded(Character character)
    {
        _characters.characters.Add(character);
    }

    public void CharacterRemoved(Character character)
    {
        _characters.characters.Remove(character);
    }

    public void loadJson(string json)
    {
        throw new NotImplementedException();
    }

    public string toJson()
    {
        return JsonConvert.SerializeObject(_characters, Formatting.Indented);
    }
}

[Serializable]
class Characters
{
    [SerializeField] public List<Character> characters = new List<Character>();
}
