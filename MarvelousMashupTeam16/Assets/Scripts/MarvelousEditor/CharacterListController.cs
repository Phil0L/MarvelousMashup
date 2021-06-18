using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.UI;
using Random = System.Random;

namespace MarvelousEditor
{
    public class CharacterListController : MonoBehaviour
    {

        public VerticalLayoutGroup content;
        public CharacterListElement listPrefab;
        public CharacterStore store;
        public Color selectedColor;

        public CharacterListElement selectedElement;
    
        public List<DataClasses.Character.Characters> available = Enum
            .GetValues(typeof(DataClasses.Character.Characters))
            .Cast<DataClasses.Character.Characters>()
            .Where(c => c != DataClasses.Character.Characters.Unassigned)
            .ToList();

        public DataClasses.Character getNewAvaliableCharacter()
        {
            if (available.Count > 0)
            {
                var rnd = new Random();
                int r = rnd.Next(available.Count);
                DataClasses.Character.Characters c = available[r];
                available.RemoveAt(r);
                foreach (var ch in store.defaultValues)
                {
                    if (ch.characterID == c)
                        return new DataClasses.Character(ch);
                }
            }
            return new DataClasses.Character(DataClasses.Character.Characters.Unassigned);
        }

        public void RemoveElementClicked(CharacterListElement element)
        {
            Destroy(element.gameObject);
            store.CharacterRemoved(element.character);
            if (!available.Contains(element.character.characterID) && element.character.characterID != DataClasses.Character.Characters.Unassigned)
                available.Add(element.character.characterID);
            if (selectedElement == element)
            {
                selectedElement = null;
            }
        }

        public void ElementSelected(CharacterListElement element)
        {
            if (selectedElement != null)
                selectedElement.GetComponent<Image>().color = Color.white;
            selectedElement = element;
            selectedElement.GetComponent<Image>().color = selectedColor;
        }

        public void AddButtonClicked()
        {
            var cle = Instantiate(listPrefab, content.transform);
            cle.SetController(this);
            DataClasses.Character character = getNewAvaliableCharacter();
            cle.SetCharacter(character);
            cle.GetComponent<Image>().color = Color.white;
            store.CharacterAdded(character);
        }
    
        // Start is called before the first frame update
        void Start()
        {
            foreach (Transform elem in content.transform)
            {
                var cle = elem.GetComponent<CharacterListElement>();
                cle.SetController(this);
                DataClasses.Character character = getNewAvaliableCharacter();
                cle.SetCharacter(character);
                cle.GetComponent<Image>().color = Color.white;
                store.CharacterAdded(character);
            }
        }

        // Update is called once per frame
        void Update()
        {
            if (store.loadFlag)
            {
                store.loadFlag = false;
                foreach (Transform elem in content.transform)
                {
                    Destroy(elem.gameObject);
                }
            
                available = Enum
                    .GetValues(typeof(DataClasses.Character.Characters))
                    .Cast<DataClasses.Character.Characters>()
                    .Where(c => c != DataClasses.Character.Characters.Unassigned)
                    .ToList();
            
                foreach (DataClasses.Character ch in store.GetCharacters())
                {
                    var cle = Instantiate(listPrefab, content.transform);
                    cle.SetController(this);
                    cle.SetCharacter(ch);
                    cle.GetComponent<Image>().color = Color.white;
                    available.Remove(ch.characterID);
                }
            }

            if (selectedElement == null && store.GetCharacters().Length != 0)
            {
                ElementSelected(content.transform.GetChild(0).GetComponent<CharacterListElement>());
            }
        }
    }
}
