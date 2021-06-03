using System;
using UnityEngine;


public class PartyStore:MonoBehaviour, IStore
{
    public Party party;
    

    public void loadJson(string json){
        throw new NotImplementedException();
	}

    public string toJson() {
        throw new NotImplementedException();
	}
}