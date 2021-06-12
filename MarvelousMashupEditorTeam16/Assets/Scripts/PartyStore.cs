using System;
using UnityEngine;
using Newtonsoft.Json;


public class PartyStore:MonoBehaviour, IStore
{
    public Party party = new Party();
    public PartyController pc;
    

    public void LoadJson(string json){
        try
        {
            this.party = JsonConvert.DeserializeObject<Party>(json);
            pc.Load();
        }
        catch (Exception)
        {
            Debug.Log("Load canceled due to errors");
        }
    }

    public string ToJson() {
        pc.Save();
        return JsonConvert.SerializeObject(party, Formatting.Indented);
	}

	public bool Savable() {
        return true;
    }
}