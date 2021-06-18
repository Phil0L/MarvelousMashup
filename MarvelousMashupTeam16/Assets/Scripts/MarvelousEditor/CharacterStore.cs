using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

namespace MarvelousEditor
{
    public class CharacterStore : MonoBehaviour, IStore
    {
        private Characters _characters = new Characters();

        public List<CharacterDefaults> defaultValues;
        public bool loadFlag;
    
        public void CharacterAdded(DataClasses.Character character)
        {
            _characters.characters.Add(character);
        }

        public void CharacterRemoved(DataClasses.Character character)
        {
            _characters.characters.Remove(character);
        }

        public DataClasses.Character[] GetCharacters() => _characters.characters.ToArray();

        public void LoadJson(string json)
        {
            try
            {
                Characters characters = JsonConvert.DeserializeObject<Characters>(json);
                _characters = characters;
                loadFlag = true;
            }
            catch (Exception)
            {
                Debug.Log("Load canceled due to errors");
            }
        }

        public string ToJson()
        {
            return JsonConvert.SerializeObject(_characters, Formatting.Indented);
        }

        public bool Savable()
        {
            return _characters.characters.Count == 24;
        }
    }

    [Serializable]
    class Characters
    {
        [SerializeField] public List<DataClasses.Character> characters = new List<DataClasses.Character>();
    }
}