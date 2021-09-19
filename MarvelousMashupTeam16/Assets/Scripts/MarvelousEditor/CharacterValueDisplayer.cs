using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.UI;

namespace MarvelousEditor
{
    public class CharacterValueDisplayer : MonoBehaviour
    {
    
        public CharacterListController controller;

        public InputField nameChanger;
        public ValueController hpChanger;
        public ValueController mpChanger;
        public ValueController apChanger;
        public ValueController lrdChanger;
        public ValueController lrrChanger;
        public ValueController crdChanger;
        public Dropdown typeChanger;

        private CharacterListElement localSelected;
    
        void Update()
        {
            if (localSelected != controller.selectedElement)
            {
                localSelected = controller.selectedElement;
                if (localSelected != null)
                    Refresh();
                else
                    Deactivate();
            }
        }

        private void Refresh()
        {
            nameChanger.text = localSelected.character.name;
            hpChanger.SetValue(localSelected.character.HP);
            mpChanger.SetValue(localSelected.character.MP);
            apChanger.SetValue(localSelected.character.AP);
            lrdChanger.SetValue(localSelected.character.rangeCombatDamage);
            lrrChanger.SetValue(localSelected.character.rangeCombatReach);
            crdChanger.SetValue(localSelected.character.meleeDamage);
            List<Dropdown.OptionData> dropdownlist = new List<Dropdown.OptionData>();
            dropdownlist.Add(new Dropdown.OptionData(controller.selectedElement.character.characterID.ToString()));
            dropdownlist.AddRange(controller.available.Select(a => new Dropdown.OptionData(a.ToString())));
            typeChanger.options = dropdownlist;
            typeChanger.value = 0;
            typeChanger.interactable = true;
        }

        private void Deactivate()
        {
            nameChanger.text = "No hero selected";
            hpChanger.SetValue(0);
            mpChanger.SetValue(0);
            apChanger.SetValue(0);
            lrdChanger.SetValue(0);
            lrrChanger.SetValue(0);
            crdChanger.SetValue(0);
            typeChanger.ClearOptions();
            typeChanger.interactable = false;
        }
    }
}
