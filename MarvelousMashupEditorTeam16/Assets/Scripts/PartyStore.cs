using System;
using UnityEngine;
using Newtonsoft.Json;


public class PartyStore:MonoBehaviour, IStore
{
    public Party party = new Party();
    public PartyController pc;
    

    public void LoadJson(string json){
        this.party = JsonConvert.DeserializeObject<Party>(json);
        pc.Load();
	}

    public string ToJson() {
        pc.Save();
        return JsonConvert.SerializeObject(party, Formatting.Indented);
	}

	public bool Savable() {
        return true;
    }
}