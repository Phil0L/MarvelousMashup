using System;
using Newtonsoft.Json;
using UnityEngine;

namespace MarvelousEditor
{
    public class PartyStore:MonoBehaviour, IStore
    {
        public DataClasses.Party party = new DataClasses.Party();
        public PartyController pc;
    

        public void LoadJson(string json){
            try
            {
                this.party = JsonConvert.DeserializeObject<DataClasses.Party>(json);
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
}