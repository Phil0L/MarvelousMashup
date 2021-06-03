using UnityEngine;
using UnityEngine.UI;

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
    }
}
