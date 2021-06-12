using System;
using UnityEngine;
using UnityEngine.UI;

public class CharacterValueChanger : MonoBehaviour
{

    public CharacterListController controller;
    public Dropdown typeDropdown;
    public CharacterSpriteDisplayer spriteDisplayer;

    public void NameChanged(string newName)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.name = newName;
        }
    }

    public void HPChanged(int hp)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.HP = hp;
        }
    }

    public void MPChanged(int mp)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.MP = mp;
        }
    }

    public void APChanged(int ap)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.AP = ap;
        }
    }

    public void LRDChanged(int lrd)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.rangeCombatDamage = lrd;
        }
    }

    public void LRRChanged(int lrr)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.rangeCombatReach = lrr;
        }
    }

    public void CRDChanged(int crd)
    {
        if (controller.selectedElement != null)
        {
            controller.selectedElement.character.meleeDamage = crd;
        }
    }

    public void TypeChanged(int index)
    {
        if (controller.selectedElement != null)
        {
            Character.Characters characterE = (Character.Characters) Enum.Parse(typeof(Character.Characters), typeDropdown.options[index].text);
            controller.selectedElement.character.characterID = characterE;
            spriteDisplayer.ForceReload();
        }
    }
}
