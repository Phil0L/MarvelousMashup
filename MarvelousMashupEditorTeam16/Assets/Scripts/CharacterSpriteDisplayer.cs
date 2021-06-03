using UnityEngine;
using UnityEngine.UI;

public class CharacterSpriteDisplayer : MonoBehaviour
{
    public Image spriteImage;
    public Text nameText;
    public CharacterListController controller;
    public CharacterStore store;
    private CharacterListElement displayedElement;

    void Update()
    {
        if (controller.selectedElement != null)
        {
            if (controller.selectedElement != displayedElement)
            {
                var selected = controller.selectedElement;
                displayedElement = selected;
                foreach (var defaultV in store.defaultValues)
                {
                    if (selected.character.characterID == defaultV.characterID)
                    {
                        Sprite displaySprite = defaultV.sprite;
                        if (displaySprite != null)
                        {
                            spriteImage.sprite = displaySprite;
                            spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b,
                                255 / 255f);
                        }
                        else
                        {
                            spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b,
                                155 / 255f);
                        }

                        return;
                    }
                }

                spriteImage.color =
                    new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b, 155 / 255f);
            }

            if (controller.selectedElement.character is {name: { }})
            {
                nameText.text = controller.selectedElement.character.name;
                nameText.color = new Color(nameText.color.r, nameText.color.g, nameText.color.b, 255 / 255f);
            }
            else
            {
                nameText.color = new Color(nameText.color.r, nameText.color.g, nameText.color.b, 155 / 255f);
            }
        }
        else
        {
            spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b, 155 / 255f);
            nameText.color = new Color(nameText.color.r, nameText.color.g, nameText.color.b, 155 / 255f);
        }
    }

    public void ForceReload()
    {
        if (controller.selectedElement != null)
        {
            var selected = controller.selectedElement;
            displayedElement = selected;
            foreach (var defaultV in store.defaultValues)
            {
                if (selected.character.characterID == defaultV.characterID)
                {
                    Sprite displaySprite = defaultV.sprite;
                    if (displaySprite != null)
                    {
                        spriteImage.sprite = displaySprite;
                        spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b,
                            255 / 255f);
                    }
                    else
                    {
                        spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b,
                            155 / 255f);
                    }

                    return;
                }
            }
            spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b, 155 / 255f);
            
            if (controller.selectedElement.character is {name: { }})
            {
                nameText.text = controller.selectedElement.character.name;
                nameText.color = new Color(nameText.color.r, nameText.color.g, nameText.color.b, 255 / 255f);
            }
            else
            {
                nameText.color = new Color(nameText.color.r, nameText.color.g, nameText.color.b, 155 / 255f);
            }
        }
        else
        {
            spriteImage.color = new Color(spriteImage.color.r, spriteImage.color.g, spriteImage.color.b, 155 / 255f);
            nameText.color = new Color(nameText.color.r, nameText.color.g, nameText.color.b, 155 / 255f);
        }
    }
}