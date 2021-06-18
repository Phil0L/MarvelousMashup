using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

public class CharacterConfigStore : MonoBehaviour, IConfigStore
{
    private Characters _characters = new Characters();
    
    public Character[] GetCharacters() => _characters.characters.ToArray();

    public void LoadJson(string json)
    {
        try
        {
            Characters characters = JsonConvert.DeserializeObject<Characters>(json);
            _characters = characters;
        }
        catch (Exception)
        {
            Debug.Log("Load canceled due to errors");
        }
    }
    
}


