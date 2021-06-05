using UnityEngine;
using UnityEngine.UI;

public class Load : MonoBehaviour
{
    public CharacterStore characterStore;

    public MapStore mapStore;
    public PartyStore partyStore;

    public Color loadColor;

    public Image image;

    public void Clicked()
    {
        string ending = "";
        if (characterStore)
        {
            ending = "character.json";
        }

        if (mapStore)
        {
            ending = "scenario.json";
        }

        if (partyStore)
        {
            ending = "game.json";
        }

        var loadWindow = new LoadWindow("Select a file", ending,
            (path, data) =>
            {
                Debug.Log("Loaded!");
                getStore().LoadJson(data);
            });

    }

    private void Start()
    {
        image.color = loadColor;
    }

    private IStore getStore()
    {
        if (characterStore)
            return characterStore;
        if (mapStore)
            return mapStore;
        if (partyStore)
            return partyStore;
        return null;
    }
}