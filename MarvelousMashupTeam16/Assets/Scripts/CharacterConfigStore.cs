using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Windows;

public class CharacterConfigStore : MonoBehaviour, IConfigStore
{
    private static Characters _characters = new Characters();
    
    public static Character[] Characters() => _characters.characters.ToArray();

    public static Character Character(Character.Characters character)
    {
        foreach (var car in Characters())
        {
            if (car.characterID == character) return car;
        }
        return null;
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


