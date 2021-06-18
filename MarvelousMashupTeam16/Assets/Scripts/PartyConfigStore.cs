using System;
using UnityEngine;
using Newtonsoft.Json;

public class PartyConfigStore : MonoBehaviour, IConfigStore
{
    private Party _party = new Party();

    public Party GetParty()
    {
        return _party;
    }

    public void LoadJson(string json)
    {
        try
        {
            this._party = JsonConvert.DeserializeObject<Party>(json);
        }
        catch (Exception)
        {
            Debug.Log("Load canceled due to errors");
        }
    }
}