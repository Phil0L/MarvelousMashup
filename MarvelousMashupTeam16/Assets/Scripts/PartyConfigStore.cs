using System;
using UnityEngine;
using Newtonsoft.Json;

public class PartyConfigStore : MonoBehaviour, IConfigStore
{
    private static Party _party = new Party();
    private static string yourName;
    private static string opponentName;
    
    public  Party GetParty() => _party;
    public static Party Party() => _party;
    public static string You() => yourName;
    public static string Opponent() => opponentName;

    public static void SetParty(Party party) => _party = party;

    public static void SetNames(string you, string other)
    {
        yourName = you;
        opponentName = other;
    }

    public void LoadJson(string json)
    {
        try
        {
            _party = JsonConvert.DeserializeObject<Party>(json);
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