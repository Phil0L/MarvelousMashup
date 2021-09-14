using System;
using UnityEngine;
using UnityEngine.UI;

public class CharacterMultipleAction : MonoBehaviour
{
    public Text Text;
    public Image TextImage;
    public Image Image;

    

    public void Set(int count, bool active = true)
    {
        if (count <= 0 || !active) Image.color = new Color(1, 1, 1, 0.7f);
        else Image.color = Color.white;
        
        if (count <= 0) TextImage.gameObject.SetActive(false);
        else TextImage.gameObject.SetActive(true);

        Text.text = "x" + count;
    }
    
    private Action callback;
    public void OnClick(Action callback) => this.callback = callback;
    public void _Clicked()
    {
        if (Image.color.a > 0.9f) callback();
    }

}
