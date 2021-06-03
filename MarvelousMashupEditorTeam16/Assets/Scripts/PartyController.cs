using UnityEngine;
public class PartyController:MonoBehaviour
{

    // Controler Class
    public PartyStore store;
    
    // All Ballancing
    public ValueController spaceStoneCD;
    public ValueController realtiyStoneCD;
    public ValueController mindStoneCD;
    public ValueController powerStoneCD;
    public ValueController timeStoneCD;
    public ValueController soulStoneCD;
    public ValueController mindStoneDMG;
    
    // General Settings
    public ValueController eins;

    public void Save()
    {
        store.party.spaceStoneCD = spaceStoneCD.GetValue();
    }
}