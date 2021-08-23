using System;
using UnityEngine;
using Newtonsoft.Json;

public class PartyConfigStore : MonoBehaviour, IConfigStore
{
    private static Party _party = new Party();

    public Party GetParty()
    {
        return _party;
    }
    
    public static Party Party()
    {
        return _party;
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