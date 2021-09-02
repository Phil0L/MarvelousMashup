using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Windows;

public class CharacterConfigStore : MonoBehaviour, IConfigStore
{
    private static Characters _characters = new Characters();
    
    public Characters GetCharacters() => _characters;
    public static List<Character> Characters() => _characters.characters;

    public static Character Character(IDs characterID)
    {
        foreach (var car in Characters())
        {
            if (car.characterID == characterID) return car;
        }
        return null;
    }

    public static void SetCharacters(List<Character> chars)
    {
        _characters = new Characters();
        _characters.characters = chars;
    }

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

    private void Awake()
    {
        if (GetComponent<FileLoader>())
        {
            LoadJson(GetComponent<FileLoader>().GetContent());
        }
    }
}


