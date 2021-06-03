using UnityEditor;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Windows;

public class Save : MonoBehaviour
{
    public CharacterStore characterStore;

    public MapStore mapStore;
    //public PartyStore partyStore;

    public Color savableColor;
    public Color unsavableColor;

    public Button button;
    public Image image;

    public void Clicked()
    {
        if (getStore().Savable())
        {
            string content = getStore().ToJson();
            string filename = "";
            string ending = "";
            if (characterStore)
            {
                filename = "heros.character.json";
                ending = "character.json";
            }

            if (mapStore)
            {
                filename = "map.scenario.json";
                ending = "scenario.json";
            }

            //if (partyStore)
            //{
            //    filename = config.game.json;
            //    ending = game.json
            //}
            var saveWindow = new SaveWindow("Save the file", filename, ending, content,
                s =>
                {
                    Debug.Log("Saved!");
                });
        }
    }

    void Update()
    {
        if (getStore().Savable())
        {
            image.color = savableColor;
            button.interactable = true;
        }
        else
        {
            image.color = unsavableColor;
            button.interactable = false;
        }
    }

    private IStore getStore()
    {
        if (characterStore)
            return characterStore;
        if (mapStore)
            return mapStore;
        //if (partyStore)
        //    return partyStore;
        return null;
    }
}