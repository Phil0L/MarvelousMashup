using UnityEngine;

public class ConfigDisplay : MonoBehaviour
{
    // ReSharper disable once MemberCanBePrivate.Global
    public Party party;
    public Map map;
    public Characters characters;
    void Update()
    {
        PartyConfigStore pcs;
        if ((pcs = GetComponent<PartyConfigStore>()) != null)
        {
            party = pcs.GetParty();
        }
        MapConfigStore mcs;
        if ((mcs = GetComponent<MapConfigStore>()) != null)
        {
            map = mcs.GetMap();
        }
        CharacterConfigStore ccs;
        if ((ccs = GetComponent<CharacterConfigStore>()) != null)
        {
            characters = ccs.GetCharacters();
        }
    }
}
